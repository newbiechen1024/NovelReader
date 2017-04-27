package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.ui.adapter.view.DiscHelpsView;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscHelpsAdapter extends WholeAdapter<BookHelpsBean>{

    public DiscHelpsAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected View createView(Context context, int viewType) {
        return new DiscHelpsView(context);
    }
}
