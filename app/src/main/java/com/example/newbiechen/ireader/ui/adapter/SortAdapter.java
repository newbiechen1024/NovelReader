package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.SortBean;
import com.example.newbiechen.ireader.ui.adapter.view.SortView;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

/**
 * Created by newbiechen on 17-4-23.
 */

public class SortAdapter extends BaseListAdapter<SortBean>{
    @Override
    protected View createView(Context context, int viewType) {
        return new SortView(context);
    }
}
