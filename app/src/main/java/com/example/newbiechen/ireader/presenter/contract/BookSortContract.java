package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookSortPackageBean;
import com.example.newbiechen.ireader.ui.base.BasePresenter;
import com.example.newbiechen.ireader.ui.base.BaseView;

/**
 * Created by newbiechen on 17-4-23.
 */

public interface BookSortContract {

    interface Presenter extends BasePresenter{
        void loadSortBean();
    }

    interface View extends BaseView<Presenter>{
        void finishLoading(BookSortPackageBean bean);
        void loadError();
    }
}
