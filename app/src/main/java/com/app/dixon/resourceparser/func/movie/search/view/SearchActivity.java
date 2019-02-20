package com.app.dixon.resourceparser.func.movie.search.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.manager.AnalyticsManager;
import com.app.dixon.resourceparser.core.pub.Param;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.pub.view.BackgroundDrawable;
import com.app.dixon.resourceparser.core.util.DialogUtils;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.movie.recommend.view.MovieOutlineAdapter;
import com.app.dixon.resourceparser.func.movie.search.present.SearchPresent;
import com.app.dixon.resourceparser.func.special.view.SpecialActivity;
import com.app.dixon.resourceparser.func.torr.view.TorrActivity;
import com.app.dixon.resourceparser.model.MovieOutline;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class SearchActivity extends BaseActivity implements ISearchView {

    private SearchPresent mPresent;
    private GridView mGridView;
    private SpinKitView mLoadingView;
    private MovieOutlineAdapter mAdapter;
    private ImageView mSearch;
    private EditText mInput;
    private FrameLayout mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mPresent = new SearchPresent(this);
        initView();
    }

    private void initView() {
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        mInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    //处理事件
                    search();
                    return true;
                }
                return false;
            }
        });
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

    private void search() {
        String text = mInput.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ToastUtils.toast("请输入正确的内容");
        } else {
            AnalyticsManager.Event.onMovieSearch(this, text);
            mPresent.search(text);
        }
    }

    public static void startSearchActivity(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
//        context.startActivity(new Intent(context, SpecialActivity.class));
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.bottom_in, R.anim.activity_nothing_out);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mGridView = findViewById(R.id.gvMovies);
        mLoadingView = findViewById(R.id.svLoadingView);
        mSearch = findViewById(R.id.ivSearch);
        mInput = findViewById(R.id.etInput);
        mBack = findViewById(R.id.flBackground);
    }

    @Override
    public void showSearchResult(List<MovieOutline> list) {
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
        mGridView.setVisibility(View.GONE);
    }

    @Override
    public void stopLoading() {
        mLoadingView.setVisibility(View.GONE);
        mGridView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean tryStartHidePage(String search) {
        //之后单独摘成一个页面中转Manager
        switch (search) {
            case Param.SPECIAL_MOVIE:
//                SpecialActivity.startSpecialActivity(this);
                DialogUtils.showSecretDialog(this);
                return true;
            case Param.TORR_MOVIE:
                TorrActivity.startTorrActivity(this);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_nothing_in, R.anim.activity_bottom_out);
    }
}
