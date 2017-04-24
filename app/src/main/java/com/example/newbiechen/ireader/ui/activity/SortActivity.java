package com.example.newbiechen.ireader.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.SortListBean;
import com.example.newbiechen.ireader.presenter.SortPresenter;
import com.example.newbiechen.ireader.presenter.contract.SortContract;
import com.example.newbiechen.ireader.ui.adapter.SortAdapter;
import com.example.newbiechen.ireader.ui.base.BaseActivity;
import com.example.newbiechen.ireader.widget.RefreshLayout;
import com.example.newbiechen.ireader.widget.itemdecoration.DefaultItemDecoration;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-23.
 */

public class SortActivity extends BaseActivity implements SortContract.View{
    private static final String TAG = "SortActivity";
    private static final int SPAN_COUNT = 3;
    @BindView(R.id.sort_rl_refresh)
    RefreshLayout mRlRefresh;
    @BindView(R.id.sort_rv_boy)
    RecyclerView mRvBoy;
    @BindView(R.id.sort_rv_girl)
    RecyclerView mRvGirl;

    private SortAdapter mBoyAdapter;
    private SortAdapter mGirlAdapter;

    private SortContract.Presenter mPresenter;
    @Override
    protected int getContentId() {
        return R.layout.activity_sort;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setUpAdapter();
    }

    private void setUpAdapter(){
        mBoyAdapter = new SortAdapter();
        mGirlAdapter = new SortAdapter();

        RecyclerView.ItemDecoration itemDecoration = new DefaultItemDecoration(this);

        mRvBoy.setLayoutManager(new GridLayoutManager(this,SPAN_COUNT));
        mRvBoy.addItemDecoration(itemDecoration);
        mRvBoy.setAdapter(mBoyAdapter);

        mRvGirl.setLayoutManager(new GridLayoutManager(this,SPAN_COUNT));
        mRvGirl.addItemDecoration(itemDecoration);
        mRvGirl.setAdapter(mGirlAdapter);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        new SortPresenter(this).subscribe();

        loadSortBean();
    }

    private void loadSortBean(){
        mRlRefresh.showLoading();
        mPresenter.loadSortBean();
    }

    @Override
    public void setPresenter(SortContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void finishLoading(SortListBean bean) {
        mRlRefresh.showFinish();
        mBoyAdapter.refreshItems(bean.getMale());
        mGirlAdapter.refreshItems(bean.getFemale());
    }

    @Override
    public void loadError() {
        mRlRefresh.showError();
    }
}
