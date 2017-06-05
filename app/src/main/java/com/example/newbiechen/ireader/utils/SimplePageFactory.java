package com.example.newbiechen.ireader.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.BookRecordBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.local.ReadSettingManager;
import com.example.newbiechen.ireader.widget.PageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-5-29.
 */

public class SimplePageFactory {
    private static final String TAG = "PageFactory";

    private static final int DEFAULT_INTERVAL = 10;
    private static final int DEFAULT_MARGIN_HEIGHT = 30;
    private static final int DEFAULT_MARGIN_WIDTH = 15;
    private static final int DEFAULT_TIP_SIZE = 12;

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_FINISH = 2;
    public static final int STATUS_ERROR = 3;
    public static final int STATUS_EMPTY = 4;

    //页面显示类
    private PageView mPageView;
    //绘制小说内容的画笔
    private Paint mTextPaint;
    private Paint mBatteryPaint;
    private ReadSettingManager mSettingManager;
    private OnPageChangeListener mPageChangeListener;
    //被遮盖的页，或者认为被取消显示的页
    private TPage mCancelPage;
    //当前页
    private TPage mCurPage;
    private List<BookChapterBean> mCategoryList;
    private List<TPage> mPageList;
    private BookRecordBean mBookRecord;
    /*****************params**************************/
    //当前的状态
    private int mStatus = STATUS_LOADING;
    //应用的宽高
    private int mAppWidth;
    private int mAppHeight;
    //间距
    private int mMarginWidth;
    private int mMarginHeight;
    //书籍绘制区域的宽高
    private int mVisibleWidth;
    private int mVisibleHeight;
    //字体的颜色
    private int mTextColor;
    //字体的大小
    private int mTextSize;
    //行间距
    private int mIntervalSize;

    private int mBatteryLevel;

    private int mPageMode;
    private int mBgTheme;
    private boolean isNightMode;
    private int mPageBg;
    //书籍信息
    private String mBookId;
    //当前章
    private int mCurChapter = 0;
    //上一章
    private int mLastChapter = 0;
    private int mLineCount;
    private boolean isBookOpen = false;

    public SimplePageFactory(){
        //初始化数据
        initData();
        //初始化画笔
        initPaint();
    }

    private void initData(){
        mSettingManager = ReadSettingManager.getInstance();
        mTextSize = mSettingManager.getTextSize();
        mPageMode = mSettingManager.getPageMode();
        isNightMode = mSettingManager.isNightMode();
        mBgTheme = mSettingManager.getReadBgTheme();
        if (isNightMode) {
            setBgColor(ReadSettingManager.NIGHT_MODE);
        }
        else {
            setBgColor(mBgTheme);
        }
        //获取当前App的宽高
        int[] size = ScreenUtils.getAppSize();
        mAppWidth = size[0];
        mAppHeight = size[1];
        //初始化参数
        mMarginWidth = ScreenUtils.dpToPx(DEFAULT_MARGIN_WIDTH);
        mMarginHeight = ScreenUtils.dpToPx(DEFAULT_MARGIN_HEIGHT);
        mVisibleWidth = mAppWidth - mMarginWidth * 2;
        mVisibleHeight = mAppHeight - mMarginHeight * 2;
        mIntervalSize = ScreenUtils.dpToPx(DEFAULT_INTERVAL);
        //计算行数
        calculateLineCount();
    }

    //计算每页的行数
    private void calculateLineCount(){
        //这里需要加上intervalSize 是为保证当达到最后一行的时候没有间距
        mLineCount = (mVisibleHeight+mIntervalSize) /(mIntervalSize + mTextSize);
    }

    private void initPaint(){
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.LEFT);//绘制的起始点
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setSubpixelText(true);

        mBatteryPaint = new Paint();
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setDither(true);
        if (isNightMode){
            mBatteryPaint.setColor(Color.WHITE);
        }
        else {
            mBatteryPaint.setColor(Color.BLACK);
        }
    }

    public void setPageWidget(PageView pageView){
        mPageView = pageView;
        initPageView();
        //正在加载状态
        mStatus = STATUS_LOADING;
        //绘制当前的状态
        onDraw(mPageView.getCurPage());
        onDraw(mPageView.getNextPage());
    }

    private void initPageView(){
        mPageView.setPageMode(mPageMode);
        mPageView.setBgColor(mPageBg);
        //当前状态变成Loading
        mStatus = STATUS_LOADING;
    }

    //初始化书籍
    public void openBook(String bookId,List<BookChapterBean> beans){
        mBookId = bookId;
        mCategoryList = beans;

        //从数据库中获取书籍缓存信息(BookRecord)
        mBookRecord = BookRepository.getInstance()
                .getBookRecord(bookId);
        if (mBookRecord == null){
            mBookRecord = new BookRecordBean();
        }
        mCurChapter = mBookRecord.getChapter();
        mLastChapter = mCurChapter;
        //提示加载下面的章节
        loadCurrentChapter();
    }

    public void openChapter(){
        //加载完成
        mStatus = STATUS_FINISH;
        mPageList = createPageList(mCurChapter);
        //获取制定页面
        if (!isBookOpen){
            isBookOpen = true;
            mCurPage = getCurPage(mBookRecord.getPagePos());
        }
        else {
            mCurPage = getCurPage(0);
        }
        onDraw(mPageView.getNextPage());
        //重绘
        mPageView.invalidate();
    }

    //章节加载错误
    public void chapterError(){
        //加载错误
        mStatus = STATUS_ERROR;
        //显示错误界面
        onDraw(mPageView.getNextPage());
        mPageView.invalidate();
    }

    //十分耗时的操作，需要异步完成，并且需要进行缓存
    @Nullable
    private List<TPage> createPageList(int chapter){
        List<TPage> pages = new ArrayList<>();
        File file = new File(Constant.BOOK_CACHE_PATH + mBookId
                + File.separator + mCategoryList.get(chapter).getTitle() + FileUtils.SUFFIX_TXT);
        if (!file.exists()) return null;

        //使用流的方式加载
        List<String> lines = new ArrayList<>();
        Reader reader = null;
        String paragraph = null;
        try {
            reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            while ((paragraph = br.readLine()) != null){
                paragraph = "    " + paragraph;

                while (paragraph.length() > 0){
                    //测量一行占用的字节数
                    int count = mTextPaint.breakText(paragraph, true, mVisibleWidth, null);
                    //将一行字节，存储到lines中
                    lines.add(paragraph.substring(0, count));
                    //裁剪
                    paragraph = paragraph.substring(count);

                    //达到行数要求,创建Page
                    if (lines.size() == mLineCount){
                        //创建Page
                        TPage tPage = new TPage();
                        tPage.position = pages.size();
                        tPage.title = mCategoryList.get(chapter).getTitle();
                        tPage.lines = new ArrayList<>(lines);
                        pages.add(tPage);
                        //重置Lines
                        lines.clear();
                    }
                }
            }
            if (lines.size() != 0){
                //创建Page
                TPage tPage = new TPage();
                tPage.position = pages.size();
                tPage.title = mCategoryList.get(chapter).getTitle();
                tPage.lines = new ArrayList<>(lines);
                pages.add(tPage);
                //重置Lines
                lines.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(reader);
        }
        //可能出现内容为空的情况
        if (pages.size() == 0){
            TPage tPage = new TPage();
            tPage.lines = new ArrayList<>(1);
            pages.add(tPage);

            mStatus = STATUS_EMPTY;
        }
        if (mPageChangeListener != null){
            mPageChangeListener.onPageListChange(pages.size());
        }
        return pages;
    }

    private void onDraw(Bitmap bitmap){
        Canvas canvas = new Canvas(bitmap);

        /****绘制背景****/
        canvas.drawColor(mPageBg);

        /******绘制内容****/
        //内容比标题先绘制的原因是，内容的字体大小和标题还有地步的字体大小不相同
        if (mStatus != STATUS_FINISH){
            //绘制字体
            String tip = "";
            switch (mStatus){
                case STATUS_LOADING:
                    tip = "正在拼命加载中...";
                    break;
                case STATUS_ERROR:
                    tip = "加载失败(点击边缘重试)";
                    break;
                case STATUS_EMPTY:
                    tip = "文章内容为空";
                    break;
            }

            //将提示语句放到正中间
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float textHeight = fontMetrics.top - fontMetrics.bottom;
            float textWidth = mTextPaint.measureText(tip);
            float pivotX = (mAppWidth - textWidth)/2;
            float pivotY = (mAppHeight - textHeight)/2;
            canvas.drawText(tip,pivotX,pivotY, mTextPaint);
        }
        else {
            float top = mMarginHeight - mTextPaint.getFontMetrics().top;
            int interval = mIntervalSize + (int) mTextPaint.getTextSize();
            for (int i=0; i<mCurPage.lines.size(); ++i){
                canvas.drawText(mCurPage.lines.get(i),mMarginWidth,top, mTextPaint);
                top += interval;
            }
        }

        //需要注意的是:绘制text的y的起始点是text的基准线的位置，而不是从text的头部的位置
        int tipMarginHeight = ScreenUtils.dpToPx(3);
        /*****初始化标题的参数********/
        mTextPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_TIP_SIZE));
        float tipTop = tipMarginHeight - mTextPaint.getFontMetrics().top;
        //根据状态不一样，数据不一样
        if (mStatus != STATUS_FINISH){
            if(mCategoryList != null){
                canvas.drawText(mCategoryList.get(mCurChapter).getTitle()
                        , mMarginWidth, tipTop, mTextPaint);
            }
        }
        else {
            canvas.drawText(mCurPage.title, mMarginWidth, tipTop, mTextPaint);
        }

        //底部的字显示的位置Y
        float y = mAppHeight - mTextPaint.getFontMetrics().bottom - tipMarginHeight;
        /******绘制页码********/
        //只有finish的时候采用页码
        if (mStatus == STATUS_FINISH){
            String percent = (mCurPage.position+1) + "/" + mPageList.size();
            canvas.drawText(percent, mMarginWidth, y, mTextPaint);
        }
        /******绘制电池********/
        int visibleRight = mAppWidth - mMarginWidth;
        int visibleBottom = mAppHeight - tipMarginHeight;

        int outFrameWidth = (int) mTextPaint.measureText("xxx");
        int outFrameHeight = (int) mTextPaint.getTextSize();

        int polarHeight = ScreenUtils.dpToPx(6);
        int polarWidth = ScreenUtils.dpToPx(2);
        int border = 1;
        int innerMargin = 1;

        //电极的制作
        int polarLeft = visibleRight - polarWidth;
        int polarTop = visibleBottom - (outFrameHeight + polarHeight)/2;
        Rect polar = new Rect(polarLeft, polarTop, visibleRight,
                polarTop + polarHeight - ScreenUtils.dpToPx(2));

        mBatteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(polar,mBatteryPaint);

        //外框的制作
        int outFrameLeft = polarLeft - outFrameWidth;
        int outFrameTop = visibleBottom - outFrameHeight;
        int outFrameBottom = visibleBottom - ScreenUtils.dpToPx(2);
        Rect outFrame = new Rect(outFrameLeft, outFrameTop, polarLeft, outFrameBottom);

        mBatteryPaint.setStyle(Paint.Style.STROKE);
        mBatteryPaint.setStrokeWidth(border);
        canvas.drawRect(outFrame, mBatteryPaint);

        //内框的制作
        float innerWidth = (outFrame.width() - innerMargin*2 - border) * (mBatteryLevel/100.0f);
        RectF innerFrame = new RectF(outFrameLeft + border + innerMargin, outFrameTop + border + innerMargin,
                outFrameLeft + border + innerMargin + innerWidth, outFrameBottom - border - innerMargin);

        mBatteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(innerFrame,mBatteryPaint);

        /******绘制当前时间********/
        String time = StringUtils.dateConvert(System.currentTimeMillis(), Constant.FORMAT_TIME);
        float x = outFrameLeft - mTextPaint.measureText(time) - ScreenUtils.dpToPx(4);
        canvas.drawText(time,x,y,mTextPaint);
        //重置字体大小
        mTextPaint.setTextSize(mTextSize);
    }

    /**
     * @return:获取初始显示的页面
     */
    private TPage getCurPage(int pos){
        if (mPageChangeListener != null){
            mPageChangeListener.onPageChange(pos);
        }
        return mPageList.get(pos);
    }

    /**
     * @return:获取下一的页面
     */
    private TPage getNextPage(){
        int pos = mCurPage.position + 1;
        if (pos >= mPageList.size()){
            return null;
        }
        if (mPageChangeListener != null){
            mPageChangeListener.onPageChange(pos);
        }
        return mPageList.get(pos);
    }

    /**
     * @return:获取上一个页面
     */
    private TPage getPrevPage(){
        int pos = mCurPage.position - 1;
        if (pos < 0){
            return null;
        }
        if (mPageChangeListener != null){
            mPageChangeListener.onPageChange(pos);
        }
        return mPageList.get(pos);
    }

    /**
     * @return:获取上一个章节的最后一页
     */
    private TPage getPrevLastPage(){
        int pos = mPageList.size() - 1;
        return mPageList.get(pos);
    }

    //翻阅上一页
    public boolean prev(){
        if (mStatus == STATUS_LOADING){
            ToastUtils.show("正在加载中，请稍等");
            return false;
        }
        else if (mStatus == STATUS_ERROR){
            //点击重试
            mStatus = STATUS_LOADING;
            onDraw(mPageView.getNextPage());
            mPageView.invalidate();
            loadCurrentChapter();
            return false;
        }

        //判断是否达到章节的起始点
        TPage prevPage = getPrevPage();
        if (prevPage == null){
            //载入上一章。
            if (!prevChapter()){
                return false;
            }
            else {
                onDraw(mPageView.getCurPage());
                mCancelPage = mCurPage;
                mCurPage = getPrevLastPage();
                onDraw(mPageView.getNextPage());
                return true;
            }
        }

        onDraw(mPageView.getCurPage());
        mCancelPage = mCurPage;
        mCurPage = prevPage;
        onDraw(mPageView.getNextPage());//nextPage:指的是准备看的下一页，无关prev还是next
        return true;
    }

    //装载上一章节的内容
    private boolean prevChapter(){
        if (mCurChapter - 1 < 0){
            ToastUtils.show("已经没有上一章了");
            return false;
        }
        else {
            int prevChapter = mCurChapter - 1;
            List<TPage> tPages = createPageList(prevChapter);
            mLastChapter = mCurChapter;
            mCurChapter = prevChapter;
            if (tPages != null){
                mPageList = tPages;
                mStatus = STATUS_FINISH;
                //提示章节改变，缓冲接下来的章节
                loadPrevChapter();
                return true;
            }
            else {
                mStatus = STATUS_LOADING;
                //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
                mCurPage.position = 0;
                onDraw(mPageView.getNextPage());
                mPageView.invalidate();
                loadCurrentChapter();
                return false;
            }
        }
    }

    //翻阅下一页
    public boolean next(){
        if (mStatus == STATUS_LOADING){
            ToastUtils.show("正在加载中，请稍等");
            return false;
        }
        else if (mStatus == STATUS_ERROR){
            //点击重试
            mStatus = STATUS_LOADING;
            onDraw(mPageView.getNextPage());
            mPageView.invalidate();
            loadCurrentChapter();
            return false;
        }

        //判断是否达到章节的终止点
        TPage nextPage = getNextPage();
        if (nextPage == null){
            if (!nextChapter()){
                return false;
            }
            else {
                onDraw(mPageView.getCurPage());
                mCancelPage = mCurPage;
                mCurPage = getCurPage(0);
                onDraw(mPageView.getNextPage());
                return true;
            }
        }

        onDraw(mPageView.getCurPage());
        mCancelPage = mCurPage;
        mCurPage = nextPage;
        onDraw(mPageView.getNextPage());
        return true;
    }

    //装载下一章节的内容
    private boolean nextChapter(){
        if (mCurChapter + 1 >= mCategoryList.size()){
            ToastUtils.show("已经没有下一章了");
            return false;
        }
        else {
            int nextChapter = mCurChapter + 1;
            //装载下一章
            List<TPage> tPages = createPageList(nextChapter);
            mLastChapter = mCurChapter;
            mCurChapter = nextChapter;
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
                onDraw(mPageView.getNextPage());
                mPageView.invalidate();
                loadCurrentChapter();
                return false;
            }
        }
    }

    //取消翻页 (这个cancel有点歧义，指的是不需要看的页面)
    public void pageCancel(){
        //加载到下一页取消了
        if (mCurPage.position == 0 && mCurChapter > mLastChapter){
            prevChapter();
        }
        //加载上一页取消了
        else if (mCurPage.position == mPageList.size()-1 && mCurChapter < mLastChapter){
            nextChapter();
        }
        //假设加载到下一页了，又取消了。那么需要重新装载的问题
        mCurPage = mCancelPage;
    }

    //清除记录，并设定是否缓存数据
    public void closeBook(boolean isCache){
        //关闭书籍，必须先打开书籍
        if (!isBookOpen) return;

        if (isCache){
            saveRecord();
        }
        else {
            //清空缓存文件
            BookRepository.getInstance()
                    .deleteBook(mBookId);
        }
    }

    /**
     * 存储记录
     */
    public void saveRecord(){
        //书没打开，就没有记录
        if (!isBookOpen) return;

        mBookRecord.setBookId(mBookId);
        mBookRecord.setChapter(mCurChapter);
        mBookRecord.setPagePos(mCurPage.position);
        //存储到数据库
        BookRepository.getInstance()
                .saveBookRecord(mBookRecord);
    }

    //跳转到指定章节
    public void skipToChapter(int pos){
        //正在加载
        mStatus = STATUS_LOADING;
        //绘制当前的状态
        mCurChapter = pos;
        if (mCurPage != null){
            //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
            mCurPage.position = 0;
        }
        onDraw(mPageView.getNextPage());
        mPageView.invalidate();
        //提示章节改变，需要下载
        loadCurrentChapter();
    }

    public int skipNextChapter(){
        //判断是否达到章节的终止点
        if (nextChapter()){
            mCurPage = getCurPage(0);
            onDraw(mPageView.getNextPage());
            mPageView.invalidate();
        }
        return mCurChapter;
    }
    //跳转到上一章
    public int skipPreChapter(){
        //载入上一章。
        if (prevChapter()){
            mCurPage = getCurPage(0);
            onDraw(mPageView.getNextPage());
            mPageView.invalidate();
        }
        return mCurChapter;
    }

    public void updateTime(){
        if (mCurPage != null && !mPageView.isRunning()){
            onDraw(mPageView.getNextPage());
            mPageView.invalidate();
        }
    }

    public void updateBattery(int level){
        mBatteryLevel = level;
        if (mCurPage != null && !mPageView.isRunning()){
            onDraw(mPageView.getNextPage());
            mPageView.invalidate();
        }
    }

    private void loadPrevChapter(){
        //提示加载上一章
        if (mPageChangeListener != null){
            //提示加载前面3个章节（不包括当前章节）
            int current = mCurChapter;
            int prev = current - 3;
            if (prev < 0){
                prev = 0;
            }
            mPageChangeListener.onChapterChange(mCategoryList.subList(prev,current),mCurChapter);
        }
    }

    private void loadCurrentChapter(){
        if (mPageChangeListener != null){
            List<BookChapterBean> bookChapters = new ArrayList<>(5);
            //提示加载当前章节和前面两章和后面两章
            int current = mCurChapter;
            bookChapters.add(mCategoryList.get(current));

            //如果当前已经是最后一章，那么就没有必要加载后面章节
            if (current != mCategoryList.size()){
                int begin = current + 1;
                int next = begin + 2;
                if (next > mCategoryList.size()){
                    next = mCategoryList.size();
                }
                bookChapters.addAll(mCategoryList.subList(begin,next));
            }

            //如果当前已经是第一章，那么就没有必要加载前面章节
            if (current != 0){
                int prev = current - 2;
                if (prev < 0){
                    prev = 0;
                }
                bookChapters.addAll(mCategoryList.subList(prev,current));
            }
            mPageChangeListener.onChapterChange(bookChapters,mCurChapter);
        }
    }

    private void loadNextChapter(){
        //提示加载下一章
        if (mPageChangeListener != null){
            //提示加载当前章节和后面3个章节
            int current = mCurChapter + 1;
            int next = mCurChapter + 3;
            if (next > mCategoryList.size()){
                next = mCategoryList.size();
            }
            mPageChangeListener.onChapterChange(mCategoryList.subList(current,next),mCurChapter);
        }
    }

    //在修改字体大小，再进行上翻页到首页的时候，会造成页面空缺的问题
    //TODO:限制TextSize的大小，一直长按可能会造成重绘的卡顿问题
    public void setTextSize(int textSize){
        //设置textSize
        mTextSize = textSize;
        //设置画笔的字体大小
        mTextPaint.setTextSize(mTextSize);
        //存储状态
        mSettingManager.setTextSize(mTextSize);
        //重新计算LineCount
        calculateLineCount();
        //重新计算页面
        mPageList = createPageList(mCurChapter);
        //重新设置文章指针的位置
        mCurPage = getCurPage(mCurPage.position);
        //绘制
        onDraw(mPageView.getNextPage());
        //通知重绘
        mPageView.invalidate();
    }

    public void setNightMode(boolean nightMode){
        isNightMode = nightMode;
        if (isNightMode){
            mBatteryPaint.setColor(Color.WHITE);
            setBgColor(ReadSettingManager.NIGHT_MODE);
        }
        else {
            mBatteryPaint.setColor(Color.BLACK);
            setBgColor(mBgTheme);
        }
        mSettingManager.setNightMode(nightMode);
    }

    //绘制背景
    public void setBgColor(int theme){
        if (isNightMode && theme == ReadSettingManager.NIGHT_MODE){
            mTextColor = ContextCompat.getColor(App.getContext(), R.color.nb_read_font_night);
            mPageBg = ContextCompat.getColor(App.getContext(), R.color.nb_read_bg_night);
        }
        else if (isNightMode){
            mBgTheme = theme;
            mSettingManager.setReadBackground(theme);
        }
        else {
            mSettingManager.setReadBackground(theme);
            switch (theme){
                case ReadSettingManager.READ_BG_DEFAULT:
                    mTextColor = ContextCompat.getColor(App.getContext(),R.color.nb_read_font_1);
                    mPageBg = ContextCompat.getColor(App.getContext(), R.color.nb_read_bg_1);
                    break;
                case ReadSettingManager.READ_BG_1:
                    mTextColor = ContextCompat.getColor(App.getContext(),R.color.nb_read_font_2);
                    mPageBg = ContextCompat.getColor(App.getContext(), R.color.nb_read_bg_2);
                    break;
                case ReadSettingManager.READ_BG_2:
                    mTextColor = ContextCompat.getColor(App.getContext(),R.color.nb_read_font_3);
                    mPageBg = ContextCompat.getColor(App.getContext(), R.color.nb_read_bg_3);
                    break;
                case ReadSettingManager.READ_BG_3:
                    mTextColor = ContextCompat.getColor(App.getContext(),R.color.nb_read_font_4);
                    mPageBg = ContextCompat.getColor(App.getContext(), R.color.nb_read_bg_4);
                    break;
                case ReadSettingManager.READ_BG_4:
                    mTextColor = ContextCompat.getColor(App.getContext(),R.color.nb_read_font_5);
                    mPageBg = ContextCompat.getColor(App.getContext(), R.color.nb_read_bg_5);
                    break;
            }
        }

        if (isBookOpen){
            //设置参数
            mPageView.setBgColor(mPageBg);
            mTextPaint.setColor(mTextColor);
            //重绘
            onDraw(mPageView.getNextPage());
            mPageView.invalidate();
        }
    }

    public void setPageMode(int pageMode){
        mPageMode = pageMode;
        mPageView.setPageMode(mPageMode);
        mSettingManager.setPageMode(mPageMode);
    }

    public int getPageStatus(){
        return mStatus;
    }

    public int getPosition(){
        return mCurChapter;
    }

    public void skipToPage(int pos){
        mCurPage = getCurPage(pos);
        onDraw(mPageView.getNextPage());
        mPageView.invalidate();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener){
        mPageChangeListener = listener;
    }

    public interface OnPageChangeListener{
        //pos:表示下一章节
        void onChapterChange(List<BookChapterBean> beanList,int pos);
        //页码改变
        void onPageListChange(int count);
        //页面改变
        void onPageChange(int pos);
    }

    //创建临时的页面类
    private class TPage{
        int position;
        String title;
        List<String> lines;
    }
}

