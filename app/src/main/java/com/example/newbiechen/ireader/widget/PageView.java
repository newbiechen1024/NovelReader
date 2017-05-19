package com.example.newbiechen.ireader.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.example.newbiechen.ireader.widget.animation.AnimationProvider;
import com.example.newbiechen.ireader.widget.animation.CoverAnimation;
import com.example.newbiechen.ireader.widget.animation.NoneAnimation;
import com.example.newbiechen.ireader.widget.animation.SimulationAnimation;
import com.example.newbiechen.ireader.widget.animation.SlideAnimation;

/**
 * Created by Administrator on 2016/8/29 0029.
 * 原作者的GitHub Project Path:(https://github.com/PeachBlossom/treader)
 * 绘制页面显示内容的类
 */
public class PageView extends View {
    public final static int PAGE_MODE_SIMULATION = 0;
    public final static int PAGE_MODE_COVER = 1;
    public final static int PAGE_MODE_SLIDE = 2;
    public final static int PAGE_MODE_NONE = 3;

    private final static String TAG = "BookPageWidget";

    private int mScreenWidth = 0; // 屏幕宽
    private int mScreenHeight = 0; // 屏幕高

    private Context mContext;
    //是否移动了
    private Boolean isMove = false;
    //是否翻到下一页
    private Boolean isNext = false;
    //是否取消翻页
    private Boolean cancelPage = false;
    //是否没下一页或者上一页
    private Boolean noNext = false;
    private int downX = 0;
    private int downY = 0;

    private int moveX = 0;
    private int moveY = 0;
    //翻页动画是否在执行
    private Boolean isRunning = false;
    private Boolean canTouch = true;
    Bitmap mCurPageBitmap = null; // 当前页 -> 意思是最终整个页面是一张图片？
    Bitmap mNextPageBitmap = null;

    private AnimationProvider mAnimationProvider;

    Scroller mScroller;
    private int mBgColor = 0xFFCEC29C;
    private TouchListener mTouchListener;

    public PageView(Context context) {
        this(context,null);
    }

    public PageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPage();
        mScroller = new Scroller(getContext(),new LinearInterpolator());
        //动画集合
        mAnimationProvider = new SimulationAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
    }

    private void initPage(){
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        //绘制Bitmap
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);      //android:LargeHeap=true  use in  manifest application
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
    }

    //设置翻页的模式
    public void setPageMode(int pageMode){
        switch (pageMode){
            case PAGE_MODE_SIMULATION:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
                break;
            case PAGE_MODE_COVER:
                mAnimationProvider = new CoverAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
                break;
            case PAGE_MODE_SLIDE:
                mAnimationProvider = new SlideAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
                break;
            case PAGE_MODE_NONE:
                mAnimationProvider = new NoneAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
                break;
            default:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
        }
    }

    public Bitmap getCurPage(){
        return mCurPageBitmap;
    }

    public Bitmap getNextPage(){
        return mNextPageBitmap;
    }

    public void setBgColor(int color){
        mBgColor = color;
    }

    public void canTouchable(boolean touchable){
        canTouch = touchable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(0xFFAAAAAA);
        //high背景
        canvas.drawColor(mBgColor);
        Log.e("onDraw","isNext:" + isNext + "          isRunning:" + isRunning);
        //什么叫绘制不滑动页面和绘制滑动页面 0 0
        if (isRunning) {
            mAnimationProvider.drawMove(canvas);
        } else {
            mAnimationProvider.drawStatic(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //是否执行onTouch事件。
        if (!canTouch){
            return false;
        }
        //获取相对点击位置
        int x = (int)event.getX();
        int y = (int)event.getY();
        //设置点击点
        mAnimationProvider.setTouchPoint(x,y);

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            //按下的点击位置
            downX = (int) event.getX();
            downY = (int) event.getY();
            //移动的点击位置
            moveX = 0;
            moveY = 0;
            //是否移动
            isMove = false;
//            cancelPage = false;
            //是否存在下一章
            noNext = false;
            //是下一章还是前一章
            isNext = false;
            //是否正在执行动画
            isRunning = false;
            //初始化动画
            mAnimationProvider.setStartPoint(downX,downY);
            abortAnimation();
            Log.e(TAG,"ACTION_DOWN");
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){

            final int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            //判断是否移动了
            if (!isMove) {
                isMove = Math.abs(downX - x) > slop || Math.abs(downY - y) > slop;
            }

            if (isMove){
                isMove = true;
                //判断是否是准备移动的状态(将要移动但是还没有移动)
                if (moveX == 0 && moveY ==0) {
                    Log.e(TAG,"isMove");
                    //判断翻得是上一页还是下一页
                    if (x - downX >0){
                        isNext = false;
                    }else{
                        isNext = true;
                    }
                    cancelPage = false;

                    if (isNext) {
                        //判断是否下一页存在
                        Boolean isNext = mTouchListener.nextPage();
//                        calcCornerXY(downX,mScreenHeight);
                        //如果存在设置动画方向
                        mAnimationProvider.setDirection(AnimationProvider.Direction.next);
                        //如果不存在表示没有下一页了
                        if (!isNext) {
                            noNext = true;
                            return true;
                        }
                    } else {
                        Boolean isPre = mTouchListener.prePage();
                        mAnimationProvider.setDirection(AnimationProvider.Direction.pre);

                        if (!isPre) {
                            noNext = true;
                            return true;
                        }
                    }
                    Log.e(TAG,"isNext:" + isNext);
                }else{
                    //判断是否取消翻页
                    if (isNext){
                        if (x - moveX > 0){
                            cancelPage = true;
                            mAnimationProvider.setCancel(true);
                        }else {
                            cancelPage = false;
                            mAnimationProvider.setCancel(false);
                        }
                    }else{
                        if (x - moveX < 0){
                            mAnimationProvider.setCancel(true);
                            cancelPage = true;
                        }else {
                            mAnimationProvider.setCancel(false);
                            cancelPage = false;
                        }
                    }
                    Log.e(TAG,"cancelPage:" + cancelPage);
                }

                moveX = x;
                moveY = y;
                isRunning = true;
                this.postInvalidate();
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            Log.e(TAG,"ACTION_UP");
            if (!isMove){
                cancelPage = false;
                //是否点击了中间
                if (downX > mScreenWidth / 5 && downX < mScreenWidth * 4 / 5 && downY > mScreenHeight / 3 && downY < mScreenHeight * 2 / 3){
                    if (mTouchListener != null){
                        mTouchListener.center();
                    }
                    return true;
                }else if (x < mScreenWidth / 2){
                    isNext = false;
                }else{
                    isNext = true;
                }

                if (isNext) {
                    Boolean isNext = mTouchListener.nextPage();
                    mAnimationProvider.setDirection(AnimationProvider.Direction.next);
                    if (!isNext) {
                        return true;
                    }
                } else {
                    Boolean isPre = mTouchListener.prePage();
                    mAnimationProvider.setDirection(AnimationProvider.Direction.pre);
                    if (!isPre) {
                        return true;
                    }
                }
            }

            if (cancelPage && mTouchListener != null){
                mTouchListener.cancel();
            }

            Log.e(TAG,"isNext:" + isNext);
            if (!noNext) {
                isRunning = true;
                mAnimationProvider.startAnimation(mScroller);
                this.postInvalidate();
            }
        }

        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            mAnimationProvider.setTouchPoint(x,y);
            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y){
                isRunning = false;
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    //如果滑动状态没有停止就取消状态，重新设置Anim的触碰点
    public void abortAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            mAnimationProvider.setTouchPoint(mScroller.getFinalX(),mScroller.getFinalY());
            postInvalidate();
        }
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void setTouchListener(TouchListener mTouchListener){
        this.mTouchListener = mTouchListener;
    }

    public interface TouchListener{
        void center();
        Boolean prePage();
        Boolean nextPage();
        void cancel();
    }
}
