package com.app.dixon.resourceparser.core.util;

import android.Manifest;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.app.dixon.resourceparser.core.pub.inter.IPermission;

import java.io.File;

/**
 * Created by dixon.xu on 2019/3/1.
 */

public class MusicUtils extends IPermission {

    public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
    public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟

    public static String[] proj_music = new String[]{
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION};

    private static final String TAG = "testkkk";

    // 音乐检索方法
    public static void getMusicList(Cursor cursor) {
        if (cursor == null) {
            return;
        }

        while (cursor.moveToNext()) {
            int songId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));
            Log.e(TAG, "id " + songId);
            int albumId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            Log.e(TAG, "album id " + albumId);
            int duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            Log.e(TAG, "duration " + duration);
            String musicName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            Log.e(TAG, "name " + musicName);
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
            Log.e(TAG, "art " + artist);
            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            Log.e(TAG, "filePath " + filePath);
            String folderPath = filePath.substring(0,
                    filePath.lastIndexOf(File.separator));
            Log.e(TAG, "folderPath " + folderPath);
        }
        cursor.close();
    }

    @Override
    protected void onRefusedPermission() {
        //弹窗

    }

    @Override
    protected String askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        return "";
    }

    @Override
    public void requestResult(int requestCode, String[] permissions, int[] grantResults) {

    }
}
