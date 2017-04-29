package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by newbiechen on 17-4-20.
 */

public interface DiscCommentContact {

    interface View extends BaseContract.BaseView{
        void finishRefresh(List<BookCommentBean> beans);
        void finishLoading(List<BookCommentBean> beans);
        void showErrorTip();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void firstLoading(String block, String sort, int start, int limited, String distillate);
        void refreshComment(String block, String sort, int start, int limited, String distillate);
        void loadingComment(String block, String sort, int start, int limited, String distillate);
        void saveComment(List<BookCommentBean> beans);
    }
}
