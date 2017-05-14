package com.example.newbiechen.ireader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-5.
 */

public class ScrollRefreshLayout extends SwipeRefreshLayout {
    private static final int CHILD_COUNT = 3;
    /********************************************/
    private TextView mTvTip;
    private View mEmptyView;
    private View mContentView;
    /*****************************************/
    private Context mContext;
    private Animation mTipOpenAnim;
    private Animation mTopCloseAnim;
    /*********************************************/
    @LayoutRes
    private int mEmptyId;
    private String mTipStr = "";
    /****************************public method**************************************************/
    /*需要自己关闭*/
    public void toggleTip(){
        initAnim();
        cancelAnim();
        if (mTvTip.getVisibility() == GONE){
            mTvTip.setVisibility(VISIBLE);
            mTvTip.startAnimation(mTipOpenAnim);
        }
        else {
            mTvTip.startAnimation(mTopCloseAnim);
            mTvTip.setVisibility(GONE);
        }
    }

    private void initAnim(){
        if (mTipOpenAnim == null || mTopCloseAnim == null){
            mTipOpenAnim = AnimationUtils.loadAnimation(mContext,R.anim.slide_top_in);
            mTopCloseAnim = AnimationUtils.loadAnimation(mContext,R.anim.slide_top_out);

            mTipOpenAnim.setFillAfter(true);
            mTopCloseAnim.setFillAfter(true);
        }
    }

    /*自动关闭*/
    public void showNetTip(){
        //自动关闭
        toggleTip();
        Runnable runnable = ()-> {
            mTvTip.startAnimation(mTopCloseAnim);
            mTvTip.setVisibility(GONE);
        };
        mTvTip.removeCallbacks(runnable);
        if (mTvTip.getVisibility() == VISIBLE){
            mTvTip.postDelayed(runnable,2000);
        }
    }

    private void cancelAnim(){
        if (mTipOpenAnim.hasStarted()){
            mTipOpenAnim.cancel();
        }
        if (mTopCloseAnim.hasStarted()){
            mTipOpenAnim.cancel();
        }
    }
    /********************************protected method*********************************************/
    protected void showEmptyView(){
        mEmptyView.setVisibility(VISIBLE);
        if (mContentView != null) {
            mContentView.setVisibility(GONE);
        }
    }

    protected void showContent(){
        mEmptyView.setVisibility(GONE);
        if (mContentView != null){
            mContentView.setVisibility(VISIBLE);
        }
    }

    /******************************init **************************************8*/
    public ScrollRefreshLayout(Context context) {
        this(context,null);
    }

    public ScrollRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray array = mContext.obtainStyledAttributes(attrs,R.styleable.ScrollRefreshLayout);
        mEmptyId = array.getResourceId(R.styleable.ScrollRefreshLayout_layout_scroll_empty,R.layout.view_empty);
        mTipStr = array.getString(R.styleable.ScrollRefreshLayout_text_error_tip);

        array.recycle();
    }

    private void initView(){
        mEmptyView = inflateId(this,mEmptyId);
        View tipView = inflateId(this, R.layout.view_refresh_tip);

        addView(mEmptyView);
        addView(tipView);

        mTvTip = ButterKnife.findById(tipView, R.id.scroll_refresh_tv_tip);
        //设置提示语句
        mTvTip.setText(mTipStr);
        mEmptyView.setVisibility(GONE);
    }

    private View inflateId(ViewGroup parent,@LayoutRes int id){
        return LayoutInflater.from(mContext)
                .inflate(id,parent,false);
    }

    public void setTip(String str){
        mTvTip.setText(str);
    }

    //除了自带的View，保证当前Layout只能添加一个子View
    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        //获取子View
        if (getChildCount() == CHILD_COUNT){
            mContentView = child;
        }
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > CHILD_COUNT) {
            throw new IllegalStateException("RefreshLayout can host only one direct child");
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > CHILD_COUNT) {
            throw new IllegalStateException("RefreshLayout can host only one direct child");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > CHILD_COUNT) {
            throw new IllegalStateException("RefreshLayout can host only one direct child");
        }

        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > CHILD_COUNT) {
            throw new IllegalStateException("RefreshLayout can host only one direct child");
        }
        super.addView(child, index, params);
    }
}
