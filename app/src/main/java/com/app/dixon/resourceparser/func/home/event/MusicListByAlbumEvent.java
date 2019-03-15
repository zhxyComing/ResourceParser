package com.app.dixon.resourceparser.func.home.event;

import com.app.dixon.resourceparser.model.MusicAlbum;

/**
 * Created by dixon.xu on 2019/3/15.
 */

public class MusicListByAlbumEvent {

    private MusicAlbum album;

    public MusicAlbum getAlbum() {
        return album;
    }

    public void setAlbum(MusicAlbum album) {
        this.album = album;
    }

    public MusicListByAlbumEvent(MusicAlbum album) {
        this.album = album;
    }
}
