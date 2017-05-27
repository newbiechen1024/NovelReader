package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-5-27.
 * 本地书籍
 */

public class LocalBookFragment extends BaseFragment{
    @BindView(R.id.local_book_rv_content)
    RecyclerView mRvContent;

    @Override
    protected int getContentId() {
        return R.layout.fragment_local_book;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter(){

    }
}
