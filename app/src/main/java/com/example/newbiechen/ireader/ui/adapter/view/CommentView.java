package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.CommentBean;
import com.example.newbiechen.ireader.model.bean.ReplyToBean;
import com.example.newbiechen.ireader.ui.base.IAdapter;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.widget.transform.CircleTransform;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-4-29.
 */

public class CommentView extends RelativeLayout implements IAdapter<CommentBean>{
    @BindView(R.id.comment_iv_portrait)
    ImageView ivPortrait;
    @BindView(R.id.comment_tv_floor)
    TextView tvFloor;
    @BindView(R.id.comment_tv_name)
    TextView tvName;
    @BindView(R.id.comment_tv_lv)
    TextView tvLv;
    @BindView(R.id.comment_tv_time)
    TextView tvTime;
    @BindView(R.id.comment_tv_like_count)
    TextView tvLikeCount;
    @BindView(R.id.comment_tv_content)
    TextView tvContent;
    @BindView(R.id.comment_tv_reply_name)
    TextView tvReplyName;
    @BindView(R.id.comment_tv_reply_floor)
    TextView tvReplyFloor;


    private boolean isBestComment = false;
    public CommentView(Context context,boolean bestComment) {
        super(context);
        isBestComment = bestComment;
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_comment,this,false);
        addView(view);
        ButterKnife.bind(this,view);
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
        tvLv.setText(getResources().getString(R.string.nb_user_lv,value.getAuthor().getLv()));
        //楼层
        tvFloor.setText(getResources().getString(R.string.nb_comment_floor,value.getFloor()));
        if (isBestComment){
            //点赞数
            tvTime.setVisibility(GONE);
            tvLikeCount.setVisibility(VISIBLE);
            tvLikeCount.setText(getResources().getString(R.string.nb_comment_like_count,value.getLikeCount()));
        }
        else {
            //时间
            tvTime.setVisibility(VISIBLE);
            tvLikeCount.setVisibility(GONE);
            tvTime.setText(StringUtils.dateConvert(value.getCreated(),Constant.FORMAT_BOOK_DATE));
        }
        //内容
        tvContent.setText(value.getContent());
        //回复的人名
        ReplyToBean replyToBean = value.getReplyTo();
        if (replyToBean != null){
            tvReplyName.setVisibility(VISIBLE);
            tvReplyFloor.setVisibility(VISIBLE);
            tvReplyName.setText(getResources().getString(R.string.nb_comment_reply_nickname,
                    replyToBean.getAuthor().getNickname()));
            //回复的楼层
            tvReplyFloor.setText(getResources().getString(R.string.nb_comment_reply_floor,
                    replyToBean.getFloor()));
        }
        else {
            tvReplyName.setVisibility(GONE);
            tvReplyFloor.setVisibility(GONE);
        }
    }

}
