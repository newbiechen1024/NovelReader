package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.model.net.NetWorkRepository;
import com.example.newbiechen.ireader.presenter.contract.BookHeplsContract;
import com.example.newbiechen.ireader.presenter.contract.BookReviewContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-21.
 */

public class BookReviewPresenter implements BookReviewContract.Presenter {
    private BookReviewContract.View mView;

    public BookReviewPresenter(BookReviewContract.View view){
        mView = view;
        mView.setPresenter(this);
    }
    @Override
    public void refreshBookReview(String sort, String bookType, int start, int limited, String distillate) {
        NetWorkRepository.getInstance()
                .getBookReviewBeanList(sort,bookType,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookReviewBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<BookReviewBean> value) {
                        mView.finishRefresh(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //一般用Toast报错
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void loadingBookReview(String sort, String bookType, int start, int limited, String distillate) {
        NetWorkRepository.getInstance()
                .getBookReviewBeanList(sort,bookType,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookReviewBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<BookReviewBean> value) {
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

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mView = null;
    }
}
