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
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.ui.base.IAdapter;
import com.example.newbiechen.ireader.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-4-21.
 */

public class BookReviewView extends RelativeLayout implements IAdapter<BookReviewBean> {
    @BindView(R.id.review_tv_portrait)
    ImageView mIvPortrait;
    @BindView(R.id.review_tv_book_name)
    TextView mTvBookName;
    @BindView(R.id.review_tv_book_type)
    TextView mTvBookType;
    @BindView(R.id.review_tv_time)
    TextView mTvTime;
    @BindView(R.id.review_tv_brief)
    TextView mTvBrief;
    @BindView(R.id.review_tv_distillate)
    TextView mTvLabelDistillate;
    @BindView(R.id.review_tv_hot)
    TextView mTvLabelHot;
    @BindView(R.id.review_tv_recommend)
    TextView mTvRecommendCount;

    public BookReviewView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_book_review,this,false);
        addView(view);
        ButterKnife.bind(this,view);
    }

    @Override
    public void onBind(BookReviewBean value, int pos) {
        //头像
        Glide.with(App.getAppContext())
                .load(Constant.IMG_BASE_URL+value.getBook().getCover())
                .placeholder(R.drawable.ic_default_portrait)
                .error(R.drawable.ic_load_error)
                .centerCrop()
                .fitCenter()
                .into(mIvPortrait);
        //名字
        mTvBookName.setText(value.getBook().getTitle());
        //类型
        mTvBookType.setText(getResources().getString(R.string.nb_section_user_lv,
                value.getBook().getType()));
        //简介
        mTvBrief.setText(value.getTitle());
        //label
        if (value.getState().equals(Constant.BOOK_STATE_DISTILLATE)){
            mTvLabelDistillate.setVisibility(VISIBLE);
        }
        else {
            mTvLabelDistillate.setVisibility(GONE);
        }
        //response count
        mTvRecommendCount.setText(value.getHelpful().getYes()+"");
    }
}