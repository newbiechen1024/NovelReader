package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.local.LocalRepository;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.DiscHelpsContract;
import com.example.newbiechen.ireader.ui.base.RxPresenter;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.newbiechen.ireader.utils.LogUtils.e;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscHelpsPresenter extends RxPresenter<DiscHelpsContract.View> implements DiscHelpsContract.Presenter {
    private boolean isLocalLoad = false;

    @Override
    public void firstLoading(String sort, int start, int limited, String distillate) {
        //获取数据库中的数据
        Single<List<BookHelpsBean>> localObserver = LocalRepository.getInstance()
                .getBookHelps(sort, start, limited, distillate);
        Single<List<BookHelpsBean>> remoteObserver = RemoteRepository.getInstance()
                .getBookHelps(sort, start, limited, distillate);

        Single.concat(localObserver,remoteObserver)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans) -> {
                            mView.finishRefresh(beans);
                        }
                        ,
                        (e) ->{
                            isLocalLoad = true;
                            mView.complete();
                            mView.showErrorTip();
                            e(e);
                        }
                        ,
                        ()-> {
                            isLocalLoad = false;
                            mView.complete();
                        }
                );

    }

    @Override
    public void refreshBookHelps(String sort, int start, int limited, String distillate) {
        Disposable refreshDispo = RemoteRepository.getInstance()
                .getBookHelps(sort,start,limited,distillate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans)-> {
                            isLocalLoad = false;
                            mView.finishRefresh(beans);
                            mView.complete();
                        }
                        ,
                        (e) ->{
                            mView.complete();
                            mView.showErrorTip();
                            e(e);
                        }
                );
        addDisposable(refreshDispo);
    }

    @Override
    public void loadingBookHelps(String sort, int start, int limited, String distillate) {
        if (isLocalLoad){
            Single<List<BookHelpsBean>> single = LocalRepository.getInstance()
                    .getBookHelps(sort, start, limited, distillate);
            loadBookHelps(single);
        }

        else{
            Single<List<BookHelpsBean>> single = RemoteRepository.getInstance()
                    .getBookHelps(sort,start,limited,distillate);
            loadBookHelps(single);
        }
    }

    @Override
    public void saveBookHelps(List<BookHelpsBean> beans) {
        LocalRepository.getInstance()
                .saveBookHelps(beans);
    }

    private void loadBookHelps(Single<List<BookHelpsBean>> observable){
        Disposable loadDispo =observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans) -> {
                            mView.finishLoading(beans);
                        }
                        ,
                        (e) -> {
                            mView.showError();
                            e(e);
                        }
                );
        addDisposable(loadDispo);
    }
}
