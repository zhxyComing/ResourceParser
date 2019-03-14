package com.app.dixon.resourceparser.func.home.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;
import com.app.dixon.resourceparser.core.pub.view.ToastView;
import com.app.dixon.resourceparser.core.util.AnimationUtils;
import com.app.dixon.resourceparser.core.util.ScreenUtils;
import com.app.dixon.resourceparser.func.home.control.HomeItemLoader;
import com.app.dixon.resourceparser.func.home.event.MusicManagerReadyEvent;
import com.app.dixon.resourceparser.func.home.event.OnDestroyEvent;
import com.app.dixon.resourceparser.func.home.event.OnPauseEvent;
import com.app.dixon.resourceparser.func.home.event.OnResumeEvent;
import com.app.dixon.resourceparser.func.music.view.MusicActivity;


public class HomeActivity extends MusicActivity {

    private HorizontalListView mSelectListView;
    private ToastView mToastView;
    private SelectAdapter mAdapter;

    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    public static void startHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
        }
    }

    private void initView() {
        mAdapter = new SelectAdapter(this, mSelectListView);
        mAdapter.setList(HomeItemLoader.loadItem(this));
        initToastCard();
    }

    @Override
    protected void onMusicManagerReady() {
        //本地列表初始化
        mAdapter.onEvent(new MusicManagerReadyEvent());
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mSelectListView = findViewById(R.id.hlvSelectList);
        mToastView = findViewById(R.id.toastCard);
    }

    @Override
    public void onBackPressed() {
        //与上次点击返回键时刻作差
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            mToastView.show("再按一次退出程序", 2000);
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private void initToastCard() {
        mToastView.setVisibility(View.GONE);
        mToastView.setToastAnimEvent(new ToastView.ToastAnimEvent() {
            @Override
            public void show(long time) {
                mToastView.setVisibility(View.VISIBLE);
                AnimationUtils.tranX(mToastView,
                        ScreenUtils.dpToPx(HomeActivity.this, -200),
                        0, 300, new DecelerateInterpolator(), null).start();
            }

            @Override
            public void hide(long time) {
                AnimationUtils.tranX(mToastView, 0,
                        ScreenUtils.dpToPx(HomeActivity.this, -200), 300,
                        new DecelerateInterpolator(),
                        new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mToastView.setVisibility(View.GONE);
                            }
                        }).start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //生命周期回调
        mAdapter.onEvent(new OnPauseEvent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.onEvent(new OnResumeEvent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.onEvent(new OnDestroyEvent());
    }
}
