package com.app.dixon.resourceparser.core.pub.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixon.xu on 2018/3/22.
 * <p>
 * 自定义dialog基类
 */

public class CustomDialog extends Dialog {

    private View view;
    private boolean isCancelOnOutSide;
    private int width;
    private int height;
    private int windowAnimStyle;
    private Map<Integer, View.OnClickListener> listeners;

    public CustomDialog(@NonNull Builder builder) {
        super(builder.context);
        init(builder);
    }

    public CustomDialog(@NonNull Builder builder, int themeResId) {
        super(builder.context, themeResId);
        init(builder);
    }

    private void init(Builder builder) {
        this.view = builder.view;
        this.isCancelOnOutSide = builder.isCancelOnOutSide;
        this.width = builder.width;
        this.height = builder.height;
        this.windowAnimStyle = builder.windowAnimStyle;
        this.listeners = builder.listeners;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public View getView() {
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(view);
        setCanceledOnTouchOutside(isCancelOnOutSide);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        if (width != 0) {
            lp.width = width;
        }
        if (height != 0) {
            lp.height = height;
        }
        window.setAttributes(lp);

        window.setWindowAnimations(windowAnimStyle);

        //给之前保存的id一一设置对应click事件 另外这样做的目的是:默认触发完任意一个点击事件后关闭dialog
        for (final Integer key : listeners.keySet()) {
            view.findViewById(key).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listeners.get(key).onClick(view);
                    dismiss();
                }
            });
        }
    }

    public static final class Builder {
        private Context context;
        private View view;
        private boolean isCancelOnOutSide;
        private int width, height;
        private int resStyle = -1;//默认-1 表示为空
        private int windowAnimStyle;
        private Map<Integer, View.OnClickListener> listeners = new HashMap<>();

        public Builder(Context context) {
            this.context = context;
        }

        public Builder view(int id) {
            this.view = LayoutInflater.from(context).inflate(id, null);
            return this;
        }

        public Builder isCancelOnTouchOutSide(boolean b) {
            this.isCancelOnOutSide = b;
            return this;
        }

        public Builder widthPx(int widthPx) {
            this.width = widthPx;
            return this;
        }

        public Builder widthDimenRes(int widthId) {
            this.width = context.getResources().getDimensionPixelOffset(widthId);
            return this;
        }

        public Builder heightPx(int heightPx) {
            this.height = heightPx;
            return this;
        }

        public Builder heightDimenRes(int heightId) {
            this.height = context.getResources().getDimensionPixelOffset(heightId);
            return this;
        }

        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        /**
         * 可以根据id 自定义多个view点击事件
         */
        public Builder addViewOnClick(int id, View.OnClickListener onClickListener) {
            listeners.put(id, onClickListener);
            return this;
        }

        /**
         * 设置dialog的进入退出动画 也可以不设置
         *
         * @param style
         * @return
         */
        public Builder windowAnimStyle(int style) {
            this.windowAnimStyle = style;
            return this;
        }

        public CustomDialog build() {
            if (resStyle == -1) {
                return new CustomDialog(this);
            } else {
                return new CustomDialog(this, resStyle);
            }
        }
    }
}
