package com.example.newbiechen.ireader.model.local;

import android.util.Log;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.bean.DownloadTaskBean;
import com.example.newbiechen.ireader.model.gen.CollBookBeanDao;
import com.example.newbiechen.ireader.model.gen.DaoSession;
import com.example.newbiechen.ireader.model.gen.DownloadTaskBeanDao;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.FileUtils;
import com.example.newbiechen.ireader.utils.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by newbiechen on 17-5-8.
 */

public class CollBookManager {
    private static final String TAG = "CollBookManager";
    private static volatile CollBookManager sInstance;
    private DaoSession mSession;
    private CollBookBeanDao mCollBookDao;
    private CollBookManager(){
        mSession = DaoDbHelper.getInstance()
                .getSession();

        mCollBookDao = mSession.getCollBookBeanDao();
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
        mCollBookDao.insertOrReplace(bean);
    }

    public void saveCollBooks(List<CollBookBean> beans){
        mCollBookDao.insertOrReplaceInTx(beans);
    }

    public void updateCollBook(CollBookBean bean){
        mCollBookDao.update(bean);
    }

    public CollBookBean getCollBook(String bookId){
        CollBookBean bean = mCollBookDao.queryBuilder()
                .where(CollBookBeanDao.Properties._id.eq(bookId))
                .unique();
        return bean;
    }

    public Single<List<CollBookBean>> getCollBooks(){
        return Single.create(new SingleOnSubscribe<List<CollBookBean>>() {
            @Override
            public void subscribe(SingleEmitter<List<CollBookBean>> e) throws Exception {
                List<CollBookBean> beans = mCollBookDao.loadAll();
                e.onSuccess(beans);
            }
        });
    }


    public void deleteCollBook(CollBookBean bean){
        mCollBookDao.delete(bean);
    }

    /********************************收藏文章的存储功能***************************************************/

    /**
     * 存储章节
     * @param folderName
     * @param fileName
     * @param content
     */
    public void saveChapterInfo(String folderName,String fileName,String content){
        File file = getBookFile(folderName, fileName);
        //获取流并存储
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.close(writer);
        }
    }

    /**
     * 创建存储文件
     * @param folderName
     * @param fileName
     * @return
     */
    private File getBookFile(String folderName, String fileName){
        return FileUtils.getFile(Constant.BOOK_CACHE_PATH + folderName
                + File.separator + fileName + FileUtils.SUFFIX_TXT);
    }

    public long getBookSize(String folderName){
        return FileUtils.getDirSize(FileUtils
                .getFolder(Constant.BOOK_CACHE_PATH + folderName));
    }

    /**
     * 根据文件名判断是否被缓存过 (因为可能数据库显示被缓存过，但是文件中却没有的情况，所以需要根据文件判断是否被缓存
     * 过)
     * @return
     */
    public boolean isChapterCached(String folderName, String fileName){
        File file = new File(Constant.BOOK_CACHE_PATH + folderName
                + File.separator + fileName + FileUtils.SUFFIX_TXT);
        return file.exists();
    }

    //异步存储，提高效率
    public void saveDownloadTask(DownloadTaskBean bean){
        mSession.startAsyncSession()
                .runInTx(
                        () -> {
                            //存储BookChapterBean
                            mSession.getBookChapterBeanDao()
                                    .insertOrReplaceInTx(bean.getBookChapters());

                            //存储DownloadTask
                            mSession.getDownloadTaskBeanDao()
                                    .insertOrReplace(bean);
                        }
                );
    }

    public void deleteBookChapterList(String taskName){

    }

    /**
     * 获取所有下载的任务
     * @return
     */
    public List<DownloadTaskBean> getDownloadTaskList(){
        return mSession.getDownloadTaskBeanDao()
                .loadAll();
    }
}
