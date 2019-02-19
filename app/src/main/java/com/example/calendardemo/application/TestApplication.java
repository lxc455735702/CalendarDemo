package com.example.calendardemo.application;

import android.app.Application;

/**
 * Created by lxc on 2019/2/19
 * e-mail ：18867762063@163.com
 */
public class TestApplication extends Application {

    private static TestApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static TestApplication getAppContext() {
        return app;
    }
}
