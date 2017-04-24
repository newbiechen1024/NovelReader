package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.SortBean;
import com.example.newbiechen.ireader.ui.base.IAdapter;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-4-23.
 */

public class SortView extends RelativeLayout implements IAdapter<SortBean>{

    private TextView mTvType;
    private TextView mTvCount;

    public SortView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sort,this,false);

        addView(view);
        mTvType = ButterKnife.findById(view,R.id.sort_tv_type);
        mTvCount = ButterKnife.findById(view,R.id.sort_tv_count);
    }

    @Override
    public void onBind(SortBean value, int pos) {
        mTvType.setText(value.getName());
        mTvCount.setText(getContext().getResources().getString(R.string.nb_sort_book_count,value.getBookCount()));
    }
}
