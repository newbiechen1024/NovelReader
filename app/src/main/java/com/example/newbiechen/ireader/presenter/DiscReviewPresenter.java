package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.DiscReviewContract;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscReviewPresenter implements DiscReviewContract.Presenter {
    private DiscReviewContract.View mView;

    private CompositeDisposable mDisposable = new CompositeDisposable();
    public DiscReviewPresenter(DiscReviewContract.View view){
        mView = view;
        mView.setPresenter(this);
    }
    @Override
    public void refreshBookReview(String sort, String bookType, int start, int limited, String distillate) {
        RemoteRepository.getInstance()
                .getBookReviewList(sort,bookType,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BookReviewBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<BookReviewBean> value) {
                        mView.finishRefresh(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void loadingBookReview(String sort, String bookType, int start, int limited, String distillate) {
        RemoteRepository.getInstance()
                .getBookReviewList(sort,bookType,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BookReviewBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<BookReviewBean> value) {
                        mView.finishRefresh(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mView = null;
        mDisposable.dispose();
    }
}
