package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.SectionBean;
import com.example.newbiechen.ireader.ui.base.IAdapter;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-4-16.
 */

public class SectionView extends RelativeLayout implements IAdapter<SectionBean>{
    private View contentView;
    ImageView mIvIcon;
    TextView mTvName;

    public SectionView(Context context) {
        super(context);
        initView();
    }

    private void initView(){
        contentView = LayoutInflater.from(getContext())
                .inflate(R.layout.item_section,this,false);
        addView(contentView);
        mIvIcon = ButterKnife.findById(contentView,R.id.section_iv_icon);
        mTvName = ButterKnife.findById(contentView,R.id.section_tv_name);
    }

    @Override
    public void onBind(SectionBean value, int pos) {
        mIvIcon.setImageResource(value.getDrawableId());
        mTvName.setText(value.getName());
    }
}
