package com.example.newbiechen.ireader.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.example.newbiechen.ireader.R;

/**
 * Created by newbiechen on 17-5-16.
 * 基于 Android 4.4
 *
 * 主要参数说明:
 *
 * SYSTEM_UI_FLAG_FULLSCREEN : 隐藏StatusBar
 * SYSTEM_UI_FLAG_HIDE_NAVIGATION : 隐藏NavigationBar
 * SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN: 视图扩展到StatusBar的位置，并且StatusBar不消失。
 * 这里需要一些处理，一般是将StatusBar设置为全透明或者半透明。之后还需要使用fitSystemWindows=防止视图扩展到Status
 * Bar上面(会在StatusBar上加一层View，该View可被移动)
 * SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION: 视图扩展到NavigationBar的位置
 * SYSTEM_UI_FLAG_LAYOUT_STABLE:稳定效果
 * SYSTEM_UI_FLAG_IMMERSIVE_STICKY:保证点击任意位置不会退出
 *
 * 可设置特效说明:
 * 1. 全屏特效
 * 2. 全屏点击不退出特效
 * 3. 注意在19 <=sdk <=21 时候，必须通过Window设置透明栏
 */

public class SystemBarUtils {

    //设置隐藏StatusBar(点击任意地方会恢复)
    public static void hideStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= 19) {
            //App全屏，隐藏StatusBar
            setFlag(activity,View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    //隐藏NavigationBar
    public static void hideNavBar(Activity activity){
        if (Build.VERSION.SDK_INT >= 19) {
            setFlag(activity,View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    //变换StatusBar(点击任意地方不会弹出)
    public static void toggleStatusBar(Activity activity){
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        setToggleFlag(activity, option);
    }

    //变换NavigationBar
    public static void toggleNavBar(Activity activity){
        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        setToggleFlag(activity, option);
    }

    //变化StatusBar和NavigationBar
    public static void toggleAllBar(Activity activity){
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        setToggleFlag(activity, option);
    }

    public static void transparentStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= 21){
            setFlag(activity, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow()
                    .setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        }
        else if (Build.VERSION.SDK_INT >= 19){
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | attrs.flags);
            activity.getWindow().setAttributes(attrs);
        }
    }

    public static void transparentNavBar(Activity activity){
        if (Build.VERSION.SDK_INT >= 21){
            setFlag(activity, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //下面这个方法在sdk:21以上才有
            activity.getWindow()
                    .setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        }
    }

    public static void setFlag(Activity activity, int flag){
        View decorView = activity.getWindow().getDecorView();
        int option = decorView.getSystemUiVisibility() | flag;
        decorView.setSystemUiVisibility(option);
    }

    public static void setToggleFlag(Activity activity, int option){
        if (Build.VERSION.SDK_INT >= 19){
            if (isFlagUsed(activity,option)){
                clearFlag(activity,option);
            }
            else {
                setFlag(activity,option);
            }
        }
    }

    //取消flag
    public static void clearFlag(Activity activity, int flag){
        View decorView = activity.getWindow().getDecorView();
        int option = decorView.getSystemUiVisibility() & (~flag);
        decorView.setSystemUiVisibility(option);
    }

    /**
     * @param activity
     * @return flag是否已被使用
     */
    public static boolean isFlagUsed(Activity activity, int flag) {
        int currentFlag = activity.getWindow().getDecorView().getSystemUiVisibility();
        if((currentFlag & flag)
                == flag) {
            return true;
        }else {
            return false;
        }
    }
}
