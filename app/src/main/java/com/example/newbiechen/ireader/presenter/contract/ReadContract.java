package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.ui.base.BaseContract;

/**
 * Created by newbiechen on 17-5-16.
 */

public interface ReadContract extends BaseContract{
    interface View extends BaseContract.BaseView {
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
    }
}
