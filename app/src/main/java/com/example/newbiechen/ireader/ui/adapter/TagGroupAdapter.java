package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.BookTagBean;
import com.example.newbiechen.ireader.ui.adapter.view.GroupTagView;
import com.example.newbiechen.ireader.ui.adapter.view.TagGroupView;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

/**
 * Created by newbiechen on 17-5-2.
 */

public class TagGroupAdapter extends BaseListAdapter<BookTagBean> {
    @Override
    protected View createView(Context context, int viewType) {
        return new TagGroupView(context);
    }
}
