package com.example.newbiechen.ireader.ui.adapter.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.ui.base.adapter.ViewHolderImpl;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.widget.transform.CircleTransform;

/**
 * Created by newbiechen on 17-4-20.
 */

public class DiscCommentHolder extends ViewHolderImpl<BookCommentBean>{

    private ImageView mIvPortrait;
    private TextView mTvName;
    private TextView mTvLv;
    private TextView mTvTime;
    private TextView mTvBrief;
    private TextView mTvLabelDistillate;
    private TextView mTvLabelHot;
    private TextView mTvResponseCount;
    private TextView mTvLikeCount;

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
    public void onBind(BookCommentBean value, int pos) {
        //头像
        Glide.with(getContext())
                .load(Constant.IMG_BASE_URL+value.getAuthorBean().getAvatar())
                .placeholder(R.drawable.ic_default_portrait)
                .error(R.drawable.ic_load_error)
                .transform(new CircleTransform(App.getContext()))
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
            mTvTime.setVisibility(View.VISIBLE);
        }
        else {
            mTvLabelDistillate.setVisibility(View.GONE);
            mTvTime.setVisibility(View.GONE);
        }
        //comment or vote
        String type = value.getType();
        Drawable drawable = null;
        switch (type){
            case Constant.BOOK_TYPE_COMMENT:
                drawable = getContext().getResources().getDrawable(R.drawable.ic_notif_post);
                break;
            case Constant.BOOK_TYPE_VOTE:
                drawable = getContext().getResources().getDrawable(R.drawable.ic_notif_vote);
                break;
            default:
                drawable = getContext().getResources().getDrawable(R.mipmap.ic_launcher);
                break;
        }
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        //time
        mTvTime.setText(StringUtils.dateConvert(value.getUpdated(),Constant.FORMAT_BOOK_DATE));

        mTvResponseCount.setCompoundDrawables(drawable,null,null,null);
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
