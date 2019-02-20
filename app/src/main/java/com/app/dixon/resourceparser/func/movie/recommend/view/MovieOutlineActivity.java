package com.app.dixon.resourceparser.func.movie.recommend.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.pub.view.BackgroundDrawable;
import com.app.dixon.resourceparser.core.pub.view.MenuView;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.movie.recommend.present.MovieOutlinePresent;
import com.app.dixon.resourceparser.func.movie.search.view.SearchActivity;
import com.app.dixon.resourceparser.func.set.EditActivity;
import com.app.dixon.resourceparser.func.torr.view.TorrActivity;
import com.app.dixon.resourceparser.model.MovieOutline;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

/**
 * 电影梗概页面
 */

public class MovieOutlineActivity extends BaseActivity implements IMovieOutlineView {

    private MovieOutlinePresent mPresent;
    private GridView mGridView;
    private SpinKitView mLoadingView;
    private MovieOutlineAdapter mAdapter;
    private ImageView mSearch;
    //    private TextView mTorr, mEdit;
//    private MenuView mMenu;
    private TextView mTitle;
    private FrameLayout mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresent = new MovieOutlinePresent(this);
        mPresent.loadData();

        initView();
    }

    private void initView() {
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.startSearchActivity(MovieOutlineActivity.this);
            }
        });

        TypeFaceUtils.yunBook(mTitle);

//        initEditTip();
//        initTorrTip();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            BackgroundDrawable drawable = BackgroundDrawable.builder()
                    .left(90)//设置左侧斜切点的高度（取值范围是大于0，小于100）
                    .right(75)
//                    .topColor(Color.parseColor(topColor))//设置上半部分的颜色（默认是白色）
                    .bottomColor(Color.parseColor("#FCD62D"))//（默认是白色）
                    .build();

            mBack.setBackground(drawable);
        }
    }

//    private void initEditTip() {
//        MenuView.Item item = new MenuView.Item(this);
//        item.setText("磁力搜");
//        item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TorrActivity.startTorrActivity(MovieOutlineActivity.this);
//            }
//        });
//        mMenu.addItem(item);
//    }
//
//    private void initTorrTip() {
//        MenuView.Item item = new MenuView.Item(this);
//        item.setText("设置");
//        item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditActivity.startEditActivity(MovieOutlineActivity.this);
//            }
//        });
//        mMenu.addItem(item);
//    }

    public static void startMovieOutlineActivity(Context context) {
        context.startActivity(new Intent(context, MovieOutlineActivity.class));
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mGridView = findViewById(R.id.gvMovies);
        mLoadingView = findViewById(R.id.svLoadingView);
        mSearch = findViewById(R.id.ivSearch);
        mTitle = findViewById(R.id.tvTitle);
        mBack = findViewById(R.id.flBackground);
//        mMenu = findViewById(R.id.mvMenu);
    }

    @Override
    public void showMovieOutlines(List<MovieOutline> list) {
        if (mAdapter == null) {
            mAdapter = new MovieOutlineAdapter(this, list);
            mGridView.setAdapter(mAdapter);
            setShowAnim();
        } else {
            mAdapter.notifyData(list);
        }
    }

    private void setShowAnim() {
        LayoutAnimationController controller = new LayoutAnimationController(
                AnimationUtils.loadAnimation(this, R.anim.bottom_in));
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mGridView.setLayoutAnimation(controller);
    }

    @Override
    public void showFail(String err) {
        ToastUtils.toast(err);
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_common_in_back, R.anim.activity_common_out_back);
    }
}
