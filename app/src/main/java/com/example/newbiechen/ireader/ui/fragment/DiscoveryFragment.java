package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.SectionBean;
import com.example.newbiechen.ireader.ui.adapter.SectionAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.widget.DashItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-15.
 */

public class DiscoveryFragment extends BaseFragment {
    @BindView(R.id.discovery_rv_content)
    RecyclerView mRvContent;
    SectionAdapter mAdapter;
    @Override
    protected int getContentId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    private void setUpAdapter(){
        ArrayList<SectionBean> sections = new ArrayList<>();
        sections.add(new SectionBean(R.string.nb_fragment_section_top,R.drawable.ic_section_top));
        sections.add(new SectionBean(R.string.nb_fragment_section_topic,R.drawable.ic_section_topic));
        sections.add(new SectionBean(R.string.nb_fragment_section_sort,R.drawable.ic_section_sort));
        sections.add(new SectionBean(R.string.nb_framgent_section_listen_novel,R.drawable.ic_section_listen));

        mAdapter = new SectionAdapter();
        mRvContent.setHasFixedSize(true);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DashItemDecoration());
        mRvContent.setAdapter(mAdapter);
        mAdapter.addItems(sections);
    }


    @Override
    protected void initClick() {
        mAdapter.setOnItemClickListener(
                (view,pos) ->{
                    //跳转
                }
        );

    }
}
