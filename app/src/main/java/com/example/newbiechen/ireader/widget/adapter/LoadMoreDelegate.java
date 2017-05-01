package com.example.newbiechen.ireader.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by newbiechen on 17-4-20.
 */

public class LoadMoreDelegate implements WholeAdapter.ItemView {
    private LoadMoreView mLoadMoreView;

    public LoadMoreDelegate(Context context, WholeAdapter.Options options){
        LoadMoreView view = new LoadMoreView(context,
                options.loadMoreId,options.errorId,options.noMoreId);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        mLoadMoreView = view;
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        return mLoadMoreView;
    }

    @Override
    public void onBindView(View view) {
        LoadMoreView loadMoreView = (LoadMoreView) view;
        loadMoreView.refreshView();
    }

    public void setLoadMoreStatus(int status){
        mLoadMoreView.setLoadMoreStatus(status);
    }

    public void setOnLoadMoreListener(LoadMoreView.OnLoadMoreListener listener){
        mLoadMoreView.setOnLoadMoreListener(listener);
    }
}
