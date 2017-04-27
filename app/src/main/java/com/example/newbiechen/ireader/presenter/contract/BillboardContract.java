package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BillboardListBean;
import com.example.newbiechen.ireader.ui.base.BasePresenter;
import com.example.newbiechen.ireader.ui.base.BaseView;

/**
 * Created by newbiechen on 17-4-23.
 */

public interface BillboardContract {

    interface View extends BaseView<Presenter> {
        void finishRefresh(BillboardListBean beans);
    }

    interface Presenter extends BasePresenter{
        void loadBillboardList();
    }
}
