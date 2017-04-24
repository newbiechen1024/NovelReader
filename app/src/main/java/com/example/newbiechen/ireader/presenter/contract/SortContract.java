package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.SortListBean;
import com.example.newbiechen.ireader.ui.base.BasePresenter;
import com.example.newbiechen.ireader.ui.base.BaseView;

/**
 * Created by newbiechen on 17-4-23.
 */

public interface SortContract {

    interface Presenter extends BasePresenter{
        void loadSortBean();
    }

    interface View extends BaseView<Presenter>{
        void finishLoading(SortListBean bean);
        void loadError();
    }
}
