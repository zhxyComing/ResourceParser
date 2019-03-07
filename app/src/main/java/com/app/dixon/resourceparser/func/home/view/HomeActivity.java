package com.app.dixon.resourceparser.func.home.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.dixon.resourceparser.IMusicChangedCallback;
import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.view.FootScrollView;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;
import com.app.dixon.resourceparser.core.pub.view.ToastView;
import com.app.dixon.resourceparser.core.util.AnimationUtils;
import com.app.dixon.resourceparser.core.util.HandlerUtils;
import com.app.dixon.resourceparser.core.util.ScreenUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.home.control.HomeItemLoader;
import com.app.dixon.resourceparser.func.music.adapter.MusicListAdapter;
import com.app.dixon.resourceparser.func.music.control.MusicLocalManager;
import com.app.dixon.resourceparser.func.music.view.MusicActivity;
import com.app.dixon.resourceparser.model.MusicInfo;

//！音乐功能调试


public class HomeActivity extends MusicActivity {

    private HorizontalListView mSelectListView;
    private TextView mGoText;
    private LinearLayout mGoLayout;
    private ToastView mToastView;
    private SelectAdapter mAdapter;

    //MusicLayout
    private FootScrollView mMusicLayout;
    private ImageView mHideBtn;
    private ListView mMusicListView;
    private MusicListAdapter mMusicAdapter;
    private TextView mMusicLayoutTitle;

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
        TypeFaceUtils.yunBook(mGoText, mMusicLayoutTitle);
        mAdapter = new SelectAdapter(this, mSelectListView);
        mAdapter.setList(HomeItemLoader.loadItem(this));
        mGoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity();
            }
        });
        initToastCard();
        initMusicLayout();
    }

    private void initMusicLayout() {
        mHideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicLayout.move();
            }
        });
        //设置只要Select变化 音乐列表就隐藏
        mSelectListView.setOnItemChangedListener(new HorizontalListView.OnItemChangedListener() {
            @Override
            public void onChanged(int pos) {
                mMusicLayout.alwaysHide();
            }
        });
        mMusicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicInfo info = mMusicAdapter.getList().get(position);
                MusicLocalManager.play(info);
            }
        });
    }

    @Override
    protected void onMusicManagerReady() {
        //本地列表初始化
        mMusicAdapter = new MusicListAdapter(this, MusicLocalManager.getMusicInfos());
        mMusicListView.setAdapter(mMusicAdapter);

        //布局初始化回调
        if (mAdapter.getMusicChangedListener() != null && MusicLocalManager.isPlaying()) {
            mAdapter.getMusicChangedListener().onChanged();
        }

        MusicLocalManager.setMusicChangedListener(new IMusicChangedCallback.Stub() {
            @Override
            public void onChanged() throws RemoteException {
                HandlerUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //列表回调
                        mMusicAdapter.notifyData();
                        //布局回调
                        if (mAdapter.getMusicChangedListener() != null) {
                            mAdapter.getMusicChangedListener().onChanged();
                        }
                    }
                });
            }
        });
    }

    private void startTargetActivity() {
        String openPageClazz = mAdapter.getList().get(mSelectListView.getCurrentIndex()).getOpenPage();
        if (TextUtils.isEmpty(openPageClazz)) {
            checkRealIntent();
            return;
        }
        try {
            Class<?> aClass = getClassLoader().loadClass(openPageClazz);
            startActivity(new Intent(HomeActivity.this, aClass));
            overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //跳转为空 则赋予该键其它操作
    private void checkRealIntent() {
        SelectAdapter.Item.Type type = mAdapter.getList().get(mSelectListView.getCurrentIndex()).getType();
        //音乐页面点击GO弹出音乐列表
        if (type == SelectAdapter.Item.Type.MUSIC) {
            mMusicLayout.move();
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mSelectListView = findViewById(R.id.hlvSelectList);
        mGoText = findViewById(R.id.tvGo);
        mGoLayout = findViewById(R.id.llGo);
        mToastView = findViewById(R.id.toastCard);
        mMusicLayout = findViewById(R.id.fsvMusicListLayout);
        mHideBtn = findViewById(R.id.ivHide);
        mMusicListView = findViewById(R.id.lvMusicList);
        mMusicLayoutTitle = findViewById(R.id.tvMusicTitle);
    }

    @Override
    public void onBackPressed() {
        //与上次点击返回键时刻作差
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            mToastView.show("再按一次退出程序", 2000);
            mExitTime = System.currentTimeMillis();
        } else {
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
