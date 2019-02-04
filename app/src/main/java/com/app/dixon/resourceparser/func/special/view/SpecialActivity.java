package com.app.dixon.resourceparser.func.special.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.special.present.SpecialPresent;
import com.app.dixon.resourceparser.model.SpecialOutline;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

public class SpecialActivity extends BaseActivity implements ISpecialView {

    private SpecialPresent mPresent;
    private GridView mGridView;
    private SpinKitView mLoadingView;
    private SpecialOutlineAdapter mAdapter;
    private TextView mLoadMore;
    private EditText mInput;
    private ImageView mSearch;

    private int mCurrentIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);

        mPresent = new SpecialPresent(this);
        mPresent.loadData();

        initView();
    }

    private void initView() {
        mLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresent.loadMore(++mCurrentIndex);
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

    public static void startSpecialActivity(Context context) {
        context.startActivity(new Intent(context, SpecialActivity.class));
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mGridView = findViewById(R.id.gvMovies);
        mLoadingView = findViewById(R.id.svLoadingView);
        mLoadMore = findViewById(R.id.tvLoadMore);
        mInput = findViewById(R.id.etInput);
        mSearch = findViewById(R.id.ivSearch);
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
    public void addSpecialList(List<SpecialOutline> list) {
        if (mAdapter != null && list != null && list.size() > 0) {
            mAdapter.addData(list);
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

    //一旦搜索 就会清除推荐数据、隐藏加载更多推荐按钮
    @Override
    public void clearRecommend() {
        mCurrentIndex = 1;
        showSpecialList(new ArrayList<SpecialOutline>());
        mLoadMore.setVisibility(View.GONE);
    }
}
