package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.ui.base.BasePresenter;
import com.example.newbiechen.ireader.ui.base.BaseView;

import java.util.List;

/**
 * Created by newbiechen on 17-4-21.
 */

public interface DiscReviewContract {
    interface View extends BaseView<Presenter> {
        void finishRefresh(List<BookReviewBean> discussionBeans);
        void finishLoading(List<BookReviewBean> discussionBeans);
    }

    interface Presenter extends BasePresenter {
        void refreshBookReview(String sort, String bookType, int start, int limited, String distillate);
        void loadingBookReview(String sort, String bookType, int start, int limited, String distillate);
    }
}
