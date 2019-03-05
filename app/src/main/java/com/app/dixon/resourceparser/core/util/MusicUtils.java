package com.app.dixon.resourceparser.core.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.app.dixon.resourceparser.model.MusicInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dixon.xu on 2019/3/1.
 * <p>
 * 查询本机音乐信息
 */

public class MusicUtils {

    private static final String TAG = "music_utils";

    public static final int FILTER_SIZE = 1024 * 1024;// 1MB
    public static final int FILTER_DURATION = 60 * 1000;// 1分钟

    public static String[] proj_music = new String[]{
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION};

    // 音乐检索方法
    public static List<MusicInfo> getMusicList(Cursor cursor) {
        List<MusicInfo> infos = new ArrayList<>();
        if (cursor == null) {
            return infos;
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

            MusicInfo info = new MusicInfo(songId, albumId, duration, musicName, artist, filePath, folderPath);
            infos.add(info);
        }
        cursor.close();
        return infos;
    }

    //代理限定好条件，并直接调用utils。业务方直接使用proxy可以省略具体的查询条件与跨线程的查询操作。
    public static class Proxy {

        public interface OnQueryResultListener {
            void onComplete(List<MusicInfo> infos);
        }

        public static void queryMusicOnPhone(final Context context, final OnQueryResultListener listener) {
            //子线程检索
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //限定检索条件
                    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver cr = context.getContentResolver();
                    String select = " 1=1 " + " and " + MediaStore.Audio.Media.SIZE + " > " + MusicUtils.FILTER_SIZE +
                            " and " + MediaStore.Audio.Media.DURATION + " > " + MusicUtils.FILTER_DURATION;
                    // 查询语句：检索出时长大于1分钟，文件大小大于1MB的媒体文件

                    final List<MusicInfo> infos = MusicUtils.getMusicList(cr.query(uri, MusicUtils.proj_music,
                            select, null,
                            MediaStore.Audio.Media.ARTIST_KEY));

                    //检索完毕回归主线程
                    HandlerUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onComplete(infos);
                        }
                    });
                }
            }).start();
        }
    }
}
