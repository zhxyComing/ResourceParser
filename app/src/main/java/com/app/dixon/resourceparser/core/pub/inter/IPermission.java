package com.app.dixon.resourceparser.core.pub.inter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by dixon.xu on 2019/3/1.
 */

public abstract class IPermission {

    private Activity mContext;

    public boolean askPermission(Activity activity) {
        mContext = activity;
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                askPermission());
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //有权限
            return true;
        }
        //没权限
        onRefusedPermission();
        return false;
    }

    protected void realAsk() {
        ActivityCompat.requestPermissions(mContext, new String[]{askPermission()}, 0);
    }

    protected abstract void onRefusedPermission();

    protected abstract String askPermission();

    public abstract void requestResult(int requestCode, String permissions[], int[] grantResults);
}
