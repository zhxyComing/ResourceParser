package com.app.dixon.resourceparser.func.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.dixon.resourceparser.BuildConfig;
import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.FileUtils;

public class EditActivity extends BaseActivity {

    private TextView mVersion;
    private TextView mUpdateDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mVersion = findViewById(R.id.tvVersion);
        mUpdateDesc = findViewById(R.id.tvUpdateDesc);

        initView();
    }

    private void initView() {
        String text = "虚无之地 版本" + BuildConfig.VERSION_NAME;
        mVersion.setText(text);

        mUpdateDesc.setText(FileUtils.getFromAssets("update.txt", this));
    }

    public static void startEditActivity(Context context) {
        context.startActivity(new Intent(context, EditActivity.class));
    }
}
