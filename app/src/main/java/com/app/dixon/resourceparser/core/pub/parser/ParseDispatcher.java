package com.app.dixon.resourceparser.core.pub.parser;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Connection;
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
            Document document;
            if (request.getPostData() == null || request.getPostData().size() == 0) {
                document = Jsoup.connect(request.getUrl()).get();
            } else {
                //有postData,走post请求
                Connection data = Jsoup.connect(request.getUrl())
                        .data(request.getPostData());
                if (!TextUtils.isEmpty(request.getCharSet())) {
                    data.postDataCharset(request.getCharSet());//坑爹的dy2018不使用URLEncode的UTF-8而使用GBK
                }
                document = data.post();
            }
            T parse = request.parse(document);
            //解析结果为空 说明当前页面拿不到需要的数据 走异常逻辑
            //后续改造:不要直接返回T 返回T的封装类 类中包括是否异常的bool值 以这个值来决定是否走异常 而不是判断空
            if (parse == null) {
                delivery.postError(request, new ParseError("parse result is null!"));
                return;
            }
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
