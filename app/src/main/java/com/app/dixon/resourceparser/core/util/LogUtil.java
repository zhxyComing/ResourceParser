package com.app.dixon.resourceparser.core.util;

import android.util.Log;

/**
 * Created by dixon.xu on 2018/4/4.
 * <p>
 * 原先的logUtil同样有问题 无法全部显示 这里暂时处理 之后完善
 */

public class LogUtil {

    public static void e(String key, String log) {

        if (log.length() > 4000) {
            for (int i = 0; i < log.length(); i += 4000) {
                if (i + 4000 < log.length())
                    Log.e(key + " " + i, log.substring(i, i + 4000));
                else
                    Log.e(key + " " + i, log.substring(i, log.length()));
            }
        } else
            Log.e(key, log);
    }

}
