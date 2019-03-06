package com.app.dixon.resourceparser.core.pub.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * Created by dixon.xu on 2019/3/6.
 * <p>
 * 底部隐藏View
 */

public class FootScrollView extends CardView {

    public FootScrollView(@NonNull Context context) {
        super(context);
        init();
    }

    public FootScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FootScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }
}
