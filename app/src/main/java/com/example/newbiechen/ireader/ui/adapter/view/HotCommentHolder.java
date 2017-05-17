package com.example.newbiechen.ireader.ui.adapter.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.HotCommentBean;
import com.example.newbiechen.ireader.ui.base.adapter.ViewHolderImpl;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.widget.EasyRatingBar;
import com.example.newbiechen.ireader.widget.transform.CircleTransform;

/**
 * Created by newbiechen on 17-5-4.
 */

public class HotCommentHolder extends ViewHolderImpl<HotCommentBean>{

    private ImageView mIvPortrait;
    private TextView mTvAuthor;
    private TextView mTvLv;
    private TextView mTvTitle;
    private EasyRatingBar mErbRate;
    private TextView mTvContent;
    private TextView mTvHelpful;
    private TextView mTvTime;

    @Override
    public void initView() {
        mIvPortrait = findById(R.id.hot_comment_iv_cover);
        mTvAuthor = findById(R.id.hot_comment_tv_author);
        mTvLv = findById(R.id.hot_comment_tv_lv);
        mTvTitle = findById(R.id.hot_comment_title);
        mErbRate = findById(R.id.hot_comment_erb_rate);
        mTvContent = findById(R.id.hot_comment_tv_content);
        mTvHelpful = findById(R.id.hot_comment_tv_helpful);
        mTvTime = findById(R.id.hot_comment_tv_time);
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
        mTvLv.setText(StringUtils.getString(R.string.nb_user_lv,value.getAuthor().getLv()));
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

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_hot_comment;
    }
}
