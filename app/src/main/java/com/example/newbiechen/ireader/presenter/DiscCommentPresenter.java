package com.example.newbiechen.ireader.presenter;

import android.util.Log;

import static com.example.newbiechen.ireader.utils.LogUtils.*;

import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.model.local.LocalRepository;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.DiscCommentContact;
import com.example.newbiechen.ireader.ui.base.RxPresenter;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-20.
 */

public class DiscCommentPresenter extends RxPresenter<DiscCommentContact.View> implements DiscCommentContact.Presenter{
    private static final String TAG = "DiscCommentPresenter";
    //是否采取直接从数据库加载
    private boolean isLocalLoad = true;

    @Override
    public void firstLoading(String block, String sort, int start, int limited, String distillate) {
        //获取数据库中的数据
        Single<List<BookCommentBean>> localObserver = LocalRepository.getInstance()
                .getBookComments(block, sort, start, limited, distillate);
        Single<List<BookCommentBean>> remoteObserver = RemoteRepository.getInstance()
                .getBookComment(block, sort, start, limited, distillate);

        //这里有问题，但是作者却用的好好的，可能是2.0之后的问题
        Disposable disposable =  localObserver
                .concatWith(remoteObserver)
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
        addDisposable(disposable);
    }

    @Override
    public void refreshComment(String block, String sort, int start, int limited, String distillate) {//刷新数据，然后将数据加入到缓存中
        Disposable refreshDispo = RemoteRepository.getInstance()
                .getBookComment(block,sort,start,limited,distillate)
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
    public void loadingComment(String block, String sort, int start, int limited, String distillate) {
        if (isLocalLoad){
            Single<List<BookCommentBean>> single = LocalRepository.getInstance()
                    .getBookComments(block, sort, start, limited, distillate);
            loadComment(single);
        }

        else{
            //单纯的加载数据
            Single<List<BookCommentBean>> single = RemoteRepository.getInstance()
                    .getBookComment(block,sort,start,limited,distillate);
            loadComment(single);

        }
    }

    @Override
    public void saveComment(List<BookCommentBean> beans) {
        LocalRepository.getInstance()
                .saveBookComments(beans);
    }

    private void loadComment(Single<List<BookCommentBean>> observable){
        Disposable loadDispo =observable.subscribeOn(Schedulers.io())
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
