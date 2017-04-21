package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.DiscussionBean;
import com.example.newbiechen.ireader.model.net.NetWorkRepository;
import com.example.newbiechen.ireader.presenter.contract.CommentContact;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.ArrayCompositeSubscription;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-20.
 */

public class CommentPresenter implements CommentContact.Presenter {
    private CommentContact.View mView;
    public CommentPresenter(CommentContact.View view){
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        //初始化数据，并从数据库，网络中获取
        //无法获取默认的参数
    }

    @Override
    public void unSubscribe() {
        //无
        mView = null;
    }

    @Override
    public void refreshDiscussion(String block,String sort,int start,int limited,String distillate) {
        NetWorkRepository.getInstance()
                .getDiscussionBeanList(block,sort,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DiscussionBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<DiscussionBean> value) {
                        mView.finishRefresh(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void loadingDiscussion(String block,String sort,int start,int limited,String distillate) {
        NetWorkRepository.getInstance()
                .getDiscussionBeanList(block,sort,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DiscussionBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<DiscussionBean> value) {
                        mView.finishLoading(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
