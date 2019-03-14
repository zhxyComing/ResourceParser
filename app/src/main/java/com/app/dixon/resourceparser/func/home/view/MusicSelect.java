package com.app.dixon.resourceparser.func.home.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.dixon.resourceparser.IMusicChangedCallback;
import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.manager.MusicManager;
import com.app.dixon.resourceparser.core.pub.view.CircleImageView;
import com.app.dixon.resourceparser.core.pub.view.FlexibleFrameLayout;
import com.app.dixon.resourceparser.core.pub.view.FootScrollView;
import com.app.dixon.resourceparser.core.util.AnimationUtils;
import com.app.dixon.resourceparser.core.util.DialogUtils;
import com.app.dixon.resourceparser.core.util.HandlerUtils;
import com.app.dixon.resourceparser.core.util.Ln;
import com.app.dixon.resourceparser.core.util.MusicUtils;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.home.event.MusicManagerReadyEvent;
import com.app.dixon.resourceparser.func.home.event.OnDestroyEvent;
import com.app.dixon.resourceparser.func.home.event.OnPauseEvent;
import com.app.dixon.resourceparser.func.home.event.OnResumeEvent;
import com.app.dixon.resourceparser.func.music.adapter.MusicAlbumListAdapter;
import com.app.dixon.resourceparser.func.music.adapter.MusicListAdapter;
import com.app.dixon.resourceparser.func.music.control.MusicLocalManager;
import com.app.dixon.resourceparser.model.MusicAlbum;
import com.app.dixon.resourceparser.model.MusicInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by dixon.xu on 2019/3/13.
 * <p>
 * Home页音乐Item
 */

public class MusicSelect extends SelectAdapter.SelectItem {

    private FrameLayout mBackground;
    private CircleImageView mCover;
    private ImageView mPlayBtn, mPlayPreBtn, mPlayNextBtn;
    private TextView mTitle;
    private CircleImageView mWarn;

    private FootScrollView mMusicListLayout;
    private ImageView mMore, mHide, mLocation, mBackFun;
    //    private TextView mMusicTitle;
    private ListView mMusicListView;

    private TextView mGoText;
    private LinearLayout mGoLayout;

    private MusicListAdapter mMusicAdapter;

    private SeekReceiver mReceiver;
    private ProgressBar mSeekBar;

    private LinearLayout mAlbum;
    private FrameLayout mTargetLayout;
    private ListView mTargetListView;
    private FlexibleFrameLayout mFunctionLayout;

    private MusicAlbumListAdapter mAlbumAdapter;

    public MusicSelect(Context context, SelectAdapter.ItemModel model) {
        super(context, model);
    }

    @Override
    protected View initView() {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_music, null);
        findView(itemView);

        initNormalView();
        //播放按钮群
        initPlayBtn();
        //音乐列表Layout
        initMusicListLayout();
        //进度条
        initReceiver();
        return itemView;
    }

    private void initReceiver() {
        mReceiver = new SeekReceiver();
        mContext.registerReceiver(mReceiver, new IntentFilter(MusicManager.PROGRESS_ACTION));
    }

    private void initNormalView() {
        mTitle.setText(mModel.getTitle());
        mCover.setImageResource(mModel.getCover());
        TypeFaceUtils.yunBook(mTitle, mGoText);
        mWarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showHomeTipDialog(mContext, mModel.getMsg());
            }
        });
    }

    private void initMusicListLayout() {
        mHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicListLayout.move();
            }
        });
        mMusicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicInfo info = mMusicAdapter.getList().get(position);
                Ln.c("play click " + info);
                MusicLocalManager.play(info);
            }
        });

        //当音乐列表打开时 才开始同步进度 这块逻辑与Resume等生命周期状态相关联
        mMusicListLayout.setOnStatueChangedListener(new FootScrollView.OnStatueChangedListener() {
            @Override
            public void onShow() {
                MusicLocalManager.startProgress();
            }

            @Override
            public void onHide() {
                MusicLocalManager.stopProgress();
            }
        });

        initMusicListTopBar(); //音乐列表横条
        initMusicListFunctionBar(); //音乐列表功能条
    }

    private void initMusicListTopBar() {
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPlayingLocation();
            }
        });
        //展开功能页
        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFunctionLayout.move();
            }
        });
        //mMore的旋转动画
        mFunctionLayout.setOnMoveListener(new FlexibleFrameLayout.OnMoveListener() {
            @Override
            public void onShow() {
                AnimationUtils.rotate(mMore, 0, 180, 300, new DecelerateInterpolator(), null).start();
            }

            @Override
            public void onHide() {
                AnimationUtils.rotate(mMore, 180, 0, 300, new DecelerateInterpolator(), null).start();
            }
        });
    }

    private void initMusicListFunctionBar() {
        mTargetLayout.setVisibility(View.GONE);
        mAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTargetLayout();
                showAlbumList();
            }
        });
        mTargetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //空点击占位
            }
        });
        mBackFun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideTargetLayout();
            }
        });
    }

    private void showAlbumList() {
        Map<Integer, MusicAlbum> musicAlbums = MusicLocalManager.getMusicAlbums();
        Ln.c("MusicAlbum " + musicAlbums);
        if (musicAlbums == null) {
            return;
        }
        Collection<MusicAlbum> valueCollection = musicAlbums.values();
        List<MusicAlbum> albums = new ArrayList<>(valueCollection);
        if (mAlbumAdapter == null) {
            mAlbumAdapter = new MusicAlbumListAdapter(mContext, albums);
            mTargetListView.setAdapter(mAlbumAdapter);
        } else {
            mAlbumAdapter.notifyData(albums);
        }
    }

    private void showTargetLayout() {
        mTargetLayout.setVisibility(View.VISIBLE);
        Animator tran = AnimationUtils.tranX(mTargetLayout, 600, 0, 300, new DecelerateInterpolator(), null);
        Animator alpha = AnimationUtils.alpha(mTargetLayout, 0, 1, 300, new DecelerateInterpolator(), null);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(tran, alpha);
        set.start();
    }

    private void hideTargetLayout() {
        Animator tran = AnimationUtils.tranX(mTargetLayout, 0, 600, 300, new DecelerateInterpolator(), null);
        Animator alpha = AnimationUtils.alpha(mTargetLayout, 1, 0, 300, new DecelerateInterpolator(), null);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(tran, alpha);
        set.start();
    }

    private void moveToPlayingLocation() {
        MusicInfo info = MusicLocalManager.getPlayingMusic();
        if (info == null) {
            ToastUtils.toast("当前没有正在播放的音乐哦～");
            return;
        }
        int pos = mMusicAdapter.getPosition(info);
        if (pos != -1) {
            mMusicListView.smoothScrollToPosition(pos);
        }
    }

    private void findView(View view) {
        mTitle = view.findViewById(R.id.tvTitle);
        mCover = view.findViewById(R.id.civCover);
        mWarn = view.findViewById(R.id.civWarn);
        mBackground = view.findViewById(R.id.flBackground);
        mPlayPreBtn = view.findViewById(R.id.ivPlayPre);
        mPlayBtn = view.findViewById(R.id.ivPlay);
        mPlayNextBtn = view.findViewById(R.id.ivPlayNext);

        mMusicListLayout = view.findViewById(R.id.fsvMusicListLayout);
        mMore = view.findViewById(R.id.ivMore);
        mHide = view.findViewById(R.id.ivHide);
        mLocation = view.findViewById(R.id.ivLocation);
//        mMusicTitle = view.findViewById(R.id.tvMusicTitle);
        mMusicListView = view.findViewById(R.id.lvMusicList);

        mGoText = view.findViewById(R.id.tvGo);
        mGoLayout = view.findViewById(R.id.llGo);

        mSeekBar = view.findViewById(R.id.pbProgressBar);

        mAlbum = view.findViewById(R.id.llAlbum);
        mTargetLayout = view.findViewById(R.id.flTargetLayout);
        mTargetListView = view.findViewById(R.id.lvTargetList);
        mFunctionLayout = view.findViewById(R.id.flFunctionLayout);
        mBackFun = view.findViewById(R.id.ivBackFun);
    }

    @Override
    public View getBackgroundView() {
        return mBackground;
    }

    //外部回调方法 后期可以考虑EventBus
    @Override
    public <T> void onEvent(T event) {
        if (event.getClass() == MusicManagerReadyEvent.class) {
            onMusicServiceReady();
        } else if (event.getClass() == OnResumeEvent.class) {
            onResume();
        } else if (event.getClass() == OnPauseEvent.class) {
            onPause();
        } else if (event.getClass() == OnDestroyEvent.class) {
            onDestroy();
        }
    }

    private void onMusicServiceReady() {
        mMusicAdapter = new MusicListAdapter(mContext, MusicLocalManager.getMusicInfos());
        mMusicListView.setAdapter(mMusicAdapter);
        reInitView();
        //音乐变更监听
        MusicLocalManager.setMusicChangedListener(new IMusicChangedCallback.Stub() {
            @Override
            public void onChanged() throws RemoteException {
                HandlerUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Ln.c("onChanged");
                        MusicSelect.this.onChanged();
                    }
                });
            }
        });
    }

    //音乐后台播放 重启app、并链接Service后，重新更新部分页面状态 这部分可以单独抽成一个方法
    private void reInitView() {
        if (MusicLocalManager.isPlaying()) {
            updatePlayingHomeLayout();
        } else {
            mPlayBtn.setImageResource(R.mipmap.ic_play);
        }
    }

    //播放按钮逻辑
    private void initPlayBtn() {

        if (MusicLocalManager.isPlaying()) {
            mPlayBtn.setImageResource(R.mipmap.ic_playing);
        } else {
            mPlayBtn.setImageResource(R.mipmap.ic_play);
        }

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicLocalManager.isPlaying()) {
                    MusicLocalManager.pause();
                    mPlayBtn.setImageResource(R.mipmap.ic_play);
                } else {
                    MusicLocalManager.resumePlay();
                    mPlayBtn.setImageResource(R.mipmap.ic_playing);
                }
            }
        });

        mPlayPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MusicLocalManager.playPre()) {
                    ToastUtils.toast("到头了～");
                }
            }
        });

        mPlayNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MusicLocalManager.playNext()) {
                    ToastUtils.toast("到头了～");
                }
            }
        });

        mGoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicListLayout.move();
            }
        });
    }

    private void onChanged() {
        //列表刷新
        mMusicAdapter.notifyData();
        updatePlayingHomeLayout();
    }

    private void updatePlayingHomeLayout() {
        //只要播放音乐 就设定图标为播放
        mPlayBtn.setImageResource(R.mipmap.ic_playing);
        MusicInfo playingMusic = MusicLocalManager.getPlayingMusic();
        if (playingMusic != null) {
            //如果音乐页面放在第二页 则会无缘无故刷新到首页 不清楚原因 只能将音乐页面放到首页
            //封面刷新
            mCover.setImageBitmap(MusicUtils.getMusicBitemp(mContext, playingMusic.getSongId(), playingMusic.getAlbumId()));
            //音乐名字刷新
            mTitle.setText(playingMusic.getMusicName());
        }
    }

    public class SeekReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("seek", 0);
            Ln.c("Seek " + pos);
            mSeekBar.setProgress(pos);
        }
    }

    private void onResume() {
        if (mMusicListLayout.isShow()) {
            MusicLocalManager.startProgress();
        }
    }

    private void onPause() {
        if (mMusicListLayout.isShow()) {
            MusicLocalManager.stopProgress();
        }
    }

    private void onDestroy() {
        MusicLocalManager.stopProgress();
        mContext.unregisterReceiver(mReceiver);
    }
}
