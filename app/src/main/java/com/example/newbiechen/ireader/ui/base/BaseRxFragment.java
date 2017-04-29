package com.example.newbiechen.ireader.ui.base;

import android.os.Bundle;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-4-25.
 */

public abstract class BaseRxFragment<T extends BaseContract.BasePresenter> extends BaseFragment implements BaseContract.BaseView{

    protected CompositeDisposable mDisposable;
    protected T mPresenter;

    protected abstract T bindPresenter();

    protected void addDisposable(Disposable d){
        if (mDisposable == null){
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
    }

    @Override
    protected void processLogic(){
        mPresenter = bindPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null){
            mDisposable.dispose();
        }
        mPresenter.detachView();
    }
}
