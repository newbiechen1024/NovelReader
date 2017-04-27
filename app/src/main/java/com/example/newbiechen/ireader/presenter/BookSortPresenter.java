package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.BookSortPackageBean;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.BookSortContract;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-23.
 */

public class BookSortPresenter implements BookSortContract.Presenter {

    private BookSortContract.View mView;

    public BookSortPresenter(BookSortContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void loadSortBean() {
        RemoteRepository.getInstance()
                .getSortListBean()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BookSortPackageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BookSortPackageBean value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
