package com.example.newbiechen.ireader.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BillboardBean;
import com.example.newbiechen.ireader.model.bean.BillboardListBean;
import com.example.newbiechen.ireader.presenter.BillboardPresenter;
import com.example.newbiechen.ireader.presenter.contract.BillboardContract;
import com.example.newbiechen.ireader.ui.adapter.BillboardAdapter;
import com.example.newbiechen.ireader.ui.base.BaseActivity;
import com.example.newbiechen.ireader.widget.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-23.
 * 数据的初始化，Expand的配置
 * 1. 查看Api制作数据Bean，制作相应的Adapter
 * 2. 初始化Expandable
 * 3. 制作数据获取类。
 */

public class BillboardActivity extends BaseActivity implements BillboardContract.View{
    private static final String TAG = "BillboardActivity";

    @BindView(R.id.billboard_rl_refresh)
    RefreshLayout mRlRefresh;
    @BindView(R.id.billboard_elv_boy)
    ExpandableListView mElvBoy;
    @BindView(R.id.billboard_elv_girl)
    ExpandableListView mElvGirl;

    private BillboardContract.Presenter mPresenter;

    private BillboardAdapter mBoyAdapter;
    private BillboardAdapter mGirlAdapter;
     @Override
    protected int getContentId() {
        return R.layout.activity_bilboard;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setUpAdapter();
    }

    private void setUpAdapter(){
        mBoyAdapter = new BillboardAdapter(this);
        mGirlAdapter = new BillboardAdapter(this);
        mElvBoy.setAdapter(mBoyAdapter);
        mElvGirl.setAdapter(mGirlAdapter);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        new BillboardPresenter(this).subscribe();

        mRlRefresh.showLoading();
        mPresenter.loadBillboardList();
    }

    @Override
    public void setPresenter(BillboardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void finishRefresh(BillboardListBean beans) {
        updateMaleBillboard(beans.getMale());
        updateFemaleBillboard(beans.getFemale());
        mRlRefresh.showFinish();
    }

    @Override
    public void loadError() {
        mRlRefresh.showError();
        mRlRefresh.setOnReloadingListener(
                () -> mPresenter.loadBillboardList()
        );
    }

    private void updateMaleBillboard(List<BillboardBean> disposes){
        List<BillboardBean> maleGroups = new ArrayList<>();
        List<BillboardBean> maleChildren = new ArrayList<>();
        for (BillboardBean bean : disposes){
            if (bean.isCollapse()){
                maleChildren.add(bean);
            }
            else {
                maleGroups.add(bean);
            }
        }
        maleGroups.add(new BillboardBean("别人家的排行榜"));
        mBoyAdapter.addGroups(maleGroups);
        mBoyAdapter.addChildren(maleChildren);
    }

    private void updateFemaleBillboard(List<BillboardBean> disposes){
        List<BillboardBean> femaleGroups = new ArrayList<>();
        List<BillboardBean> femaleChildren = new ArrayList<>();

        for(BillboardBean bean : disposes){
            if (bean.isCollapse()){
                femaleChildren.add(bean);
            }
            else {
                femaleGroups.add(bean);
            }
        }
        femaleGroups.add(new BillboardBean("别人家的排行榜"));
        mGirlAdapter.addGroups(femaleGroups);
        mGirlAdapter.addChildren(femaleChildren);
    }
}
