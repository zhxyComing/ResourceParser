package com.app.dixon.resourceparser.core.pub.parser;

import java.util.Map;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * Request的另一种形式 比Action1多postData
 * <p>
 * 后续改造 ActionPost1尽量不要继承Action1 而直接继承其接口 否则queue.add(action)可能会有不可预计的走向错误
 */

public abstract class ActionPost1<T> extends Action1<T> {

    public abstract Map<String, String> postData();
}
