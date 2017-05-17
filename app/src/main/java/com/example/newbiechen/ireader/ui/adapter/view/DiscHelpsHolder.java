package com.example.newbiechen.ireader.ui.adapter.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.ui.base.adapter.ViewHolderImpl;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.widget.transform.CircleTransform;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscHelpsHolder extends ViewHolderImpl<BookHelpsBean> {

    ImageView mIvPortrait;
    TextView mTvName;
    TextView mTvLv;
    TextView mTvTime;
    TextView mTvBrief;
    TextView mTvLabelDistillate;
    TextView mTvLabelHot;
    TextView mTvResponseCount;
    TextView mTvLikeCount;

    @Override
    public void initView() {
        mIvPortrait = findById(R.id.disc_comment_iv_portrait);
        mTvName = findById(R.id.disc_comment_tv_name);
        mTvLv = findById(R.id.disc_comment_tv_lv);
        mTvTime = findById(R.id.disc_comment_tv_time);
        mTvBrief = findById(R.id.disc_comment_tv_brief);
        mTvLabelDistillate = findById(R.id.disc_comment_tv_label_distillate);
        mTvLabelHot = findById(R.id.disc_comment_tv_label_hot);
        mTvResponseCount = findById(R.id.disc_comment_tv_response_count);
        mTvLikeCount = findById(R.id.disc_comment_tv_like_count);
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
        mTvLv.setText(StringUtils.getString(R.string.nb_user_lv,
                value.getAuthorBean().getLv()));
        //简介
        mTvBrief.setText(value.getTitle());
        //label
        if (value.getState().equals(Constant.BOOK_STATE_DISTILLATE)){
            mTvLabelDistillate.setVisibility(View.VISIBLE);
        }
        else {
            mTvLabelDistillate.setVisibility(View.GONE);
        }
        //response count
        mTvResponseCount.setText(value.getCommentCount()+"");
        //like count
        mTvLikeCount.setText(value.getLikeCount()+"");
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_disc_comment;
    }
}
