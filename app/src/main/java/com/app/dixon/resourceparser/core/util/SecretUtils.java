package com.app.dixon.resourceparser.core.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by dixon.xu on 2019/2/22.
 * <p>
 * 临时简单加解密
 */

public class SecretUtils {

    //编码
    public static String simpleEncrypt(String text) {
        try {
            return Base64.encodeToString(text.getBytes("utf-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    //解码
    public static String simpleDecrypt(String password) {
        try {
            return new String(Base64.decode(password, Base64.NO_WRAP), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
