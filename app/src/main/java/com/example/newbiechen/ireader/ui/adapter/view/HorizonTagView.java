package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookTagBean;
import com.example.newbiechen.ireader.model.bean.SectionBean;
import com.example.newbiechen.ireader.ui.base.IAdapter;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-2.
 */

public class HorizonTagView extends RelativeLayout implements IAdapter<String> {

    private TextView mTvName;

    public HorizonTagView(Context context) {
        super(context);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_tag,this,false);
        addView(view);
        mTvName = ButterKnife.findById(view,R.id.tag_tv_name);
    }

    @Override
    public void onBind(String value, int pos) {
        mTvName.setText(value);
        mTvName.setSelected(true);
    }
}
