package com.app.dixon.resourceparser.core.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.view.View;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.view.CustomDialog;

/**
 * Created by dixon.xu on 2018/3/22.
 * <p>
 * 诸多弹窗提醒 以后弹窗提醒统一到这个类
 */

public class DialogUtils {

    private static int torrShowCount = 0;

    private DialogUtils() {

    }

    public static void showTorrTipDialog(Context context) {
        if (!canShow(context)) {
            return;
        }
        //逻辑需要优化 这里指定一次app使用期间仅能显示一次
        if (torrShowCount > 0) {
            return;
        }
        torrShowCount += 1;
        CustomDialog dialog = new CustomDialog.Builder(context)
                .view(R.layout.dialog_tip)
                .style(R.style.dialog)
                .isCancelOnTouchOutSide(true)
                .windowAnimStyle(R.style.dialogAnim)
                .widthPx(ScreenUtils.dpToPxInt(context, 280))
//                .heightPx(ScreenUtils.dpToPxInt(context, 196))
                .addViewOnClick(R.id.tvOk, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //添加空事件 表示点击后dismiss
                    }
                })
                .build();

        show(dialog);
    }

    private static void show(Dialog dialog) {
        if (dialog != null) {
            dialog.show();
        }
    }

    private static boolean canShow(Context context) {
        if (context != null && context instanceof Activity && Looper.myLooper() == Looper.getMainLooper()) {
            if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                return true;
            }
        }
        return false;
    }
}
