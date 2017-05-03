package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.ui.adapter.view.GroupTagView;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

/**
 * Created by newbiechen on 17-5-2.
 */

public class GroupTagAdapter extends BaseListAdapter<String> {
    @Override
    protected View createView(Context context, int viewType) {
        return new GroupTagView(context);
    }
}
