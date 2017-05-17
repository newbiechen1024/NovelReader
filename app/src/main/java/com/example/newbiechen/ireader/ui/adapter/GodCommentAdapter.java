package com.example.newbiechen.ireader.ui.adapter;

import com.example.newbiechen.ireader.model.bean.CommentBean;
import com.example.newbiechen.ireader.ui.adapter.view.CommentHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-4-29.
 */

public class GodCommentAdapter extends BaseListAdapter<CommentBean>{
    @Override
    protected IViewHolder<CommentBean> createViewHolder(int viewType) {
        return new CommentHolder(true);
    }
}
