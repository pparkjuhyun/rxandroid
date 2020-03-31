package com.pparkjuhyun.rxandroid;

import android.app.Application;

import com.pparkjuhyun.rxandroid.network.LocalVolley;

public class RxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LocalVolley.init(getApplicationContext());
    }
}
