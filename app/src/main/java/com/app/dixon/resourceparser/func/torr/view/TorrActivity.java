package com.app.dixon.resourceparser.func.torr.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.torr.present.TorrPresent;
import com.app.dixon.resourceparser.model.TorrDetail;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class TorrActivity extends BaseActivity implements ITorrView {

    private TorrPresent mPresent;
    private EditText mInput;
    private ImageView mSearch, mCopy;
    private ListView mListView;
    private SpinKitView mLoadingView;
    private TorrListAdapter mAdapter;
    private TextView mDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torr);

        mPresent = new TorrPresent(this);
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
        mCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy(mDownloadUrl.getText().toString());
                ToastUtils.toast("已复制到剪贴板");
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

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mInput = findViewById(R.id.etInput);
        mSearch = findViewById(R.id.ivSearch);
        mListView = findViewById(R.id.lvListView);
        mLoadingView = findViewById(R.id.svLoadingView);
        mDownloadUrl = findViewById(R.id.tvDownloadUrl);
        mCopy = findViewById(R.id.ivCopy);
    }

    public static void startTorrActivity(Context context) {
        context.startActivity(new Intent(context, TorrActivity.class));
    }

    @Override
    public void showSearchResult(List<TorrDetail> list) {
        if (mAdapter == null) {
            mAdapter = new TorrListAdapter(this, list, new TorrParse());
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyData(list);
        }
    }

    private class TorrParse implements TorrListAdapter.OnParseDownloadListener {

        @Override
        public void onParseStart() {
            mDownloadUrl.setText("解析中…");
        }

        @Override
        public void onParseSuccess(String result) {
            mDownloadUrl.setText(result);
        }

        @Override
        public void onParseFail(String err) {
            String display = "解析失败: " + err;
            mDownloadUrl.setText(display);
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

    //抽成工具类
    private void copy(String url) {
        ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(url);
    }
}
