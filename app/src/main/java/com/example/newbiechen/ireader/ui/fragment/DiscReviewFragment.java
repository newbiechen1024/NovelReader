package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.SelectorEvent;
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.model.flag.BookDistillate;
import com.example.newbiechen.ireader.model.flag.BookSort;
import com.example.newbiechen.ireader.model.flag.BookType;
import com.example.newbiechen.ireader.presenter.DiscReviewPresenter;
import com.example.newbiechen.ireader.presenter.contract.DiscReviewContract;
import com.example.newbiechen.ireader.ui.adapter.DiscReviewAdapter;
import com.example.newbiechen.ireader.ui.base.BaseRxFragment;
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

public class DiscReviewFragment extends BaseRxFragment<DiscReviewContract.Presenter> implements DiscReviewContract.View{
    private static final String BUNDLE_BOOK = "bundle_book";
    private static final String BUNDLE_SORT = "bundle_sort";
    private static final String BUNDLE_DISTILLATE = "bundle_distillate";
    private static final String BUNDLE_START = "bundle_start";
    /*******************View**********************/
    @BindView(R.id.discussion_rv_content)
    ScrollRefreshRecyclerView mRvContent;
    /*******************Object*********************/
    private DiscReviewAdapter mDiscReviewAdapter;
    /*******************Params**********************/
    private String mSortType = BookSort.DEFAULT.getNetName();
    private String mBookType = BookType.ALL.getNetName();
    private String mDistillate = BookDistillate.ALL.getNetName();
    private int mStart = 0;
    private int mLimited = 20;

    /**********************init method****************************/
    @Override
    protected int getContentId() {
        return R.layout.fragment_discussion;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null){
            mBookType = savedInstanceState.getString(BUNDLE_BOOK);
            mSortType = savedInstanceState.getString(BUNDLE_SORT);
            mDistillate = savedInstanceState.getString(BUNDLE_DISTILLATE);
            mStart = savedInstanceState.getInt(BUNDLE_START);
        }
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DashItemDecoration());
        mDiscReviewAdapter = new DiscReviewAdapter(getContext(),new WholeAdapter.Options());
        mRvContent.setAdapter(mDiscReviewAdapter);
    }

    @Override
    protected DiscReviewContract.Presenter bindPresenter() {
        return new DiscReviewPresenter();
    }
    /*************************click method************************/

    @Override
    protected void initClick() {
        super.initClick();

        mRvContent.setOnRefreshListener(() -> startRefresh());
        mDiscReviewAdapter.setOnLoadMoreListener(
                () -> mPresenter.loadingBookReview(mSortType,mBookType,mStart, mLimited,mDistillate)
        );

        RxBus.getInstance()
                .toObservable(Constant.MSG_SELECTOR, SelectorEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) ->{
                            mSortType = event.sort.getNetName();
                            mBookType = event.type.getNetName();
                            mDistillate = event.distillate.getNetName();
                            startRefresh();
                        }
                );
    }

    /****************************logic method*********************************/
    @Override
    protected void processLogic() {
        super.processLogic();
        //首次自动刷新
        mRvContent.autoRefresh();
        mPresenter.firstLoading(mSortType,mBookType,mStart,mLimited,mDistillate);
    }

    private void startRefresh(){
        mStart = 0;
        mPresenter.refreshBookReview(mSortType,mBookType,mStart,mLimited,mDistillate);
    }

    /****************************rewrite method******************************************/
    @Override
    public void finishRefresh(List<BookReviewBean> beans) {
        mDiscReviewAdapter.refreshItems(beans);
        mStart = beans.size();
    }

    @Override
    public void finishLoading(List<BookReviewBean> beans) {
        mDiscReviewAdapter.addItems(beans);
        mStart += beans.size();
    }

    @Override
    public void showErrorTip() {
        mRvContent.showNetTip();
    }


    @Override
    public void showError() {
            mDiscReviewAdapter.showLoadError();
    }

    @Override
    public void complete() {
        mRvContent.finishRefresh();
    }
    /****************************lifecycle method************************************/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_BOOK, mBookType);
        outState.putString(BUNDLE_SORT,mSortType);
        outState.putString(BUNDLE_DISTILLATE,mDistillate);
        outState.putInt(BUNDLE_START,mStart);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.saveBookReview(mDiscReviewAdapter.getItems());
    }
}
