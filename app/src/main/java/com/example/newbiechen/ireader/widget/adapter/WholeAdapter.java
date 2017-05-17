package com.example.newbiechen.ireader.widget.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-4-19.
 *
 */

public abstract class WholeAdapter<T> extends BaseListAdapter<T>{
    private static final String TAG = "WholeAdapter";
    private static final int TYPE_ITEM = 0;

    //刷新类
    private LoadMoreDelegate mLoadDelegate = null;

    private final ArrayList<WholeAdapter.ItemView> mHeaderList = new ArrayList<>(2);
    private final ArrayList<WholeAdapter.ItemView> mFooterList = new ArrayList<>(2);


    public WholeAdapter(){
    }

    public WholeAdapter(Context context,Options options){
        if (options != null){
            mLoadDelegate = new LoadMoreDelegate(context,options);
            mFooterList.add(mLoadDelegate);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            return super.onCreateViewHolder(parent,viewType);
        }
        else {
            return createOtherViewHolder(parent,viewType);
        }
    }

    private RecyclerView.ViewHolder createOtherViewHolder(ViewGroup parent,int viewType){
        View view = null;
        for (int i=0; i<mHeaderList.size(); ++i){
            WholeAdapter.ItemView itemView = mHeaderList.get(i);
            if (viewType == itemView.hashCode()){
                view = itemView.onCreateView(parent);
            }
        }
        for (int i=0; i<mFooterList.size(); ++i){
            WholeAdapter.ItemView itemView = mFooterList.get(i);
            if (viewType == itemView.hashCode()){
                view = itemView.onCreateView(parent);
            }
        }
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view){};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mHeaderList.size()){
            mHeaderList.get(position).onBindView(holder.itemView);
        }
        else if (position < mHeaderList.size() + getItemSize()){
            super.onBindViewHolder(holder,position - mHeaderList.size());
        }
        else {
            int pos = position - mHeaderList.size() - getItemSize();
            mFooterList.get(pos).onBindView(holder.itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (position < mHeaderList.size()){
            type = mHeaderList.get(position).hashCode();
        }
        else if (position < mHeaderList.size() + getItemSize()){
            type = TYPE_ITEM;
        }
        else {
            int pos = position - mHeaderList.size() - getItemSize();
            type = mFooterList.get(pos).hashCode();
        }
        return type;
    }

    @Override
    public final int getItemCount() {
        return mHeaderList.size() + getItemSize() + mFooterList.size();
    }

    public void addHeaderView(ItemView itemView){
        mHeaderList.add(itemView);
    }

    public void addFooterView(ItemView itemView){
        if (mLoadDelegate != null){
            int count = mFooterList.size() - 1;
            mFooterList.add(count,itemView);
        }
        else {
            mFooterList.add(itemView);
        }
    }

    @Override
    public void addItems(List<T> values) {
        if (values.size() == 0 && mLoadDelegate != null){
            mLoadDelegate.setLoadMoreStatus(LoadMoreView.TYPE_NO_MORE);
        }
        super.addItems(values);
    }

    public void setOnLoadMoreListener(LoadMoreView.OnLoadMoreListener listener){
        checkLoadMoreExist();
        mLoadDelegate.setOnLoadMoreListener(listener);
    }

    private void checkLoadMoreExist(){
        if (mLoadDelegate == null)
            throw new IllegalArgumentException("you must setting LoadMore Option");
    }

    @Override
    public void refreshItems(List<T> list) {
        if (mLoadDelegate != null){
            mLoadDelegate.setLoadMoreStatus(LoadMoreView.TYPE_LOAD_MORE);
        }
        super.refreshItems(list);
    }

    public void showLoadError(){
        //设置为加载错误
        checkLoadMoreExist();
        mLoadDelegate.setLoadMoreStatus(LoadMoreView.TYPE_LOAD_ERROR);
        notifyDataSetChanged();
    }



    //设置当GridLayout的情况下
    class WholeGridSpanSizeLookUp extends GridLayoutManager.SpanSizeLookup{
        int maxSize = 1;

        public WholeGridSpanSizeLookUp(int maxSize){
            this.maxSize = maxSize;
        }

        @Override
        public int getSpanSize(int position) {
            if (position < mHeaderList.size()){
                return maxSize;
            }
            if (position < mHeaderList.size() +getItemSize()){
                return 1;
            }
            else {
                return maxSize;
            }
        }
    }

    public static class Options{
        @LayoutRes public int loadMoreId = R.layout.view_load_more;
        @LayoutRes public int errorId = R.layout.view_error;
        @LayoutRes public int noMoreId = R.layout.view_nomore;
    }

    public interface ItemView{
        View onCreateView(ViewGroup parent);
        void onBindView(View view);
    }
}
