package com.app.dixon.resourceparser.core.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 基于SharedPreferences的配置存储
 */

public class SharedConfig {
    final static String RESOURCE_PARSER = "resource_parser_config";

    public static final String RUNNING_ALBUM_ID = "running_album_id";
    public static final String RUNNING_MUSIC_INFO = "running_music_info";

    private static SharedConfig mInstance;
    private static SharedPreferences mPreferences;
    private static Editor mEditor;

    SharedConfig(Context context) {
        mPreferences = context.getApplicationContext().getSharedPreferences(RESOURCE_PARSER, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public static void init(Context context) {
        if (mInstance == null) {
            mInstance = new SharedConfig(context);
        }
    }

    public static SharedConfig Instance() {
        return mInstance;
    }

    public static SharedPreferences getPreferences() {
        return mPreferences;
    }


    public boolean setRunningAlbumId(int id) {
        mEditor.putInt(RUNNING_ALBUM_ID, id);
        return mEditor.commit();
    }

    public int getRunningAlbumId() {
        return mPreferences.getInt(RUNNING_ALBUM_ID, -1);
    }

    public boolean setPlayingMusicInfo(String json) {
        mEditor.putString(RUNNING_MUSIC_INFO, json);
        return mEditor.commit();
    }

    public String getPlayingMusicInfo() {
        return mPreferences.getString(RUNNING_MUSIC_INFO, null);
    }
}

