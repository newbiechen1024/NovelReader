package com.example.newbiechen.ireader.model.local;

import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.gen.CollBookBeanDao;

import java.util.List;
import java.util.Observable;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.internal.operators.maybe.MaybeDefer;

/**
 * Created by newbiechen on 17-5-8.
 */

public class CollBookManager {

    private static volatile CollBookManager sInstance;

    private CollBookBeanDao mDao;
    private CollBookManager(){
        mDao = DaoDbHelper.getInstance()
                .getSession()
                .getCollBookBeanDao();
    }

    public static CollBookManager getInstance(){
        if (sInstance == null){
            synchronized (LocalRepository.class){
                if (sInstance == null){
                    sInstance = new CollBookManager();
                }
            }
        }
        return sInstance;
    }

    public void saveCollBook(CollBookBean bean){
        mDao.insertOrReplace(bean);
    }

    public void saveCollBooks(List<CollBookBean> beans){
        mDao.insertOrReplaceInTx(beans);
    }

    public void updateCollBook(CollBookBean bean){
        mDao.update(bean);
    }

    public CollBookBean getCollBook(String bookId){
        CollBookBean bean = mDao.queryBuilder()
                .where(CollBookBeanDao.Properties._id.eq(bookId))
                .unique();
        return bean;
    }

    public Single<List<CollBookBean>> getCollBooks(){
        return Single.create(new SingleOnSubscribe<List<CollBookBean>>() {
            @Override
            public void subscribe(SingleEmitter<List<CollBookBean>> e) throws Exception {
                List<CollBookBean> beans = mDao.loadAll();
                e.onSuccess(beans);
            }
        });
    }


    public void deleteCollBook(CollBookBean bean){
        mDao.delete(bean);
    }

    /**
     * 根据id判断是否被收藏
     * @return
     */
    public boolean isCollected(String bookId){
        CollBookBean bean = getCollBook(bookId);
        return bean != null ? true : false;
    }
}
