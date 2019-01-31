package com.app.dixon.resourceparser.core.pub.parser;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * 返回数据
 */

public class Response<T> {

    public final T result;

    public Response(T result) {
        this.result = result;
    }

    public interface Listener<T> {
        void onResponse(T response);
    }

    public interface ErrorListener {
        void onErrorResponse(ParseError error);
    }
}
