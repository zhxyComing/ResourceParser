package com.app.dixon.resourceparser.core.pub.parser;


/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * 生成解析队列
 */

public class Parser {

    public static ParseQueue newParseQueue() {
        ParseQueue queue = new ParseQueue();
        queue.start();
        return queue;
    }

    public static ParseQueue newParseQueue(int maxRequest, int maxDispatcher) {
        ParseQueue queue = new ParseQueue(maxRequest, maxDispatcher);
        queue.start();
        return queue;
    }
}
