package com.app.dixon.resourceparser.core.manager;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.RemoteException;
import android.text.TextUtils;

import com.app.dixon.resourceparser.ICompleteCallback;
import com.app.dixon.resourceparser.IMusicChangedCallback;
import com.app.dixon.resourceparser.core.util.Ln;
import com.app.dixon.resourceparser.core.util.MusicUtils;
import com.app.dixon.resourceparser.model.MusicAlbum;
import com.app.dixon.resourceparser.model.MusicInfo;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public static final String PROGRESS_ACTION = "com.dixon.music.progress";
    public static final int ID_ALL_MUSIC_ALBUM = -199; //所有音乐的专辑ID

    private static IMusicChangedCallback mMusicChangedCallback;
    private static List<MusicInfo> mAllMusicInfos = new ArrayList<>();
    private static Map<Integer, MusicAlbum> mMusicAlbums = new LinkedHashMap<>(); //专辑列表 其中第一个为所有音乐
    private static MediaPlayer mPlayer = new MediaPlayer();
    private static MusicInfo mPlayingMusic;
    private static int mSeek;
    private static ProgressThread mProgressThread;
    private static List<MusicInfo> mRunningMusicInfos = new ArrayList<>();

    //MusicManager的初始化操作
    public static void init(Context context, final ICompleteCallback callback) {
        initPlay();
        //非冷启动 无须刷新列表 除非用户手动去刷
        if (mAllMusicInfos.size() != 0) {
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
                mAllMusicInfos.clear();
                mAllMusicInfos.addAll(infos);
                parseToAlbums();
                loadCacheAlbum();
                loadCacheMusic();
                if (callback != null) try {
                    callback.onComplete();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void loadCacheAlbum() {
        //加载缓存列表
        int cacheId = SharedConfig.Instance().getRunningAlbumId();
        if (cacheId != -1) {
            MusicAlbum album = mMusicAlbums.get(cacheId);
            if (album != null) {
                mRunningMusicInfos = album.getInfos();
            } else {
                mRunningMusicInfos = mAllMusicInfos;
            }
        } else {
            mRunningMusicInfos = mAllMusicInfos;
        }
    }

    private static void loadCacheMusic() {
        //加载缓存音乐
        if (mPlayingMusic == null) {
            String musicCache = SharedConfig.Instance().getPlayingMusicInfo();
            if (!TextUtils.isEmpty(musicCache)) {
                mPlayingMusic = new Gson().fromJson(musicCache, MusicInfo.class);
            }
        }
    }

    private static void parseToAlbums() {
        //首个Item为所有音乐的集合
        MusicAlbum allMusicAlbum = new MusicAlbum();
        allMusicAlbum.setName("所有音乐");
        allMusicAlbum.setId(ID_ALL_MUSIC_ALBUM);
        allMusicAlbum.setInfos(mAllMusicInfos);
        mMusicAlbums.put(ID_ALL_MUSIC_ALBUM, allMusicAlbum);
        //开始解析专辑数据
        for (MusicInfo info : mAllMusicInfos) {
            int albumId = info.getAlbumId();
            if (mMusicAlbums.containsKey(albumId)) {
                mMusicAlbums.get(albumId).getInfos().add(info);
            } else {
                MusicAlbum album = new MusicAlbum();
                album.setId(albumId);
                album.setName(info.getArtist());
                ArrayList<MusicInfo> list = new ArrayList<>();
                list.add(info);
                album.setInfos(list);
                mMusicAlbums.put(albumId, album);
            }
        }
        Ln.c("MusicAlbum init " + mMusicAlbums);
    }

    public static Map<Integer, MusicAlbum> getMusicAlbums() {
        return mMusicAlbums;
    }

    private static void initPlay() {
        //自动播放下一首
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Ln.c("onCompletion");
                playNext();
            }
        });
    }

    public static List<MusicInfo> getMusicInfos() {
        return mRunningMusicInfos;
    }

    public static void play(final MusicInfo info) {
        Ln.c("reset music 0");
        if (isPlayingMusic(info)) {
            return;
        }
        mPlayer.reset();
        try {
            mPlayer.setDataSource(info.getFilePath());//设置播放mp3文件的路径
            mPlayer.prepare();//播放准备
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Ln.c("start music 0");
                    mPlayer.start();//播放开始
                    mPlayingMusic = info;
                    musicChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPlayingMusic(MusicInfo info) {
        if (mPlayingMusic == null) return false;
        return mPlayingMusic.equals(info);
    }

    public static void startProgress(Context context) {
        if (mProgressThread == null) {
            mProgressThread = new ProgressThread(context);
            mProgressThread.start();
        } else {
            stopProgress();
            startProgress(context);
        }
    }

    public static void stopProgress() {
        if (mProgressThread != null) {
            mProgressThread.setStop(true);
            mProgressThread = null;
        }
    }

    private static class ProgressThread extends Thread {
        private Context mContext;
        private boolean isStop;

        public ProgressThread(Context context) {
            this.mContext = context;
        }

        public void setStop(boolean isStop) {
            this.isStop = isStop;
        }

        @Override
        public void run() {
            super.run();
            while (!isStop) {
                if (isPlaying()) {
                    int currentPosition = mPlayer.getCurrentPosition();
                    Intent intent = new Intent(PROGRESS_ACTION);
                    if (mPlayingMusic == null) {
                        break;
                    }
                    intent.putExtra("seek", currentPosition * 100 / mPlayingMusic.getDuration());
                    mContext.sendBroadcast(intent);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
                syncMusicInfoCache();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private static void syncMusicInfoCache() {
        if (mPlayingMusic == null) return;
        SharedConfig.Instance().setPlayingMusicInfo(new Gson().toJson(mPlayingMusic));
    }

    private static void syncMusicAlbumCache(int id) {
        if (mRunningMusicInfos == null) return;
        SharedConfig.Instance().setRunningAlbumId(id);
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
        Ln.c("Resume");
        //说明是续播
        if (mPlayingMusic != null) {
            play(mPlayingMusic, mSeek);
            return true;
        }
        //新打开 列表从头播放
        if (mRunningMusicInfos.size() != 0) {
            play(mRunningMusicInfos.get(0));
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
        Ln.c("play next");
        if (mPlayingMusic == null) {
            //新打开 列表从头播放
            if (mRunningMusicInfos.size() != 0) {
                play(mRunningMusicInfos.get(0));
                return true;
            }
            return false;
        }
        for (int i = 0; i < mRunningMusicInfos.size(); i++) {
            MusicInfo info = mRunningMusicInfos.get(i);
            if (info.equals(mPlayingMusic)) {
                if (i == mRunningMusicInfos.size() - 1) {
                    //到头了 不能next
                    return false;
                }
                play(mRunningMusicInfos.get(i + 1));
                return true;
            }
        }
        return false;
    }

    //算法可优化
    public static boolean playPre() {
        Ln.c("play pre");
        if (mPlayingMusic == null) {
            //新打开 列表从头播放
            if (mRunningMusicInfos.size() != 0) {
                play(mRunningMusicInfos.get(0));
                return true;
            }
            return false;
        }
        for (int i = 0; i < mRunningMusicInfos.size(); i++) {
            MusicInfo info = mRunningMusicInfos.get(i);
            if (info.equals(mPlayingMusic)) {
                if (i == 0) {
                    //到头了 不能pre
                    return false;
                }
                play(mRunningMusicInfos.get(i - 1));
                return true;
            }
        }
        return false;
    }

    public static boolean setMusicListByAlbum(int id) {
        MusicAlbum album = mMusicAlbums.get(id);
        if (album != null) {
            mRunningMusicInfos = album.getInfos();
            syncMusicAlbumCache(id);
            return true;
        }
        return false;
    }
}
