package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.ui.adapter.view.CollBookView;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-5-8.
 */

public class CollBookAdapter extends WholeAdapter<CollBookBean> {

    @Override
    protected View createView(Context context, int viewType) {
        return new CollBookView(context);
    }
}
