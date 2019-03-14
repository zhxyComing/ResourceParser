package com.app.dixon.resourceparser.func.music.control;

import android.os.RemoteException;

import com.app.dixon.resourceparser.IMusicChangedCallback;
import com.app.dixon.resourceparser.IMusicManagerService;
import com.app.dixon.resourceparser.model.MusicAlbum;
import com.app.dixon.resourceparser.model.MusicInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by dixon.xu on 2019/3/7.
 * <p>
 * 主进程控制音乐播放及UI响应
 * <p>
 * 主进程对音乐的操作不直接使用IMusicManagerService，原因有俩点：
 * 1.频繁的try-catch
 * 2.IMusicManagerService依赖与MusicActivity，为了脱离依赖故将IMMS赋值给该单例类。(这样做的主要目的是：虽然当前音乐播放确实依赖页面，但是后续的功能想即使跳离到其它页面，也能通过悬浮窗控制音乐的播放)
 * 风险就是存在内存泄漏，因为IMMS作为成员变量本应在Activity销毁时一并销毁，直到重新绑定才能重新拿到！
 * 所以一定记得手动销毁！
 */

public class MusicLocalManager {

    private static IMusicManagerService readyService;

    private MusicLocalManager() {
    }

    public static void initReadyService(IMusicManagerService readyService) {
        MusicLocalManager.readyService = readyService;
    }

    public static void play(MusicInfo musicInfo) {
        if (readyService == null) {
            return;
        }
        try {
            readyService.play(musicInfo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static List<MusicInfo> getMusicInfos() {
        if (readyService == null) {
            return null;
        }
        try {
            return readyService.getMusicInfos();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Integer, MusicAlbum> getMusicAlbums() {
        if (readyService == null) {
            return null;
        }
        try {
            return readyService.getMusicAlbums();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    //mReadyService作为bind后返回的binder对象 解绑后不存在 记得手动销毁
    public static void destroy() {
        readyService = null;
    }

    //注意
    public static void setMusicChangedListener(IMusicChangedCallback.Stub cb) {
        if (readyService == null) {
            return;
        }
        try {
            readyService.setOnMusicChangedListener(cb);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static MusicInfo getPlayingMusic() {
        if (readyService == null) {
            return null;
        }
        try {
            return readyService.getPlayingMusic();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean resumePlay() {
        if (readyService == null) {
            return false;
        }
        try {
            return readyService.resumePlay();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void pause() {
        if (readyService == null) {
            return;
        }
        try {
            readyService.pause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static boolean playNext() {
        if (readyService == null) {
            return false;
        }
        try {
            return readyService.playNext();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean playPre() {
        if (readyService == null) {
            return false;
        }
        try {
            return readyService.playPre();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPlaying() {
        if (readyService == null) {
            return false;
        }
        try {
            return readyService.isPlaying();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void startProgress() {
        if (readyService == null) {
            return;
        }
        try {
            readyService.startProgress();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void stopProgress() {
        if (readyService == null) {
            return;
        }
        try {
            readyService.stopProgress();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
