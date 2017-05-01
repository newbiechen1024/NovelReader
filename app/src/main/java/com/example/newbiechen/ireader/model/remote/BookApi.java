package com.example.newbiechen.ireader.model.remote;

import com.example.newbiechen.ireader.model.bean.BillboardPackage;
import com.example.newbiechen.ireader.model.bean.BookHelpsPackage;
import com.example.newbiechen.ireader.model.bean.BookReviewPackage;
import com.example.newbiechen.ireader.model.bean.BookCommentPackage;
import com.example.newbiechen.ireader.model.bean.BookSortPackage;
import com.example.newbiechen.ireader.model.bean.CommentDetailPackage;
import com.example.newbiechen.ireader.model.bean.CommentsPackage;
import com.example.newbiechen.ireader.model.bean.HelpsDetailPackage;
import com.example.newbiechen.ireader.model.bean.ReviewDetailPackage;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by newbiechen on 17-4-20.
 */

public interface BookApi {

    /*******************************Community *******************************************************/
    /**
     * 获取综合讨论区、原创区，女生区帖子列表
     * 全部、默认排序  http://api.zhuishushenqi.com/post/by-block?block=ramble&duration=all&sort=updated&type=all&start=0&limit=20&distillate=
     * 精品、默认排序  http://api.zhuishushenqi.com/post/by-block?block=ramble&duration=all&sort=updated&type=all&start=0&limit=20&distillate=true
     *
     * @param block      ramble:综合讨论区
     *                   original：原创区
     *                   girl:女生区
     * @param duration   all
     * @param sort       updated(默认排序)
     *                   created(最新发布)
     *                   comment-count(最多评论)
     * @param type       all
     * @param start      0
     * @param limit      20
     * @param distillate true(精品)
     * @return
     */
    @GET("/post/by-block")
    Single<BookCommentPackage> getBookCommentList(@Query("block") String block, @Query("duration") String duration, @Query("sort") String sort, @Query("type") String type, @Query("start") String start, @Query("limit") String limit, @Query("distillate") String distillate);


    /**
     * 获取书荒区帖子列表
     * 全部、默认排序  http://api.zhuishushenqi.com/post/help?duration=all&sort=updated&start=0&limit=20&distillate=
     * 精品、默认排序  http://api.zhuishushenqi.com/post/help?duration=all&sort=updated&start=0&limit=20&distillate=true
     *
     * @param duration   all
     * @param sort       updated(默认排序)
     *                   created(最新发布)
     *                   comment-count(最多评论)
     * @param start      0
     * @param limit      20
     * @param distillate true(精品) 、空字符（全部）
     * @return
     */
    @GET("/post/help")
    Single<BookHelpsPackage> getBookHelpList(@Query("duration") String duration, @Query("sort") String sort, @Query("start") String start, @Query("limit") String limit, @Query("distillate") String distillate);

    /**
     * 获取书评区帖子列表
     * 全部、全部类型、默认排序  http://api.zhuishushenqi.com/post/review?duration=all&sort=updated&type=all&start=0&limit=20&distillate=
     * 精品、玄幻奇幻、默认排序  http://api.zhuishushenqi.com/post/review?duration=all&sort=updated&type=xhqh&start=0&limit=20&distillate=true
     *
     * @param duration   all
     * @param sort       updated(默认排序)
     *                   created(最新发布)
     *                   helpful(最有用的)
     *                   comment-count(最多评论)
     * @param type       all(全部类型)、xhqh(玄幻奇幻)、dsyn(都市异能)...
     * @param start      0
     * @param limit      20
     * @param distillate true(精品) 、空字符（全部）
     * @return
     */
    @GET("/post/review")
    Single<BookReviewPackage> getBookReviewList(@Query("duration") String duration, @Query("sort") String sort, @Query("type") String type, @Query("start") String start, @Query("limit") String limit, @Query("distillate") String distillate);


    /***********************************帖子详情*******************************************************/
    /**
     * 获取综合讨论区帖子详情 :/post/{detailId}
     * @param detailId ->_id
     * @return
     */
    @GET("/post/{detailId}")
    Single<CommentDetailPackage> getCommentDetailPackage(@Path("detailId") String detailId);


    /**
     * 获取书评区帖子详情
     *
     * @param detailId->_id
     * @return
     * */
    @GET("/post/review/{detailId}")
    Single<ReviewDetailPackage> getReviewDetailPacakge(@Path("detailId") String detailId);


    /**
     * 获取书荒区帖子详情
     *
     * @param detailId->_id
     * @return
     **/
    @GET("/post/help/{detailId}")
    Single<HelpsDetailPackage> getHelpsDetailPackage(@Path("detailId") String detailId);


     /**
     * 获取神评论列表(综合讨论区、书评区、书荒区皆为同一接口)
     *
     * @param detailId -> _id
     * @return
     **/
    @GET("/post/{detailId}/comment/best")
    Single<CommentsPackage> getBestCommentPackage(@Path("detailId") String detailId);

     /**
     * 获取综合讨论区帖子详情内的评论列表        :/post/{disscussionId}/comment
     * 获取书评区、书荒区帖子详情内的评论列表     :/post/review/{disscussionId}/comment
     * @param detailId->_id
     * @param start              0
     * @param limit              30
     * @return
     **/
     @GET("/post/{detailId}/comment")
     Single<CommentsPackage> getCommentPackage(@Path("detailId") String detailId, @Query("start") String start, @Query("limit") String limit);

     @GET("/post/review/{detailId}/comment")
     Single<CommentsPackage> getBookCommentPackage( @Path("detailId") String detailId, @Query("start") String start, @Query("limit") String limit);


    /************************************find****************************************************/

    /**
     * 获取所有排行榜
     *
     * @return
     */
    @GET("/ranking/gender")
    Single<BillboardPackage> getBillboardPackage();

    /**
     * 获取分类
     *
     * @return
     */
    @GET("/cats/lv2/statistics")
    Single<BookSortPackage> getBookSortPackage();
}
