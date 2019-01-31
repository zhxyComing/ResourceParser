package com.app.dixon.resourceparser.core.pub.parser;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * Request的另一种形式
 */

public abstract class Action<T> implements Request.ParseListener<T>, Response.Listener<T> {

    public abstract String url();
}
