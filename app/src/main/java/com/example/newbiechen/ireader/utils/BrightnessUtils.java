package com.example.newbiechen.ireader.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by newbiechen on 17-5-19.
 * 调节亮度的工具类
 */

public class BrightnessUtils {
    private static final String TAG = "BrightnessUtils";

    /**
     * 判断是否开启了自动亮度调节
     */
    public static boolean isAutoBrightness(Activity activity) {
        boolean isAuto = false;
        try {
            isAuto = Settings.System.getInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e){
            e.printStackTrace();
        }
        return isAuto;
    }

    /**
     * 获取屏幕的亮度
     * 系统亮度模式中，自动模式与手动模式获取到的系统亮度的值不同
     */
    public static int getScreenBrightness(Activity activity) {
        if(isAutoBrightness(activity)){
            return getAutoScreenBrightness(activity);
        }else{
            return getManualScreenBrightness(activity);
        }
    }

    /**
     * 获取手动模式下的屏幕亮度
     * @return value:0~255
     */
    public static int getManualScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 获取自动模式下的屏幕亮度
     * @return value:0~255
     */
    public static int getAutoScreenBrightness(Activity activity) {
        float nowBrightnessValue = 0;

        //获取自动调节下的亮度范围在 0~1 之间
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getFloat(resolver, Settings.System.SCREEN_BRIGHTNESS);
            Log.d(TAG, "getAutoScreenBrightness: " + nowBrightnessValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换范围为 (0~255)
        float fValue = nowBrightnessValue * 225.0f;
        Log.d(TAG,"brightness: " + fValue);
        return (int)fValue;
    }

    /**
     * 设置亮度:通过设置 Windows 的 screenBrightness 来修改当前 Windows 的亮度
     * lp.screenBrightness:参数范围为 0~1
     */
    public static void setBrightness(Activity activity, int brightness) {
        try{
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            //将 0~255 范围内的数据，转换为 0~1
            lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
            Log.d(TAG, "lp.screenBrightness == " + lp.screenBrightness);
            activity.getWindow().setAttributes(lp);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 获取当前系统的亮度
     * @param activity
     */
    public static void setDefaultBrightness(Activity activity) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            activity.getWindow().setAttributes(lp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
