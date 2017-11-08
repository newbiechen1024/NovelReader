package com.example.newbiechen.ireader.model.local;

import com.example.newbiechen.ireader.model.bean.AuthorBean;
import com.example.newbiechen.ireader.model.bean.DownloadTaskBean;
import com.example.newbiechen.ireader.model.bean.packages.BillboardPackage;
import com.example.newbiechen.ireader.model.bean.ReviewBookBean;
import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.model.bean.BookHelpfulBean;
import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.model.bean.packages.BookSortPackage;
import com.example.newbiechen.ireader.model.flag.BookSort;
import com.example.newbiechen.ireader.model.gen.AuthorBeanDao;

import com.example.newbiechen.ireader.model.gen.BookCommentBeanDao;
import com.example.newbiechen.ireader.model.gen.BookHelpfulBeanDao;
import com.example.newbiechen.ireader.model.gen.BookHelpsBeanDao;
import com.example.newbiechen.ireader.model.gen.BookReviewBeanDao;
import com.example.newbiechen.ireader.model.gen.DaoSession;
import com.example.newbiechen.ireader.model.gen.ReviewBookBeanDao;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.SharedPreUtils;
import com.google.gson.Gson;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by newbiechen on 17-4-26.
 */

public class LocalRepository implements SaveDbHelper,GetDbHelper,DeleteDbHelper{
    private static final String TAG = "LocalRepository";
    private static final String DISTILLATE_ALL = "normal";
    private static final String DISTILLATE_BOUTIQUES = "distillate";

    private static volatile LocalRepository sInstance;
    private DaoSession mSession;
    private LocalRepository(){
        mSession = DaoDbHelper.getInstance().getSession();
    }

    public static LocalRepository getInstance(){
        if (sInstance == null){
            synchronized (LocalRepository.class){
                if (sInstance == null){
                    sInstance = new LocalRepository();
                }
            }
        }
        return sInstance;
    }

    /*************************************数据存储*******************************************/
    /**
     * 存储BookComment
     * @param beans
     */
    public void saveBookComments(List<BookCommentBean> beans){
        //存储Author,为了保证高效性，所以首先转换成List进行统一存储。
        List<AuthorBean> authorBeans = new ArrayList<>(beans.size());
        for (int i=0; i<beans.size(); ++i){
            BookCommentBean commentBean = beans.get(i);
            authorBeans.add(commentBean.getAuthorBean());
        }
        saveAuthors(authorBeans);
        //
        mSession.getBookCommentBeanDao()
                .insertOrReplaceInTx(beans);
    }

    public void saveBookHelps(List<BookHelpsBean> beans){
        mSession.startAsyncSession()
                .runInTx(
                        ()->{
                            //存储Author,为了保证高效性，所以首先转换成List进行统一存储。
                            List<AuthorBean> authorBeans = new ArrayList<>(beans.size());
                            for (BookHelpsBean helpsBean : beans){
                                authorBeans.add(helpsBean.getAuthorBean());
                            }
                            saveAuthors(authorBeans);

                            mSession.getBookHelpsBeanDao()
                                    .insertOrReplaceInTx(beans);
                        }
                );

    }

    public void saveBookReviews(List<BookReviewBean> beans){
        mSession.startAsyncSession()
                .runInTx(
                        ()->{
                            //数据转换
                            List<ReviewBookBean> bookBeans = new ArrayList<>(beans.size());
                            List<BookHelpfulBean> helpfulBeans = new ArrayList<>(beans.size());
                            for (BookReviewBean reviewBean : beans){
                                bookBeans.add(reviewBean.getBookBean());
                                helpfulBeans.add(reviewBean.getHelpfulBean());
                            }
                            saveBookHelpfuls(helpfulBeans);
                            saveBooks(bookBeans);
                            //存储BookReview
                            mSession.getBookReviewBeanDao()
                                    .insertOrReplaceInTx(beans);
                        }
                );
    }

    public void saveBooks(List<ReviewBookBean> beans){
        mSession.getReviewBookBeanDao()
                .insertOrReplaceInTx(beans);
    }

    public void saveAuthors(List<AuthorBean> beans){
        mSession.getAuthorBeanDao()
                .insertOrReplaceInTx(beans);
    }

    public void saveBookHelpfuls(List<BookHelpfulBean> beans){
        mSession.getBookHelpfulBeanDao()
                .insertOrReplaceInTx(beans);
    }

    @Override
    public void saveBookSortPackage(BookSortPackage bean) {
        String json = new Gson().toJson(bean);
        SharedPreUtils.getInstance()
                .putString(Constant.SHARED_SAVE_BOOK_SORT,json);
    }

    @Override
    public void saveBillboardPackage(BillboardPackage bean) {
        String json = new Gson().toJson(bean);
        SharedPreUtils.getInstance()
                .putString(Constant.SHARED_SAVE_BILLBOARD,json);
    }

    @Override
    public void saveDownloadTask(DownloadTaskBean bean) {
        BookRepository.getInstance()
                .saveBookChaptersWithAsync(bean.getBookChapters());
        mSession.getDownloadTaskBeanDao()
                .insertOrReplace(bean);
    }

    /***************************************read data****************************************************/

    /**
     * 获取数据
     * @param block
     * @param sort
     * @param distillate
     * @param start
     * @param limited
     * @return
     */
    public Single<List<BookCommentBean>> getBookComments(String block, String sort, int start, int limited, String distillate){

        QueryBuilder<BookCommentBean> queryBuilder = mSession.getBookCommentBeanDao()
                .queryBuilder()
                .where(BookCommentBeanDao.Properties.Block.eq(block),
                        BookCommentBeanDao.Properties.State.eq(distillate))
                .offset(start)
                .limit(limited);

        queryOrderBy(queryBuilder,BookCommentBeanDao.class,sort);
        return queryToRx(queryBuilder);
    }

    /**
     *
     * @param sort
     * @param start
     * @param limited
     * @param distillate
     * @return
     */
    public Single<List<BookHelpsBean>> getBookHelps(String sort, int start, int limited, String distillate){
        QueryBuilder<BookHelpsBean> queryBuilder = mSession.getBookHelpsBeanDao()
                .queryBuilder()
                .where(BookHelpsBeanDao.Properties.State.eq(distillate))
                .offset(start)
                .limit(limited);


        queryOrderBy(queryBuilder,BookHelpsBean.class,sort);
        return queryToRx(queryBuilder);
    }

    public Single<List<BookReviewBean>> getBookReviews(String sort, String bookType, int start, int limited, String distillate){
        QueryBuilder<BookReviewBean> queryBuilder = mSession.getBookReviewBeanDao()
                .queryBuilder()
                .where(BookReviewBeanDao.Properties.State.eq(distillate))
                .limit(limited)
                .offset(start);
        //多表关联
        Join bookJoin = queryBuilder.join(BookReviewBeanDao.Properties.BookId,ReviewBookBean.class)
                .where(ReviewBookBeanDao.Properties.Type.eq(bookType));

        queryBuilder.join(bookJoin,BookReviewBeanDao.Properties._id,
                BookHelpfulBean.class,BookHelpsBeanDao.Properties._id);

        //排序
        if (sort.equals(BookSort.HELPFUL.getDbName())){
            queryBuilder.orderDesc(BookHelpfulBeanDao.Properties.Yes);
        }
        else {
            queryOrderBy(queryBuilder,BookReviewBeanDao.class,sort);
        }

        return queryToRx(queryBuilder);
    }

    @Override
    public BookSortPackage getBookSortPackage() {
        String json = SharedPreUtils.getInstance()
                .getString(Constant.SHARED_SAVE_BOOK_SORT);
        if (json == null){
            return null;
        }
        else {
            return new Gson().fromJson(json,BookSortPackage.class);
        }
    }

    @Override
    public BillboardPackage getBillboardPackage() {
        String json = SharedPreUtils.getInstance()
                .getString(Constant.SHARED_SAVE_BILLBOARD);
        if (json == null){
            return null;
        }
        else {
            return new Gson().fromJson(json,BillboardPackage.class);
        }
    }

    public AuthorBean getAuthor(String id){
        return mSession.getAuthorBeanDao()
                .queryBuilder()
                .where(AuthorBeanDao.Properties._id.eq(id))
                .unique();
    }

    public ReviewBookBean getReviewBook(String id){
        return mSession.getReviewBookBeanDao()
                .queryBuilder()
                .where(ReviewBookBeanDao.Properties._id.eq(id))
                .unique();
    }

    public BookHelpfulBean getBookHelpful(String id){
        return mSession.getBookHelpfulBeanDao()
                .queryBuilder()
                .where(BookHelpfulBeanDao.Properties._id.eq(id))
                .unique();
    }

    @Override
    public List<DownloadTaskBean> getDownloadTaskList() {
        return mSession.getDownloadTaskBeanDao()
                .loadAll();
    }

    private <T> void queryOrderBy(QueryBuilder queryBuilder, Class<T> daoCls,String orderBy){
        //获取Dao中的Properties
        Class<?>[] innerCls = daoCls.getClasses();
        Class<?> propertiesCls = null;
        for (Class<?> cls : innerCls){
            if (cls.getSimpleName().equals("Properties")){
                propertiesCls = cls;
                break;
            }
        }
        //如果不存在则返回
        if (propertiesCls == null) return;

        //这里没有进行异常处理有点小问题
        try {
            Field field = propertiesCls.getField(orderBy);
            Property property = (Property) field.get(propertiesCls);
            queryBuilder.orderDesc(property);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            LogUtils.e(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LogUtils.e(e);
        }
    }

    private <T> Single<List<T>> queryToRx(QueryBuilder<T> builder){
        return Single.create(new SingleOnSubscribe<List<T>>() {
            @Override
            public void subscribe(SingleEmitter<List<T>> e) throws Exception {
                List<T> data = builder.list();
                if (data == null){
                    data = new ArrayList<T>(1);
                }
                e.onSuccess(data);
            }
        });
    }

    /*******************************************************************************************/
    /**
     * 处理多出来的数据,一般在退出程序的时候进行
     */
    public void disposeOverflowData(){
        //固定存储100条数据，剩下的数据都删除
        mSession.startAsyncSession()
                .runInTx(
                        ()->{
                            disposeBookComment();
                            disposeBookHelps();
                            disposeBookReviews();
                        }
                );
    }

    private void disposeBookComment(){
        //第一种方法:使用get获取对象之后再依次删除。
        //第二种方法:直接调用Sqlite语句进行删除
        BookCommentBeanDao commentBeanDao = mSession.getBookCommentBeanDao();
        int count = (int)commentBeanDao.count();
        List<BookCommentBean> bookCommentBeans = commentBeanDao
                .queryBuilder()
                .orderDesc(BookCommentBeanDao.Properties.Updated)
                .limit(count)
                .offset(100)
                .list();
        //存储Author,为了保证高效性，所以首先转换成List进行统一存储。
        List<AuthorBean> authorBeans = new ArrayList<>(bookCommentBeans.size());
        for (BookCommentBean commentBean : bookCommentBeans){
            authorBeans.add(commentBean.getAuthorBean());
        }
        deleteAuthors(authorBeans);
        deleteBookComments(bookCommentBeans);
    }

    private void disposeBookHelps(){
        BookHelpsBeanDao helpfulDao = mSession.getBookHelpsBeanDao();
        int count = (int) helpfulDao.count();
        List<BookHelpsBean> helpsBeans = helpfulDao
                .queryBuilder()
                .orderDesc(BookHelpsBeanDao.Properties.Updated)
                .limit(count)
                .offset(100)
                .list();
        List<AuthorBean> authorBeans = new ArrayList<>(helpsBeans.size());
        for (BookHelpsBean commentBean : helpsBeans){
            authorBeans.add(commentBean.getAuthorBean());
        }
        deleteAuthors(authorBeans);
        deleteBookHelps(helpsBeans);
    }

    private void disposeBookReviews(){
        BookReviewBeanDao reviewDao = mSession.getBookReviewBeanDao();
        int count = (int) reviewDao.count();
        List<BookReviewBean> reviewBeans = reviewDao
                .queryBuilder()
                .orderDesc(BookHelpsBeanDao.Properties.Updated)
                .limit(count)
                .offset(100)
                .list();
        List<ReviewBookBean> bookBeans = new ArrayList<>(reviewBeans.size());
        List<BookHelpfulBean> helpfulBeans = new ArrayList<>(reviewBeans.size());
        for (BookReviewBean reviewBean : reviewBeans){
            bookBeans.add(reviewBean.getBookBean());
            helpfulBeans.add(reviewBean.getHelpfulBean());
        }
        deleteBooks(bookBeans);
        deleteBookHelpful(helpfulBeans);
        deleteBookReviews(reviewBeans);
    }

    /************************************delete********************************************/
    @Override
    public void deleteBookComments(List<BookCommentBean> beans) {
        mSession.getBookCommentBeanDao()
                .deleteInTx(beans);
    }

    @Override
    public void deleteBookReviews(List<BookReviewBean> beans) {
        mSession.getBookReviewBeanDao()
                .deleteInTx(beans);
    }

    @Override
    public void deleteBookHelps(List<BookHelpsBean> beans) {
        mSession.getBookHelpsBeanDao()
                .deleteInTx(beans);
    }

    @Override
    public void deleteAuthors(List<AuthorBean> beans) {
        mSession.getAuthorBeanDao()
                .deleteInTx(beans);
    }

    @Override
    public void deleteBooks(List<ReviewBookBean> beans) {
        mSession.getReviewBookBeanDao()
                .deleteInTx(beans);
    }

    @Override
    public void deleteBookHelpful(List<BookHelpfulBean> beans) {
        mSession.getBookHelpfulBeanDao()
                .deleteInTx(beans);
    }

    @Override
    public void deleteAll() {
        //清空全部数据。
    }
}
