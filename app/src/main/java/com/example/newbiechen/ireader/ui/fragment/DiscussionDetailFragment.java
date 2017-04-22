package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.widget.RefreshRecyclerView;

/**
 * Created by newbiechen on 17-4-22.
 * 之后的事情是:
 * 1. 创建作者的Header,第二创建神评论的Header，第三个是创建Content
 * 2. 设置加载(感觉没什么好写的，都是流水帐了) (以后再说吧)
 */

public class DiscussionDetailFragment extends BaseFragment {

    private static final String EXTRA_DETAIL_ID = "extra_detail_id";

    private RefreshRecyclerView mRvContent;

    public static Fragment newInstance(String detailId){
        Bundle args = new Bundle();
        args.putString(EXTRA_DETAIL_ID,detailId);
        Fragment fragment = new DiscussionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_discussion_detail;
    }

}
