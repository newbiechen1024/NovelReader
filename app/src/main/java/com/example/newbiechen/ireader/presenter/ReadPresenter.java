package com.example.newbiechen.ireader.presenter;

import android.util.Log;

import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.utils.BookManager;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.ReadContract;
import com.example.newbiechen.ireader.ui.base.RxPresenter;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-5-16.
 */

public class ReadPresenter extends RxPresenter<ReadContract.View>
        implements ReadContract.Presenter{
    private static final String TAG = "ReadPresenter";
    @Override
    public void loadCategory(String bookId) {
        Disposable disposable = RemoteRepository.getInstance()
                .getBookChapters(bookId)

                .subscribeOn(Schedulers.io())
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        beans -> {
                            mView.showCategory(beans);
                        }
                        ,
                        e -> {
                            //TODO: Haven't grate conversation method.
                            LogUtils.e(e);
                        }
                );
        addDisposable(disposable);
    }

    @Override
    public void loadChapter(String bookId,List<BookChapterBean> bookChapterList) {
        List<Single<int[]>> chapterInfoList = new ArrayList<>(bookChapterList.size());
        for (BookChapterBean bookChapter : bookChapterList){
            Single<int[]> chapterSingle = Single.create(new SingleOnSubscribe<int[]>() {
                @Override
                public void subscribe(SingleEmitter<int[]> e) throws Exception {
                    //判断是否存在于本地文件中
                    if (BookManager
                            .isChapterCached(bookId,bookChapter.getTitle())){
                        e.onSuccess(new int[]{1});
                    }
                    else{
                        e.onSuccess(new int[]{-1});
                    }
                }
            }).doOnSuccess(new Consumer<int[]>() {
                @Override
                public void accept(int[] isLoad) throws Exception {
                    if (isLoad[0] == 1) return;
                    //网络中获取数据
                    RemoteRepository.getInstance()
                            .getChapterInfo(bookChapter.getLink())
                            .subscribe(
                                    chapterInfo -> {
                                        //将获取到的数据进行存储
                                        BookRepository.getInstance().saveChapterInfo(
                                                bookId, bookChapter.getTitle(), chapterInfo.getBody()
                                        );
                                        isLoad[0] = 1;
                                    },
                                    e -> {
                                        isLoad[0] = -1;
                                        LogUtils.e(e);
                                    }
                            );
                }
            });
            chapterInfoList.add(chapterSingle);
        }
        Disposable disposable = Single.concat(chapterInfoList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isLoad -> {
                            //如果bean为null 通知错误
                            if (isLoad[0] == 1){
                                mView.finishChapter();
                            }
                            else {
                                mView.errorChapter();
                            }
                        }
                );
        addDisposable(disposable);
    }
}
