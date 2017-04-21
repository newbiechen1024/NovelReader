package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.SelectorEvent;
import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.bean.DiscussionBean;
import com.example.newbiechen.ireader.presenter.BookHelpsPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookHeplsContract;
import com.example.newbiechen.ireader.ui.adapter.BookHelpsAdapter;
import com.example.newbiechen.ireader.ui.adapter.DiscussionAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.widget.DashItemDecoration;
import com.example.newbiechen.ireader.widget.ScrollRefreshRecyclerView;
import com.example.newbiechen.ireader.widget.WholeAdapter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by newbiechen on 17-4-21.
 */

public class BookHelpsFragment extends BaseFragment implements BookHeplsContract.View{

    @BindView(R.id.section_rv_content)
    ScrollRefreshRecyclerView mRvContent;
    private BookHelpsAdapter mBookHelpsAdapter;

    private BookHeplsContract.Presenter mPresenter;

    private String mSortType = Constant.SortType.DEFAULT;
    private String mDistillate = Constant.Distillate.ALL;
    private int mStart = 0;
    private int mLimited = 20;

    @Override
    protected int getContentId() {
        return R.layout.fragment_section;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DashItemDecoration());
        mBookHelpsAdapter = new BookHelpsAdapter(getContext(),new WholeAdapter.Options());
        mRvContent.setAdapter(mBookHelpsAdapter);
    }

    @Override
    protected void initClick() {
        mRvContent.setOnRefreshListener(
                () -> startRefresh()
        );
        mBookHelpsAdapter.setOnLoadMoreListener(
                () -> mPresenter.loadingBookHelps(mSortType,mStart, mLimited,mDistillate)
        );

        RxBus.getInstance()
                .toObservable(Constant.MSG_SELECTOR, SelectorEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) ->{
                            mSortType = event.sort;
                            mDistillate = event.distillate;
                            startRefresh();
                        }
                );
    }

    private void startRefresh(){
        mStart = 0;
        mRvContent.initRefresh();
        mPresenter.refreshBookHelps(mSortType,mStart,mLimited,mDistillate);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        new BookHelpsPresenter(this).subscribe();
        startRefresh();
    }

    @Override
    public void finishRefresh(List<BookHelpsBean> discussionBeans) {
        mBookHelpsAdapter.refreshItems(discussionBeans);
        mStart = discussionBeans.size();
        mRvContent.setRefreshing(false);
    }

    @Override
    public void finishLoading(List<BookHelpsBean> discussionBeans) {
        mBookHelpsAdapter.addItems(discussionBeans);
        mStart += discussionBeans.size();
    }

    @Override
    public void loadError() {
        mBookHelpsAdapter.setLoadError();
    }

    @Override
    public void setPresenter(BookHeplsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }
}
