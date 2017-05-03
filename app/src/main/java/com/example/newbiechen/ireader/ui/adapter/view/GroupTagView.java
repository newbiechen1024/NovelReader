package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.IAdapter;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-2.
 */

public class GroupTagView extends RelativeLayout implements IAdapter<String> {

    private TextView mTvName;
    public GroupTagView(Context context) {
        super(context);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_group_tag,this,false);
        addView(view);
        mTvName = ButterKnife.findById(view,R.id.group_tag_name);
    }

    @Override
    public void onBind(String value, int pos) {
        mTvName.setText(value);
        //这里要重置点击事件
    }

}