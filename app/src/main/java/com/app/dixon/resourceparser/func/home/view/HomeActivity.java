package com.app.dixon.resourceparser.func.home.view;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.ICompleteCallback;
import com.app.dixon.resourceparser.IMusicManagerService;
import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;
import com.app.dixon.resourceparser.core.pub.view.ToastView;
import com.app.dixon.resourceparser.core.util.AnimationUtils;
import com.app.dixon.resourceparser.core.util.DialogUtils;
import com.app.dixon.resourceparser.core.util.HandlerUtils;
import com.app.dixon.resourceparser.core.util.ScreenUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.home.control.HomeItemLoader;
import com.app.dixon.resourceparser.func.music.service.MusicManagerService;


public class HomeActivity extends BaseActivity implements ServiceConnection {

    private HorizontalListView mSelectListView;
    private TextView mGoText;
    private LinearLayout mGoLayout;
    private ToastView mToastView;
    private SelectAdapter mAdapter;

    private long mExitTime;

    private static final int PERMISSION_ASK_READ_SD = 1000;

    private IMusicManagerService mMms;
    private boolean isMmsReady = false; //mms是否初始化完毕

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initData();
    }

    public static void startHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
        }
    }

    private void initView() {
        TypeFaceUtils.yunBook(mGoText);
        mAdapter = new SelectAdapter(this, mSelectListView);
        mAdapter.setList(HomeItemLoader.loadItem(this));
        mGoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity();
            }
        });
        initToastCard();
    }

    private void initData() {
        queryMusic();
    }

    private void queryMusic() {
        if (!checkReadPermission()) {
            return;
        }
        realQueryMusic();
    }

    private void realQueryMusic() {
        startService(new Intent(HomeActivity.this, MusicManagerService.class));
        bindService(new Intent(HomeActivity.this, MusicManagerService.class), this, BIND_AUTO_CREATE);
    }

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
                DialogUtils.showHomeTipDialog(HomeActivity.this, getString(R.string.read_permission_tip), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_ASK_READ_SD);
                        }
                    }
                });
            }
        }, 1000);
        return false;
    }

    private void startTargetActivity() {
        String openPageClazz = mAdapter.getList().get(mSelectListView.getCurrentIndex()).getOpenPage();
        try {
            Class<?> aClass = getClassLoader().loadClass(openPageClazz);
            startActivity(new Intent(HomeActivity.this, aClass));
            overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mSelectListView = findViewById(R.id.hlvSelectList);
        mGoText = findViewById(R.id.tvGo);
        mGoLayout = findViewById(R.id.llGo);
        mToastView = findViewById(R.id.toastCard);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //与上次点击返回键时刻作差
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            //大于2000ms则认为是误操作，使用Toast进行提示
            mToastView.show("再按一次退出程序", 2000);
            //并记录下本次点击“返回键”的时刻，以便下次进行判断
            mExitTime = System.currentTimeMillis();
        } else {
            //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
            super.onBackPressed();
        }

    }

    private void initToastCard() {
        mToastView.setVisibility(View.GONE);
        mToastView.setToastAnimEvent(new ToastView.ToastAnimEvent() {
            @Override
            public void show(long time) {
                mToastView.setVisibility(View.VISIBLE);
                AnimationUtils.tranX(mToastView,
                        ScreenUtils.dpToPx(HomeActivity.this, -200),
                        0, 300, new DecelerateInterpolator(), null).start();
            }

            @Override
            public void hide(long time) {
                AnimationUtils.tranX(mToastView, 0,
                        ScreenUtils.dpToPx(HomeActivity.this, -200), 300,
                        new DecelerateInterpolator(),
                        new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mToastView.setVisibility(View.GONE);
                            }
                        }).start();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_ASK_READ_SD) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                realQueryMusic();
            }
        }
    }

    /**
     * 通信顺序:@主进程 -> MusicManagerService -> MusicManager
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        initMusicManagerService(service);
    }

    //初始化mms 初始化成功后会即可获取音乐列表 播放音乐等等
    private void initMusicManagerService(IBinder service) {
        mMms = IMusicManagerService.Stub.asInterface(service);
        try {
            mMms.init(new ICompleteCallback.Stub() {
                @Override
                public void onComplete() throws RemoteException {
                    isMmsReady = true;
                    initMusicListView();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initMusicListView() {
        //TODO 加载音乐列表 并初始化隐藏布局
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }
}
