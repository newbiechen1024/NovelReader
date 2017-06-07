package com.example.newbiechen.ireader.widget.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public abstract class AnimationProvider {

    public static enum Direction {
        none(true),next(true), pre(true), up(false), down(false);

        public final boolean IsHorizontal;

        Direction(boolean isHorizontal) {
            IsHorizontal = isHorizontal;
        }
    };

    protected Bitmap mCurPageBitmap,mNextPageBitmap;
    protected float myStartX;
    protected float myStartY;
    protected int myEndX;
    protected int myEndY;
    protected Direction myDirection;
    protected float mySpeed;

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected PointF mTouch = new PointF(); // 拖拽点
    private Direction direction = Direction.none;
    private boolean isCancel = false;

    public AnimationProvider(Bitmap mCurrentBitmap,Bitmap mNextBitmap,int width,int height) {
        this.mCurPageBitmap = mCurrentBitmap;
        this.mNextPageBitmap = mNextBitmap;
        this.mScreenWidth = width;
        this.mScreenHeight = height;
    }

    //绘制滑动页面
    public abstract void drawMove(Canvas canvas);

    //绘制不滑动页面
    public abstract void drawStatic(Canvas canvas);

    //设置开始拖拽点
    public void setStartPoint(float x,float y){
        myStartX = x;
        myStartY = y;
    }

    //设置拖拽点
    public void setTouchPoint(float x,float y){
        mTouch.x = x;
        mTouch.y = y;
    }

    //设置方向
    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public Direction getDirection(){
        return direction;
    }

    public void setCancel(boolean isCancel){
        this.isCancel = isCancel;
    }

    public abstract void startAnimation(Scroller scroller);

    public boolean getCancel(){
        return isCancel;
    }

    public void setParams(Bitmap mCurrentBitmap,Bitmap mNextBitmap,int width,int height){
        this.mCurPageBitmap = mCurrentBitmap;
        this.mNextPageBitmap = mNextBitmap;
        this.mScreenWidth = width;
        this.mScreenHeight = height;
    }

}
