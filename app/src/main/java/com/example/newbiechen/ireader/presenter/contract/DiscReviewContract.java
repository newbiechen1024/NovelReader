package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by newbiechen on 17-4-21.
 */

public interface DiscReviewContract {
    interface View extends BaseContract.BaseView {
        void finishRefresh(List<BookReviewBean> beans);
        void finishLoading(List<BookReviewBean> beans);
        void showErrorTip();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void firstLoading(String sort, String bookType, int start, int limited, String distillate);
        void refreshBookReview(String sort, String bookType, int start, int limited, String distillate);
        void loadingBookReview(String sort, String bookType, int start, int limited, String distillate);
        void saveBookReview(List<BookReviewBean> beans);
    }
}
