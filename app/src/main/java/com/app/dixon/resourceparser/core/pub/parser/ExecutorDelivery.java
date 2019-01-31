package com.app.dixon.resourceparser.core.pub.parser;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * 指定线程分发器
 */

public class ExecutorDelivery implements ResponseDelivery {

    private final Executor executor;

    public ExecutorDelivery(final Handler handler) {
        executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    @Override
    public <T> void postResponse(final Request<T> request, final Response<T> response) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //指定线程执行下面代码
                request.deliverResponse(response.result);
            }
        });
    }

    @Override
    public void postError(final Request<?> request, final ParseError error) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //指定线程执行下面代码
                request.deliverError(error);
            }
        });
    }
}
