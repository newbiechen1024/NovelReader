package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookSortPackageBean;
import com.example.newbiechen.ireader.ui.base.BaseContract;

/**
 * Created by newbiechen on 17-4-23.
 */

public interface BookSortContract {

    interface View extends BaseContract.BaseView{
        void finishRefresh(BookSortPackageBean bean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void loadSortBean();
    }
}
