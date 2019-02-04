package com.app.dixon.resourceparser.core.pub.parser;

import java.util.Map;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * Request的另一种形式 比Action多postData
 */

public abstract class ActionPost<T> extends Action<T> {

    public abstract Map<String, String> postData();
}
