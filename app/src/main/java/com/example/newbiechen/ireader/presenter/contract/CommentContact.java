package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.DiscussionBean;
import com.example.newbiechen.ireader.ui.base.BasePresenter;
import com.example.newbiechen.ireader.ui.base.BaseView;

import java.util.List;

/**
 * Created by newbiechen on 17-4-20.
 */

public interface CommentContact {

    public interface View extends BaseView<Presenter>{
        void finishRefresh(List<DiscussionBean> discussionBeans);
        void finishLoading(List<DiscussionBean> discussionBeans);
        void loadError();
    }

    public interface Presenter extends BasePresenter{
        void refreshDiscussion(String block,String sort,int start,int limited,String distillate);
        void loadingDiscussion(String block,String sort,int start,int limited,String distillate);
    }
}
