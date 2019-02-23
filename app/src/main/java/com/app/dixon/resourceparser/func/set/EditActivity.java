package com.app.dixon.resourceparser.func.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.BuildConfig;
import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.manager.theme.BackType;
import com.app.dixon.resourceparser.core.manager.theme.ThemeManager;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.FileUtils;

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

        yunBook(mTitle);
        ThemeManager.normalBackground(BackType.LEFT, null, "#4FC3F7", mBack);
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
