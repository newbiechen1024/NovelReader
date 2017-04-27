package com.example.newbiechen.ireader;

import android.app.Application;
import android.content.Context;

/**
 * Created by newbiechen on 17-4-15.
 */

public class App extends Application {
    private static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Context getContext(){
        return sInstance;
    }
}
