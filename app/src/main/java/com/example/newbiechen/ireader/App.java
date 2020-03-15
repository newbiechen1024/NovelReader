package com.example.newbiechen.ireader;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.newbiechen.ireader.service.DownloadService;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by newbiechen on 17-4-15.
 */

public class App extends Application {
    private static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // TODO:暂时没空适配高版本
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startService(new Intent(getContext(), DownloadService.class));
        }

        // 初始化内存分析工具
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    public static Context getContext() {
        return sInstance;
    }
}