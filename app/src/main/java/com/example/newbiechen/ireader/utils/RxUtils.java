package com.example.newbiechen.ireader.utils;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.CommentBean;
import com.example.newbiechen.ireader.model.bean.DetailBean;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function3;

/**
 * Created by newbiechen on 17-4-29.
 */

public class RxUtils {

    public static <T> Single<DetailBean<T>> toCommentDetail(Single<T> detailSingle,
                                                Single<List<CommentBean>> bestCommentsSingle,
                                                Single<List<CommentBean>> commentsSingle){
        return Single.zip(detailSingle, bestCommentsSingle, commentsSingle,
                new Function3<T, List<CommentBean>, List<CommentBean>, DetailBean<T>>() {
                    @Override
                    public DetailBean<T> apply(T t, List<CommentBean> commentBeen,
                                               List<CommentBean> commentBeen2) throws Exception {
                        return new DetailBean<T>(t,commentBeen,commentBeen2);
                    }
                });
    }
}
