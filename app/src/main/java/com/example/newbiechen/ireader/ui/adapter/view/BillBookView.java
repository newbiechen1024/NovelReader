package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.example.newbiechen.ireader.ui.base.IAdapter;
import com.example.newbiechen.ireader.utils.Constant;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-3.
 */

public class BillBookView extends RelativeLayout implements IAdapter<BillBookBean> {

    private ImageView mIvPortrait;
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvBrief;
    private TextView mTvMsg;


    public BillBookView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_book_brief,this,false);

        addView(view);
        mIvPortrait = ButterKnife.findById(view,R.id.book_brief_iv_portrait);
        mTvTitle = ButterKnife.findById(view,R.id.book_brief_tv_title);
        mTvAuthor = ButterKnife.findById(view,R.id.book_brief_tv_author);
        mTvBrief = ButterKnife.findById(view,R.id.book_brief_tv_brief);
        mTvMsg = ButterKnife.findById(view,R.id.book_brief_tv_msg);
    }

    @Override
    public void onBind(BillBookBean value, int pos) {

        //头像
        Glide.with(getContext())
                .load(Constant.IMG_BASE_URL+value.getCover())
                .placeholder(R.drawable.ic_default_portrait)
                .error(R.drawable.ic_load_error)
                .fitCenter()
                .into(mIvPortrait);
        //书单名
        mTvTitle.setText(value.getTitle());
        //作者
        mTvAuthor.setText(value.getAuthor());
        //简介
        mTvBrief.setText(value.getShortIntro());
        //信息
        mTvMsg.setText(getResources().getString(R.string.nb_book_message,
                value.getLatelyFollower(),value.getRetentionRatio()));
    }
}
