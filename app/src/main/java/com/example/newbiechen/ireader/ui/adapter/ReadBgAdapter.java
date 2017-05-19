package com.example.newbiechen.ireader.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newbiechen.ireader.ui.adapter.view.ReadBgHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.adapter.BaseViewHolder;
import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-5-19.
 */

public class ReadBgAdapter extends BaseListAdapter<Drawable> {
    private int currentChecked = 0;

    @Override
    protected IViewHolder<Drawable> createViewHolder(int viewType) {
        return new ReadBgHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        IViewHolder iHolder = ((BaseViewHolder) holder).holder;
        ReadBgHolder readBgHolder = (ReadBgHolder) iHolder;
        if (currentChecked == position){
            readBgHolder.setChecked();
        }
    }


    public void setBgChecked(int pos){
        currentChecked = pos;
    }

    @Override
    protected void onItemClick(View v, int pos) {
        super.onItemClick(v, pos);
        currentChecked = pos;
        notifyDataSetChanged();
    }
}
