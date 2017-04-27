package com.example.newbiechen.ireader.model;

import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.model.local.LocalRepository;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-4-26.
 */

public class ModelRepository {
    private static ModelRepository sInstance;

    private LocalRepository localRepo;
    private RemoteRepository remoteRepo;

    private ModelRepository(){
        localRepo = LocalRepository.getInstance();
        remoteRepo = RemoteRepository.getInstance();
    }

    public static ModelRepository getInstance(){
        if (sInstance == null){
            synchronized (LocalRepository.class){
                if (sInstance == null){
                    sInstance = new ModelRepository();
                }
            }
        }
        return sInstance;
    }

    public Flowable<List<BookCommentBean>> getBookComment(String block, String sort, int start, int limited, String distillate){
        Single<List<BookCommentBean>> localObserver = localRepo.getBookComment(block, sort, start, limited, distillate);
        Single<List<BookCommentBean>> remoteObserver = remoteRepo.getBookComment(block, sort, start, limited, distillate);
        return Single.concat(localObserver,remoteObserver)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
