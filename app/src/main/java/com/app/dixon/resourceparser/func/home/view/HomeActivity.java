package com.app.dixon.resourceparser.func.home.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.dson.Dson;
import com.app.dixon.resourceparser.core.dson.DsonData;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;
import com.app.dixon.resourceparser.core.pub.view.ToastView;
import com.app.dixon.resourceparser.core.util.AnimationUtils;
import com.app.dixon.resourceparser.core.util.FileUtils;
import com.app.dixon.resourceparser.core.util.ScreenUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.movie.recommend.view.MovieOutlineActivity;
import com.app.dixon.resourceparser.func.set.EditActivity;
import com.app.dixon.resourceparser.func.torr.view.TorrActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private HorizontalListView mSelectListView;
    private TextView mGoText;
    private LinearLayout mGoLayout;
    private ToastView mToastView;

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
        TypeFaceUtils.yunBook(mGoText);

        SelectAdapter adapter = new SelectAdapter(this, mSelectListView);
        adapter.setList(loadItemList());

        mGoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity();
            }
        });

        initToastCard();
    }

    private void startTargetActivity() {
        switch (mSelectListView.getCurrentIndex()) {
            case 0:
                MovieOutlineActivity.startMovieOutlineActivity(this);
                overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
                break;
            case 1:
                TorrActivity.startTorrActivity(this);
                overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
                break;
            case 2:
                EditActivity.startEditActivity(this);
                overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
                break;
        }
    }

    private List<SelectAdapter.Item> loadItemList() {
        List<SelectAdapter.Item> itemList = new ArrayList<>();
        String fromAssets = FileUtils.getFromAssets("home.dson", this);
        try {
            List<DsonData> selects = Dson.parse(fromAssets);
            for (int i = 0; i < selects.size(); i++) {
                DsonData data = selects.get(i);
                SelectAdapter.Item item = new SelectAdapter.Item(data.get("title"), data.get("titleChinese"), 0, data.get("bg"), data.get("msg"));
                setItemCover(item, data.get("title"));
                itemList.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    private void setItemCover(SelectAdapter.Item item, String title) {
        if (title.contains("Movie")) {
            item.setCover(R.drawable.cover_movie);
        } else if (title.contains("Magnet")) {
            item.setCover(R.drawable.cover_magnet);
        } else if (title.contains("Message")) {
            item.setCover(R.drawable.cover_message);
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
