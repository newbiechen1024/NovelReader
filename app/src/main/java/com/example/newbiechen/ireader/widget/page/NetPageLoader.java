package com.example.newbiechen.ireader.widget.page;

import android.support.annotation.Nullable;

import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.BookRecordBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.FileUtils;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-5-29.
 */

public class NetPageLoader extends PageLoader{
    private static final String TAG = "PageFactory";

    public NetPageLoader(PageView pageView) {
        super(pageView);
    }

    //初始化书籍
    @Override
    public void openBook(CollBookBean collBook){
        super.openBook(collBook);
        isBookOpen = false;
        if (collBook.getBookChapters() == null) return;
        mChapterList = convertTxtChapter(collBook.getBookChapters());
        //设置目录回调
        if (mPageChangeListener != null){
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
        //提示加载下面的章节
        loadCurrentChapter();
    }

    private List<TxtChapter> convertTxtChapter(List<BookChapterBean> bookChapters){
        List<TxtChapter> txtChapters = new ArrayList<>(bookChapters.size());
        for (BookChapterBean bean : bookChapters){
            TxtChapter chapter = new TxtChapter();
            chapter.bookId = bean.getBookId();
            chapter.title = bean.getTitle();
            chapter.link = bean.getLink();
            txtChapters.add(chapter);
        }
        return txtChapters;
    }

    //章节加载错误
    public void chapterError(){
        //加载错误
        mStatus = STATUS_ERROR;
        //显示加载错误
        onDraw(mPageView.getNextPage(),false);
    }

    @Nullable
    @Override
    protected List<TxtPage> loadPageList(int chapter) {
        if (mChapterList == null){
            throw new IllegalArgumentException("Chapter list must not null");
        }

        TxtChapter txtChapter = mChapterList.get(chapter);
        File file = new File(Constant.BOOK_CACHE_PATH + mCollBook.get_id()
                + File.separator + mChapterList.get(chapter).title + FileUtils.SUFFIX_NB);
        if (!file.exists()) return null;
        Reader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);
        return loadPages(txtChapter,br);
    }

    //装载上一章节的内容
    @Override
    protected boolean prevChapter(){
        if (mCurChapterPos - 1 < 0){
            ToastUtils.show("已经没有上一章了");
            return false;
        }
        else {
            int prevChapter = mCurChapterPos - 1;
            List<TxtPage> pages = loadPageList(prevChapter);
            mLastChapter = mCurChapterPos;
            mCurChapterPos = prevChapter;

            if (mPageChangeListener != null){
                mPageChangeListener.onChapterChange(mCurChapterPos);
            }

            if (pages != null){
                mStatus = STATUS_FINISH;
                mPageList = pages;
                //提示章节改变，缓冲接下来的章节
                loadPrevChapter();
                return true;
            }
            else {
                mStatus = STATUS_LOADING;
                //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
                mCurPage.position = 0;
                onDraw(mPageView.getNextPage(),false);
                loadCurrentChapter();
                return false;
            }
        }
    }

    //装载下一章节的内容
    @Override
    protected boolean nextChapter(){
        if (mCurChapterPos + 1 >= mChapterList.size()){
            ToastUtils.show("已经没有下一章了");
            return false;
        }
        else {
            int nextChapter = mCurChapterPos + 1;
            //装载下一章
            List<TxtPage> tPages = loadPageList(nextChapter);
            mLastChapter = mCurChapterPos;
            mCurChapterPos = nextChapter;

            if (mPageChangeListener != null){
                mPageChangeListener.onChapterChange(mCurChapterPos);
            }

            if (tPages != null){
                mPageList = tPages;
                mStatus = STATUS_FINISH;
                loadNextChapter();
                return true;
            }
            else {
                mStatus = STATUS_LOADING;
                //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
                mCurPage.position = 0;
                onDraw(mPageView.getNextPage(),false);
                loadCurrentChapter();
                return false;
            }
        }
    }

    //跳转到指定章节
    public void skipToChapter(int pos){
        //正在加载
        mStatus = STATUS_LOADING;

        //绘制当前的状态
        mCurChapterPos = pos;
        if (mCurPage != null){
            //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
            mCurPage.position = 0;
        }

        if (mPageChangeListener != null){
            mPageChangeListener.onChapterChange(mCurChapterPos);
        }

        onDraw(mPageView.getNextPage(),false);

        //提示章节改变，需要下载
        loadCurrentChapter();
    }

    private void loadPrevChapter(){
        //提示加载上一章
        if (mPageChangeListener != null){
            //提示加载前面3个章节（不包括当前章节）
            int current = mCurChapterPos;
            int prev = current - 3;
            if (prev < 0){
                prev = 0;
            }
            mPageChangeListener.onLoadChapter(mChapterList.subList(prev,current),mCurChapterPos);
        }
    }

    private void loadCurrentChapter(){
        if (mPageChangeListener != null){
            List<TxtChapter> bookChapters = new ArrayList<>(5);
            //提示加载当前章节和前面两章和后面两章
            int current = mCurChapterPos;
            bookChapters.add(mChapterList.get(current));

            //如果当前已经是最后一章，那么就没有必要加载后面章节
            if (current != mChapterList.size()){
                int begin = current + 1;
                int next = begin + 2;
                if (next > mChapterList.size()){
                    next = mChapterList.size();
                }
                bookChapters.addAll(mChapterList.subList(begin,next));
            }

            //如果当前已经是第一章，那么就没有必要加载前面章节
            if (current != 0){
                int prev = current - 2;
                if (prev < 0){
                    prev = 0;
                }
                bookChapters.addAll(mChapterList.subList(prev,current));
            }
            mPageChangeListener.onLoadChapter(bookChapters,mCurChapterPos);
        }
    }

    private void loadNextChapter(){
        //提示加载下一章
        if (mPageChangeListener != null){
            //提示加载当前章节和后面3个章节
            int current = mCurChapterPos + 1;
            int next = mCurChapterPos + 3;
            if (next > mChapterList.size()){
                next = mChapterList.size();
            }
            mPageChangeListener.onLoadChapter(mChapterList.subList(current,next),mCurChapterPos);
        }
    }

    public void skipToPage(int pos){
        mCurPage = getCurPage(pos);
        onDraw(mPageView.getNextPage(),false);
    }

    @Override
    public void setChapterList(List<BookChapterBean> bookChapters) {
        if (bookChapters == null) return;

        mChapterList = convertTxtChapter(bookChapters);

        if (mPageChangeListener != null){
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
    }

    @Override
    public void saveRecord() {
        super.saveRecord();
        if (mCollBook != null){
            //表示当前CollBook已经阅读
            mCollBook.setIsUpdate(false);
            mCollBook.setLastRead(StringUtils.
                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            //直接更新
            BookRepository.getInstance()
                    .saveCollBook(mCollBook);
        }
    }
}

