package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.DiscussionBean;
import com.example.newbiechen.ireader.ui.base.IAdapter;
import com.example.newbiechen.ireader.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by newbiechen on 17-4-20.
 */

public class DiscussionView extends RelativeLayout implements IAdapter<DiscussionBean>{
    @BindView(R.id.discussion_iv_portrait)
    ImageView mIvPortrait;
    @BindView(R.id.discussion_tv_name)
    TextView mTvName;
    @BindView(R.id.discussion_tv_lv)
    TextView mTvLv;
    @BindView(R.id.discussion_tv_time)
    TextView mTvTime;
    @BindView(R.id.discussion_tv_brief)
    TextView mTvBrief;
    @BindView(R.id.discussion_tv_label_distillate)
    TextView mTvLableDistillate;
    @BindView(R.id.discussion_tv_label_hot)
    TextView mTvLableHot;
    @BindView(R.id.discussion_tv_response_count)
    TextView mTvResponseCount;
    @BindView(R.id.discussion_tv_like_count)
    TextView mTvLikeCount;

    public DiscussionView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_discussion,this,false);
        addView(view);
        ButterKnife.bind(this,view);
    }

    @Override
    public void onBind(DiscussionBean value, int pos) {
        //头像
        Glide.with(App.getAppContext())
                .load(Constant.IMG_BASE_URL+value.getAuthor().getAvatar())
                .placeholder(R.drawable.ic_default_portrait)
                .error(R.drawable.ic_load_error)
                .centerCrop()
                .fitCenter()
                .into(mIvPortrait);
        //名字
        mTvName.setText(value.getAuthor().getNickname());
        //等级
        mTvLv.setText(getResources().getString(R.string.nb_section_user_lv,
                value.getAuthor().getLv()));
        //简介
        mTvBrief.setText(value.getTitle());
        //label
        if (value.getState().equals(Constant.BOOK_STATE_DISTILLATE)){
            mTvLableDistillate.setVisibility(VISIBLE);
        }
        else {
            mTvLableDistillate.setVisibility(GONE);
        }
        //comment or vote
        String type = value.getType();
        Drawable drawable = null;
        switch (type){
            case Constant.BOOK_TYPE_COMMENT:
                drawable = getResources().getDrawable(R.drawable.ic_notif_post);
                break;
            case Constant.BOOK_TYPE_VOTE:
                drawable = getResources().getDrawable(R.drawable.ic_notif_vote);
                break;
            default:
                drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                break;
        }
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        mTvResponseCount.setCompoundDrawables(drawable,null,null,null);
        //response count
        mTvResponseCount.setText(value.getCommentCount()+"");
        //like count
        mTvLikeCount.setText(value.getLikeCount()+"");
    }
}
