package com.app.dixon.resourceparser.core.util;

import android.util.Log;

/**
 * Created by dixon.xu on 2019/3/13.
 */

public class Ln {

    private static final String COMMON = "ResourceParser";

    public static void c(String s) {
        Log.e(COMMON, Thread.currentThread().getName() + " : " + s);
    }
}
