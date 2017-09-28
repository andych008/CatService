package com.founder.catservice;

import android.app.Application;

import timber.log.Timber;

/**
 * MyApp
 * @author 喵叔catuncle
 * Created at 2017/9/11 14:27
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
