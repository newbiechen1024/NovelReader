package com.example.newbiechen.ireader.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookSortPackageBean;
import com.example.newbiechen.ireader.presenter.BookSortPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookSortContract;
import com.example.newbiechen.ireader.ui.adapter.BookSortAdapter;
import com.example.newbiechen.ireader.ui.base.BaseActivity;
import com.example.newbiechen.ireader.widget.RefreshLayout;
import com.example.newbiechen.ireader.widget.itemdecoration.DefaultItemDecoration;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-23.
 * 分类选择
 *
 */

public class BookSortActivity extends BaseActivity implements BookSortContract.View{
    /*******************Constant*********************/
    private static final String TAG = "SortActivity";
    private static final int SPAN_COUNT = 3;

    @BindView(R.id.book_sort_rl_refresh)
    RefreshLayout mRlRefresh;
    @BindView(R.id.book_sort_rv_boy)
    RecyclerView mRvBoy;
    @BindView(R.id.book_sort_rv_girl)
    RecyclerView mRvGirl;

    private BookSortAdapter mBoyAdapter;
    private BookSortAdapter mGirlAdapter;
    private BookSortContract.Presenter mPresenter;

    /**********************init***********************************/
    @Override
    protected int getContentId() {
        return R.layout.activity_book_sort;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setUpAdapter();
    }

    private void setUpAdapter(){
        mBoyAdapter = new BookSortAdapter();
        mGirlAdapter = new BookSortAdapter();

        RecyclerView.ItemDecoration itemDecoration = new DefaultItemDecoration(this);

        mRvBoy.setLayoutManager(new GridLayoutManager(this,SPAN_COUNT));
        mRvBoy.addItemDecoration(itemDecoration);
        mRvBoy.setAdapter(mBoyAdapter);

        mRvGirl.setLayoutManager(new GridLayoutManager(this,SPAN_COUNT));
        mRvGirl.addItemDecoration(itemDecoration);
        mRvGirl.setAdapter(mGirlAdapter);
    }

    /*********************logic*******************************/

    @Override
    protected void processLogic() {
        super.processLogic();
        new BookSortPresenter(this).subscribe();

        loadSortBean();
    }

    private void loadSortBean(){
        mRlRefresh.showLoading();
        mPresenter.loadSortBean();
    }

    /***********************rewrite**********************************/
    @Override
    public void setPresenter(BookSortContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void finishLoading(BookSortPackageBean bean) {
        mRlRefresh.showFinish();
        mBoyAdapter.refreshItems(bean.getMale());
        mGirlAdapter.refreshItems(bean.getFemale());
    }

    @Override
    public void loadError() {
        mRlRefresh.showError();
    }
}
