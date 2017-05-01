package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.CommentBean;
import com.example.newbiechen.ireader.model.bean.CommentDetailBean;
import com.example.newbiechen.ireader.model.bean.HelpsDetailBean;
import com.example.newbiechen.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by newbiechen on 17-4-30.
 */

public interface HelpsDetailContract{

    interface View extends BaseContract.BaseView{
        //全部加载的时候显示
        void finishRefresh(HelpsDetailBean commentDetail,
                           List<CommentBean> bestComments, List<CommentBean> comments);
        void finishLoad(List<CommentBean> comments);
        void showLoadError();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshHelpsDetail(String detailId,int start,int limit);
        void loadComment(String detailId,int start,int limit);
    }
}
