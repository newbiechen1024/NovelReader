package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.SelectorEvent;
import com.example.newbiechen.ireader.model.bean.DiscussionBean;
import com.example.newbiechen.ireader.presenter.CommentPresenter;
import com.example.newbiechen.ireader.presenter.contract.CommentContact;
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
 * Created by newbiechen on 17-4-17.
 * 1. 初始化RecyclerView
 * 2. 初始化视图和逻辑的交互
 */

public class DiscussionFragment extends BaseFragment implements CommentContact.View{
    private static final String TAG = "DiscussionFragment";
    private static final String EXTRA_BLOCK = "extra_block";

    @BindView(R.id.section_rv_content)
    ScrollRefreshRecyclerView mRvContent;

    private DiscussionAdapter mDiscussionAdapter;
    private CommentContact.Presenter mPresenter;

    private String block = Constant.Block.COMMENT;
    private String sort = Constant.SortType.DEFAULT;
    private String distillate = Constant.Distillate.ALL;
    private int mStart = 0;
    private final int mLimited = 20;

    public static Fragment newInstance(String block){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_BLOCK,block);
        Fragment fragment = new DiscussionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_section;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        block = getArguments().getString(EXTRA_BLOCK);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DashItemDecoration());
        mDiscussionAdapter = new DiscussionAdapter(getContext(),new WholeAdapter.Options());
        mRvContent.setAdapter(mDiscussionAdapter);
    }

    @Override
    protected void initClick() {
        mRvContent.setOnRefreshListener(
                () -> startRefresh()
        );
        mDiscussionAdapter.setOnLoadMoreListener(
                () -> mPresenter.loadingDiscussion(block,sort, mStart, mLimited,distillate)
        );
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        new CommentPresenter(this).subscribe();
        //开始刷新
        mRvContent.initRefresh();
        mPresenter.refreshDiscussion(block,sort, mStart, mLimited,distillate);

        RxBus.getInstance()
                .toObservable(Constant.MSG_SELECTOR, SelectorEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) ->{
                            sort = event.sort;
                            distillate = event.distillate;
                            startRefresh();
                        }
                );
    }

    private void startRefresh(){
        mStart = 0;
        mRvContent.initRefresh();//有点小问题
        mPresenter.refreshDiscussion(block,sort, mStart, mLimited,distillate);
    }

    @Override
    public void setPresenter(CommentContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void finishRefresh(List<DiscussionBean> discussionBeans) {
        mRvContent.setRefreshing(false);
        mDiscussionAdapter.refreshItems(discussionBeans);
        mStart = discussionBeans.size();
    }

    @Override
    public void finishLoading(List<DiscussionBean> discussionBeans) {
        mDiscussionAdapter.addItems(discussionBeans);
        mStart += discussionBeans.size();
    }

    @Override
    public void loadError() {
        mDiscussionAdapter.setLoadError();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }
}
