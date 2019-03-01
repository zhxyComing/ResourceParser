package com.app.dixon.resourceparser.func.home.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;
import com.app.dixon.resourceparser.core.pub.view.ToastView;
import com.app.dixon.resourceparser.core.util.AnimationUtils;
import com.app.dixon.resourceparser.core.util.MusicUtils;
import com.app.dixon.resourceparser.core.util.ScreenUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.home.control.HomeItemLoader;

public class HomeActivity extends BaseActivity {

    private HorizontalListView mSelectListView;
    private TextView mGoText;
    private LinearLayout mGoLayout;
    private ToastView mToastView;
    private SelectAdapter mAdapter;

    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = HomeActivity.this.getContentResolver();
                StringBuffer select = new StringBuffer(" 1=1 ");
                // 查询语句：检索出时长大于1分钟，文件大小大于1MB的媒体文件
                select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + MusicUtils.FILTER_SIZE);
                select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + MusicUtils.FILTER_DURATION);

                MusicUtils.getMusicList(cr.query(uri, MusicUtils.proj_music,
                        select.toString(), null,
                        MediaStore.Audio.Media.ARTIST_KEY));
            }
        }).start();
    }

    public static void startHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
        }
    }

    private void initView() {
        TypeFaceUtils.yunBook(mGoText);

        mAdapter = new SelectAdapter(this, mSelectListView);
        mAdapter.setList(HomeItemLoader.loadItem(this));

        mGoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity();
            }
        });

        initToastCard();
    }

    private void startTargetActivity() {
        String openPageClazz = mAdapter.getList().get(mSelectListView.getCurrentIndex()).getOpenPage();
        try {
            Class<?> aClass = getClassLoader().loadClass(openPageClazz);
            startActivity(new Intent(HomeActivity.this, aClass));
            overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mSelectListView = findViewById(R.id.hlvSelectList);
        mGoText = findViewById(R.id.tvGo);
        mGoLayout = findViewById(R.id.llGo);
        mToastView = findViewById(R.id.toastCard);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //与上次点击返回键时刻作差
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            //大于2000ms则认为是误操作，使用Toast进行提示
            mToastView.show("再按一次退出程序", 2000);
            //并记录下本次点击“返回键”的时刻，以便下次进行判断
            mExitTime = System.currentTimeMillis();
        } else {
            //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
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
}
