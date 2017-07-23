package com.example.newbiechen.ireader.widget.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.Log;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.example.newbiechen.ireader.utils.ScreenUtils;
import com.example.newbiechen.ireader.widget.page.PageLoader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by newbiechen on 17-7-22.
 */

public class ScrollAnimation {
    private static final String TAG = "ScrollAnimation";

    private int mScreenWidth;
    private int mScreenHeight;

    private int mMarginWidth;
    private int mMarginHeight;

    private int mViewWidth;
    private int mViewHeight;

    private int mStartX;
    private int mStartY;

    private int mTouchX;
    private int mTouchY;

    private int mLastX;
    private int mLastY;

    //是否处于初始化阶段
    private boolean isInit = true;

    //被废弃的图片
    private ArrayDeque<BitmapView> mScrapViews;
    //正在被利用的图片
    private ArrayList<BitmapView> mActiveViews = new ArrayList<>(2);

    //整个Bitmap的背景显示
    private Bitmap mBgBitmap;
    private Bitmap mNextBitmap;

    private OnPageChangeListener mListener;

    private Scroller mScroller;

/*    //存储初始的Bitmap的Pixels (为了之后保证上下滑动的透明效果，需要对Bitmap进行擦除再现)
    private int[] mInitBitmapPix = null;*/

    public ScrollAnimation(int w,int h,OnPageChangeListener listener,Scroller scroller){
        this(w, h,0,0,listener,scroller);
    }

    public ScrollAnimation(int w, int h, int marginWidth, int marginHeight,
                           OnPageChangeListener listener,Scroller scroller){
        mScreenWidth = w;
        mScreenHeight = h;

        mMarginWidth = marginWidth;
        mMarginHeight = marginHeight;

        mViewWidth = mScreenWidth - mMarginWidth * 2;
        mViewHeight = mScreenHeight - mMarginHeight * 2;

        mListener = listener;
        mScroller = scroller;
        //创建两个BitmapView
        initBitmapView();
    }

    private void initBitmapView(){
        mBgBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);

        mScrapViews = new ArrayDeque<>(2);
        for (int i=0; i<2; ++i){
            BitmapView view = new BitmapView();
            view.bitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);
            view.srcRect = new Rect(0, 0, mViewWidth, mViewHeight);
            view.destRect = new Rect(0, 0, mViewWidth, mViewHeight);
            view.top = 0;
            view.bottom = view.bitmap.getHeight();

            mScrapViews.push(view);
        }
        onLayout();
        isInit = false;
    }

    //绘制效果
    public void draw(Canvas canvas){
        //进行布局
        onLayout();
        //绘制背景
        canvas.drawBitmap(mBgBitmap,0,0,null);
        //绘制内容
        canvas.save();
        //移动位置
        canvas.translate(0, mMarginHeight);
        //裁剪显示区域
        canvas.clipRect(0, 0, mViewWidth, mViewHeight);
/*        //设置背景透明
        canvas.drawColor(0x40);*/
        //绘制Bitmap
        for (int i=0; i<mActiveViews.size(); ++i){
            BitmapView view = mActiveViews.get(i);
            canvas.drawBitmap(view.bitmap,view.srcRect,view.destRect,null);
        }
        canvas.restore();
    }

    public Bitmap getBgBitmap(){
        return mBgBitmap;
    }

    public Bitmap getNextBitmap(){
        return mNextBitmap;
    }

    public void setStartPoint(int x, int y){
        mStartX = x;
        mStartY = y;

        mLastX = mStartX;
        mLastY = mStartY;
    }

    public void setTouchPoint(int x, int y){
        mLastX = mTouchX;
        mLastY = mTouchY;

        mTouchX = x;
        mTouchY = y;
    }

    //修改布局,填充内容
    private void onLayout(){
        //如果还没有开始加载，则从上到下进行绘制
        if (mActiveViews.size() == 0){
            fillDown(0,0);
        }
        else {
            int offset = mTouchY - mLastY;
            //判断是下滑还是上拉 (下滑)
            if (offset > 0){
                int topEdge = mActiveViews.get(0).top;
                fillUp(topEdge,offset);
            }
            //上拉
            else {
                //底部的距离 = 当前底部的距离 + 滑动的距离 (因为上滑，得到的值肯定是负的)
                int bottomEdge = mActiveViews.get(mActiveViews.size() - 1).bottom;
                fillDown(bottomEdge,offset);
            }
        }
    }

    //底部填充

    /**
     * 创建View填充底部空白部分
     * @param bottomEdge :当前最后一个View到底部的距离
     */
    private void fillDown(int bottomEdge,int offset){
        //首先进行布局的调整
        Iterator<BitmapView> it = mActiveViews.iterator();
        while (it.hasNext()){
            BitmapView view = it.next();
            view.top = view.top + offset;
            view.bottom = view.bottom + offset;
            //设置允许显示的范围
            view.destRect.top = view.top;
            view.destRect.bottom = view.bottom;

            //判断是否越界了
            if (view.bottom <= 0){
                //添加到废弃的View中
                mScrapViews.add(view);
                //从Active中移除
                it.remove();
            }
        }

        //再进行布局的添加
        int realEdge = bottomEdge + offset;

        while (realEdge < mViewHeight && mActiveViews.size() < 2){
            //从废弃的Views中获取一个
            BitmapView view = mScrapViews.poll();

/*            //擦除其Bitmap(重新创建会不会更好一点)
            eraseBitmap(view.bitmap,view.bitmap.getWidth(),view.bitmap.getHeight(),0,0);*/

            //添加到存活的Bitmap中
            mActiveViews.add(view);
            //设置Bitmap的范围
            view.top = realEdge;
            view.bottom = realEdge + view.bitmap.getHeight();
            //设置允许显示的范围
            view.destRect.top = view.top;
            view.destRect.bottom = view.bottom;

            Bitmap cancelBitmap = mNextBitmap;
            mNextBitmap = view.bitmap;
            if (!isInit){
                boolean hasNext =  mListener.hasNext(); //如果不成功则无法滑动
                //如果不存在next,则移除刚添加的View,并退出
                if (!hasNext){
                    mActiveViews.remove(view);
                    mScrapViews.add(view);
                    mNextBitmap = cancelBitmap;
                    abortAnimation();
                    break;
                }
            }

            realEdge += view.bitmap.getHeight();
        }
    }

    public void startAnimation(int velocityY){
        //进行滚动
        mScroller.fling(0,mTouchY,0,velocityY,0,0,0,99999999);
    }

    private void abortAnimation(){
        if (!mScroller.isFinished()){
            mScroller.abortAnimation();
        }
    }

    private void eraseBitmap(Bitmap b, int width, int height,
                             int paddingLeft, int paddingTop){
     /*   if (mInitBitmapPix == null) return;
        b.setPixels(mInitBitmapPix, 0, width, paddingLeft, paddingTop, width, height);*/
    }

    /**
     * 创建View填充顶部空白部分
     * @param topEdge
     * @param offset
     */
    private void fillUp(int topEdge,int offset){
        //首先进行布局的调整
        Iterator<BitmapView> it = mActiveViews.iterator();
        while (it.hasNext()){
            BitmapView view = it.next();
            view.top = view.top + offset;
            view.bottom = view.bottom + offset;
            //设置允许显示的范围
            view.destRect.top = view.top;
            view.destRect.bottom = view.bottom;

            //判断是否越界了
            if (view.top >= mViewHeight){
                //添加到废弃的View中
                mScrapViews.add(view);
                //从Active中移除
                it.remove();
            }
        }

        int realEdge = topEdge + offset;
        while (realEdge > 0 && mActiveViews.size() < 2){
            //从废弃的Views中获取一个
            BitmapView view = mScrapViews.poll();
            mActiveViews.add(0,view);

            //设置Bitmap的范围
            view.top = realEdge - view.bitmap.getHeight();
            view.bottom = realEdge;

            //设置允许显示的范围
            view.destRect.top = view.top;
            view.destRect.bottom = view.bottom;

            Bitmap cancelBitmap = mNextBitmap;
            mNextBitmap = view.bitmap;
            if (!isInit){
                boolean hasPrev =  mListener.hasPrev(); //如果不成功则无法滑动
                //如果不存在next,则移除刚添加的View,并退出
                if (!hasPrev){
                    mActiveViews.remove(view);
                    mScrapViews.add(view);
                    mNextBitmap = cancelBitmap;
                    abortAnimation();
                    break;
                }
            }
            realEdge -= view.bitmap.getHeight();
        }
    }

    //重置当前位移状态 ==> 将ActiveViews全部删除，然后重新加载
    public void refreshContent(){
        isInit = true;
        //将所有的Active加入到Scrap中
        for (BitmapView view : mActiveViews){
            mScrapViews.add(view);
        }
        //清除所有的Active
        mActiveViews.clear();
        //重新进行布局
        onLayout();
        isInit = false;
    }

    public interface OnPageChangeListener {
        boolean hasPrev();
        boolean hasNext();
    }

    private static class BitmapView{
        Bitmap bitmap;
        Rect srcRect;
        Rect destRect;
        int top;
        int bottom;
    }
}
