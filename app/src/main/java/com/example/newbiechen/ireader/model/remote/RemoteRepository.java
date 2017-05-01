package com.example.newbiechen.ireader.model.remote;

import com.example.newbiechen.ireader.model.bean.BillboardPackage;
import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.model.bean.BookSortPackage;
import com.example.newbiechen.ireader.model.bean.CommentBean;
import com.example.newbiechen.ireader.model.bean.CommentDetailBean;
import com.example.newbiechen.ireader.model.bean.CommentDetailPackage;
import com.example.newbiechen.ireader.model.bean.HelpsDetailBean;
import com.example.newbiechen.ireader.model.bean.ReviewDetailBean;
import com.example.newbiechen.ireader.model.bean.ReviewDetailPackage;

import java.io.File;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;

/**
 * Created by newbiechen on 17-4-20.
 */

public class RemoteRepository {
    private static final String TAG = "RemoteRepository";

    private static RemoteRepository sInstance;
    private Retrofit mRetrofit;
    private BookApi mBookApi;

    private RemoteRepository(){
        mRetrofit = RemoteHelper.getInstance()
                .getRetrofit();

        mBookApi = mRetrofit.create(BookApi.class);
    }

    public static RemoteRepository getInstance(){
        if (sInstance == null){
            synchronized (RemoteHelper.class){
                if (sInstance == null){
                    sInstance = new RemoteRepository();
                }
            }
        }
        return sInstance;
    }

    public Single<List<BookCommentBean>> getBookComment(String block, String sort, int start, int limit, String distillate){

        return mBookApi.getBookCommentList(block,"all",sort,"all",start+"",limit+"",distillate)
                .map((listBean)-> listBean.getPosts());
    }

    public Single<List<BookHelpsBean>> getBookHelps(String sort, int start, int limit, String distillate){
        return mBookApi.getBookHelpList("all",sort,start+"",limit+"",distillate)
                .map((listBean)-> listBean.getHelps());
    }

    public Single<List<BookReviewBean>> getBookReviews(String sort, String bookType, int start, int limited, String distillate){
        return mBookApi.getBookReviewList("all",sort,bookType,start+"",limited+"",distillate)
                .map(listBean-> listBean.getReviews());
    }

    public Single<BillboardPackage> getBillboardPackage(){
        return mBookApi.getBillboardPackage();
    }

    public Single<BookSortPackage> getBookSortPackage(){
        return mBookApi.getBookSortPackage();
    }

    public Single<CommentDetailBean> getCommentDetail(String detailId){
        return mBookApi.getCommentDetailPackage(detailId)
                .map(bean -> bean.getPost());
    }

    public Single<ReviewDetailBean> getReviewDetail(String detailId){
        return mBookApi.getReviewDetailPacakge(detailId)
                .map(bean -> bean.getReview());
    }

    public Single<HelpsDetailBean> getHelpsDetail(String detailId){
        return mBookApi.getHelpsDetailPackage(detailId)
                .map(bean -> bean.getHelp());
    }

    public Single<List<CommentBean>> getBestComments(String detailId){
        return mBookApi.getBestCommentPackage(detailId)
                .map(bean -> bean.getComments());
    }

    /**
     * 获取的是 综合讨论区的 评论
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    public Single<List<CommentBean>> getDetailComments(String detailId, int start, int limit){
        return mBookApi.getCommentPackage(detailId,start+"",limit+"")
                .map(bean -> bean.getComments());
    }

    /**
     * 获取的是 书评区和书荒区的 评论
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    public Single<List<CommentBean>> getDetailBookComments(String detailId, int start, int limit){
        return mBookApi.getBookCommentPackage(detailId,start+"",limit+"")
                .map(bean -> bean.getComments());
    }
}
