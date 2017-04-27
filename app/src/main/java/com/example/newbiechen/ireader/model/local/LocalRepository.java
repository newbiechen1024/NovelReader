package com.example.newbiechen.ireader.model.local;

import com.example.newbiechen.ireader.model.bean.AuthorBean;
import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.flag.BookDistillate;
import com.example.newbiechen.ireader.model.flag.BookSort;
import com.example.newbiechen.ireader.model.gen.AuthorBeanDao;
import com.example.newbiechen.ireader.model.gen.BookCommentBeanDao;
import com.example.newbiechen.ireader.model.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by newbiechen on 17-4-26.
 */

public class LocalRepository {
    private static final String DISTILLATE_ALL = "normal";
    private static final String DISTILLATE_BOUTIQUES = "distillate";

    private static LocalRepository sInstance;
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

    /**
     * 存储BookComment
     * @param beans
     */
    public void saveBookComment(List<BookCommentBean> beans){
        mSession.getBookCommentBeanDao()
                .insertOrReplaceInTx(beans);
    }

    public void saveAuthor(AuthorBean bean){
        mSession.getAuthorBeanDao()
                .insertOrReplace(bean);
    }

    /**
     * 获取数据
     * @param block
     * @param sort
     * @param distillate
     * @param start
     * @param limited
     * @return
     */
    public Single<List<BookCommentBean>> getBookComment(String block, String sort, int start, int limited, String distillate){
        if (distillate.equals(BookDistillate.ALL.getNetName())){
            distillate = DISTILLATE_ALL;
        }
        else {
            distillate = DISTILLATE_BOUTIQUES;
        }

        QueryBuilder<BookCommentBean> queryBuilder = mSession.getBookCommentBeanDao()
                .queryBuilder()
                .where(BookCommentBeanDao.Properties.Block.eq(block),
                        BookCommentBeanDao.Properties.State.eq(distillate))
                .offset(start)
                .limit(limited);

        if (sort.equals(BookSort.DEFAULT.getNetName())){
            queryBuilder.orderDesc(BookCommentBeanDao.Properties.Updated);
        }
        else if (sort.equals(BookSort.CREATED.getNetName())){
            queryBuilder.orderDesc(BookCommentBeanDao.Properties.Created);
        }
        else if (sort.equals(BookSort.COMMENT_COUNT.getNetName())){
            queryBuilder.orderDesc(BookCommentBeanDao.Properties.CommentCount);
        }
        else if (sort.equals(BookSort.HELPFUL.getNetName())){
            queryBuilder.orderDesc(BookCommentBeanDao.Properties.LikeCount);
        }
        Single<List<BookCommentBean>> observable = Single.create(
                new SingleOnSubscribe<List<BookCommentBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<BookCommentBean>> e) throws Exception {
                        e.onSuccess(queryBuilder.list());
                    }
                }
        );
        return observable;
    }

    public AuthorBean getAuthor(String id){
        return mSession.getAuthorBeanDao()
                .queryBuilder()
                .where(AuthorBeanDao.Properties._id.eq(id))
                .unique();
    }
}
