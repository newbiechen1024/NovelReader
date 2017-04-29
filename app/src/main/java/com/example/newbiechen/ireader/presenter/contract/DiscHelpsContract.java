package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by newbiechen on 17-4-21.
 */

public interface DiscHelpsContract {

    interface View extends BaseContract.BaseView{
        void finishRefresh(List<BookHelpsBean> beans);
        void finishLoading(List<BookHelpsBean> beans);
        void showErrorTip();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void firstLoading(String sort, int start, int limited, String distillate);
        void refreshBookHelps(String sort, int start, int limited, String distillate);
        void loadingBookHelps(String sort, int start,int limited,String distillate);
        void saveBookHelps(List<BookHelpsBean> beans);
    }
}
