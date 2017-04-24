package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.SelectorEvent;
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.presenter.BookReviewPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookReviewContract;
import com.example.newbiechen.ireader.ui.adapter.BookReviewAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.widget.itemdecoration.DashItemDecoration;
import com.example.newbiechen.ireader.widget.refresh.ScrollRefreshRecyclerView;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by newbiechen on 17-4-21.
 */

public class BookReviewFragment extends BaseFragment implements BookReviewContract.View{

    @BindView(R.id.section_rv_content)
    ScrollRefreshRecyclerView mRvContent;

    private BookReviewAdapter mBookReviewAdapter;
    private BookReviewContract.Presenter mPresenter;

    private String mSortType = Constant.SortType.DEFAULT;
    private String mBookType = Constant.BookType.ALL;
    private String mDistillate = Constant.Distillate.ALL;
    private int mStart = 0;
    private int mLimited = 20;

    @Override
    protected int getContentId() {
        return R.layout.fragment_section;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DashItemDecoration());
        mBookReviewAdapter = new BookReviewAdapter(getContext(),new WholeAdapter.Options());
        mRvContent.setAdapter(mBookReviewAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();

        mRvContent.setOnRefreshListener(
                () -> startRefresh()
        );
        mBookReviewAdapter.setOnLoadMoreListener(
                () -> mPresenter.loadingBookReview(mSortType,mBookType,mStart, mLimited,mDistillate)
        );

        RxBus.getInstance()
                .toObservable(Constant.MSG_SELECTOR, SelectorEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) ->{
                            mSortType = event.sort;
                            mBookType = event.type;
                            mDistillate = event.distillate;
                            startRefresh();
                        }
                );
    }

    private void startRefresh(){
        mStart = 0;
        mRvContent.initRefresh();
        mPresenter.refreshBookReview(mSortType,mBookType,mStart,mLimited,mDistillate);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        super.processLogic(savedInstanceState);
        new BookReviewPresenter(this).subscribe();
        //初始化刷新
        startRefresh();
    }

    @Override
    public void finishRefresh(List<BookReviewBean> discussionBeans) {
        mBookReviewAdapter.refreshItems(discussionBeans);
        mStart = discussionBeans.size();
        mRvContent.setRefreshing(false);
    }

    @Override
    public void finishLoading(List<BookReviewBean> discussionBeans) {
        mBookReviewAdapter.addItems(discussionBeans);
        mStart += discussionBeans.size();
    }

    @Override
    public void loadError() {
        mBookReviewAdapter.setLoadError();
    }

    @Override
    public void setPresenter(BookReviewContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }
}
