package com.example.newbiechen.ireader.ui.adapter;

import com.example.newbiechen.ireader.model.bean.BookSortBean;
import com.example.newbiechen.ireader.ui.adapter.view.BookSortHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-4-23.
 */

public class BookSortAdapter extends BaseListAdapter<BookSortBean>{

    @Override
    protected IViewHolder<BookSortBean> createViewHolder(int viewType) {
        return new BookSortHolder();
    }
}
