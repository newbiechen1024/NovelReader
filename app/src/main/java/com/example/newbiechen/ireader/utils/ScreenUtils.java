package com.example.newbiechen.ireader.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.example.newbiechen.ireader.App;

/**
 * Created by newbiechen on 17-5-1.
 */

public class ScreenUtils {

    public static int dpToPx(int dp){
        DisplayMetrics metrics = App
                .getContext()
                .getResources()
                .getDisplayMetrics();

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,metrics);
    }
}
