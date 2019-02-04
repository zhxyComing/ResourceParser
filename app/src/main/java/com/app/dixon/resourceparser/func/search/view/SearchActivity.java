package com.app.dixon.resourceparser.func.search.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.Param;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.home.view.MovieOutlineAdapter;
import com.app.dixon.resourceparser.func.search.present.SearchPresent;
import com.app.dixon.resourceparser.func.special.view.SpecialActivity;
import com.app.dixon.resourceparser.model.MovieOutline;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class SearchActivity extends BaseActivity implements ISearchView {

    private SearchPresent mPresent;
    private ListView mListView;
    private SpinKitView mLoadingView;
    private MovieOutlineAdapter mAdapter;
    private ImageView mSearch;
    private EditText mInput;

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
    }

    private void search() {
        String text = mInput.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ToastUtils.toast("请输入正确的内容");
        } else {
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
        mListView = findViewById(R.id.lvMovies);
        mLoadingView = findViewById(R.id.svLoadingView);
        mSearch = findViewById(R.id.ivSearch);
        mInput = findViewById(R.id.etInput);
    }

    @Override
    public void showSearchResult(List<MovieOutline> list) {
        if (mAdapter == null) {
            mAdapter = new MovieOutlineAdapter(this, list);
            mListView.setAdapter(mAdapter);
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
        mListView.setLayoutAnimation(controller);
    }

    @Override
    public void showFail(String err) {
        ToastUtils.toast(err);
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }

    @Override
    public void stopLoading() {
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean tryStartHidePage(String search) {
        switch (search) {
            case Param.SPECIAL_MOVIE:
                SpecialActivity.startSpecialActivity(this);
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