package com.example.newbiechen.ireader.ui.adapter.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.CommentBean;
import com.example.newbiechen.ireader.model.bean.ReplyToBean;
import com.example.newbiechen.ireader.ui.base.adapter.ViewHolderImpl;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.widget.transform.CircleTransform;

/**
 * Created by newbiechen on 17-4-29.
 */

public class CommentHolder extends ViewHolderImpl<CommentBean>{

    ImageView ivPortrait;
    TextView tvFloor;
    TextView tvName;
    TextView tvLv;
    TextView tvTime;
    TextView tvLikeCount;
    TextView tvContent;
    TextView tvReplyName;
    TextView tvReplyFloor;

    private boolean isBestComment = false;

    public CommentHolder(boolean bestComment) {
        isBestComment = bestComment;
    }

    @Override
    public void initView() {
        ivPortrait = findById(R.id.comment_iv_portrait);
        tvFloor = findById(R.id.comment_tv_floor);
        tvName = findById(R.id.comment_tv_name);
        tvLv = findById(R.id.comment_tv_lv);
        tvTime = findById(R.id.comment_tv_time);
        tvLikeCount = findById(R.id.comment_tv_like_count);
        tvContent = findById(R.id.comment_tv_content);
        tvReplyName = findById(R.id.comment_tv_reply_name);
        tvReplyFloor = findById(R.id.comment_tv_reply_floor);
    }

    @Override
    public void onBind(CommentBean value, int pos) {
        //头像
        Glide.with(getContext())
                .load(Constant.IMG_BASE_URL+value.getAuthor().getAvatar())
                .placeholder(R.drawable.ic_loadding)
                .error(R.drawable.ic_load_error)
                .transform(new CircleTransform(getContext()))
                .into(ivPortrait);
        //名字
        tvName.setText(value.getAuthor().getNickname());
        //等级
        tvLv.setText(StringUtils.getString(R.string.nb_user_lv,value.getAuthor().getLv()));
        //楼层
        tvFloor.setText(StringUtils.getString(R.string.nb_comment_floor,value.getFloor()));
        if (isBestComment){
            //点赞数
            tvTime.setVisibility(View.GONE);
            tvLikeCount.setVisibility(View.VISIBLE);
            tvLikeCount.setText(StringUtils.getString(R.string.nb_comment_like_count,value.getLikeCount()));
        }
        else {
            //时间
            tvTime.setVisibility(View.VISIBLE);
            tvLikeCount.setVisibility(View.GONE);
            tvTime.setText(StringUtils.dateConvert(value.getCreated(),Constant.FORMAT_BOOK_DATE));
        }
        //内容
        tvContent.setText(value.getContent());
        //回复的人名
        ReplyToBean replyToBean = value.getReplyTo();
        if (replyToBean != null){
            tvReplyName.setVisibility(View.VISIBLE);
            tvReplyFloor.setVisibility(View.VISIBLE);
            tvReplyName.setText(StringUtils.getString(R.string.nb_comment_reply_nickname,
                    replyToBean.getAuthor().getNickname()));
            //回复的楼层
            tvReplyFloor.setText(StringUtils.getString(R.string.nb_comment_reply_floor,
                    replyToBean.getFloor()));
        }
        else {
            tvReplyName.setVisibility(View.GONE);
            tvReplyFloor.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_comment;
    }
}
