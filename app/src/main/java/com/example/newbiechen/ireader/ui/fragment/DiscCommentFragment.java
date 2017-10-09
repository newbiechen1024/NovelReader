package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.SelectorEvent;
import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.model.flag.BookDistillate;
import com.example.newbiechen.ireader.model.flag.BookSort;
import com.example.newbiechen.ireader.model.flag.CommunityType;
import com.example.newbiechen.ireader.presenter.DiscCommentPresenter;
import com.example.newbiechen.ireader.presenter.contract.DiscCommentContact;
import com.example.newbiechen.ireader.ui.activity.DiscDetailActivity;
import com.example.newbiechen.ireader.ui.adapter.DiscCommentAdapter;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;
import com.example.newbiechen.ireader.widget.refresh.ScrollRefreshRecyclerView;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * DiscussionCommentFragment
 * Created by newbiechen on 17-4-17.
 * 讨论组中的评论区，包括Comment、Girl、Origin
 * 1. 初始化RecyclerView
 * 2. 初始化视图和逻辑的交互
 */

public class DiscCommentFragment extends BaseMVPFragment<DiscCommentContact.Presenter> implements DiscCommentContact.View{
    private static final String TAG = "DiscCommentFragment";
    private static final String EXTRA_BLOCK = "extra_block";
    private static final String BUNDLE_BLOCK = "bundle_block";
    private static final String BUNDLE_SORT = "bundle_sort";
    private static final String BUNDLE_DISTILLATE = "bundle_distillate";
    /***********************view********************************/
    @BindView(R.id.scroll_refresh_rv_content)
    ScrollRefreshRecyclerView mRvContent;

    /************************object**********************************/
    private DiscCommentAdapter mDiscCommentAdapter;

    /*************************Params*******************************/
    private CommunityType mBlock = CommunityType.COMMENT;
    private BookSort mBookSort = BookSort.DEFAULT;
    private BookDistillate mDistillate = BookDistillate.ALL;
    private int mStart = 0;
    private final int mLimited = 20;

    /****************************open method**********************************/
    public static Fragment newInstance(CommunityType block){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_BLOCK,block);
        Fragment fragment = new DiscCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**********************************************************************/

    @Override
    protected int getContentId() {
        return R.layout.fragment_scroll_refresh_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            mBlock = (CommunityType) savedInstanceState.getSerializable(BUNDLE_BLOCK);
            mBookSort = (BookSort) savedInstanceState.getSerializable(BUNDLE_SORT);
            mDistillate = (BookDistillate) savedInstanceState.getSerializable(BUNDLE_DISTILLATE);
        }
        else {
            mBlock = (CommunityType) getArguments().getSerializable(EXTRA_BLOCK);
        }
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mDiscCommentAdapter = new DiscCommentAdapter(getContext(),new WholeAdapter.Options());
        mRvContent.setAdapter(mDiscCommentAdapter);
    }

    /******************************init click method***********************************/

    @Override
    protected void initClick() {
        //下滑刷新
        mRvContent.setOnRefreshListener( () -> refreshData() );
        //上滑加载
        mDiscCommentAdapter.setOnLoadMoreListener(
                () -> mPresenter.loadingComment(mBlock, mBookSort, mStart, mLimited, mDistillate)
        );
        mDiscCommentAdapter.setOnItemClickListener(
                (view,pos) -> {
                    BookCommentBean bean = mDiscCommentAdapter.getItem(pos);
                    String detailId = bean.get_id();
                    DiscDetailActivity.startActivity(getContext(),mBlock,detailId);
                }
        );
        //选择刷新
        RxBus.getInstance()
                .toObservable(Constant.MSG_SELECTOR, SelectorEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) -> {
                            mBookSort = event.sort;
                            mDistillate = event.distillate;
                            refreshData();
                        }
                );
    }

    @Override
    protected DiscCommentPresenter bindPresenter() {
        return new DiscCommentPresenter();
    }

    /*******************************logic********************************/
    @Override
    protected void processLogic() {
        super.processLogic();
        //首次加载数据
        mRvContent.startRefresh();
        mPresenter.firstLoading(mBlock,mBookSort,mStart,mLimited,mDistillate);
    }

    private void refreshData(){
        mStart = 0;
        mRvContent.startRefresh();
        mPresenter.refreshComment(mBlock, mBookSort, mStart, mLimited, mDistillate);
    }
    /********************************rewrite method****************************************/

    @Override
    public void finishRefresh(List<BookCommentBean> beans){
        mDiscCommentAdapter.refreshItems(beans);
        mStart = beans.size();
    }

    @Override
    public void finishLoading(List<BookCommentBean> beans) {
        mDiscCommentAdapter.addItems(beans);
        mStart += beans.size();
    }

    @Override
    public void showErrorTip() {
        mRvContent.showTip();
    }

    @Override
    public void showError() {
        mDiscCommentAdapter.showLoadError();
    }

    @Override
    public void complete() {
        mRvContent.finishRefresh();
    }

    /****************************save*************************************/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_BLOCK, mBlock);
        outState.putSerializable(BUNDLE_SORT,mBookSort);
        outState.putSerializable(BUNDLE_DISTILLATE,mDistillate);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.saveComment(mDiscCommentAdapter.getItems());
    }
}
