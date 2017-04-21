package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.ui.adapter.view.BookHelpsView;
import com.example.newbiechen.ireader.widget.WholeAdapter;

/**
 * Created by newbiechen on 17-4-21.
 */

public class BookHelpsAdapter extends WholeAdapter<BookHelpsBean>{

    public BookHelpsAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected View createView(Context context, int viewType) {
        return new BookHelpsView(context);
    }
}
