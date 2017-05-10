package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookDetailBean;
import com.example.newbiechen.ireader.model.bean.BookListBean;
import com.example.newbiechen.ireader.model.bean.HotCommentBean;
import com.example.newbiechen.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by newbiechen on 17-5-4.
 */

public interface BookDetailContract {
    interface View extends BaseContract.BaseView{
        void finishRefresh(BookDetailBean bean);
        void finishHotComment(List<HotCommentBean> beans);
        void finishRecommendBookList(List<BookListBean> beans);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshBookDetail(String bookId);
    }
}
