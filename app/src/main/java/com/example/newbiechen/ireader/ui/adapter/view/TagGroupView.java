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
 * Created by newbiechen on 17-5-5.
 */

public class TagGroupView extends RelativeLayout implements IAdapter<String> {
    private TextView mTvGroupName;

    public TagGroupView(Context context) {
        super(context);
        init();
    }
    private void init(){
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_tag_group,this,false);
        addView(view);
        mTvGroupName = ButterKnife.findById(view,R.id.tag_group_name);
    }


    @Override
    public void onBind(String value, int pos) {
        mTvGroupName.setText(value);
    }
}
