package com.app.dixon.resourceparser.func.home.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.home.present.DownloadListPresent;
import com.app.dixon.resourceparser.model.MovieDownload;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class DownloadListActivity extends BaseActivity implements IMovieDownloadView {

    private DownloadListPresent mPresent;
    private ListView mListView;
    private SpinKitView mLoadingView;
    private DownloadListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);

        mPresent = new DownloadListPresent(this);
        mPresent.loadData(getIntent().getStringExtra("address"));
    }

    public static void startDownloadListActivity(Context context, String address) {
        Intent intent = new Intent(context, DownloadListActivity.class);
        intent.putExtra("address", address);
        context.startActivity(intent);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mListView = findViewById(R.id.lvDownloadList);
        mLoadingView = findViewById(R.id.svLoadingView);
    }

    @Override
    public void showMovieDownloadList(List<MovieDownload> list) {
        if (mAdapter == null) {
            mAdapter = new DownloadListAdapter(this, list);
            mListView.setAdapter(mAdapter);
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
