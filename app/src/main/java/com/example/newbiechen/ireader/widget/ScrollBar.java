package com.example.newbiechen.ireader.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by newbiechen on 17-6-4.
 */

public class ScrollBar extends View {
    private static final String TAG = "ScrollBar";
    public ScrollBar(Context context) {
        this(context,null);
    }

    public ScrollBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        Log.d(TAG, "onTouchEvent: "+y);
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                setTranslationY(getTranslationY()+y);
/*                Log.d(TAG, "onTouchEvent: "+y);
                setTranslationY(5+getTranslationY());*/
                break;
        }
        return true;
    }
}
