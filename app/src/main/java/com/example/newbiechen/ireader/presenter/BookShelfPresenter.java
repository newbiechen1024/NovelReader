package com.example.newbiechen.ireader.presenter;

import android.util.Log;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookDetailBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.CollBookManager;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.BookShlefContract;
import com.example.newbiechen.ireader.ui.activity.OtherBillBookActivity;
import com.example.newbiechen.ireader.ui.base.RxPresenter;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by newbiechen on 17-5-8.
 */

public class BookShelfPresenter extends RxPresenter<BookShlefContract.View>
        implements BookShlefContract.Presenter {
    private static final String TAG = "BookShelfPresenter";

    @Override
    public void refreshCollBooks() {
        //首先从数据库加载数据，然后根据id从网络中获取详情，之后根据最新时间进行修改
        CollBookManager.getInstance()
                .getCollBooks()
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        beans -> {
                            mView.finishRefresh(beans);
                            mView.complete();
                        }
                );
    }

    @Override
    public void loadRecommendBooks(String gender) {
        Disposable disposable = RemoteRepository.getInstance()
                .getRecommendBooks(gender)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        beans -> {
                            mView.finishRefresh(beans);
                            mView.complete();
                        },
                        (e) -> {
                            //提示没有网络
                            LogUtils.e(e);
                        }
                );
        addDisposable(disposable);
    }

    @Override
    public void updateCollBooks(List<CollBookBean> collBookBeans) {
        if (collBookBeans == null || collBookBeans.isEmpty()) return;

        List<Single<BookDetailBean>> observables = new ArrayList<>(collBookBeans.size());
        for (int i=0; i<collBookBeans.size(); ++i){
            observables.add(RemoteRepository.getInstance()
                    .getBookDetail(collBookBeans.get(i).get_id()));
        }
        Single.zip(observables, new Function<Object[], List<CollBookBean>>() {
            @Override
            public List<CollBookBean> apply(Object[] objects) throws Exception {

                List<CollBookBean> newCollBooks = new ArrayList<CollBookBean>(objects.length);
                for (int i=0; i<collBookBeans.size(); ++i){
                    CollBookBean oldCollBook = collBookBeans.get(i);
                    CollBookBean newCollBook = ((BookDetailBean)objects[i]).getCollBookBean();

                    //如果是oldBook是update状态，或者newCollBook与oldBook章节数不同
                    if (oldCollBook.isUpdate() ||
                            !oldCollBook.getLastChapter().equals(newCollBook.getLastChapter())){
                        newCollBook.setUpdate(true);
                    }
                    else {
                        newCollBook.setUpdate(false);
                    }
                    newCollBooks.add(newCollBook);
                }
                return newCollBooks;
            }
        }).compose(RxUtils::toSimpleSingle)
                .subscribe(new SingleObserver<List<CollBookBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<CollBookBean> value) {
                        //跟原先比较
                        mView.finishRefresh(value);
                        mView.complete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //提示没有网络
                        Log.d(TAG, "onError: "+e);
                        mView.complete();
                        LogUtils.e(e);
                    }
                });
    }
}
