package com.example.newbiechen.ireader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.example.newbiechen.ireader.presenter.BillBookPresenter;
import com.example.newbiechen.ireader.presenter.contract.BillBookContract;
import com.example.newbiechen.ireader.ui.adapter.BillBookAdapter;
import com.example.newbiechen.ireader.ui.base.BaseMVPActivity;
import com.example.newbiechen.ireader.widget.RefreshLayout;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-5-3.
 */

public class OtherBillBookActivity extends BaseMVPActivity<BillBookContract.Presenter>
        implements BillBookContract.View{
    private static final String EXTRA_BILL_ID = "extra_bill_id";
    private static final String EXTRA_BILL_NAME = "extra_bill_name";
    /********************/
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvContent;
    /*******************/
    private BillBookAdapter mBillBookAdapter;
    /*****************/
    private String mBillId;
    private String mBillName;
    public static void startActivity(Context context,String billName, String billId){
        Intent intent = new Intent(context,OtherBillBookActivity.class);
        intent.putExtra(EXTRA_BILL_ID,billId);
        intent.putExtra(EXTRA_BILL_NAME,billName);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_refresh_list;
    }

    @Override
    protected BillBookContract.Presenter bindPresenter() {
        return new BillBookPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null){
            mBillId = savedInstanceState.getString(EXTRA_BILL_ID);
            mBillName = savedInstanceState.getString(EXTRA_BILL_NAME);
        }
        else {
            mBillId = getIntent().getStringExtra(EXTRA_BILL_ID);
            mBillName = getIntent().getStringExtra(EXTRA_BILL_NAME);
        }
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        toolbar.setTitle(mBillName);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setUpAdapter();
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRefreshLayout.showLoading();
        mPresenter.refreshBookBrief(mBillId);
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new DividerItemDecoration(this));
        mBillBookAdapter = new BillBookAdapter();
        mRvContent.setAdapter(mBillBookAdapter);
    }

    @Override
    public void finishRefresh(List<BillBookBean> beans) {
        mBillBookAdapter.refreshItems(beans);
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }
    /***************************************************/
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(EXTRA_BILL_ID,mBillId);
        outState.putString(EXTRA_BILL_NAME,mBillName);
    }
}
