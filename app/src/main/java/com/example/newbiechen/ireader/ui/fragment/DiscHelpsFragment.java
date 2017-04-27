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

public class DiscHelpsFragment extends BaseFragment implements DiscHelpsContract.View{
    /*****************View********************/
    @BindView(R.id.discussion_rv_content)
    ScrollRefreshRecyclerView mRvContent;
    /******************Object******************/
    private DiscHelpsAdapter mDiscHelpsAdapter;
    private DiscHelpsContract.Presenter mPresenter;
    /******************Parms*******************/
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

        RxBus.getInstance()
                .toObservable(Constant.MSG_SELECTOR, SelectorEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) ->{
                            mSortType = event.sort.getNetName();
                            mDistillate = event.distillate.getNetName();
                            startRefresh();
                        }
                );
    }

    /*****************************logic method*****************************/
    @Override
    protected void processLogic() {
        new DiscHelpsPresenter(this).subscribe();
        startRefresh();
    }

    private void startRefresh(){
        mStart = 0;
        mRvContent.startRefresh();
        mPresenter.refreshBookHelps(mSortType,mStart,mLimited,mDistillate);
    }

    /**************************rewrite method****************************************/

    @Override
    public void setPresenter(DiscHelpsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void finishRefresh(List<BookHelpsBean> discussionBeans) {
        mDiscHelpsAdapter.refreshItems(discussionBeans);
        mStart = discussionBeans.size();
        mRvContent.setRefreshing(false);
    }

    @Override
    public void finishLoading(List<BookHelpsBean> discussionBeans) {
        mDiscHelpsAdapter.addItems(discussionBeans);
        mStart += discussionBeans.size();
    }

    @Override
    public void loadError() {
        mDiscHelpsAdapter.showLoadError();
    }

    /***************************lifecycle method***********************************/
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }
}
