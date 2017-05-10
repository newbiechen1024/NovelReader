package com.example.newbiechen.ireader.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.newbiechen.ireader.model.bean.BookTagBean;
import com.example.newbiechen.ireader.ui.adapter.view.TagChildView;
import com.example.newbiechen.ireader.ui.adapter.view.TagGroupView;
import com.example.newbiechen.ireader.widget.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-5-5.
 * BookListTagGroup
 */

public class TagGroupAdapter extends GroupAdapter<String,String> {
    private static final String TAG = "TagGroupAdapter";
    private List<BookTagBean> mBookTagList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    public TagGroupAdapter(RecyclerView recyclerView, int spanSize) {
        super(recyclerView, spanSize);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getGroupCount() {
        return mBookTagList.size();
    }

    @Override
    public int getChildCount(int groupPos) {
        List<String> tagList = mBookTagList.get(groupPos).getTags();
        return tagList.size();
    }

    @Override
    public String getGroupItem(int groupPos) {
        return mBookTagList.get(groupPos).getName();
    }

    @Override
    public String getChildItem(int groupPos, int childPos) {
        List<String> tagList = getChildItems(groupPos);
        return tagList.get(childPos);
    }

    @Override
    protected View createGroupView(ViewGroup parent) {
        //是个TextView
        return new TagGroupView(parent.getContext());
    }

    @Override
    protected View createChildView(ViewGroup parent) {
        //是个TextView
        return new TagChildView(parent.getContext());
    }

    public List<String> getChildItems(int groupPos){
        return mBookTagList.get(groupPos).getTags();
    }

    public void refreshItems(List<BookTagBean> bookTags){
        mBookTagList.clear();
        mBookTagList.addAll(bookTags);
        notifyDataSetChanged();
    }

}
