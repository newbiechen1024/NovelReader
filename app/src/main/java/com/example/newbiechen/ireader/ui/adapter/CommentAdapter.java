package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.CommentBean;
import com.example.newbiechen.ireader.ui.adapter.view.CommentView;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-4-29.
 */

public class CommentAdapter extends WholeAdapter<CommentBean> {

    public CommentAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected View createView(Context context, int viewType) {
        return new CommentView(context,false);
    }
}
