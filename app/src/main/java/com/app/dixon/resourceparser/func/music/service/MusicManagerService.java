package com.app.dixon.resourceparser.func.music.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.app.dixon.resourceparser.ICompleteCallback;
import com.app.dixon.resourceparser.IMusicChangedCallback;
import com.app.dixon.resourceparser.IMusicManagerService;
import com.app.dixon.resourceparser.core.manager.MusicManager;
import com.app.dixon.resourceparser.model.MusicInfo;

import java.util.List;

/**
 * 运行在:music进程，负责链接主进程与MusicManager
 * <p>
 * 通信顺序:主进程 -> @MusicManagerService -> MusicManager
 * <p>
 * 主进程与Service建立Binder连接，进而与MusicManager通信
 */

public class MusicManagerService extends Service {
    public MusicManagerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * MusicManagerService中包含了IMusicManagerService这个Binder。
     * 该Binder的接口用于与MusicManager通信。
     * <p>
     * 需要注意：MMS的启动初始化操作有一些是异步操作，如获取本机的音乐列表。
     * 所以业务方bind成功第一时间获取MusicInfos可能为空。
     */
    private final IMusicManagerService.Stub mBinder = new IMusicManagerService.Stub() {
        @Override
        public List<MusicInfo> getMusicInfos() throws RemoteException {
            return MusicManager.getMusicInfos();
        }

        //绑定后由客户端决定初始化的时机
        @Override
        public void init(ICompleteCallback cb) throws RemoteException {
            MusicManager.init(MusicManagerService.this, cb);
        }

        @Override
        public void play(MusicInfo info) throws RemoteException {
            MusicManager.play(info);
        }

        @Override
        public MusicInfo getPlayingMusic() throws RemoteException {
            return MusicManager.getPlayingMusic();
        }

        @Override
        public void setOnMusicChangedListener(IMusicChangedCallback cb) throws RemoteException {
            MusicManager.setMusicChangedCallback(cb);
        }

        @Override
        public void playBySeek(MusicInfo info, int seek) throws RemoteException {
            MusicManager.play(info, seek);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return MusicManager.isPlaying();
        }

        @Override
        public void pause() throws RemoteException {
            MusicManager.pause();
        }

        @Override
        public boolean resumePlay() throws RemoteException {
            return MusicManager.resumePlay();
        }

        @Override
        public boolean playNext() throws RemoteException {
            return MusicManager.playNext();
        }

        @Override
        public boolean playPre() throws RemoteException {
            return MusicManager.playPre();
        }
    };
}
