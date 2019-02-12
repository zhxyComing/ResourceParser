package com.app.dixon.resourceparser.func.special.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.special.present.SpecialSearchResultPresent;
import com.app.dixon.resourceparser.model.SpecialOutline;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class SpecialSearchResultActivity extends BaseActivity implements ISpecialSearchResultView {

    private GridView mGridView;
    private SpinKitView mLoadingView;
    private SpecialOutlineAdapter mAdapter;

    private SpecialSearchResultPresent mPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_search_result);

        mPresent = new SpecialSearchResultPresent(this);
        mPresent.search(getIntent().getStringExtra("search"));
    }

    public static void startSSRActivity(Context context, String text) {
        Intent intent = new Intent(context, SpecialSearchResultActivity.class);
        intent.putExtra("search", text);
        context.startActivity(intent);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mGridView = findViewById(R.id.gvMovies);
        mLoadingView = findViewById(R.id.svLoadingView);
    }

    @Override
    public void showSpecialList(List<SpecialOutline> list) {
        if (mAdapter == null) {
            mAdapter = new SpecialOutlineAdapter(this, list);
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyData(list);
        }
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
}
