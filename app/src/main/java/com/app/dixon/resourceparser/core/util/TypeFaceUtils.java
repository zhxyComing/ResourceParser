package com.app.dixon.resourceparser.core.util;

import android.graphics.Typeface;
import android.widget.TextView;

import com.app.dixon.resourceparser.core.pub.activity.BaseApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixon.xu on 2018/11/1.
 */

public class TypeFaceUtils {

    private static Map<String, Typeface> cache = new HashMap<>();

    private static final String YUN_BOOK = "Yun-Book.ttf";

    public static void yunBook(TextView v) {

        v.setTypeface(getType(YUN_BOOK));
    }

    public static void yunBook(TextView... v) {

        for (TextView view : v) {

            view.setTypeface(getType(YUN_BOOK));
        }
    }

    private static Typeface getType(String name) {

        Typeface typeface = cache.get(name);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(BaseApplication.getApplication().getAssets(), "font/" + name);
            cache.put(name, typeface);
        }
        return typeface;
    }
}
