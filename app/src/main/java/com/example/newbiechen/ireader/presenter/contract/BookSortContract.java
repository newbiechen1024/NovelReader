package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookSortPackage;
import com.example.newbiechen.ireader.ui.base.BaseContract;

/**
 * Created by newbiechen on 17-4-23.
 */

public interface BookSortContract {

    interface View extends BaseContract.BaseView{
        void finishRefresh(BookSortPackage bean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void loadSortBean();
    }
}
