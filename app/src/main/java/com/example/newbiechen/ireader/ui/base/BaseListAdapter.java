package com.example.newbiechen.ireader.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by newbiechen on 17-3-21.
 */

public abstract class BaseListAdapter <T> extends RecyclerView.Adapter {

    private static final String TAG = "BaseListAdapter";
    /*common statement*/
    protected final List<T> mList = new ArrayList<>();
    protected OnItemClickListener mClickListener;
    protected OnItemLongClickListener mLongClickListener;

    /************************abstract area************************/
    protected abstract View createView(Context context, int viewType);


    /*************************rewrite logic area***************************************/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = createView(parent.getContext(),viewType);
        //初始化
        RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {};
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (! (holder.itemView instanceof IAdapter))
            throw new IllegalArgumentException("The adapter view must extend IAdapter");
        IAdapter<T> view = (IAdapter<T>)holder.itemView;

        //设置点击事件
        holder.itemView.setOnClickListener((v)->{
            onItemClick(v,position);
            if (mClickListener != null){
                mClickListener.onItemClick(v,position);
            }
        });

        holder.itemView.setOnLongClickListener(
                (v) -> {
                    boolean isClicked = false;
                    if (mLongClickListener != null){
                        isClicked =  mLongClickListener.onItemLongClick(v,position);
                    }
                    return isClicked;
                }
        );
        view.onBind(mList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected void onItemClick(View v,int pos){

    }

    /******************************public area***********************************/

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mClickListener = mListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mListener){
        this.mLongClickListener = mListener;
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

    public T getItem(int position){
        return mList.get(position);
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


    /***************************inner class area***********************************/
    public interface OnItemClickListener{
        void onItemClick(View view, int pos);
    }

    public interface OnItemLongClickListener{
        boolean onItemLongClick(View view, int pos);
    }
}
