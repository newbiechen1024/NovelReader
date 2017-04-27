package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.DiscHelpsContract;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscHelpsPresenter implements DiscHelpsContract.Presenter {
    private DiscHelpsContract.View mView;

    private CompositeDisposable mDisposable = new CompositeDisposable();
    public DiscHelpsPresenter(DiscHelpsContract.View view){
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void refreshBookHelps(String sort, int start, int limited, String distillate) {
        RemoteRepository.getInstance()
                .getBookHelps(sort,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BookHelpsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<BookHelpsBean> value) {
                        mView.finishLoading(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadError();
                    }
                });
    }

    @Override
    public void loadingBookHelps(String sort, int start, int limited, String distillate) {
        RemoteRepository.getInstance()
                .getBookHelps(sort,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BookHelpsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<BookHelpsBean> value) {
                        mView.finishLoading(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadError();
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
