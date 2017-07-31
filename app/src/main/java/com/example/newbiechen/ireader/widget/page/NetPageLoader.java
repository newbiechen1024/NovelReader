package com.example.newbiechen.ireader.widget.page;

import android.support.annotation.Nullable;

import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.FileUtils;
import com.example.newbiechen.ireader.utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-5-29.
 * 网络页面加载器
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

    @Nullable
    @Override
    protected List<TxtPage> loadPageList(int chapter) {
        if (mChapterList == null){
            throw new IllegalArgumentException("chapter list must not null");
        }

        //获取要加载的文件
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
    boolean prevChapter(){

        boolean hasPrev = super.prevChapter();
        if (!hasPrev) return false;

        if (mStatus == STATUS_FINISH){
            loadCurrentChapter();
            return true;
        }
        else if (mStatus == STATUS_LOADING){
            loadCurrentChapter();
            return false;
        }
        return false;
    }

    //装载下一章节的内容
    @Override
    boolean nextChapter(){
        boolean hasNext = super.nextChapter();
        if (!hasNext) return false;

        if (mStatus == STATUS_FINISH){
            loadNextChapter();
            return true;
        }
        else if (mStatus == STATUS_LOADING){
            loadCurrentChapter();
            return false;
        }
        return false;
    }

    //跳转到指定章节
    public void skipToChapter(int pos){
        super.skipToChapter(pos);

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
        if (mCollBook != null && isBookOpen){
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

