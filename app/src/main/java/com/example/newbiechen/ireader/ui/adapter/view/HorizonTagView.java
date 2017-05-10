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

public class HorizonTagView extends RelativeLayout implements IAdapter<String> {

    private TextView mTvName;
    private int  mTag;
    public HorizonTagView(Context context) {
        super(context);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_horizon_tag,this,false);
        addView(view);
        mTvName = ButterKnife.findById(view,R.id.horizon_tag_tv_name);
    }

    @Override
    public void onBind(String value, int pos) {
        mTvName.setText(value);
        if (pos == mTag){
            mTvName.setTextColor(getResources().getColor(R.color.light_red));
        }
        else {
            mTvName.setTextColor(getResources().getColor(R.color.nb_text_common_h2));
        }
    }

    public void setSelectedTag(int tag){
        mTag = tag;
    }
}
