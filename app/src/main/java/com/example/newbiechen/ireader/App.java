package com.example.newbiechen.ireader;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.newbiechen.ireader.service.DownloadService;

/**
 * Created by newbiechen on 17-4-15.
 */

public class App extends Application {
    private static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        startService(new Intent(getContext(), DownloadService.class));
    }

    public static Context getContext(){
        return sInstance;
    }
}