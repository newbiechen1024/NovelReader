package com.example.newbiechen.ireader.model.remote;

import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.BookDetailBean;
import com.example.newbiechen.ireader.model.bean.ChapterInfoBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.bean.packages.BillboardPackage;
import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.bean.BookListBean;
import com.example.newbiechen.ireader.model.bean.BookListDetailBean;
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.model.bean.packages.BookSortPackage;
import com.example.newbiechen.ireader.model.bean.packages.BookSubSortPackage;
import com.example.newbiechen.ireader.model.bean.BookTagBean;
import com.example.newbiechen.ireader.model.bean.CommentBean;
import com.example.newbiechen.ireader.model.bean.CommentDetailBean;
import com.example.newbiechen.ireader.model.bean.HelpsDetailBean;
import com.example.newbiechen.ireader.model.bean.HotCommentBean;
import com.example.newbiechen.ireader.model.bean.ReviewDetailBean;
import com.example.newbiechen.ireader.model.bean.SortBookBean;
import com.example.newbiechen.ireader.model.bean.packages.ChapterInfoPackage;
import com.example.newbiechen.ireader.model.bean.packages.SearchBookPackage;

import java.util.ArrayList;
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

    public Single<List<CollBookBean>> getRecommendBooks(String gender){
        return mBookApi.getRecommendBookPackage(gender)
                .map(bean -> bean.getBooks());
    }

    public Single<List<BookChapterBean>> getBookChapters(String bookId){
        return mBookApi.getBookChapterPackage(bookId, "chapter")
                .map(bean -> {
                    if (bean.getMixToc() == null){
                        return new ArrayList<BookChapterBean>(1);
                    }
                    else {
                        return bean.getMixToc().getChapters();
                    }
                });
    }

    /**
     * 注意这里用的是同步请求
     * @param url
     * @return
     */
    public Single<ChapterInfoBean> getChapterInfo(String url){
        return mBookApi.getChapterInfoPackage(url)
                .map(bean -> bean.getChapter());
    }

    /***********************************************************************************/


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

    /*****************************************************************************/
    /**
     * 获取书籍的分类
     * @return
     */
    public Single<BookSortPackage> getBookSortPackage(){
        return mBookApi.getBookSortPackage();
    }

    /**
     * 获取书籍的子分类
     * @return
     */
    public Single<BookSubSortPackage> getBookSubSortPackage(){
        return mBookApi.getBookSubSortPackage();
    }

    /**
     * 根据分类获取书籍列表
     * @param gender
     * @param type
     * @param major
     * @param minor
     * @param start
     * @param limit
     * @return
     */
    public Single<List<SortBookBean>> getSortBooks(String gender,String type,String major,String minor,int start,int limit){
        return mBookApi.getSortBookPackage(gender, type, major, minor, start, limit)
                .map(bean -> bean.getBooks());
    }

    /*******************************************************************************/

    /**
     * 排行榜的类型
     * @return
     */
    public Single<BillboardPackage> getBillboardPackage(){
        return mBookApi.getBillboardPackage();
    }

    /**
     * 排行榜的书籍
     * @param billId
     * @return
     */
    public Single<List<BillBookBean>> getBillBooks(String billId){
        return mBookApi.getBillBookPackage(billId)
                .map(bean -> bean.getRanking().getBooks());
    }

    /***********************************书单*************************************/

    /**
     * 获取书单列表
     * @param duration
     * @param sort
     * @param start
     * @param limit
     * @param tag
     * @param gender
     * @return
     */
    public Single<List<BookListBean>> getBookLists(String duration, String sort,
                                                   int start, int limit,
                                                   String tag, String gender){
        return mBookApi.getBookListPackage(duration, sort, start+"", limit+"", tag, gender)
                .map(bean -> bean.getBookLists());
    }

    /**
     * 获取书单的标签|类型
     * @return
     */
    public Single<List<BookTagBean>> getBookTags(){
        return mBookApi.getBookTagPackage()
                .map(bean -> bean.getData());
    }

    /**
     * 获取书单的详情
     * @param detailId
     * @return
     */
    public Single<BookListDetailBean> getBookListDetail(String detailId){
        return mBookApi.getBookListDetailPackage(detailId)
                .map(bean -> bean.getBookList());
    }

    /***************************************书籍详情**********************************************/
    public Single<BookDetailBean> getBookDetail(String bookId){
        return mBookApi.getBookDetail(bookId);
    }

    public Single<List<HotCommentBean>> getHotComments(String bookId){
        return mBookApi.getHotCommnentPackage(bookId)
                .map(bean -> bean.getReviews());
    }

    public Single<List<BookListBean>> getRecommendBookList(String bookId,int limit){
        return mBookApi.getRecommendBookListPackage(bookId,limit+"")
                .map(bean -> bean.getBooklists());
    }
    /********************************书籍搜索*********************************************/
    /**
     * 搜索热词
     * @return
     */
    public Single<List<String>> getHotWords(){
        return mBookApi.getHotWordPackage()
                .map(bean -> bean.getHotWords());
    }

    /**
     * 搜索关键字
     * @param query
     * @return
     */
    public Single<List<String>> getKeyWords(String query){
        return mBookApi.getKeyWordPacakge(query)
                .map(bean -> bean.getKeywords());

    }

    /**
     * 查询书籍
     * @param query:书名|作者名
     * @return
     */
    public Single<List<SearchBookPackage.BooksBean>> getSearchBooks(String query){
        return mBookApi.getSearchBookPackage(query)
                .map(bean -> bean.getBooks());
    }
}
