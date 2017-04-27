package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.ModelRepository;
import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.DiscCommentContact;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-20.
 */

public class DiscCommentPresenter implements DiscCommentContact.Presenter {
    private DiscCommentContact.View mView;
    private boolean isLocalLoad = true;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    public DiscCommentPresenter(DiscCommentContact.View view){
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        //加载数据，并且自动刷新
    }

    @Override
    public void unSubscribe() {
        mView = null;
        mDisposable.dispose();
        //将数据存储到数据库中 (其实可以在onStop中存储数据)
    }

    @Override
    public void refreshDiscussion(String block,String sort,int start,int limited,String distillate) {
        mView.showRefreshView();
        if (isLocalLoad){
            ModelRepository.getInstance()
                    .getBookComment(block, sort, start, limited, distillate)
                    .subscribe(new Subscriber<List<BookCommentBean>>() {
                        @Override
                        public void onSubscribe(Subscription s) {

                        }

                        @Override
                        public void onNext(List<BookCommentBean> bookCommentBeen) {
                            mView.finishRefresh(bookCommentBeen);
                        }

                        @Override
                        public void onError(Throwable t) {
                            //错误日志
                            mView.loadError();
                        }

                        @Override
                        public void onComplete() {
                            mView.finishRefreshView();
                        }
                    });
        }
        else {
            //刷新数据，然后将数据加入到缓存中
            RemoteRepository.getInstance()
                    .getBookComment(block,sort,start,limited,distillate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<BookCommentBean>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDisposable.add(d);
                        }

                        @Override
                        public void onSuccess(List<BookCommentBean> value) {
                            mView.finishRefresh(value);
                            mView.finishRefreshView();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.loadError();
                        }
                    });
        }
    }

    @Override
    public void loadingDiscussion(String block,String sort,int start,int limited,String distillate) {
        //单纯的加载数据
        RemoteRepository.getInstance()
                .getBookComment(block,sort,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BookCommentBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<BookCommentBean> value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
