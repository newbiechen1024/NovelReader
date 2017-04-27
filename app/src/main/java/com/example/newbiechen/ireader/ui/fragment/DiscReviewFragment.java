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

public class DiscReviewFragment extends BaseFragment implements DiscReviewContract.View{
    /*******************View**********************/
    @BindView(R.id.discussion_rv_content)
    ScrollRefreshRecyclerView mRvContent;
    /*******************Object*********************/
    private DiscReviewAdapter mDiscReviewAdapter;
    private DiscReviewContract.Presenter mPresenter;
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
        new DiscReviewPresenter(this).subscribe();
        //初始化刷新
        startRefresh();
    }

    private void startRefresh(){
        mStart = 0;
        mRvContent.startRefresh();
        mPresenter.refreshBookReview(mSortType,mBookType,mStart,mLimited,mDistillate);
    }

    /****************************rewrite method******************************************/
    @Override
    public void finishRefresh(List<BookReviewBean> discussionBeans) {
        mDiscReviewAdapter.refreshItems(discussionBeans);
        mStart = discussionBeans.size();
        mRvContent.setRefreshing(false);
    }

    @Override
    public void finishLoading(List<BookReviewBean> discussionBeans) {
        mDiscReviewAdapter.addItems(discussionBeans);
        mStart += discussionBeans.size();
    }

    @Override
    public void loadError() {
        mDiscReviewAdapter.showLoadError();
    }

    @Override
    public void setPresenter(DiscReviewContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /****************************lifecycler method************************************/
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }
}
