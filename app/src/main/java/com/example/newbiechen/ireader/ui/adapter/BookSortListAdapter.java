package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;

import com.example.newbiechen.ireader.model.bean.SortBookBean;
import com.example.newbiechen.ireader.ui.adapter.view.BookSortListHolder;
import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-5-3.
 */

public class BookSortListAdapter extends WholeAdapter<SortBookBean>{
    public BookSortListAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<SortBookBean> createViewHolder(int viewType) {
        return new BookSortListHolder();
    }
}
