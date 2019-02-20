package com.app.dixon.resourceparser.func.home.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.pub.view.BackgroundDrawable;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;

public class HomeActivity extends BaseActivity {

    private HorizontalListView mSelectListView;
    private TextView mGoText;
    private LinearLayout mGoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    public static void startHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
        }
    }

    private void initView() {

        TypeFaceUtils.yunBook(mGoText);

        View inflate = LayoutInflater.from(this).inflate(R.layout.item_home_select, null);
        TextView tv = inflate.findViewById(R.id.tvTitle);
        FrameLayout fl = inflate.findViewById(R.id.flBackground);
        TypeFaceUtils.yunBook(tv);
        setBackground(fl, 90, 75, "#FCD62D");
        mSelectListView.addChild(inflate);

        View inflate1 = LayoutInflater.from(this).inflate(R.layout.item_home_select, null);
        TextView tv1 = inflate1.findViewById(R.id.tvTitle);
        FrameLayout fl1 = inflate1.findViewById(R.id.flBackground);
        TypeFaceUtils.yunBook(tv1);
        setBackground(fl1, 75, 90, "#CDDC39");
        mSelectListView.addChild(inflate1);

        View inflate2 = LayoutInflater.from(this).inflate(R.layout.item_home_select, null);
        TextView tv2 = inflate2.findViewById(R.id.tvTitle);
        FrameLayout fl2 = inflate2.findViewById(R.id.flBackground);
        TypeFaceUtils.yunBook(tv2);
        setBackground(fl2, 90, 75, "#4fc3f7");
        mSelectListView.addChild(inflate2);

        View inflate3 = LayoutInflater.from(this).inflate(R.layout.item_home_select, null);
        mSelectListView.addChild(inflate3);

    }

    //将这个drawable设置给View
    public void setBackground(View view, int left, int right, String topColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            BackgroundDrawable drawable = BackgroundDrawable.builder()
                    .left(left)//设置左侧斜切点的高度（取值范围是大于0，小于100）
                    .right(right)
                    .topColor(Color.parseColor(topColor))//设置上半部分的颜色（默认是白色）
//                    .bottomColor(Color.parseColor("#76C4EB"))//（默认是白色）
                    .build();

            view.setBackground(drawable);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mSelectListView = findViewById(R.id.hlvSelectList);
        mGoText = findViewById(R.id.tvGo);
        mGoLayout = findViewById(R.id.llGo);
    }
}
