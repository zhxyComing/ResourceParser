package com.app.dixon.resourceparser.func.special.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.special.present.SpecialDetailPresent;
import com.app.dixon.resourceparser.model.SpecialDetail;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class SpecialDetailActivity extends BaseActivity implements ISpecialDetailView {

    private SpecialDetailPresent mPresent;
    private GridView mGridView;
    private SpinKitView mLoadingView;
    private SpecialDetailAdapter mAdapter;
    private TextView mTitleView, mLoadingTip;

    private String mTitle;
    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_detail);

        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        mAddress = intent.getStringExtra("address");

        mPresent = new SpecialDetailPresent(this);
        mPresent.loadData(mAddress);

        initView();
    }

    private void initView() {
        mTitleView.setText(mTitle);
    }

    public static void startSpecialDetailActivity(Context context, String title, String address) {
        Intent intent = new Intent(context, SpecialDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("address", address);
        context.startActivity(intent);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mGridView = findViewById(R.id.gvMovies);
        mLoadingView = findViewById(R.id.svLoadingView);
        mTitleView = findViewById(R.id.tvTitle);
        mLoadingTip = findViewById(R.id.tvLoadingTip);
    }

    @Override
    public void showList(List<SpecialDetail> list) {
        if (mAdapter == null) {
            mAdapter = new SpecialDetailAdapter(this, list);
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
        mLoadingTip.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        mLoadingView.setVisibility(View.GONE);
        mLoadingTip.setVisibility(View.GONE);
    }

}
