package com.example.newbiechen.ireader.ui.adapter;

import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.example.newbiechen.ireader.ui.adapter.view.BillBookHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-5-3.
 */

public class BillBookAdapter extends BaseListAdapter<BillBookBean> {
    @Override
    protected IViewHolder<BillBookBean> createViewHolder(int viewType) {
        return new BillBookHolder();
    }
}
