package com.app.dixon.resourceparser.core.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.app.dixon.resourceparser.core.pub.activity.BaseApplication;

/**
 * Created by dixon.xu on 2018/1/23.
 * <p>
 * 原先的toastUtils不仅不能方便使用 而且对弹出不加控制 所以弃用
 */

public class ToastUtils {
    /**
     * 全局Toast对象
     */
    private static Toast mToast;

    public static void toast(final String message, final int duration) {

        toast(BaseApplication.getApplication(), message, duration);
    }

    public static void toast(final Context context, final String message, final int duration) {

        HandlerUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //先取消正在显示的Toast
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, message, duration);
                mToast.show();
            }
        });
    }

    public static void toast(String message) {
        if (!TextUtils.isEmpty(message)) {
            toast(message, Toast.LENGTH_SHORT);
        }
    }

    public static void toast(int message) {
        String text = BaseApplication.getApplication().getResources().getString(message);
        if (!TextUtils.isEmpty(text)) {
            toast(text, Toast.LENGTH_SHORT);
        }
    }

    public static void toast(Context context, String message) {
        if (!TextUtils.isEmpty(message)) {
            toast(context, message, Toast.LENGTH_SHORT);
        }
    }

    public static void toast(Context context, int message) {
        String text = context.getResources().getString(message);
        if (!TextUtils.isEmpty(text)) {
            toast(context, text, Toast.LENGTH_SHORT);
        }
    }
}