package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.SortListBean;
import com.example.newbiechen.ireader.model.net.NetWorkRepository;
import com.example.newbiechen.ireader.presenter.contract.SortContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-23.
 */

public class SortPresenter implements SortContract.Presenter {

    private SortContract.View mView;

    public SortPresenter(SortContract.View view) {
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
        NetWorkRepository.getInstance()
                .getSortListBean()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SortListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SortListBean value) {
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
