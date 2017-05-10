package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.packages.BookSortPackage;
import com.example.newbiechen.ireader.model.bean.packages.BookSubSortPackage;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.BookSortContract;
import com.example.newbiechen.ireader.ui.base.RxPresenter;
import com.example.newbiechen.ireader.utils.LogUtils;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-23.
 */

public class BookSortPresenter extends RxPresenter<BookSortContract.View> implements BookSortContract.Presenter {
    @Override
    public void refreshSortBean() {
        //这个最好是设定一个默认时间采用Remote加载，如果Remote加载失败则采用数据中的数据。我这里先写死吧
        Single<BookSortPackage> sortSingle = RemoteRepository.getInstance()
                .getBookSortPackage();
        Single<BookSubSortPackage> subSortSingle = RemoteRepository.getInstance()
                .getBookSubSortPackage();

        Single<SortPackage> zipSingle =  Single.zip(sortSingle, subSortSingle,
                new BiFunction<BookSortPackage, BookSubSortPackage, SortPackage>() {
                    @Override
                    public SortPackage apply(BookSortPackage bookSortPackage, BookSubSortPackage subSortPackage) throws Exception {
                        return new SortPackage(bookSortPackage,subSortPackage);
                    }
                });

        Disposable disposable = zipSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (bean) ->{
                            mView.finishRefresh(bean.sortPackage,bean.subSortPackage);
                            mView.complete();
                        }
                        ,
                        (e) -> {
                            mView.showError();
                            LogUtils.e(e);
                        }
                );
        addDisposable(disposable);
        //保存在数据库中的，之后使用
    /*    BookSortPackage bean = LocalRepository.getInstance()
                .getBookSortPackage();
        if (bean == null){
            RemoteRepository.getInstance()
                    .getBookSortPackage()
                    .doOnSuccess(
                            (value) ->{
                                Schedulers.io().createWorker()
                                        .schedule(
                                                () ->{
                                                    LocalRepository.getInstance()
                                                            .saveBookSortPackage(value);
                                                }
                                        );
                            }
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<BookSortPackage>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onSuccess(BookSortPackage value) {
                            mView.finishRefresh(value);
                            mView.complete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showError();
                        }
                    });
        }
        else {
            mView.finishRefresh(bean);
            mView.complete();
        }*/
    }

    class SortPackage{
        BookSortPackage sortPackage;
        BookSubSortPackage subSortPackage;

        public SortPackage(BookSortPackage sortPackage, BookSubSortPackage subSortPackage){
            this.sortPackage = sortPackage;
            this.subSortPackage = subSortPackage;
        }
    }
}
