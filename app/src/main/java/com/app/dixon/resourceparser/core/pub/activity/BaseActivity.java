package com.app.dixon.resourceparser.core.pub.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.app.dixon.resourceparser.core.manager.AnalyticsManager;
import com.app.dixon.resourceparser.core.pub.inter.IPermission;
import com.app.dixon.resourceparser.core.util.StatusBarUtil;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 * <p>
 * BaseActivity
 */

public class BaseActivity extends Activity {

    //权限请求集合
    private List<IPermission> mAsks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.setColorForStatus(this);

        askPermission();
    }

    private void askPermission() {
        for (IPermission ask : mAsks) {
            ask.askPermission(this);
        }
    }

    protected void addPermissionAsk(IPermission permission) {
        mAsks.add(permission);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsManager.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyticsManager.onPause(this);
    }

    //设置自定义字体
    public void yunBook(TextView... views) {
        TypeFaceUtils.yunBook(views);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (IPermission ask : mAsks) {
            ask.requestResult(requestCode, permissions, grantResults);
        }
    }
}
