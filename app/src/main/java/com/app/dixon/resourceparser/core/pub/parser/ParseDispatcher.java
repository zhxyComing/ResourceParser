package com.app.dixon.resourceparser.core.pub.parser;

import android.os.Handler;
import android.os.Looper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * Request处理器
 */

public class ParseDispatcher extends Thread {

    private BlockingQueue<Request<?>> mQueue;
    private boolean mQuit;
    private ExecutorDelivery delivery;

    public ParseDispatcher(BlockingQueue queue) {
        this.mQueue = queue;
        delivery = new ExecutorDelivery(new Handler(Looper.getMainLooper()));
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            Request<?> request;
            try {
                request = mQueue.take();
            } catch (InterruptedException e) {
                if (mQuit) {
                    return;
                }
                continue;
            }
            parse(request);
        }
    }

    private <T> void parse(Request<T> request) {
        try {
            Document document = Jsoup.connect(request.getUrl()).get();
            T parse = request.parse(document);
            Response<T> response = new Response<>(parse);
            delivery.postResponse(request, response);
        } catch (IOException e) {
            delivery.postError(request, new ParseError(e.getMessage()));
        }
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }
}
