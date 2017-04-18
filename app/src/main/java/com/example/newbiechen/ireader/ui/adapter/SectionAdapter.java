package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.SectionBean;
import com.example.newbiechen.ireader.ui.adapter.view.SectionView;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

/**
 * Created by newbiechen on 17-4-16.
 */

public class SectionAdapter extends BaseListAdapter<SectionBean> {
    @Override
    protected View createView(Context context, int viewType) {
        return new SectionView(context);
    }
}
