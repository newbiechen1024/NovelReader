package com.example.newbiechen.ireader.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.ui.adapter.view.CategoryHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.adapter.BaseViewHolder;
import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-5-17.
 */

public class CategoryAdapter extends BaseListAdapter<BookChapterBean>{
    private int currentSelected = 0;
    @Override
    protected IViewHolder<BookChapterBean> createViewHolder(int viewType) {
        return new CategoryHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        //配置点击事件改变状态
        IViewHolder iHolder = ((BaseViewHolder) holder).holder;
        CategoryHolder categoryHolder = (CategoryHolder) iHolder;
        if (position == currentSelected){
            categoryHolder.setSelectedChapter();
        }
    }

    public void setSelectedChapter(int pos){
        setChapter(pos);
    }

    @Override
    protected void onItemClick(View v, int pos) {
        super.onItemClick(v, pos);
        setChapter(pos);
    }

    private void setChapter(int pos){
        currentSelected = pos;
        notifyDataSetChanged();
    }
}
