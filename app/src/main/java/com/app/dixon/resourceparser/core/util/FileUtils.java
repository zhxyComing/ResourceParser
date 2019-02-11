package com.app.dixon.resourceparser.core.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dixon.xu on 2019/2/11.
 */

public class FileUtils {

    public static String getFromAssets(String fileName, Context context) {

        // load text
        try {
            // get input stream for text
            InputStream is = context.getAssets().open(fileName);
            // check size
            int size = is.available();
            // create buffer for IO
            byte[] buffer = new byte[size];
            // get data to buffer
            is.read(buffer);
            // close stream
            is.close();
            // set result to TextView
            return new String(buffer);
        } catch (IOException ex) {
            return "";
        }
    }
}
