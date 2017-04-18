package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.SectionBean;
import com.example.newbiechen.ireader.ui.activity.CommunityActivity;
import com.example.newbiechen.ireader.ui.adapter.SectionAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;
import com.example.newbiechen.ireader.widget.DashItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-15.
 */

public class CommunityFragment extends BaseFragment implements BaseListAdapter.OnItemClickListener{
    @BindView(R.id.community_rv_content)
    RecyclerView mRvContent;

    private SectionAdapter mAdapter;
    @Override
    protected int getContentId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    @Override
    protected void initClick() {
        mAdapter.setOnItemClickListener(this);
    }

    private void setUpAdapter(){
        ArrayList<SectionBean> sections = new ArrayList<>();
        String [] fragmentSection = getResources().getStringArray(R.array.nb_fragment_section);
        sections.add(new SectionBean(fragmentSection[0],R.drawable.ic_section_comment));
        sections.add(new SectionBean(fragmentSection[1],R.drawable.ic_section_discuss));
        sections.add(new SectionBean(fragmentSection[2],R.drawable.ic_section_help));
        sections.add(new SectionBean(fragmentSection[3],R.drawable.ic_section_girl));
        sections.add(new SectionBean(fragmentSection[4],R.drawable.ic_section_compose));

        mAdapter = new SectionAdapter();
        mRvContent.setHasFixedSize(true);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DashItemDecoration());
        mRvContent.setAdapter(mAdapter);
        mAdapter.addItems(sections);
    }

    @Override
    public void onItemClick(View view, int pos) {
        String section = mAdapter.getItems().get(pos)
                .getName();
        CommunityActivity.startActivity(getContext(),section);
    }
}
