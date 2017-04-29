package com.example.newbiechen.ireader.ui.activity;

import android.widget.ExpandableListView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BillboardBean;
import com.example.newbiechen.ireader.model.bean.BillboardPackageBean;
import com.example.newbiechen.ireader.presenter.BillboardPresenter;
import com.example.newbiechen.ireader.presenter.contract.BillboardContract;
import com.example.newbiechen.ireader.ui.adapter.BillboardAdapter;
import com.example.newbiechen.ireader.ui.base.BaseRxActivity;
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

public class BillboardActivity extends BaseRxActivity<BillboardContract.Presenter> implements BillboardContract.View{
    private static final String TAG = "BillboardActivity";

    @BindView(R.id.billboard_rl_refresh)
    RefreshLayout mRlRefresh;
    @BindView(R.id.billboard_elv_boy)
    ExpandableListView mElvBoy;
    @BindView(R.id.billboard_elv_girl)
    ExpandableListView mElvGirl;

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
    protected void initClick() {
        super.initClick();
        mRlRefresh.setOnReloadingListener(
                () -> mPresenter.loadBillboardList()
        );
    }

    @Override
    protected BillboardContract.Presenter bindPresenter() {
        return new BillboardPresenter();
    }

    @Override
    protected void processLogic() {
        super.processLogic();

        mRlRefresh.showLoading();
        mPresenter.loadBillboardList();
    }

    @Override
    public void finishRefresh(BillboardPackageBean beans) {
        if (beans == null || beans.getMale() == null || beans.getFemale() == null
                || beans.getMale().size() == 0 || beans.getFemale().size() == 0){
            mRlRefresh.showEmpty();
            return;
        }
        updateMaleBillboard(beans.getMale());
        updateFemaleBillboard(beans.getFemale());
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

    @Override
    public void showError() {
        mRlRefresh.showError();
    }

    @Override
    public void complete() {
        mRlRefresh.showFinish();
    }
}
