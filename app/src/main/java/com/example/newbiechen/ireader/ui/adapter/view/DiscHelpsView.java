package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.ui.base.IAdapter;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.widget.transform.CircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscHelpsView extends RelativeLayout implements IAdapter<BookHelpsBean> {
    @BindView(R.id.disc_comment_iv_portrait)
    ImageView mIvPortrait;
    @BindView(R.id.disc_comment_tv_name)
    TextView mTvName;
    @BindView(R.id.disc_comment_tv_lv)
    TextView mTvLv;
    @BindView(R.id.disc_comment_tv_time)
    TextView mTvTime;
    @BindView(R.id.disc_comment_tv_brief)
    TextView mTvBrief;
    @BindView(R.id.disc_comment_tv_label_distillate)
    TextView mTvLableDistillate;
    @BindView(R.id.disc_comment_tv_label_hot)
    TextView mTvLableHot;
    @BindView(R.id.disc_comment_tv_response_count)
    TextView mTvResponseCount;
    @BindView(R.id.disc_comment_tv_like_count)
    TextView mTvLikeCount;

    public DiscHelpsView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_disc_comment,this,false);
        addView(view);
        ButterKnife.bind(this,view);
    }

    @Override
    public void onBind(BookHelpsBean value, int pos) {
        //头像
        Glide.with(getContext())
                .load(Constant.IMG_BASE_URL+value.getAuthorBean().getAvatar())
                .placeholder(R.drawable.ic_default_portrait)
                .error(R.drawable.ic_load_error)
                .transform(new CircleTransform(getContext()))
                .into(mIvPortrait);
        //名字
        mTvName.setText(value.getAuthorBean().getNickname());
        //等级
        mTvLv.setText(getResources().getString(R.string.nb_user_lv,
                value.getAuthorBean().getLv()));
        //简介
        mTvBrief.setText(value.getTitle());
        //label
        if (value.getState().equals(Constant.BOOK_STATE_DISTILLATE)){
            mTvLableDistillate.setVisibility(VISIBLE);
        }
        else {
            mTvLableDistillate.setVisibility(GONE);
        }
        //response count
        mTvResponseCount.setText(value.getCommentCount()+"");
        //like count
        mTvLikeCount.setText(value.getLikeCount()+"");
    }
}
