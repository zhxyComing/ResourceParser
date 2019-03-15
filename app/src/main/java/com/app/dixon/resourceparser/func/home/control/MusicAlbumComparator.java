package com.app.dixon.resourceparser.func.home.control;

import com.app.dixon.resourceparser.model.MusicAlbum;

import java.util.Comparator;

/**
 * Created by dixon.xu on 2019/3/15.
 */

public class MusicAlbumComparator implements Comparator<MusicAlbum> {
    @Override
    public int compare(MusicAlbum s1, MusicAlbum s2) {
        if (s1.getId() > s2.getId()) return 1;
        if (s1.getId() == s2.getId()) return 0;
        return -1;
    }
}
