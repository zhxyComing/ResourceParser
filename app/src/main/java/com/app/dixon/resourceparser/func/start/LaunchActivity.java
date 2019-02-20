package com.app.dixon.resourceparser.func.start;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.home.view.HomeActivity;
import com.app.dixon.resourceparser.func.movie.recommend.view.MovieOutlineActivity;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends BaseActivity {

    private TextView mLaunchTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        initView();
        countDown();
    }

    private void initView() {
        mLaunchTip = findViewById(R.id.tvLaunchTip);
        //设置字体
        TypeFaceUtils.yunBook(mLaunchTip);
    }

    private void countDown() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (!LaunchActivity.this.isFinishing() && !LaunchActivity.this.isDestroyed()) {
//                                MovieOutlineActivity.startMovieOutlineActivity(LaunchActivity.this);
                                HomeActivity.startHomeActivity(LaunchActivity.this);
                            }
                        } else {
//                            MovieOutlineActivity.startMovieOutlineActivity(LaunchActivity.this);
                            HomeActivity.startHomeActivity(LaunchActivity.this);
                        }
                        finish();
                    }
                });
            }
        }, 1000);
    }
}
