package com.example.newbiechen.ireader.widget.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.example.newbiechen.ireader.model.bean.CollBookBean;
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

    private int mViewWidth = 0; // 当前View的宽
    private int mViewHeight = 0; // 当前View的高

    private int downX = 0;
    private int downY = 0;

    private int moveX = 0;
    private int moveY = 0;
    //翻页动画是否在执行
    private Boolean isRunning = false;
    //是否允许点击
    private Boolean canTouch = true;
    //是否移动了
    private Boolean isMove = false;
    //是否翻到下一页
    private Boolean isNext = false;
    //是否取消翻页
    private Boolean cancelPage = false;
    //是否没下一页或者上一页
    private Boolean noNext = false;

    //初始化参数
    private int mBgColor = 0xFFCEC29C;
    private int mPageMode = PAGE_MODE_SIMULATION;

    //滑动类
    private Scroller mScroller;
    //动画执行
    private AnimationProvider mAnimationProvider;
    //点击监听
    private TouchListener mTouchListener;
    //显示的图片
    private Bitmap mCurPageBitmap = null;
    private Bitmap mNextPageBitmap = null;
    //内容加载器
    private PageLoader mPageLoader;

    public PageView(Context context) {
        this(context,null);
    }

    public PageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext(),new LinearInterpolator());
        //动画集合
        mAnimationProvider = new SimulationAnimation(mCurPageBitmap,mNextPageBitmap, mViewWidth, mViewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        //重置图片的大小,由于w,h可能比原始的Bitmap更大，所以如果使用Bitmap.setWidth/Height()是会报错的。
        //所以最终还是创建Bitmap的方式。这种方式比较消耗性能，暂时没有找到更好的方法。
        initPage();
        //重置页面加载器的页面
        mPageLoader.setDisplaySize(w,h);
    }

    private void initPage(){
        //绘制Bitmap
        mCurPageBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);      //android:LargeHeap=true  use in  manifest application
        mNextPageBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);
        setPageMode(mPageMode);
    }

    //设置翻页的模式
    public void setPageMode(int pageMode){
        mPageMode = pageMode;
        switch (pageMode){
            case PAGE_MODE_SIMULATION:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap,mNextPageBitmap, mViewWidth, mViewHeight);
                break;
            case PAGE_MODE_COVER:
                mAnimationProvider = new CoverAnimation(mCurPageBitmap,mNextPageBitmap, mViewWidth, mViewHeight);
                break;
            case PAGE_MODE_SLIDE:
                mAnimationProvider = new SlideAnimation(mCurPageBitmap,mNextPageBitmap, mViewWidth, mViewHeight);
                break;
            case PAGE_MODE_NONE:
                mAnimationProvider = new NoneAnimation(mCurPageBitmap,mNextPageBitmap, mViewWidth, mViewHeight);
                break;
            default:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap,mNextPageBitmap, mViewWidth, mViewHeight);
        }
    }

    public Bitmap getCurPage(){
        return mCurPageBitmap;
    }

    public Bitmap getNextPage(){
        return mNextPageBitmap;
    }

    //这个有点小问题
    public void autoPrevPage(){
        startPageAnim(AnimationProvider.Direction.pre);
    }

    public void autoNextPage(){
        startPageAnim(AnimationProvider.Direction.next);
    }

    private void startPageAnim(AnimationProvider.Direction direction){
        if (mTouchListener == null) return;
        //是否正在执行动画
        isRunning = false;
        abortAnimation();
        if (direction == AnimationProvider.Direction.next){
            int x = mViewWidth;
            int y = mViewHeight;
            //设置点击点
            mAnimationProvider.setTouchPoint(x,y);
            //初始化动画
            mAnimationProvider.setStartPoint(x,y);
            //设置方向方向
            Boolean hasNext = hasNext();

            mAnimationProvider.setDirection(direction);
            if (!hasNext) {
                return;
            }
        }
        else{
            int x = 0;
            int y = mViewHeight;
            //设置点击点
            mAnimationProvider.setTouchPoint(x,y);
            //初始化动画
            mAnimationProvider.setStartPoint(x,y);
            //设置方向方向
            Boolean hashPrev = hasPrev();
            mAnimationProvider.setDirection(direction);
            if (!hashPrev) {
                return;
            }
        }
        //执行动画
        isRunning = true;
        mAnimationProvider.startAnimation(mScroller);
        this.postInvalidate();
    }

    public void openBook(CollBookBean cookBook,boolean isLocal){}

    public void setBgColor(int color){
        mBgColor = color;
    }

    public void canTouchable(boolean touchable){
        canTouch = touchable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //high背景
        canvas.drawColor(mBgColor);
        Log.e("onDraw","isNext:" + isNext + "          isRunning:" + isRunning);

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
        //设置点击位置
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
                        Boolean hasNext = hasNext();
                        //如果存在设置动画方向
                        mAnimationProvider.setDirection(AnimationProvider.Direction.next);
                        //如果不存在表示没有下一页了
                        if (!hasNext) {
                            noNext = true;
                            return true;
                        }
                    } else {
                        Boolean hasPrev = hasPrev();
                        mAnimationProvider.setDirection(AnimationProvider.Direction.pre);

                        if (!hasPrev) {
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
                if (downX > mViewWidth / 5 && downX < mViewWidth * 4 / 5 && downY > mViewHeight / 3 && downY < mViewHeight * 2 / 3){
                    if (mTouchListener != null){
                        mTouchListener.center();
                    }
                    return true;
                }else if (x < mViewWidth / 2){
                    isNext = false;
                }else{
                    isNext = true;
                }

                if (isNext) {
                    //判断是否下一页存在
                    Boolean hasNext = hasNext();
                    //设置动画方向
                    mAnimationProvider.setDirection(AnimationProvider.Direction.next);
                    if (!hasNext) {
                        return true;
                    }
                } else {
                    Boolean hasPrev = hasPrev();
                    mAnimationProvider.setDirection(AnimationProvider.Direction.pre);
                    if (!hasPrev) {
                        return true;
                    }
                }
            }

            if (cancelPage && mTouchListener != null){
                mTouchListener.cancel();
                mPageLoader.pageCancel();
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

    //判断是否下一页存在
    private boolean hasNext(){
        Boolean hasNext = false;
        if (mTouchListener != null){
            hasNext = mTouchListener.nextPage();
            //加载下一页
            if (hasNext){
                hasNext = mPageLoader.next();
            }
        }
        return hasNext;
    }

    //判断是否存在上一页
    private boolean hasPrev(){
        Boolean hasPrev = false;
        if (mTouchListener != null){
            hasPrev = mTouchListener.prePage();
            //加载下一页
            if (hasPrev){
                hasPrev = mPageLoader.prev();
            }
        }
        return hasPrev;
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

    //获取PageLoader
    public PageLoader getPageLoader(boolean isLocal){
        if (mPageLoader == null){
            if (isLocal){
                mPageLoader = new LocalPageLoader(this);
            }
            else {
                mPageLoader = new NetPageLoader(this);
            }
        }
        return mPageLoader;
    }

    public interface TouchListener{
        void center();
        boolean prePage();
        boolean nextPage();
        void cancel();
    }
}
