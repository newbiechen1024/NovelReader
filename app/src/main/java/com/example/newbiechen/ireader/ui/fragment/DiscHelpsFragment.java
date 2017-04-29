package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.SelectorEvent;
import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.flag.BookDistillate;
import com.example.newbiechen.ireader.model.flag.BookSort;
import com.example.newbiechen.ireader.presenter.DiscHelpsPresenter;
import com.example.newbiechen.ireader.presenter.contract.DiscHelpsContract;
import com.example.newbiechen.ireader.ui.adapter.DiscHelpsAdapter;
import com.example.newbiechen.ireader.ui.base.BaseRxFragment;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.widget.itemdecoration.DashItemDecoration;
import com.example.newbiechen.ireader.widget.refresh.ScrollRefreshRecyclerView;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscHelpsFragment extends BaseRxFragment<DiscHelpsContract.Presenter> implements DiscHelpsContract.View{
    private static final String BUNDLE_SORT = "bundle_sort";
    private static final String BUNDLE_DISTILLATE = "bundle_distillate";
    private static final String BUNDLE_START = "bundle_start";
    /*****************View********************/
    @BindView(R.id.discussion_rv_content)
    ScrollRefreshRecyclerView mRvContent;
    /******************Object******************/
    private DiscHelpsAdapter mDiscHelpsAdapter;
    /******************Params*******************/
    private String mSortType = BookSort.DEFAULT.getNetName();
    private String mDistillate = BookDistillate.ALL.getNetName();
    private int mStart = 0;
    private int mLimited = 20;

    /************************init method*********************************/
    @Override
    protected int getContentId() {
        return R.layout.fragment_discussion;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null){
            mSortType = savedInstanceState.getString(BUNDLE_SORT);
            mDistillate = savedInstanceState.getString(BUNDLE_DISTILLATE);
            mStart = savedInstanceState.getInt(BUNDLE_START);
        }
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DashItemDecoration());
        mDiscHelpsAdapter = new DiscHelpsAdapter(getContext(),new WholeAdapter.Options());
        mRvContent.setAdapter(mDiscHelpsAdapter);
    }

    /******************************click method******************************/
    @Override
    protected void initClick() {
        mRvContent.setOnRefreshListener(
                () -> startRefresh()
        );
        mDiscHelpsAdapter.setOnLoadMoreListener(
                () -> mPresenter.loadingBookHelps(mSortType,mStart, mLimited,mDistillate)
        );

        Disposable eventDispo = RxBus.getInstance()
                .toObservable(Constant.MSG_SELECTOR, SelectorEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) ->{
                            mSortType = event.sort.getNetName();
                            mDistillate = event.distillate.getNetName();
                            startRefresh();
                        }
                );
        addDisposable(eventDispo);
    }

    @Override
    protected DiscHelpsContract.Presenter bindPresenter() {
        return new DiscHelpsPresenter();
    }

    /*****************************logic method*****************************/
    @Override
    protected void processLogic() {
        super.processLogic();

        mRvContent.autoRefresh();
        mPresenter.firstLoading(mSortType,mStart,mLimited,mDistillate);
    }

    private void startRefresh(){
        mStart = 0;
        mPresenter.refreshBookHelps(mSortType,mStart,mLimited,mDistillate);
    }

    /**************************rewrite method****************************************/
    @Override
    public void finishRefresh(List<BookHelpsBean> beans) {
        mDiscHelpsAdapter.refreshItems(beans);
        mStart = beans.size();
        mRvContent.setRefreshing(false);
    }

    @Override
    public void finishLoading(List<BookHelpsBean> beans) {
        mDiscHelpsAdapter.addItems(beans);
        mStart += beans.size();
    }

    @Override
    public void showErrorTip() {
        mRvContent.showNetTip();
    }

    @Override
    public void showError() {
        mDiscHelpsAdapter.showLoadError();
    }

    @Override
    public void complete() {
        mRvContent.finishRefresh();
    }
    /****************************************************************************/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_SORT,mSortType);
        outState.putString(BUNDLE_DISTILLATE,mDistillate);
        outState.putInt(BUNDLE_START,mStart);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.saveBookHelps(mDiscHelpsAdapter.getItems());
    }
}
