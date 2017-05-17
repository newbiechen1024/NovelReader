package com.example.newbiechen.ireader.ui.adapter;

import com.example.newbiechen.ireader.model.bean.HotCommentBean;
import com.example.newbiechen.ireader.ui.adapter.view.HotCommentHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-5-4.
 */

public class HotCommentAdapter extends BaseListAdapter<HotCommentBean>{
    @Override
    protected IViewHolder<HotCommentBean> createViewHolder(int viewType) {
        return new HotCommentHolder();
    }
}
