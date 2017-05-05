package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.HotCommentBean;
import com.example.newbiechen.ireader.ui.base.IAdapter;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.widget.EasyRatingBar;
import com.example.newbiechen.ireader.widget.transform.CircleTransform;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-4.
 */

public class HotCommentView extends RelativeLayout implements IAdapter<HotCommentBean> {

    private ImageView mIvPortrait;
    private TextView mTvAuthor;
    private TextView mTvLv;
    private TextView mTvTitle;
    private EasyRatingBar mErbRate;
    private TextView mTvContent;
    private TextView mTvHelpful;
    private TextView mTvTime;

    public HotCommentView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_hot_comment, this, false);
        addView(view);

        mIvPortrait = ButterKnife.findById(view, R.id.hot_comment_iv_cover);
        mTvAuthor = ButterKnife.findById(view, R.id.hot_comment_tv_author);
        mTvLv = ButterKnife.findById(view, R.id.hot_comment_tv_lv);
        mTvTitle = ButterKnife.findById(view, R.id.hot_comment_title);
        mErbRate = ButterKnife.findById(view, R.id.hot_comment_erb_rate);
        mTvContent = ButterKnife.findById(view, R.id.hot_comment_tv_content);
        mTvHelpful = ButterKnife.findById(view, R.id.hot_comment_tv_helpful);
        mTvTime = ButterKnife.findById(view, R.id.hot_comment_tv_time);
    }

    @Override
    public void onBind(HotCommentBean value, int pos) {
        //头像
        Glide.with(getContext())
                .load(Constant.IMG_BASE_URL+value.getAuthor().getAvatar())
                .placeholder(R.drawable.ic_default_portrait)
                .error(R.drawable.ic_load_error)
                .transform(new CircleTransform(getContext()))
                .into(mIvPortrait);
        //作者
        mTvAuthor.setText(value.getAuthor().getNickname());
        //等级
        mTvLv.setText(getResources().getString(R.string.nb_user_lv,value.getAuthor().getLv()));
        //标题
        mTvTitle.setText(value.getTitle());
        //评分
        mErbRate.setRating(value.getRating());
        //内容
        mTvContent.setText(value.getContent());
        //点赞数
        mTvHelpful.setText(value.getLikeCount()+"");
        //时间
        mTvTime.setText(StringUtils.dateConvert(value.getUpdated(),Constant.FORMAT_BOOK_DATE));
    }
}
