package com.example.newbiechen.ireader.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.example.newbiechen.ireader.ui.base.IAdapter;

/**
 * Created by newbiechen on 17-5-5.
 * 用于头标签 + 表格的布局View
 */

public abstract class GroupAdapter<T,R> extends RecyclerView.Adapter{
    private static final String TAG = "GroupAdapter";

    private static final int TYPE_GROUP = 1;
    private static final int TYPE_CHILD = 2;

    private OnGroupClickListener mGroupListener;
    private OnChildClickListener mChildClickListener;

    public abstract int getGroupCount();
    public abstract int getChildCount(int groupPos);

    public abstract T getGroupItem(int groupPos);
    public abstract R getChildItem(int groupPos,int childPos);

    protected abstract View createGroupView(ViewGroup parent);
    protected abstract View createChildView(ViewGroup parent);

    public GroupAdapter(RecyclerView recyclerView,int spanSize){
        GridLayoutManager manager = new GridLayoutManager(recyclerView.getContext(),spanSize);
        manager.setSpanSizeLookup(new GroupSpanSizeLookup(spanSize));
        recyclerView.setLayoutManager(manager);
    }

    public void setOnGroupItemListener(OnGroupClickListener listener){
        mGroupListener = listener;
    }

    public void setOnChildItemListener(OnChildClickListener listener) {
        mChildClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_GROUP){
            view = createGroupView(parent);
        }
        else if (viewType == TYPE_CHILD){
            view = createChildView(parent);
        }
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (! (holder.itemView instanceof IAdapter))
            throw new IllegalArgumentException("The adapter view must extend IAdapter");
        IAdapter view = (IAdapter)holder.itemView;

        int type = getItemViewType(position);
        if (type == TYPE_GROUP){
            //计算当前的group
            int groupPos = calculateGroup(position);
            holder.itemView.setOnClickListener(
                    (v) -> {
                        if (mGroupListener != null){
                            mGroupListener.onGroupClick(v,groupPos);
                        }
                    }
            );
            view.onBind(getGroupItem(groupPos),groupPos);
        }
        else if (type == TYPE_CHILD){
            int groupPos = calculateGroup(position);
            int childPos = calculateChild(position);

            holder.itemView.setOnClickListener(
                    v -> {
                        if (mChildClickListener != null) {
                            mChildClickListener.onChildClick(v,groupPos,childPos);
                        }
                    }
            );
            //这里有点小问题，返回的是childPos
            view.onBind(getChildItem(groupPos,childPos),childPos);
        }
    }

    //计算position是哪个group中的头
    private int calculateGroup(int position){
        int total = 0;
        for (int i=0; i<getGroupCount(); ++i){
            total += getChildCount(i)+1; //当前group的大小范围
            if (total > position){    //判断是否pos在total内
                return i;
            }
        }
        return -1;
    }

    //计算position是那个group中的child
    protected int calculateChild(int position){
        for (int i=0; i<getGroupCount(); ++i){
            int total = getChildCount(i)+1; //每个队列的总和
            int loc = position - total; //Loc表示在第二队列的位置
            if (loc < 0){              //如果 < 0 表示在上一队列中，则返回
                return position-1;
            }
            else {                  //否则设置当前队列为pos
                position = loc;
            }
        }
        //返回child在指定group的位置
        return -1;
    }

    @Override
    public int getItemCount() {
        int groupCount = getGroupCount();
        //因为Group需要有头部
        int totalCount = groupCount;
        for (int i=0; i<groupCount; ++i){
            totalCount += getChildCount(i);
        }
        return totalCount;
    }

    //判断获取的item是group还是child
    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_GROUP;
        }

        for (int i=0; i<getGroupCount(); ++i){
            int total = getChildCount(i)+1; //每个队列的总和
            if (position == 0){
                return TYPE_GROUP;
            }
            else if (position < 0){
                return TYPE_CHILD;
            }
            position -= total;
        }
        //剩下的肯定是最后一行
        return TYPE_CHILD;
    }

    /**
     * 设置Group与child在GridLayoutManager情况下占用的格子
     */
    class GroupSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{
        private int maxSize;
        public GroupSpanSizeLookup(int maxSize) {
            this.maxSize = maxSize;
        }

        @Override
        public int getSpanSize(int position) {
            if (getItemViewType(position) == TYPE_GROUP){
                return maxSize;
            }
            else {
                return 1;
            }
        }
    }


    public int getGroupToPosition(int groupPos){
        int position = 0;
        for (int i=0; i<groupPos; ++i){
            position += getChildCount(groupPos)+1;
        }
        return position;
    }
    //child转换成position
    public int getChildToPosition(int groupPos, int childPos){
        int position = 0;
        for (int i=0; i<groupPos; ++i){
            position += getChildCount(i)+1;
        }
        position += childPos + 1;
        return position;
    }

    public interface OnGroupClickListener {
       void onGroupClick(View view,int pos);
    }

    public interface OnChildClickListener {
        void onChildClick(View view,int groupPos,int childPos);
    }
}
