package com.example.newbiechen.ireader.ui.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by newbiechen on 17-6-5.
 * ListView 使用的Adapter
 */

public abstract class EasyAdapter<T> extends android.widget.BaseAdapter {

    private List<T> mList = new ArrayList<T>();

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(T value){
        mList.add(value);
        notifyDataSetChanged();
    }

    public void addItem(int index,T value){
        mList.add(index, value);
        notifyDataSetChanged();
    }

    public void addItems(List<T> values){
        mList.addAll(values);
        notifyDataSetChanged();
    }

    public void removeItem(T value){
        mList.remove(value);
        notifyDataSetChanged();
    }

    public List<T> getItems(){
        return Collections.unmodifiableList(mList);
    }

    public int getItemSize(){
        return mList.size();
    }

    public void refreshItems(List<T> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear(){
        mList.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IViewHolder holder = null;
        if (convertView == null){
            holder = onCreateViewHolder(getItemViewType(position));
            convertView = holder.createItemView(parent);
            convertView.setTag(holder);
            //初始化
            holder.initView();
        }
        else {
            holder = (IViewHolder)convertView.getTag();
        }
        //执行绑定
        holder.onBind(getItem(position),position);
        return convertView;
    }

    protected abstract IViewHolder<T> onCreateViewHolder(int viewType);
}
