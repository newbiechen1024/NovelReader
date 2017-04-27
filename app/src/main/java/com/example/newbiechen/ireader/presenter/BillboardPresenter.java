package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.BillboardListBean;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.BillboardContract;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-23.
 */

public class BillboardPresenter implements BillboardContract.Presenter {

    private BillboardContract.View mView;

    public BillboardPresenter(BillboardContract.View view){
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mView = null;
    }

    @Override
    public void loadBillboardList() {
        RemoteRepository.getInstance()
                .getBillboardPackage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BillboardListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BillboardListBean value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
