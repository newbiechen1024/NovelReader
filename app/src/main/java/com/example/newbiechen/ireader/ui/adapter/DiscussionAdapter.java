package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.DiscussionBean;
import com.example.newbiechen.ireader.ui.adapter.view.DiscussionView;
import com.example.newbiechen.ireader.widget.WholeAdapter;

/**
 * Created by newbiechen on 17-4-20.
 */

public class DiscussionAdapter extends WholeAdapter<DiscussionBean> {

    public DiscussionAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected View createView(Context context, int viewType) {
        return new DiscussionView(context);
    }
}
