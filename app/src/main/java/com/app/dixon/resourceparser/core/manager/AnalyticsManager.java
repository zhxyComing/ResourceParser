package com.app.dixon.resourceparser.core.manager;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixon.xu on 2019/2/11.
 * <p>
 * 统计
 */

public class AnalyticsManager {

    private AnalyticsManager() {
    }

    public static void init(Context context) {
        UMConfigure.init(context, "5c611418b465f51a75001140", null, UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void onEvent(Context context, String eventId, Map<String, Object> map) {
        MobclickAgent.onEventObject(context, eventId, map);
    }

    public static class Event {
        public static void onMovieSearch(Context context, String text) {
            Map<String, Object> map = new HashMap<>();
            map.put("search_text", text);
            map.put("search_time", System.currentTimeMillis());
            onEvent(context, "movie_search", map);
        }

        public static void onTorrSearch(Context context, String text) {
            Map<String, Object> map = new HashMap<>();
            map.put("search_text", text);
            map.put("search_time", System.currentTimeMillis());
            onEvent(context, "torr_search", map);
        }
    }
}
