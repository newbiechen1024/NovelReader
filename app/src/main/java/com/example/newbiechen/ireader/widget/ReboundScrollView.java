package com.example.newbiechen.ireader.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by newbiechen on 17-4-23.
 */

public class ReboundScrollView extends ScrollView{
    private static final String TAG = "ReboundScrollView";
    //阻尼因子
    private static final float MOVE_FACTOR = 0.5f;
    private static final int SCROLL_TIME = 800;
    private View mContentView;
    private Rect mViewOriginRect;
    private Scroller mScroller;

    private boolean canPullUp = false;
    private boolean canPullDown = false;

    private int mStartY;

    private boolean isMove = false;
    public ReboundScrollView(Context context) {
        this(context, null);
    }

    public ReboundScrollView(Context context, AttributeSet attrs) {
        super(context,attrs);
        mScroller = new Scroller(context);
    }

    public ReboundScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0){
            //获取包裹的View
            mContentView = getChildAt(0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mContentView == null){
            return;
        }
        if (mViewOriginRect == null){
            mViewOriginRect = new Rect();
            mViewOriginRect.set(mContentView.getLeft(),mContentView.getTop(),
                    mContentView.getRight(),mContentView.getBottom());
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mContentView == null){
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartY = (int) ev.getY();

                canPullDown = isCanPullDown();
                canPullUp = isCanPullUp();
                break;
            case MotionEvent.ACTION_MOVE:
                //首先判断，需要滑动吗，找到不需要的并排除
                int currentY = (int) ev.getY();

                if (!canPullDown && !canPullUp){
                    canPullDown = isCanPullDown();
                    canPullUp = isCanPullUp();
                    break;
                }
                //表示可以进行滑动，计算滑动值 (下滑为负，上划为正)
                int deltaY = mStartY - currentY;
                //以下情况才真正可以进行滑动
                boolean shouldMove = (canPullDown && deltaY < 0)
                        || (canPullUp && deltaY > 0) || (canPullDown && canPullUp);

                if (shouldMove){
                    int moveValue = (int) (deltaY * MOVE_FACTOR);
                    mContentView.layout(mViewOriginRect.left,mViewOriginRect.top - moveValue,
                            mViewOriginRect.right,mViewOriginRect.bottom - moveValue);
                    isMove = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isMove){
                    break;
                }
                int distance = mViewOriginRect.top - mContentView.getTop();
                //移动到初始时候的位置
                mScroller.startScroll(0,mContentView.getTop(),0,distance,SCROLL_TIME);
                invalidate();

                canPullDown = false;
                canPullUp = false;
                isMove = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            int value = mScroller.getCurrY();
            mContentView.layout(mViewOriginRect.left,mViewOriginRect.top+value,mViewOriginRect.right,
                    mViewOriginRect.bottom+value);
            postInvalidate();
        }
    }

    /**
     * 判断是否滚动到顶部
     * contentView < 屏幕 - 滑动大小
     */

    private boolean isCanPullDown(){
        return getScrollY() <= 0 ;
    }

    /**
     * 判断是否滚动到底部
     * contentView < 屏幕 + 滑动大小
     */
    private boolean isCanPullUp() {
        return mContentView.getHeight() <= getHeight() + getScrollY();
    }
}
