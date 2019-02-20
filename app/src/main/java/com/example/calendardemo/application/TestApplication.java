package com.example.calendardemo.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by lxc on 2019/2/19
 * e-mail ：18867762063@163.com
 */
public class TestApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        TestApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TestApplication.context;
    }


}
