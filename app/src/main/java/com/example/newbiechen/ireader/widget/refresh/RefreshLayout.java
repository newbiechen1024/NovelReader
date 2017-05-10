package com.example.newbiechen.ireader.widget.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.newbiechen.ireader.R;

/**
 * Created by newbiechen on 17-4-22.
 * 功能:
 * 1. 加载动画
 * 2. 加载错误点击重新加载
 */

public abstract class RefreshLayout extends FrameLayout {

    protected static final int STATUS_LOADING = 0;
    protected static final int STATUS_FINISH = 1;
    protected static final int STATUS_ERROR = 2;
    protected static final int STATUS_EMPTY = 3;

    private Context mContext;

    private int mEmptyViewId;
    private int mErrorViewId;
    private int mLoadingViewId;

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mContentView;

    private OnReloadingListener mListener;
    private int mStatus = 0;

    public RefreshLayout(Context context) {
        this(context,null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RefreshLayout);
        mEmptyViewId = typedArray.getResourceId(R.styleable.RefreshLayout_layout_refresh_empty,R.layout.view_empty);
        mErrorViewId = typedArray.getResourceId(R.styleable.RefreshLayout_layout_refresh_error,R.layout.view_net_error);
        mLoadingViewId = typedArray.getResourceId(R.styleable.RefreshLayout_layout_refresh_loading,R.layout.view_loading);
    }

    private void initView(){
        mEmptyView = inflateView(mEmptyViewId);
        mErrorView = inflateView(mErrorViewId);
        mLoadingView = inflateView(mLoadingViewId);

        mContentView = createContentView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        mContentView.setLayoutParams(params);

        //默认显示加载
        toggleStatus(STATUS_LOADING);

        addView(mEmptyView);
        addView(mErrorView);
        addView(mLoadingView);
        addView(mContentView);

        //设置监听器
        mErrorView.setOnClickListener(
                (view) -> {
                    if (mListener != null){
                        toggleStatus(STATUS_LOADING);
                        mListener.onReload();
                    }
                }
        );
    }

    public void showLoading(){
        if (mStatus != STATUS_LOADING){
            toggleStatus(STATUS_LOADING);
        }
    }

    public void showFinish(){
        if (mStatus == STATUS_LOADING){
            toggleStatus(STATUS_FINISH);
        }
    }

    public void showError(){
        if (mStatus != STATUS_ERROR){
            toggleStatus(STATUS_ERROR);
        }
    }

    public void showEmpty(){
        if (mStatus != STATUS_EMPTY){
            toggleStatus(STATUS_EMPTY);
        }
    }

    private void toggleStatus(int status){
        switch (status){
            case STATUS_LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mEmptyView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mContentView.setVisibility(GONE);
                break;
            case STATUS_FINISH:
                mContentView.setVisibility(VISIBLE);
                mLoadingView.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                break;
            case STATUS_ERROR:
                mErrorView.setVisibility(VISIBLE);
                mContentView.setVisibility(GONE);
                mLoadingView.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                break;
            case STATUS_EMPTY:
                mEmptyView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mContentView.setVisibility(GONE);
                mLoadingView.setVisibility(GONE);
                break;
        }
        mStatus = status;
    }

    protected abstract View createContentView(ViewGroup parent);

    protected int getStatus(){
        return mStatus;
    }

    public void setOnReloadingListener(OnReloadingListener listener){
        mListener = listener;
    }



    private View inflateView(int id){
        return LayoutInflater.from(mContext)
                .inflate(id,this,false);
    }

    public interface OnReloadingListener{
        void onReload();
    }
}
