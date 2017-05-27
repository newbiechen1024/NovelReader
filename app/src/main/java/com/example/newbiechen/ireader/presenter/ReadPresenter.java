package com.example.newbiechen.ireader.presenter;

import android.util.Log;

import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.ChapterInfoBean;
import com.example.newbiechen.ireader.utils.BookManager;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.ReadContract;
import com.example.newbiechen.ireader.ui.base.RxPresenter;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.RxUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
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

    //需要重新考虑
    @Override
    public void loadChapter(String bookId,List<BookChapterBean> bookChapters) {
        int size = bookChapters.size();

        List<Single<ChapterInfoBean>> chapterInfos = new ArrayList<>(bookChapters.size());
        ArrayDeque<String> titles = new ArrayDeque<>(bookChapters.size());

        //首先判断是否Chapter已经存在
        for (int i=0; i < size; ++i){
            BookChapterBean bookChapter = bookChapters.get(i);
            if (!(BookManager
                    .isChapterCached(bookId,bookChapter.getTitle()))){
                //网络中获取数据
                Single<ChapterInfoBean> chapterInfoSingle = RemoteRepository.getInstance()
                        .getChapterInfo(bookChapter.getLink());

                chapterInfos.add(chapterInfoSingle);

                titles.add(bookChapter.getTitle());
            }
            //如果已经存在，再判断是不是我们需要的下一个章节，如果是才返回加载成功
            else if (i == 0){
                mView.finishChapter();
            }
        }

        Single.concat(chapterInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<ChapterInfoBean>() {
                            String title = "";
                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(Integer.MAX_VALUE);
                            }

                            @Override
                            public void onNext(ChapterInfoBean chapterInfoBean) {
                                //将获取到的数据进行存储
                                title = titles.poll();
                                //存储数据
                                BookRepository.getInstance().saveChapterInfo(
                                        bookId, title, chapterInfoBean.getBody()
                                );
                                mView.finishChapter();
                            }

                            @Override
                            public void onError(Throwable t) {
                                //只有第一个加载失败才会调用errorChapter
                                if (bookChapters.get(0).getTitle().equals(title)){
                                    mView.errorChapter();
                                }
                                LogUtils.e(t);
                            }

                            @Override
                            public void onComplete() {
                            }
                        }
                );
    }
}
