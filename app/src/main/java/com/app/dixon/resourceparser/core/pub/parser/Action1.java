package com.app.dixon.resourceparser.core.pub.parser;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * Request的另一种形式 比Action多ErrorListener
 */

public abstract class Action1<T> implements Request.ParseListener<T>, Response.Listener<T>, Response.ErrorListener {

    public abstract String url();
}
