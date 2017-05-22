package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.BookDetailBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.bean.DownloadTaskBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.BookShelfContract;
import com.example.newbiechen.ireader.ui.base.RxPresenter;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.RxUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by newbiechen on 17-5-8.
 */

public class BookShelfPresenter extends RxPresenter<BookShelfContract.View>
        implements BookShelfContract.Presenter {
    private static final String TAG = "BookShelfPresenter";

    @Override
    public void refreshCollBooks() {
        //首先从数据库加载数据，然后根据id从网络中获取详情，之后根据最新时间进行修改
        BookRepository.getInstance()
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
    public void createDownloadTask(CollBookBean collBookBean) {
        DownloadTaskBean task = new DownloadTaskBean();
        task.setTaskName(collBookBean.getTitle());
        task.setBookId(collBookBean.get_id());
        task.setBookChapters(collBookBean.getBookChapters());
        task.setLastChapter(collBookBean.getBookChapters().size());

        RxBus.getInstance().post(task);
    }


    @Override
    public void loadRecommendBooks(String gender) {
        Disposable disposable = RemoteRepository.getInstance()
                .getRecommendBooks(gender)
                .doOnSuccess(new Consumer<List<CollBookBean>>() {
                    @Override
                    public void accept(List<CollBookBean> collBookBeen) throws Exception {
                        //更新目录
                        updateCategory(collBookBeen);
                        //存储到数据库中
                        BookRepository.getInstance()
                                .saveCollBooks(collBookBeen);
                    }
                })
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        beans -> {
                            mView.finishRefresh(beans);
                            mView.complete();
                        },
                        (e) -> {
                            //提示没有网络
                            LogUtils.e(e);
                            mView.showErrorTip(e.toString());
                            mView.complete();
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
        }).doOnSuccess(new Consumer<List<CollBookBean>>() {
                    @Override
                    public void accept(List<CollBookBean> collBookBeen) throws Exception {
                        List<CollBookBean> beans = new ArrayList<CollBookBean>();
                        //获取更新的CollBook
                        for (CollBookBean bean : collBookBeen){
                            if (bean.isUpdate()){
                                beans.add(bean);
                            }
                        }
                        //更新目录
                        updateCategory(beans);

                        //存储到数据库中
                        BookRepository.getInstance()
                                .saveCollBooks(collBookBeen);
                    }
                })
                .compose(RxUtils::toSimpleSingle)
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
                        mView.showErrorTip(e.toString());
                        mView.complete();
                        LogUtils.e(e);
                    }
                });
    }

    //更新每个CollBook的目录
    private void updateCategory(List<CollBookBean> collBookBeans){
        List<Single<List<BookChapterBean>>> observables = new ArrayList<>(collBookBeans.size());
        for (CollBookBean bean : collBookBeans){
            observables.add(
                    RemoteRepository.getInstance().getBookChapters(bean.get_id())
            );
        }
        Iterator<CollBookBean> it = collBookBeans.iterator();
        //执行在上一个方法中的子线程中
        Single.concat(observables)
                .subscribe(
                        chapterList -> {
                            CollBookBean bean = it.next();
                            bean.setBookChapters(chapterList);
                        }
                );
    }
}
