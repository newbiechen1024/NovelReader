package com.example.newbiechen.ireader.widget.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class SlideAnimation extends AnimationProvider {
    private Rect mSrcRect, mDestRect,mNextSrcRect,mNextDestRect;

    public SlideAnimation(int width, int height) {
        super(width, height);
        mSrcRect = new Rect(0, 0, mScreenWidth, mScreenHeight);
        mDestRect = new Rect(0, 0, mScreenWidth, mScreenHeight);
        mNextSrcRect = new Rect(0, 0, mScreenWidth, mScreenHeight);
        mNextDestRect = new Rect(0, 0, mScreenWidth, mScreenHeight);
    }

    @Override
    public void drawMove(Canvas canvas) {
        if (getDirection().equals(AnimationProvider.Direction.next)){
            //左半边的剩余区域
            int dis = (int) (mScreenWidth - myStartX + mTouch.x);
            if (dis > mScreenWidth){
                dis = mScreenWidth;
            }
            //计算bitmap截取的区域
            mSrcRect.left = mScreenWidth - dis;
            //计算bitmap在canvas显示的区域
            mDestRect.right = dis;
            //计算下一页截取的区域
            mNextSrcRect.right = mScreenWidth - dis;
            //计算下一页在canvas显示的区域
            mNextDestRect.left = dis;

            canvas.drawBitmap(mNextPageBitmap,mNextSrcRect,mNextDestRect,null);
            canvas.drawBitmap(mCurPageBitmap,mSrcRect,mDestRect,null);
        }else{
            int dis = (int) (mTouch.x - myStartX);
            if (dis < 0){
                dis = 0;
                myStartX = mTouch.x;
            }
            mSrcRect.left =  mScreenWidth - dis;
            mDestRect.right = dis;

            //计算下一页截取的区域
            mNextSrcRect.right = mScreenWidth - dis;
            //计算下一页在canvas显示的区域
            mNextDestRect.left = dis;

            canvas.drawBitmap(mCurPageBitmap,mNextSrcRect,mNextDestRect,null);
            canvas.drawBitmap(mNextPageBitmap,mSrcRect,mDestRect,null);
        }
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (getCancel()){
            mNextPageBitmap = mCurPageBitmap.copy(Bitmap.Config.RGB_565, true);
            canvas.drawBitmap(mCurPageBitmap, 0, 0, null);
        }else {
            canvas.drawBitmap(mNextPageBitmap, 0, 0, null);
        }
    }

    @Override
    public void startAnimation(Scroller scroller) {

        int dx = 0;
        if (getDirection().equals(Direction.next)){
            if (getCancel()){
                int dis = (int) ((mScreenWidth - myStartX) + mTouch.x);
                if (dis > mScreenWidth){
                    dis = mScreenWidth;
                }
                dx = mScreenWidth - dis;
            }else{
                dx = (int) - (mTouch.x + (mScreenWidth - myStartX));
            }
        }else{
            if (getCancel()){
                dx = (int) - Math.abs(mTouch.x - myStartX);
            }else{
//                dx = (int) (mScreenWidth - mTouch.x);
                dx = (int) (mScreenWidth - (mTouch.x - myStartX));
            }
        }
        //滑动速度保持一致
         int duration =  (400 * Math.abs(dx)) / mScreenWidth;
        scroller.startScroll((int) mTouch.x, 0, dx, 0, duration);
    }
}
