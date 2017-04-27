package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.ui.base.BasePresenter;
import com.example.newbiechen.ireader.ui.base.BaseView;

import java.util.List;

/**
 * Created by newbiechen on 17-4-21.
 */

public interface DiscHelpsContract {

    interface View extends BaseView<Presenter> {
        void finishRefresh(List<BookHelpsBean> discussionBeans);
        void finishLoading(List<BookHelpsBean> discussionBeans);
    }

    interface Presenter extends BasePresenter{
        void refreshBookHelps(String sort, int start, int limited, String distillate);
        void loadingBookHelps(String sort, int start,int limited,String distillate);
    }
}
