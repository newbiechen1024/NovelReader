package com.example.newbiechen.ireader.widget.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by newbiechen on 17-4-19.
 * 前提是，保证不会重新绘制
 */

public class LoadMoreView extends FrameLayout {
    private static final String TAG = "LoadMoreView";
    public static final int TYPE_HIDE = 0;
    public static final int TYPE_LOAD_MORE = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_LOAD_ERROR = 3;

    private View mLoadMoreView;
    private View mErrorView;
    private View mNoMoreView;

    private OnLoadMoreListener mListener;

    private int mStatus = TYPE_HIDE;

    public LoadMoreView(Context context,@LayoutRes int loadMoreId,
                        @LayoutRes int errorId,@LayoutRes int noMoreId) {
        super(context);
        initView(loadMoreId,errorId,noMoreId);
    }

    private void initView(int loadMoreId,int errorId,int noMoreId){
        mLoadMoreView = inflateId(loadMoreId);
        mErrorView = inflateId(errorId);
        mNoMoreView = inflateId(noMoreId);

        addView(mLoadMoreView);
        addView(mErrorView);
        addView(mNoMoreView);

        refreshView();

        mErrorView.setOnClickListener(
                (v) -> setLoadMore()
        );
    }

    private View inflateId(int id){
        return LayoutInflater.from(getContext())
                .inflate(id,this,false);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        mListener = listener;
    }

    public void refreshView(){
        switch (mStatus){
            case TYPE_HIDE:
                setHide();
                break;
            case TYPE_LOAD_MORE:
                setLoadMore();
                break;
            case TYPE_NO_MORE:
                setLoadNoMore();
                break;
            case TYPE_LOAD_ERROR:
                setLoadError();
                break;
        }
    }

    public void setLoadMoreStatus(int status){
        mStatus = status;
        refreshView();
    }

    private void setHide(){
        mLoadMoreView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mNoMoreView.setVisibility(GONE);
    }

    private void setLoadMore(){
        //加载数据
        mLoadMoreView.setVisibility(VISIBLE);
        mErrorView.setVisibility(GONE);
        mNoMoreView.setVisibility(GONE);
        //加载
        if (mListener != null){
            mListener.onLoadMore();
        }
    }

    private void setLoadError(){
        mLoadMoreView.setVisibility(GONE);
        mErrorView.setVisibility(VISIBLE);
        mNoMoreView.setVisibility(GONE);
    }

    private void setLoadNoMore(){
        mLoadMoreView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mNoMoreView.setVisibility(VISIBLE);
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
}
