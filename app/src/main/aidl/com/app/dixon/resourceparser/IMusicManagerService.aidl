// IMusicManagerService.aidl
package com.app.dixon.resourceparser;

// Declare any non-default types here with import statements
import com.app.dixon.resourceparser.model.MusicInfo;
import com.app.dixon.resourceparser.model.MusicAlbum;
import com.app.dixon.resourceparser.ICompleteCallback;
import com.app.dixon.resourceparser.IMusicChangedCallback;

interface IMusicManagerService {
    /**
     * 通用的音乐列表获取方法 但是该方法存在问题：当MusicManager初始化时，获取到的音乐列表为空，通常用于初始化成功后使用。
     */
    List<MusicInfo> getMusicInfos();

    //这里带数据类型无法编译...擦
    Map getMusicAlbums();

    /**
     * MusicManager不会自己去获取音乐列表 需要业务方手动调用init初始化 初始化complete后可以获取到音乐列表
     */
    void init(ICompleteCallback cb);

    void play(in MusicInfo info);

    void startProgress();

    void stopProgress();

    MusicInfo getPlayingMusic();

    void setOnMusicChangedListener(IMusicChangedCallback cb);

    void playBySeek(in MusicInfo info, int seek);

    boolean isPlaying();

    void pause();

    boolean resumePlay();

    boolean playNext();

    boolean playPre();

    boolean setMusicListByAlbum(int id);
}
