package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.net.NetWorkRepository;
import com.example.newbiechen.ireader.presenter.contract.BookHeplsContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-21.
 */

public class BookHelpsPresenter implements BookHeplsContract.Presenter {
    private BookHeplsContract.View mView;
    public BookHelpsPresenter(BookHeplsContract.View view){
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void refreshBookHelps(String sort, int start, int limited, String distillate) {
        NetWorkRepository.getInstance()
                .getBookHelpsBeanList(sort,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookHelpsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<BookHelpsBean> value) {
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
    public void loadingBookHelps(String sort, int start, int limited, String distillate) {
        NetWorkRepository.getInstance()
                .getBookHelpsBeanList(sort,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookHelpsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<BookHelpsBean> value) {
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
