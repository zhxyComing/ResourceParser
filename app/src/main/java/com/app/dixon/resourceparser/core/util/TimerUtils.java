package com.app.dixon.resourceparser.core.util;

import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dixon.xu on 2018/1/8.
 * <p>
 * 定时器 以前写的 懒得改了
 */

public class TimerUtils {

    //注意 这里的time是延迟多少秒执行

    public static void timer(final Activity activity, long time, final TimerEvent event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                event.back();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        event.main();
                    }
                });
            }
        }, time);
    }

    public static void back(long time, final BackEvent event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                event.back();
            }
        }, time);
    }

    public static void main(Activity activity, final MainEvent event) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                event.main();
            }
        });
    }

    public static void mainDelay(long time, final Activity activity, final MainEvent event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        event.main();
                    }
                });
            }
        }, time);
    }

    public interface TimerEvent {
        void back();

        void main();
    }

    public interface BackEvent {
        void back();
    }

    public interface MainEvent {
        void main();
    }
}
