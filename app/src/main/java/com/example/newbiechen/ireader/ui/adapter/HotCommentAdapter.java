package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.HotCommentBean;
import com.example.newbiechen.ireader.ui.adapter.view.HotCommentView;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

/**
 * Created by newbiechen on 17-5-4.
 */

public class HotCommentAdapter extends BaseListAdapter<HotCommentBean>{
    @Override
    protected View createView(Context context, int viewType) {
        return new HotCommentView(context);
    }
}
