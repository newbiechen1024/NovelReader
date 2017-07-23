package com.example.newbiechen.ireader.widget.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/8/30 0030.
 */
public class NoneAnimation extends AnimationProvider {

    public NoneAnimation(int width, int height) {
        super(width, height);
    }

    @Override
    public void drawMove(Canvas canvas) {
        if (getCancel()){
            canvas.drawBitmap(mCurPageBitmap, 0, 0, null);
        }else {
            canvas.drawBitmap(mNextPageBitmap, 0, 0, null);
        }
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (getCancel()){
            canvas.drawBitmap(mCurPageBitmap, 0, 0, null);
        }else {
            canvas.drawBitmap(mNextPageBitmap, 0, 0, null);
        }
    }

    @Override
    public void startAnimation(Scroller scroller) {

    }
}
