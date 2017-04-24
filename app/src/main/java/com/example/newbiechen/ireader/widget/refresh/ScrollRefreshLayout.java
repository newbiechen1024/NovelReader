package com.example.newbiechen.ireader.widget.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
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
 * Created by newbiechen on 17-4-18.
 * 1.  设置不同属性的empty
 */

public abstract class ScrollRefreshLayout extends SwipeRefreshLayout {
    private static final int ATTR_NULL = -1;

    private Context mContext;

    private FrameLayout mFlContent;
    private TextView mTvTip;

    private View mEmptyView;
    private View mContentView;

    private Animation mTipOpenAnim;
    private Animation mTopCloseAnim;

    @LayoutRes
    private int mEmptyId = R.layout.view_empty;

    public abstract View getContentView(ViewGroup parent);

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
        int emptyId = array.getResourceId(R.styleable.ScrollRefreshLayout_scroll_empty,ATTR_NULL);

        if (emptyId != ATTR_NULL) mEmptyId = emptyId;
    }

    private void initView(){
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_scroll_refresh,this,false);
        addView(view);
        //init View
        mFlContent = ButterKnife.findById(view,R.id.scroll_refresh_fl_content);
        mTvTip = ButterKnife.findById(view,R.id.scroll_refresh_tv_tip);

        mEmptyView = inflateId(mFlContent,mEmptyId);
        mFlContent.addView(mEmptyView);

        mContentView = getContentView(mFlContent);

        //默认不显示
        if (mContentView != null){
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT
            );
            mFlContent.addView(mContentView,params);
            mContentView.setVisibility(GONE);
        }
    }

    protected View inflateId(ViewGroup parent,@LayoutRes int id){
        return LayoutInflater.from(mContext)
                .inflate(id,parent,false);
    }

    /*需要自己关闭*/
    public void toggleTip(){
        if (mTipOpenAnim == null || mTopCloseAnim == null){
            mTipOpenAnim = AnimationUtils.loadAnimation(mContext,R.anim.slide_top_in);
            mTopCloseAnim = AnimationUtils.loadAnimation(mContext,R.anim.slide_top_out);

            mTipOpenAnim.setFillAfter(true);
            mTopCloseAnim.setFillAfter(true);
        }

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

    /*自动关闭*/
    public void showTip(){
        //自动关闭
        Runnable runnable = ()-> {
            mTvTip.startAnimation(mTopCloseAnim);
            mTvTip.setVisibility(GONE);
        };
        //取消正在进行的动画和callback
        cancelAnim();
        mTvTip.removeCallbacks(runnable);
        //再次发送
        if (mTvTip.getVisibility() == GONE){
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

    protected void showEmptyView(){
        mEmptyView.setVisibility(VISIBLE);
    }

    protected void hideEmptyView(){
        mEmptyView.setVisibility(GONE);
    }
}
