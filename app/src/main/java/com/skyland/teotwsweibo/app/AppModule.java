package com.skyland.teotwsweibo.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by skyland on 2017/8/11
 */
@Module
public class AppModule {

    Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Singleton
    @Provides
    Application provideApplication(){
        return application;
    }

}
