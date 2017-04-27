package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.ui.adapter.view.DiscCommentView;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-4-20.
 */

public class DiscCommentAdapter extends WholeAdapter<BookCommentBean> {

    public DiscCommentAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected View createView(Context context, int viewType) {
        return new DiscCommentView(context);
    }
}
