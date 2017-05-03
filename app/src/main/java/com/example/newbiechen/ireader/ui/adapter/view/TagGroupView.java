package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookTagBean;
import com.example.newbiechen.ireader.ui.adapter.GroupTagAdapter;
import com.example.newbiechen.ireader.ui.adapter.HorizonTagAdapter;
import com.example.newbiechen.ireader.ui.base.IAdapter;
import com.example.newbiechen.ireader.widget.itemdecoration.DefaultItemDecoration;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-2.
 */

public class TagGroupView extends RelativeLayout implements IAdapter<BookTagBean> {
    private static final int SPAN_COUNT = 4;

    private TextView mTvGroupName;
    private RecyclerView mRvTag;

    private GroupTagAdapter mTagAdapter;
    public TagGroupView(Context context) {
        super(context);
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_tag_group,this,false);
        addView(view);
        mTvGroupName = ButterKnife.findById(view,R.id.tag_group_name);
        mRvTag = ButterKnife.findById(view,R.id.tag_group_rv_tag);

        mTagAdapter = new GroupTagAdapter();
        mRvTag.addItemDecoration(new DefaultItemDecoration(getContext()));
        mRvTag.setLayoutManager(new GridLayoutManager(getContext(),SPAN_COUNT));
        mRvTag.setAdapter(mTagAdapter);
    }

    @Override
    public void onBind(BookTagBean value, int pos) {
        mTvGroupName.setText(value.getName());
        mTagAdapter.refreshItems(value.getTags());
    }
}
