package com.app.dixon.resourceparser.core.manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.RemoteException;

import com.app.dixon.resourceparser.ICompleteCallback;
import com.app.dixon.resourceparser.IMusicChangedCallback;
import com.app.dixon.resourceparser.core.util.MusicUtils;
import com.app.dixon.resourceparser.model.MusicInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dixon.xu on 2019/3/5.
 * <p>
 * 运行在进程[:music]里 即音乐相关信息都在音乐进程里
 * <p>
 * 通信顺序:主进程 -> MusicManagerService -> @MusicManager
 * <p>
 * 目前逻辑：
 * 冷启动刷新列表
 * 普通启动不再刷新 除非用户手动去刷
 */

public class MusicManager {

    private static IMusicChangedCallback mMusicChangedCallback;
    private static List<MusicInfo> mMusicInfos = new ArrayList<>();
    private static MediaPlayer mPlayer = new MediaPlayer();
    private static MusicInfo mPlayingMusic;
    private static int mSeek;

    //MusicManager的初始化操作
    public static void init(Context context, final ICompleteCallback callback) {
        initPlay();
        //非冷启动 无须刷新列表 除非用户手动去刷
        if (mMusicInfos.size() != 0) {
            if (callback != null) try {
                callback.onComplete();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return;
        }
        MusicUtils.Proxy.queryMusicOnPhone(context, new MusicUtils.Proxy.OnQueryResultListener() {
            @Override
            public void onComplete(List<MusicInfo> infos) {
                mMusicInfos.clear();
                mMusicInfos.addAll(infos);
                if (callback != null) try {
                    callback.onComplete();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void initPlay() {
        //自动播放下一首
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
    }

    public static List<MusicInfo> getMusicInfos() {
        return mMusicInfos;
    }

    public static void play(final MusicInfo info) {
        mPlayer.reset();
        try {
            mPlayer.setDataSource(info.getFilePath());//设置播放mp3文件的路径
            mPlayer.prepare();//播放准备
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mPlayer.start();//播放开始
                    mPlayingMusic = info;
                    musicChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MusicInfo getPlayingMusic() {
        return mPlayingMusic;
    }

    public static void setMusicChangedCallback(IMusicChangedCallback musicChangedCallback) {
        MusicManager.mMusicChangedCallback = musicChangedCallback;
    }

    private static void musicChanged() {
        if (mMusicChangedCallback != null) {
            try {
                mMusicChangedCallback.onChanged();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    //暂停 只关注从播放到停止的情况
    public static void pause() {
        mPlayer.pause();
        mSeek = mPlayer.getCurrentPosition();
    }

    //播放 只关注当前从停止到播放的情况 不关注正在播放的情况
    public static boolean resumePlay() {
        //说明是续播
        if (mPlayingMusic != null) {
            play(mPlayingMusic, mSeek);
            return true;
        }
        //新打开 列表从头播放
        if (mMusicInfos.size() != 0) {
            play(mMusicInfos.get(0));
            return true;
        }
        return false;
    }

    public static void play(final MusicInfo info, int seek) {
        mPlayer.reset();
        try {
            mPlayer.setDataSource(info.getFilePath());//设置播放mp3文件的路径
            mPlayer.prepare();//播放准备
            mPlayer.seekTo(seek);
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mPlayer.start();//播放开始
                    mPlayingMusic = info;
                    musicChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //算法可优化
    public static boolean playNext() {
        if (mPlayingMusic == null) {
            //新打开 列表从头播放
            if (mMusicInfos.size() != 0) {
                play(mMusicInfos.get(0));
                return true;
            }
            return false;
        }
        for (int i = 0; i < mMusicInfos.size(); i++) {
            MusicInfo info = mMusicInfos.get(i);
            if (info.equals(mPlayingMusic)) {
                if (i == mMusicInfos.size() - 1) {
                    //到头了 不能next
                    return false;
                }
                play(mMusicInfos.get(i + 1));
                return true;
            }
        }
        return false;
    }

    //算法可优化
    public static boolean playPre() {
        if (mPlayingMusic == null) {
            //新打开 列表从头播放
            if (mMusicInfos.size() != 0) {
                play(mMusicInfos.get(0));
                return true;
            }
            return false;
        }
        for (int i = 0; i < mMusicInfos.size(); i++) {
            MusicInfo info = mMusicInfos.get(i);
            if (info.equals(mPlayingMusic)) {
                if (i == 0) {
                    //到头了 不能pre
                    return false;
                }
                play(mMusicInfos.get(i - 1));
                return true;
            }
        }
        return false;
    }
}
