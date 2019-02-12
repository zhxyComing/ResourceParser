package com.app.dixon.resourceparser.core.pub.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by dixon.xu on 2019/2/12.
 * <p>
 * 上拉加载更多的GridView 临时使用
 */

public class LoadMoreGridView extends GridView {

    public LoadMoreGridView(Context context) {
        super(context);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setAutoLoadCallBack(AutoLoadListener.AutoLoadCallBack callBack) {
        setOnScrollListener(new AutoLoadListener(callBack));
    }
}
