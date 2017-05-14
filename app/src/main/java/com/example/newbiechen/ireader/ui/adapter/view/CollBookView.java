package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.CollBookManager;
import com.example.newbiechen.ireader.ui.base.IAdapter;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StringUtils;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-8.
 * CollectionBookView
 */

public class CollBookView extends RelativeLayout implements IAdapter<CollBookBean>{

    private static final String TAG = "CollBookView";
    private View mView;
    private ImageView mIvCover;
    private TextView mTvName;
    private TextView mTvChapter;
    private TextView mTvTime;
    private CheckBox mCbSelected;
    private ImageView mIvRedDot;
    private ImageView mIvTop;


    private CollBookBean mCollBookBean;
    public CollBookView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        mView = LayoutInflater.from(context)
                .inflate(R.layout.item_coll_book, this, false);
        addView(mView);

        mIvCover = ButterKnife.findById(mView, R.id.coll_book_iv_cover);
        mTvName = ButterKnife.findById(mView, R.id.coll_book_tv_name);
        mTvChapter = ButterKnife.findById(mView, R.id.coll_book_tv_chapter);
        mTvTime = ButterKnife.findById(mView, R.id.coll_book_tv_lately_update);
        mCbSelected = ButterKnife.findById(mView, R.id.coll_book_cb_selected);
        mIvRedDot = ButterKnife.findById(mView, R.id.coll_book_iv_red_rot);
        mIvTop = ButterKnife.findById(mView, R.id.coll_book_iv_top);
    }



    @Override
    public void onBind(CollBookBean value, int pos) {
        //书的图片
        Glide.with(getContext())
                .load(Constant.IMG_BASE_URL+value.getCover())
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .fitCenter()
                .into(mIvCover);
        //书名
        mTvName.setText(value.getTitle());
        //章节
        mTvChapter.setText(value.getLastChapter());
        //时间
        mTvTime.setText(StringUtils.dateConvert(value.getUpdated(), Constant.FORMAT_BOOK_DATE));
        //红点的显示，怎么办。
        //我的想法是，在Collection中加一个字段，当追更的时候设置为true。当点击的时候设置为false。
        //当更新的时候，最新数据跟旧数据进行比较，如果更新的话，设置为true。
        if (value.isUpdate()){
            mIvRedDot.setVisibility(VISIBLE);
        }
        else {
            mIvRedDot.setVisibility(GONE);
        }

        mCollBookBean = value;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(
                (v) -> {
                    //点击更新红点，并且更新数据
                    if (mIvRedDot.getVisibility() == VISIBLE){
                        mIvRedDot.setVisibility(GONE);
                        //更新数据
                        mCollBookBean.setIsUpdate(false);
                        CollBookManager.getInstance()
                                .updateCollBook(mCollBookBean);
                    }

                    l.onClick(v);
                }
        );
    }
}
