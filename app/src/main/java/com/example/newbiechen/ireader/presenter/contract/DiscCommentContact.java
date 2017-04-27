package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.ui.base.BasePresenter;
import com.example.newbiechen.ireader.ui.base.BaseView;

import java.util.List;

/**
 * Created by newbiechen on 17-4-20.
 */

public interface DiscCommentContact {

    interface View extends BaseView<Presenter>{
        void finishRefresh(List<BookCommentBean> discussionBeans);
        void finishLoading(List<BookCommentBean> discussionBeans);
        void showRefreshView();
        void finishRefreshView();
    }

    interface Presenter extends BasePresenter{
        void refreshDiscussion(String block,String sort,int start,int limited,String distillate);
        void loadingDiscussion(String block,String sort,int start,int limited,String distillate);
    }
}
