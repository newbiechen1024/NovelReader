package com.example.newbiechen.ireader.ui.base;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by PC on 2016/9/9.
 * 1、默认使用List作为容器
 */
public abstract class BaseAdapter <E,VH extends ViewHolder> extends Adapter<VH> {

    //在Adapter中建立一个容器
    protected final List<E> mItemList = new ArrayList<>();
    //点击事件的监听
    private OnItemClickListener mItemClickListener;
    @Override
    public void onBindViewHolder(VH holder, int position) {
        bindData(holder,getItem(position),position);
        setUpClickListener(holder.itemView,position);
    }

    /**
     * 设置每个Item的点击事件
     */
    private void setUpClickListener(final View view , final int position){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Adapter监听item的点击事件
                onItemClick(v,position);
                //设置点击回调
                if (mItemClickListener != null){
                    mItemClickListener.itemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * 设置ViewHolder显示的数据
     * @param holder
     * @param position
     */
    public abstract void bindData(VH holder, E data,int position);

    protected void onItemClick(View v,int pos){
    }

    /**
     * 设置ITEM点击事件的接口
     */
    public interface OnItemClickListener{
        void itemClick(View view, int pos);
    }

/******************************公共方法*****************************************/
    /**
     * 设置点击事件的监听器
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mItemClickListener = listener;
    }

    /**
     * 添加单个数据
     * @param item
     */
    public void addItems(E item){
        mItemList.add(item);
        notifyDataSetChanged();
    }

    /**
     * 添加多个数据
     * @param items
     */
    public void addItems(List<E> items){
        mItemList.addAll(items);
        notifyDataSetChanged();
    }


    public void removeItems(E item){
        mItemList.remove(item);
    }
    /**
     * 移除全部数据
     */
    public void removeItems(){
        mItemList.clear();
    }

    /**
     * 专门为刷新数据的方法
     * @param items
     */
    public void refreshItems(List<E> items){
        removeItems();
        addItems(items);
    }

    public E getItem(int position){
        return mItemList.get(position);
    }

    public List<E> getItems(){
        return Collections.unmodifiableList(mItemList);
    }
}

