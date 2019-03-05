package com.app.dixon.resourceparser.core.manager;

import android.content.Context;
import android.os.RemoteException;

import com.app.dixon.resourceparser.ICompleteCallback;
import com.app.dixon.resourceparser.core.util.MusicUtils;
import com.app.dixon.resourceparser.model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dixon.xu on 2019/3/5.
 * <p>
 * 运行在进程[:music]里 即音乐相关信息都在音乐进程里
 * <p>
 * 通信顺序:主进程 -> MusicManagerService -> @MusicManager
 */

public class MusicManager {

    private static List<MusicInfo> mMusicInfos = new ArrayList<>();

    //MusicManager的初始化操作
    public static void init(Context context, final ICompleteCallback callback) {
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

    public static List<MusicInfo> getMusicInfos() {
        return mMusicInfos;
    }
}
