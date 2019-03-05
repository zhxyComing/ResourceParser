package com.app.dixon.resourceparser.core.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.dson.Dson;
import com.app.dixon.resourceparser.core.dson.DsonData;
import com.app.dixon.resourceparser.core.pub.view.CustomDialog;
import com.app.dixon.resourceparser.func.special.view.SpecialActivity;

import java.io.IOException;
import java.util.List;

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

    public static void showSecretDialog(final Context context) {
        if (!canShow(context)) {
            return;
        }
        CustomDialog dialog = new CustomDialog.Builder(context)
                .view(R.layout.dialog_secret)
                .style(R.style.dialog)
                .isCancelOnTouchOutSide(true)
                .windowAnimStyle(R.style.dialogAnim)
                .widthPx(ScreenUtils.dpToPxInt(context, 280))
//                .heightPx(ScreenUtils.dpToPxInt(context, 196))
                .build();

        final EditText input = dialog.getView().findViewById(R.id.etPassword);
        TextView ok = dialog.getView().findViewById(R.id.tvOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = input.getText().toString();
                String secret = parseSecret(context);
                if (!TextUtils.isEmpty(pass) && pass.equals(secret)) {
                    SpecialActivity.startSpecialActivity(context);
                }
            }
        });
        show(dialog);
    }

    private static String parseSecret(Context context) {
        try {
            List<DsonData> parse = Dson.parse(FileUtils.getFromAssets("secret.dson", context));
            String origin = parse.get(0).get("secret");
            if (!TextUtils.isEmpty(origin)) {
                return SecretUtils.simpleDecrypt(origin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void showHomeTipDialog(Context context, String text) {
        if (!canShow(context)) {
            return;
        }
        CustomDialog dialog = new CustomDialog.Builder(context)
                .view(R.layout.dialog_home_tip)
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

        TextView tip = dialog.getView().findViewById(R.id.tvTip);
        tip.setText(text);

        show(dialog);
    }

    public static void showHomeTipDialog(Context context, String text, View.OnClickListener clickListener) {
        if (!canShow(context)) {
            return;
        }
        CustomDialog dialog = new CustomDialog.Builder(context)
                .view(R.layout.dialog_home_tip)
                .style(R.style.dialog)
                .isCancelOnTouchOutSide(true)
                .windowAnimStyle(R.style.dialogAnim)
                .widthPx(ScreenUtils.dpToPxInt(context, 280))
//                .heightPx(ScreenUtils.dpToPxInt(context, 196))
                .addViewOnClick(R.id.tvOk, clickListener)
                .build();

        TextView tip = dialog.getView().findViewById(R.id.tvTip);
        tip.setText(text);

        show(dialog);
    }

    private static void show(Dialog dialog) {
        if (dialog != null) {
            dialog.show();
        }
    }

    private static boolean canShow(Context context) {
        if (context != null && context instanceof Activity && Looper.myLooper() == Looper.getMainLooper()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                    return true;
                }
            } else if (!((Activity) context).isFinishing()) {
                return true;
            }
        }
        return false;
    }
}
