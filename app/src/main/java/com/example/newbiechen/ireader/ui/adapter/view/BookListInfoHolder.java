package com.example.newbiechen.ireader.ui.adapter.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookListDetailBean;
import com.example.newbiechen.ireader.ui.base.adapter.ViewHolderImpl;
import com.example.newbiechen.ireader.utils.Constant;

/**
 * Created by newbiechen on 17-5-2.
 */

public class BookListInfoHolder extends ViewHolderImpl<BookListDetailBean.BooksBean.BookBean> {

    private ImageView mIvPortrait;
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvMsg;
    private TextView mTvWord;
    private TextView mTvContent;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_book_list_info;
    }

    @Override
    public void initView() {
        mIvPortrait = findById(R.id.book_list_info_iv_cover);
        mTvTitle = findById(R.id.book_list_info_tv_title);
        mTvAuthor = findById(R.id.book_list_info_tv_author);
        mTvContent = findById(R.id.book_list_info_tv_content);
        mTvMsg = findById(R.id.book_list_info_tv_msg);
        mTvWord = findById(R.id.book_list_info_tv_word);
    }

    @Override
    public void onBind(BookListDetailBean.BooksBean.BookBean value, int pos) {
        //头像
        Glide.with(getContext())
                .load(Constant.IMG_BASE_URL+value.getCover())
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .fitCenter()
                .into(mIvPortrait);
        //书单名
        mTvTitle.setText(value.getTitle());
        //作者
        mTvAuthor.setText(value.getAuthor());
        //简介
        mTvContent.setText(value.getLongIntro());
        //信息
        mTvMsg.setText(getContext().getResources().getString(R.string.nb_book_message,
                value.getLatelyFollower(),value.getRetentionRatio()));
        //书籍字数
        mTvWord.setText(getContext().getResources().getString(R.string.nb_book_word,value.getWordCount()/10000));
    }
}
