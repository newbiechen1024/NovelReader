package com.example.newbiechen.ireader.service;

import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.DeleteResponseEvent;
import com.example.newbiechen.ireader.event.DeleteTaskEvent;
import com.example.newbiechen.ireader.event.DownloadMessage;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.DownloadTaskBean;
import com.example.newbiechen.ireader.utils.BookManager;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.local.LocalRepository;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.ui.base.BaseService;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-5-10.
 */

public class DownloadService extends BaseService {
    private static final String TAG = "DownloadService";
    //加载状态
    private static final int LOAD_ERROR= -1;
    private static final int LOAD_NORMAL= 0;
    private static final int LOAD_PAUSE = 1;
    private static final int LOAD_DELETE = 2; //正在加载时候，用户删除收藏书籍的情况。

    //下载状态
    public static final int STATUS_CONTINUE = DownloadTaskBean.STATUS_LOADING;
    public static final int STATUS_PAUSE = DownloadTaskBean.STATUS_PAUSE;

    //线程池
    private final ExecutorService mSingleExecutor = Executors.newSingleThreadExecutor();
    //加载队列
    private final List<DownloadTaskBean> mDownloadTaskQueue = Collections.synchronizedList(new ArrayList<>());
    //Handler
    private Handler mHandler;

    //包含所有的DownloadTask
    private List<DownloadTaskBean> mDownloadTaskList;

    private OnDownloadListener mDownloadListener;
    private boolean isBusy = false;
    private boolean isCancel = false;
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(getMainLooper());
        //从数据库中获取所有的任务
        mDownloadTaskList = LocalRepository
                .getInstance()
                .getDownloadTaskList();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new TaskBuilder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //接受创建的DownloadTask
        Disposable disposable = RxBus.getInstance()
                .toObservable(DownloadTaskBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) ->  {
                            //判断任务是否为轮询标志
                            //判断任务是否存在，并修改任务
                            if (TextUtils.isEmpty(event.getBookId())
                              || !checkAndAlterDownloadTask(event)){
                                addToExecutor(event);
                            }
                        }
                );
        addDisposable(disposable);

        //是否删除数据的问题
        Disposable deleteDisp = RxBus.getInstance()
                .toObservable(DeleteTaskEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) -> {
                            //判断是否该数据存在加载列表中
                            boolean isDelete = true;
                            for (DownloadTaskBean bean : mDownloadTaskQueue){
                                if (bean.getBookId().equals(event.collBook.get_id())){
                                    isDelete = false;
                                    break;
                                }
                            }
                            //如果不存在则删除List中的task
                            if (isDelete){
                                //
                                Iterator<DownloadTaskBean> taskIt = mDownloadTaskList.iterator();
                                while (taskIt.hasNext()){
                                    DownloadTaskBean task = taskIt.next();
                                    if (task.getBookId().equals(event.collBook.get_id())) {
                                        taskIt.remove();
                                    }
                                }
                            }
                            //返回状态
                            RxBus.getInstance().post(new DeleteResponseEvent(isDelete,event.collBook));
                        }
                );
        addDisposable(deleteDisp);
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 1. 查看是否任务已存在
     * 2. 修改DownloadTask的 taskName 和 list
     * @return
     */
    private boolean checkAndAlterDownloadTask(DownloadTaskBean newTask){
        boolean isExist = false;
        for (DownloadTaskBean downloadTask : mDownloadTaskList){
            //如果不相同则不往下执行，往下执行都是存在相同的情况
            if (!downloadTask.getBookId().equals(newTask.getBookId())) continue;

            if (downloadTask.getStatus() == DownloadTaskBean.STATUS_FINISH){
                //判断是否newTask是已完成
                if (downloadTask.getLastChapter() == newTask.getLastChapter()){
                    isExist = true;

                    //发送回去已缓存
                    postMessage("当前书籍已缓存");
                }
                //判断，是否已完成的章节的起始点比新Task大，如果更大则表示新Task中的该章节已被加载，所以需要剪切
                else if(downloadTask.getLastChapter() >
                        (newTask.getLastChapter() - newTask.getBookChapterList().size())){
                    //删除掉已经完成的章节
                    List<BookChapterBean> remainChapterBeans = newTask.getBookChapterList()
                            .subList(downloadTask.getLastChapter(),
                                    newTask.getLastChapter());
                    String taskName = newTask.getTaskName()
                            + getString(R.string.nb_download_chapter_scope,
                            downloadTask.getLastChapter(), newTask.getLastChapter());
                    //重置任务
                    newTask.setBookChapters(remainChapterBeans);
                    newTask.setTaskName(taskName);

                    //发送添加到任务的提示
                    postMessage("成功添加到缓存队列");
                }
            }
            //表示该任务已经在 下载、等待、暂停、网络错误中
            else {
                isExist = true;
                //发送回去:已经在加载队列中。
                postMessage("任务已存在");
            }
        }
        //重置名字
        if (!isExist){
            String taskName = newTask.getTaskName()
                    + getString(R.string.nb_download_chapter_scope,
                    1, newTask.getLastChapter());
            newTask.setTaskName(taskName);
            postMessage("成功添加到缓存队列");
        }
        return isExist;
    }

    private void addToExecutor(DownloadTaskBean taskEvent){

        //判断是否为轮询请求
        if (!TextUtils.isEmpty(taskEvent.getBookId())) {

            if (!mDownloadTaskList.contains(taskEvent)){
                //加入总列表中，表示创建，修改CollBean的状态。
                mDownloadTaskList.add(taskEvent);
            }
            // 添加到下载队列
            mDownloadTaskQueue.add(taskEvent);
        }

        // 从队列顺序取出第一条下载
        if (mDownloadTaskQueue.size() > 0 && !isBusy) {
            isBusy = true;
            executeTask(mDownloadTaskQueue.get(0));
        }
    }

    private void executeTask(DownloadTaskBean taskEvent){
        Runnable runnable = () -> {

            taskEvent.setStatus(DownloadTaskBean.STATUS_LOADING);

            int result = LOAD_NORMAL;
            List<BookChapterBean> bookChapterBeans = taskEvent.getBookChapters();

            //调用for循环，下载数据
            for (int i=taskEvent.getCurrentChapter(); i<bookChapterBeans.size();++i) {

                BookChapterBean bookChapterBean = bookChapterBeans.get(i);
                //首先判断该章节是否曾经被加载过 (从文件中判断)
                if (BookManager
                        .isChapterCached(taskEvent.getBookId(),bookChapterBean.getTitle())){

                    //设置任务进度
                    taskEvent.setCurrentChapter(i);

                    //章节加载完成
                    postDownloadChange(taskEvent, DownloadTaskBean.STATUS_LOADING, i + "");

                    //无需进行下一步
                    continue;
                }

                //判断网络是否出问题
                if (!NetworkUtils.isAvailable()){
                    //章节加载失败
                    result = LOAD_ERROR;
                    break;
                }

                if (isCancel){
                    result = LOAD_PAUSE;
                    isCancel = false;
                    break;
                }

                //加载数据
                result = loadChapter(taskEvent.getBookId(),bookChapterBean);
                //章节加载完成
                if (result == LOAD_NORMAL){
                    taskEvent.setCurrentChapter(i);
                    postDownloadChange(taskEvent, DownloadTaskBean.STATUS_LOADING, i + "");
                }
                //章节加载失败
                else {
                    //遇到错误退出
                    break;
                }
            }


            if (result == LOAD_NORMAL){
                //存储DownloadTask的状态
                taskEvent.setStatus(DownloadTaskBean.STATUS_FINISH);//Task的状态
                taskEvent.setCurrentChapter(taskEvent.getBookChapters().size());//当前下载的章节数量
                taskEvent.setSize(BookManager.getBookSize(taskEvent.getBookId()));//Task的大小

                //发送完成状态
                postDownloadChange(taskEvent, DownloadTaskBean.STATUS_FINISH, "下载完成");
            }
            else if (result == LOAD_ERROR){
                taskEvent.setStatus(DownloadTaskBean.STATUS_ERROR);//Task的状态
                //任务加载失败
                postDownloadChange(taskEvent, DownloadTaskBean.STATUS_ERROR, "资源或网络错误");
            }
            else if (result == LOAD_PAUSE){
                taskEvent.setStatus(DownloadTaskBean.STATUS_PAUSE);//Task的状态
                postDownloadChange(taskEvent, DownloadTaskBean.STATUS_PAUSE, "暂停加载");
            }
            else if (result == LOAD_DELETE){
                //没想好怎么做
            }

            //存储状态
            LocalRepository.getInstance().saveDownloadTask(taskEvent);

            //轮询下一个事件，用RxBus用来保证事件是在主线程

            //移除完成的任务
            mDownloadTaskQueue.remove(taskEvent);
            //设置为空闲
            isBusy = false;
            //轮询
            post(new DownloadTaskBean());
        };
        mSingleExecutor.execute(runnable);
    }

    private int loadChapter(String folderName,BookChapterBean bean){
        //加载的结果参数
        final int[] result = {LOAD_NORMAL};

        //问题:(这里有个问题，就是body其实比较大，如何获取数据流而不是对象，)是不是直接使用OkHttpClient交互会更好一点
        Disposable disposable = RemoteRepository.getInstance()
                .getChapterInfo(bean.getLink())
                //表示在当前环境下执行
                .subscribe(
                        chapterInfo -> {
                            //TODO:这里文件的名字用的是BookChapter的title,而不是chapter的title。
                            //原因是Chapter的title可能重复，但是BookChapter的title不会重复
                            //BookChapter的title = 卷名 + 章节名 chapter 的 title 就是章节名。。
                            BookRepository.getInstance()
                                    .saveChapterInfo(folderName, bean.getTitle(),chapterInfo.getBody());
                        },
                        e -> {
                            //当前进度加载错误（这里需要判断是什么问题，根据相应的问题做出相应的回答）
                            LogUtils.e(e);
                            //设置加载结果
                            result[0] = LOAD_ERROR;
                        }
                );
        addDisposable(disposable);
        return result[0];
    }

    private void postDownloadChange(DownloadTaskBean task,int status,String msg){
        if (mDownloadListener!= null){
            int position = mDownloadTaskList.indexOf(task);
            //通过handler,切换回主线程
            mHandler.post(
                    () -> mDownloadListener.onDownloadChange(
                            position, status, msg)
            );
        }
    }

    private void postMessage(String msg){
        RxBus.getInstance().post(new DownloadMessage(msg));
    }

    private void post(DownloadTaskBean task){
        RxBus.getInstance().post(task);
    }


    @Override
    public boolean onUnbind(Intent intent){

        mDownloadListener = null;
        return super.onUnbind(intent);
    }

    class TaskBuilder extends Binder implements IDownloadManager{
        @Override
        public List<DownloadTaskBean> getDownloadTaskList() {
            return Collections.unmodifiableList(mDownloadTaskList);
        }

        @Override
        public void setOnDownloadListener(OnDownloadListener listener) {
            mDownloadListener = listener;
        }

        @Override
        public void setDownloadStatus(String taskName, int status) {
            //修改某个Task的状态
            switch (status){
                //加入缓存队列
                case DownloadTaskBean.STATUS_WAIT:
                    for (int i=0; i<mDownloadTaskList.size(); ++i){
                        DownloadTaskBean bean = mDownloadTaskList.get(i);
                        if (taskName.equals(bean.getTaskName())){
                            bean.setStatus(DownloadTaskBean.STATUS_WAIT);
                            mDownloadListener.onDownloadResponse(i,DownloadTaskBean.STATUS_WAIT);
                            addToExecutor(bean);
                            break;
                        }
                    }
                    break;
                //从缓存队列中删除
                case DownloadTaskBean.STATUS_PAUSE:
                    Iterator<DownloadTaskBean> it = mDownloadTaskQueue.iterator();
                    while (it.hasNext()){
                        DownloadTaskBean bean = it.next();
                        if (bean.getTaskName().equals(taskName)){
                            if (bean.getStatus() == DownloadTaskBean.STATUS_LOADING
                                    && bean.getTaskName().equals(taskName)){
                                isCancel = true;
                                break;
                            }
                            else{
                                bean.setStatus(DownloadTaskBean.STATUS_PAUSE);
                                mDownloadTaskQueue.remove(bean);
                                int position = mDownloadTaskList.indexOf(bean);
                                mDownloadListener.onDownloadResponse(position,DownloadTaskBean.STATUS_PAUSE);
                                break;
                            }
                        }
                    }
                    break;
            }
        }

        @Override
        public void setAllDownloadStatus(int status) {
            //修改所有Task的状态
        }

        //首先判断是否在加载队列中。
        //如果在加载队列中首先判断是否正在下载，
        //然后判断是否在完成队列中。
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    public interface IDownloadManager{
        List<DownloadTaskBean> getDownloadTaskList();
        void setOnDownloadListener(OnDownloadListener listener);
        void setDownloadStatus(String taskName,int status);
        void setAllDownloadStatus(int status);
    }

    public interface OnDownloadListener{
        /**
         *
         * @param pos : Task在item中的位置
         * @param status : Task的状态
         * @param msg: 传送的Msg
         */
        void onDownloadChange(int pos,int status,String msg);

        /**
         * 回复
         */
        void onDownloadResponse(int pos,int status);
    }
}
