package com.example.newbiechen.ireader.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newbiechen.ireader.ui.adapter.view.HorizonTagHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseViewHolder;

/**
 * Created by newbiechen on 17-5-2.
 */

public class HorizonTagAdapter extends BaseListAdapter<String>{
    private int currentSelected = 0;

    @Override
    protected IViewHolder<String> createViewHolder(int viewType) {
        return new HorizonTagHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);

        //配置点击事件改变状态
        IViewHolder iHolder = ((BaseViewHolder) holder).holder;
        HorizonTagHolder horizonTagHolder = (HorizonTagHolder) iHolder;
        if (position == currentSelected){
            horizonTagHolder.setSelectedTag();
        }
    }

    /***
     * 设定当前的点击事件
     * @param pos
     */
    public void setCurrentSelected(int pos){
        selectTag(pos);
    }

    @Override
    protected void onItemClick(View v, int pos) {
        super.onItemClick(v, pos);
        selectTag(pos);
    }

    private void selectTag(int position){
        currentSelected = position;
        notifyDataSetChanged();
    }
}
