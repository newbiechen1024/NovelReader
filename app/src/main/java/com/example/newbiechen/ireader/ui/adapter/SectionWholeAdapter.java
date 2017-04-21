package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.SectionBean;
import com.example.newbiechen.ireader.ui.adapter.view.SectionView;
import com.example.newbiechen.ireader.widget.WholeAdapter;

/**
 * Created by newbiechen on 17-4-19.
 */

public class SectionWholeAdapter extends WholeAdapter<SectionBean>{

    public SectionWholeAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected View createView(Context context, int viewType) {
        return new SectionView(context);
    }
}
