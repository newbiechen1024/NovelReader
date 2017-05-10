package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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

public class TagChildView extends RelativeLayout implements IAdapter<String> {
    private TextView mTvName;

    private int mSelectTag = -1;
    public TagChildView(Context context) {
        super(context);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_tag_child,this,false);
        addView(view);
        mTvName = ButterKnife.findById(view,R.id.tag_child_btn_name);
    }

    @Override
    public void onBind(String value, int pos) {
        mTvName.setText(value);
        //这里要重置点击事件
        if (mSelectTag == pos){
            mTvName.setTextColor(ContextCompat.getColor(getContext(),R.color.light_red));
        }
        else {
            mTvName.setTextColor(ContextCompat.getColor(getContext(),R.color.nb_text_default));
        }
    }

    public void setTagSelected(int pos){
        mSelectTag = pos;
    }
}