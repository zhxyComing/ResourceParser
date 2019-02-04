package com.app.dixon.resourceparser.core.pub.parser;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * 解析队列 提供队列的添加功能
 */

public class ParseQueue {

    private static final int MAX_REQUEST = 100; //最大请求数 超过限制会crash:java.lang.IllegalStateException: Queue full
    private static final int MAX_RUNNING_DISPATCHER = 3; //同时处理请求最大数 最大同时3个线程处理H5解析

    private ArrayBlockingQueue<Request> mQueue;

    private ParseDispatcher[] mDispatchers;

    public ParseQueue() {
        this(MAX_REQUEST, MAX_RUNNING_DISPATCHER);
    }

    public ParseQueue(int maxRequest, int maxDispatcher) {
        mQueue = new ArrayBlockingQueue<>(maxRequest);
        mDispatchers = new ParseDispatcher[maxDispatcher];
    }

    public void start() {
        for (int i = 0; i < mDispatchers.length; i++) {
            ParseDispatcher dispatcher = new ParseDispatcher(mQueue);
            mDispatchers[i] = dispatcher;
            dispatcher.start();
        }
    }

    public <T> void add(Request<T> request) {
        mQueue.add(request);
    }

    public <T> void add(Action<T> action) {
        mQueue.add(new Request<>(action));
    }

    public <T> void add(Action1<T> action) {
        mQueue.add(new Request<>(action));
    }

    public <T> void add(ActionPost<T> action) {
        mQueue.add(new Request<>(action));
    }

    public <T> void add(ActionPost1<T> action) {
        mQueue.add(new Request<>(action));
    }


    public void stop() {
        for (int i = 0; i < mDispatchers.length; i++) {
            if (mDispatchers[i] != null) {
                mDispatchers[i].quit();
            }
        }
    }

}
