package com.app.dixon.resourceparser.func.set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.BuildConfig;
import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.pub.view.BackgroundDrawable;
import com.app.dixon.resourceparser.core.util.FileUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;

public class EditActivity extends BaseActivity {

    private TextView mVersion;
    private TextView mUpdateDesc;
    private TextView mTitle;
    private LinearLayout mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mVersion = findViewById(R.id.tvVersion);
        mUpdateDesc = findViewById(R.id.tvUpdateDesc);
        mTitle = findViewById(R.id.tvTitle);
        mBack = findViewById(R.id.llBackground);

        initView();
    }

    private void initView() {
        String text = "虚无之地 版本" + BuildConfig.VERSION_NAME;
        mVersion.setText(text);

        mUpdateDesc.setText(FileUtils.getFromAssets("update.txt", this));

        TypeFaceUtils.yunBook(mTitle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            BackgroundDrawable drawable = BackgroundDrawable.builder()
                    .left(90)//设置左侧斜切点的高度（取值范围是大于0，小于100）
                    .right(75)
//                    .topColor(Color.parseColor(topColor))//设置上半部分的颜色（默认是白色）
                    .bottomColor(Color.parseColor("#4FC3F7"))//（默认是白色）
                    .build();

            mBack.setBackground(drawable);
        }
    }

    public static void startEditActivity(Context context) {
        context.startActivity(new Intent(context, EditActivity.class));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_common_in_back, R.anim.activity_common_out_back);
    }
}
