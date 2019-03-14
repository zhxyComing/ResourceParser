package com.app.dixon.resourceparser.func.music.view;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.app.dixon.resourceparser.ICompleteCallback;
import com.app.dixon.resourceparser.IMusicChangedCallback;
import com.app.dixon.resourceparser.IMusicManagerService;
import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.DialogUtils;
import com.app.dixon.resourceparser.core.util.HandlerUtils;
import com.app.dixon.resourceparser.func.music.control.MusicLocalManager;
import com.app.dixon.resourceparser.func.music.service.MusicManagerService;

/**
 * Created by dixon.xu on 2019/3/6.
 * <p>
 * 音乐基类 包含申请权限、建立MMS通讯
 */

public abstract class MusicActivity extends BaseActivity implements ServiceConnection {

    private static final int PERMISSION_ASK_READ_SD = 1000;

    protected IMusicManagerService mMusicManagerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectService();
    }

    private void connectService() {
        if (!checkReadPermission()) {
            return;
        }
        realConnectService();
    }

    private void realConnectService() {
        startService(new Intent(this, MusicManagerService.class));
        bindService(new Intent(this, MusicManagerService.class), this, BIND_AUTO_CREATE);
    }

    //通信顺序:@主进程 -> MusicManagerService -> MusicManager
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        initMusicManagerService(service);
    }

    //初始化mms 初始化成功后会即可获取音乐列表 播放音乐等等
    private void initMusicManagerService(IBinder service) {
        mMusicManagerService = IMusicManagerService.Stub.asInterface(service);
        try {
            mMusicManagerService.init(new ICompleteCallback.Stub() {
                @Override
                public void onComplete() throws RemoteException {
                    //注意:回调在子线程(且以Binder命名)
                    HandlerUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MusicLocalManager.initReadyService(mMusicManagerService);
                            onMusicManagerReady();
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected abstract void onMusicManagerReady();

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    //检查权限 没权限弹窗申请
    private boolean checkReadPermission() {
        //低于16默认有权限
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //有权限
            return true;
        }
        //没权限 延迟1s弹出

        HandlerUtils.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtils.showHomeTipDialog(MusicActivity.this, getString(R.string.read_permission_tip), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ActivityCompat.requestPermissions(MusicActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,}, PERMISSION_ASK_READ_SD);
                        }
                    }
                });
            }
        }, 1000);
        return false;
    }

    //权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_ASK_READ_SD) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                realConnectService();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
        MusicLocalManager.destroy();
    }
}
