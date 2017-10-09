package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.flag.CommunityType;
import com.example.newbiechen.ireader.model.bean.SectionBean;
import com.example.newbiechen.ireader.ui.activity.BookDiscussionActivity;
import com.example.newbiechen.ireader.ui.adapter.SectionAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-15.
 * 讨论区
 */

public class CommunityFragment extends BaseFragment implements BaseListAdapter.OnItemClickListener{
    /***************view******************/
    @BindView(R.id.community_rv_content)
    RecyclerView mRvContent;

    private SectionAdapter mAdapter;

    @Override
    protected int getContentId() {
        return R.layout.fragment_community;
    }

    /***********************************init method*************************************************/

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    private void setUpAdapter(){
        ArrayList<SectionBean> sections = new ArrayList<>();

        /*觉得采用枚举会好一些，要不然就是在Constant中创建Map类*/
        for (CommunityType type : CommunityType.values()){
            sections.add(new SectionBean(type.getTypeName(),type.getIconId()));
        }

        mAdapter = new SectionAdapter();
        mRvContent.setHasFixedSize(true);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mAdapter);
        mAdapter.addItems(sections);
    }

    /****************************click method********************************/

    @Override
    protected void initClick() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int pos) {
        //根据类型，启动相应的Discussion区
        CommunityType type = CommunityType.values()[pos];
        BookDiscussionActivity.startActivity(getContext(),type);
    }
}
