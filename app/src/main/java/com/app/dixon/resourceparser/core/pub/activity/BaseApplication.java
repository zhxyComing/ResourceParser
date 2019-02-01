package com.app.dixon.resourceparser.core.pub.activity;

import android.app.Application;

import com.app.dixon.resourceparser.core.manager.AsyncImageLoader;
import com.app.dixon.resourceparser.core.manager.ParserManager;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class BaseApplication extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        init();
    }

    private void init() {
        ParserManager.init();
        AsyncImageLoader.init(this);
    }

    public static Application getApplication() {
        return application;
    }
}
