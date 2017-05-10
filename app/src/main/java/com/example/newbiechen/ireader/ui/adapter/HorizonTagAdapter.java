package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newbiechen.ireader.ui.adapter.view.HorizonTagView;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.IAdapter;

/**
 * Created by newbiechen on 17-5-2.
 */

public class HorizonTagAdapter extends BaseListAdapter<String>{
    private int currentSelected = 0;
    @Override
    protected View createView(Context context, int viewType) {
        return new HorizonTagView(context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (! (holder.itemView instanceof IAdapter))
            throw new IllegalArgumentException("The adapter view must extend IAdapter");
        HorizonTagView view = (HorizonTagView)holder.itemView;
        //设置点击事件
        holder.itemView.setOnClickListener((v)->{
            selectTag(v,position);
        });

        view.setSelectedTag(currentSelected);
        view.onBind(mList.get(position),position);
    }

    /***
     * 设定当前的点击事件 (哦，是不是用ViewPager会更好一点)
     * @param pos
     */
    public void setCurrentSelected(int pos){
        selectTag(null,pos);
    }

    private void selectTag(View v,int position){
        currentSelected = position;
        if (mClickListener != null){
            mClickListener.onItemClick(v,position);
        }
        notifyDataSetChanged();
    }
}
