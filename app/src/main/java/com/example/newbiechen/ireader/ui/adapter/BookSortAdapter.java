package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.BookSortBean;
import com.example.newbiechen.ireader.ui.adapter.view.BookSortView;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

/**
 * Created by newbiechen on 17-4-23.
 */

public class BookSortAdapter extends BaseListAdapter<BookSortBean>{
    @Override
    protected View createView(Context context, int viewType) {
        return new BookSortView(context);
    }
}
