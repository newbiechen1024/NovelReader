package com.example.newbiechen.ireader.utils;

import android.app.Service;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.example.newbiechen.ireader.App;

/**
 * Created by newbiechen on 17-5-11.
 */

public class NetworkUtils {


    /**
     * 获取活动网络信息
     * @return NetworkInfo
     */
    public static NetworkInfo getNetworkInfo(){
        ConnectivityManager cm = (ConnectivityManager) App
                .getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo();
    }

    /**
     * 网络是否可用
     * @return
     */
    public static boolean isAvailable(){
        NetworkInfo info = getNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 网络是否连接
     * @return
     */
    public static boolean isConnected(){
        NetworkInfo info = getNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 判断wifi是否连接状态
     * <p>需添加权限 {@code <uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>}</p>
     *
     * @param context 上下文
     * @return {@code true}: 连接<br>{@code false}: 未连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }



}
