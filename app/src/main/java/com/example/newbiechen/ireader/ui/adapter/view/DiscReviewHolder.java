package com.example.newbiechen.ireader.ui.adapter.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.ui.base.adapter.ViewHolderImpl;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StringUtils;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscReviewHolder extends ViewHolderImpl<BookReviewBean>{

    ImageView mIvPortrait;
    TextView mTvBookName;
    TextView mTvBookType;
    TextView mTvTime;
    TextView mTvBrief;
    TextView mTvLabelDistillate;
    TextView mTvLabelHot;
    TextView mTvRecommendCount;

    @Override
    public void initView() {
        mIvPortrait = findById(R.id.review_iv_portrait);
        mTvBookName = findById(R.id.review_tv_book_name);
        mTvBookType = findById(R.id.review_tv_book_type);
        mTvTime = findById(R.id.review_tv_time);
        mTvBrief = findById(R.id.review_tv_brief);
        mTvLabelDistillate = findById(R.id.review_tv_distillate);
        mTvLabelHot = findById(R.id.review_tv_hot);
        mTvRecommendCount = findById(R.id.review_tv_recommend);
    }

    @Override
    public void onBind(BookReviewBean value, int pos) {
        //头像
        Glide.with(App.getContext())
                .load(Constant.IMG_BASE_URL+value.getBookBean().getCover())
                .placeholder(R.drawable.ic_default_portrait)
                .error(R.drawable.ic_load_error)
                .fitCenter()
                .into(mIvPortrait);
        //名字
        mTvBookName.setText(value.getBookBean().getTitle());
        //类型
        String bookType = Constant.bookType.get(value.getBookBean().getType());
        mTvBookType.setText(StringUtils.getString(R.string.nb_book_type,bookType));
        //简介
        mTvBrief.setText(value.getTitle());
        //time
        mTvTime.setText(StringUtils.dateConvert(value.getUpdated(),Constant.FORMAT_BOOK_DATE));
        //label
        if (value.getState().equals(Constant.BOOK_STATE_DISTILLATE)){
            mTvLabelDistillate.setVisibility(View.VISIBLE);
        }
        else {
            mTvLabelDistillate.setVisibility(View.GONE);
        }
        //response count
        mTvRecommendCount.setText(StringUtils.getString(R.string.nb_book_recommend,value.getHelpfulBean().getYes()));
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_disc_review;
    }
}