package com.app.dixon.resourceparser.core.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by dixon.xu on 2018/3/22.
 */

public class HandlerUtils {

    private static final Handler UI_HANDLER = new Handler(Looper.getMainLooper());
    private static final HandlerThread DEFAULT_THREAD;
    private static final Handler DEFAULT_HANDLER;

    static {
        DEFAULT_THREAD = new HandlerThread("default work thread");
        DEFAULT_THREAD.start();
        DEFAULT_HANDLER = new Handler(DEFAULT_THREAD.getLooper());
    }

    private HandlerUtils() {

    }


    public static void runOnUiThread(Runnable r) {
        if (Thread.currentThread().getId() == UI_HANDLER.getLooper().getThread().getId()) {
            r.run();
        } else {
            UI_HANDLER.post(r);
        }
    }

    public static void runOnUiThreadDelayed(Runnable r, long time) {
        UI_HANDLER.postDelayed(r, time);
    }

    public static Handler getUiHandler() {
        return UI_HANDLER;
    }

    public static Handler getDefaultHandler() {
        return DEFAULT_HANDLER;
    }

    public static void executeDelayed(Runnable r, long delayMillis) {
        getDefaultHandler().postDelayed(r, delayMillis);
    }

    public static void execute(Runnable r) {
        getDefaultHandler().post(r);
    }
}
