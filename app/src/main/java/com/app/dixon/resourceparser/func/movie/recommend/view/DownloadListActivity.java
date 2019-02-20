package com.app.dixon.resourceparser.func.movie.recommend.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.pub.view.BackgroundDrawable;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.movie.recommend.present.DownloadListPresent;
import com.app.dixon.resourceparser.model.MovieDownload;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class DownloadListActivity extends BaseActivity implements IMovieDownloadView {

    private DownloadListPresent mPresent;
    private ListView mListView;
    private SpinKitView mLoadingView;
    private DownloadListAdapter mAdapter;
    private TextView mTitle;
    private FrameLayout mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);

        mPresent = new DownloadListPresent(this);
        mPresent.loadData(getIntent().getStringExtra("address"));

        TypeFaceUtils.yunBook(mTitle);
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
        mTitle = findViewById(R.id.tvTitle);
        mBack = findViewById(R.id.flBackground);
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
