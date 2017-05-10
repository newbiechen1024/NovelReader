package com.example.newbiechen.ireader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.BookSubSortEvent;
import com.example.newbiechen.ireader.model.bean.BookSubSortBean;
import com.example.newbiechen.ireader.model.flag.BookSortListType;
import com.example.newbiechen.ireader.ui.adapter.HorizonTagAdapter;
import com.example.newbiechen.ireader.ui.base.BaseTabActivity;
import com.example.newbiechen.ireader.ui.fragment.BookSortListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-24.
 * Book Sort List: 分类书籍列表
 */

public class BookSortListActivity extends BaseTabActivity {
    private static final String EXTRA_GENDER = "extra_gender";
    private static final String EXTRA_SUB_SORT = "extra_sub_sort";

    /*******************/
    @BindView(R.id.book_sort_list_rv_tag)
    RecyclerView mRvTag;
    /************************************/
    private HorizonTagAdapter mTagAdapter;
    /**********************************/
    private BookSubSortBean mSubSortBean;
    private String mGender;

    public static void startActivity(Context context, String gender, BookSubSortBean subSortBean){
        Intent intent = new Intent(context,BookSortListActivity.class);
        intent.putExtra(EXTRA_GENDER,gender);
        intent.putExtra(EXTRA_SUB_SORT, subSortBean);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_book_sort_list;
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle(mSubSortBean.getMajor());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null){
            mGender = savedInstanceState.getString(EXTRA_GENDER);
            mSubSortBean = savedInstanceState.getParcelable(EXTRA_SUB_SORT);
        }
        else {
            mGender = getIntent().getStringExtra(EXTRA_GENDER);
            mSubSortBean = getIntent().getParcelableExtra(EXTRA_SUB_SORT);
        }
    }

    @Override
    protected List<Fragment> createTabFragments() {
        List<Fragment> fragments = new ArrayList<>();
        for (BookSortListType type : BookSortListType.values()){
            fragments.add(BookSortListFragment.newInstance(mGender,mSubSortBean.getMajor(),type));
        }
        return fragments;
    }

    @Override
    protected List<String> createTabTitles() {
        List<String> titles = new ArrayList<>();
        for (BookSortListType type : BookSortListType.values()){
            titles.add(type.getTypeName());
        }
        return titles;
    }

    @Override
    protected void initClick() {
        super.initClick();
        mTagAdapter.setOnItemClickListener(
                (view,pos) -> {
                    String subType = mTagAdapter.getItem(pos);
                    RxBus.getInstance().post(new BookSubSortEvent(subType));
                }
        );
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setUpAdapter();
    }

    private void setUpAdapter(){
        mTagAdapter = new HorizonTagAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvTag.setLayoutManager(linearLayoutManager);
        mRvTag.setAdapter(mTagAdapter);

        mSubSortBean.getMins().add(0,"全部");
        mTagAdapter.addItems(mSubSortBean.getMins());
    }
    /*****************************************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_GENDER, mGender);
        outState.putParcelable(EXTRA_SUB_SORT, mSubSortBean);
    }
}
