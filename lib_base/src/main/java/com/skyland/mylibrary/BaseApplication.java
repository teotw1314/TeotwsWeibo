package com.skyland.mylibrary;

import android.app.Application;

/**
 * Created by skyland on 2017/8/10
 */

public class BaseApplication extends Application{

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BaseApplication getInstance(){
        return instance;
    }
}
