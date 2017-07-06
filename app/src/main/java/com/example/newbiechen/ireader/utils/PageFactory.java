package com.example.newbiechen.ireader.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.BookRecordBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.local.ReadSettingManager;
import com.example.newbiechen.ireader.widget.page.PageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-5-19.
 * 已弃用，仅供参考,最新见SimplePageFactory。
 */

public class PageFactory {
    private static final String TAG = "PageFactory";

    private static final int DEFAULT_INTERVAL = 10;
    private static final int DEFAULT_MARGIN_HEIGHT = 30;
    private static final int DEFAULT_MARGIN_WIDTH = 15;
    private static final int DEFAULT_TIP_SIZE = 12;

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_FINISH = 2;
    public static final int STATUS_ERROR = 3;
    public static final int STATUS_EMPTY = 4;

    private static PageFactory sInstance;
    //页面显示类
    private PageView mPageView;
    //章节数据管理类
    private BookManager mBookManager;
    //绘制小说内容的画笔
    private Paint mTextPaint;
    private Paint mBatteryPaint;
    private ReadSettingManager mSettingManager;
    private OnPageChangeListener mPageChangeListener;
    //被遮盖的页，或者认为被取消显示的页
    private TPage mCancelPage;
    //当前页
    private TPage mCurPage;
    private DecimalFormat mPerFormat = new DecimalFormat("#00.00");
    private List<BookChapterBean> mCategoryList;
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

    private int mCurChapter = 0;
    private int mLineCount;
    private boolean isBookOpen = false;

    public static PageFactory getInstance(){
        if (sInstance == null){
            synchronized (PageFactory.class){
                if (sInstance == null){
                    sInstance = new PageFactory();
                }
            }
        }
        return sInstance;
    }

    private PageFactory(){
        mBookManager = BookManager.getInstance();
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
        //初始化正在加载
        mStatus = STATUS_LOADING;
        //绘制当前的状态
        drawStatus(mPageView.getCurPage());
        drawStatus(mPageView.getNextPage());
    }

    private void initPageView(){
        mPageView.setPageMode(mPageMode);
        mPageView.setBgColor(mPageBg);
        //当前状态变成Loading
        mStatus = STATUS_LOADING;
    }

    private void drawStatus(Bitmap bitmap){
        //获取画笔
        Canvas canvas = new Canvas(bitmap);
        //获取背景
        canvas.drawColor(mPageBg);
        //
        String tip = "";
        switch (mStatus){
            case STATUS_LOADING:
                tip = "正在拼命加载中...";
                break;
            case STATUS_ERROR:
                tip = "很抱歉,加载失败...";
                break;
            case STATUS_EMPTY:
                tip = "返回文章内容为空";
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

        //提示加载下面的章节
        loadCurrentChapter();
    }

    public void startRead(){
        mStatus = STATUS_FINISH;
        if (!isBookOpen){
            //打开具体章节，并且跳转到指定位置
            mBookManager.openChapter(mBookId,
                    mCategoryList.get(mBookRecord.getChapter()).getTitle());
/*            //创建currentPage对象
            mCurPage = getCurPage(mBookRecord.getStart());*/

            isBookOpen = true;
        }
        else {
            //打开之前未加载完成的章节
            mBookManager.openChapter(mBookId, mCategoryList.get(mCurChapter).getTitle());
            mCurPage = getCurPage(0);
        }
        onDraw(mPageView.getNextPage(),mCurPage);
        //重绘
        mPageView.invalidate();
    }

    private void onDraw(Bitmap bitmap,TPage page){
        Canvas canvas = new Canvas(bitmap);

        //可能出现lines没数据的情况
        if (page.lines.size() == 0){
            int status = mStatus;
            mStatus = STATUS_EMPTY;
            drawStatus(bitmap);
            mStatus = status;
            return;
        }

        /****绘制背景****/
        canvas.drawColor(mPageBg);
        /******绘制内容****/
        float top = mMarginHeight - mTextPaint.getFontMetrics().top;
        int interval = mIntervalSize + (int) mTextPaint.getTextSize();
        for (int i=0; i<page.lines.size(); ++i){
            canvas.drawText(page.lines.get(i),mMarginWidth,top, mTextPaint);
            top += interval;
        }

        //需要注意的是:绘制text的y的起始点是text的基准线的位置，而不是从text的头部的位置
        int tipMarginHeight = ScreenUtils.dpToPx(3);
        /******绘制标题********/
        mTextPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_TIP_SIZE));
        float tipTop = tipMarginHeight - mTextPaint.getFontMetrics().top;
        canvas.drawText(page.title, mMarginWidth, tipTop, mTextPaint);

        /******绘制百分比********/
        float y = mAppHeight - mTextPaint.getFontMetrics().bottom - tipMarginHeight;
        String percent = mPerFormat.format(page.fraction) + "%";
        canvas.drawText(percent, mMarginWidth, y, mTextPaint);
        /******绘制电池********/

        int visibleRight = mAppWidth - mMarginWidth;
        int visibleBottom = mAppHeight - tipMarginHeight;

        int outFrameWidth = (int) mTextPaint.measureText("102");
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
     * @param begin:书的起始位置。
     * @return:获取初始显示的页面
     */
    private TPage getCurPage(long begin){
        mBookManager.setPosition(begin);

        TPage tPage = new TPage();
        tPage.begin = begin; //起始字节
        tPage.lines = getNextLines();
        tPage.end = mBookManager.getPosition(); //下个位置的起始点
        tPage.title = mCategoryList.get(mCurChapter).getTitle();
        tPage.fraction = (tPage.begin / (1.0f * mBookManager.getChapterLen())) * 100;
        return tPage;
    }

    /**
     * @return:获取下一的页面
     */
    private TPage getNextPage(){
        mBookManager.setPosition(mCurPage.end); //调回到当前页面的起始点

        TPage tPage = new TPage();
        tPage.begin = mCurPage.end;
        tPage.lines = getNextLines();
        tPage.end = mBookManager.getPosition(); //下个位置的起始点
        tPage.title = mCategoryList.get(mCurChapter).getTitle();
        tPage.fraction = (tPage.begin / (1.0f * mBookManager.getChapterLen())) * 100;
        return tPage;
    }

    /**
     * @return:获取上一个页面
     */
    private TPage getPrevPage(){
        mBookManager.setPosition(mCurPage.begin-1); //调回到当前页的结束点

        TPage tPage = new TPage();
        tPage.end = mCurPage.begin; //下一页面的起始点
        tPage.lines = getPrevLines();
        if (mBookManager.getPosition() <= 0){
            tPage.begin = 0; //特殊情况,因为此时pos为0，而不会指向下一个字节的位置
        }
        else {
            tPage.begin = mBookManager.getPosition()+1; //当前页面的起始点
        }
        tPage.title = mCategoryList.get(mCurChapter).getTitle();
        tPage.fraction = (tPage.begin / (1.0f * mBookManager.getChapterLen())) * 100;
        return tPage;
    }

    /**
     * @return:获取上一个章节的最后一页
     */
    private TPage getPrevLastPage(){
        TPage tPage = new TPage();
        tPage.end = mBookManager.getChapterLen();//下一页面的起始点
        tPage.lines = getPrevLastLines();
        tPage.begin = mBookManager.getPosition(); //当前页面的起始点

        tPage.title = mCategoryList.get(mCurChapter).getTitle();
        tPage.fraction = (tPage.begin / (1.0f * mBookManager.getChapterLen())) * 100;
        return tPage;
    }

    //获取上一页的内容
    private List<String> getPrevLines(){
        List<String> lines = new ArrayList<>();
        String paragraph = null;
        while((paragraph = mBookManager.getPrevPara()) != null){
            paragraph.replaceAll("\n", "");
            List<String> paraLines = new ArrayList<>();
            while (paragraph.length() > 0){
                //测量一行占用的字节数
                int count = mTextPaint.breakText(paragraph, true, mVisibleWidth, null);
                //将一行字节，存储到lines中
                paraLines.add(paragraph.substring(0, count));
                //裁剪
                paragraph = paragraph.substring(count);
            }
            boolean isFinish = false;
            for (int i=paraLines.size()-1; i>=0;--i){
                if (lines.size() < mLineCount){
                    lines.add(0,paraLines.get(i));
                }
                else {
                    //还原到未利用的位置
                    isFinish = true;
                    mBookManager.setPosition(mBookManager.getPosition() + paraLines.get(i).length());
                }
            }
            if (isFinish){
                break;
            }
        }
        return lines;
    }

    //获取下一页的内容
    private List<String> getNextLines(){
        List<String> lines = new ArrayList<>();
        String paragraph = null;
        while((paragraph = mBookManager.getNextPara()) != null){
            paragraph.replaceAll("\n", "");
            while (lines.size() < mLineCount && paragraph.length() > 0){
                //测量一行占用的字节数
                int count = mTextPaint.breakText(paragraph, true, mVisibleWidth, null);
                //将一行字节，存储到lines中
                lines.add(paragraph.substring(0, count));
                //裁剪
                paragraph = paragraph.substring(count);
            }
            //达到行数要求，退出
            if (lines.size() == mLineCount){
                if(paragraph.length() != 0){
                    //还原到未利用的位置
                    mBookManager.setPosition(mBookManager.getPosition() - paragraph.length());
                }
                break;
            }
        }
        return lines;
    }

    //获取上一章的最后一页的内容
    private List<String> getPrevLastLines(){
        List<String> lines = new ArrayList<>();
        String paragraph = null;
        while((paragraph = mBookManager.getNextPara()) != null){
            paragraph.replaceAll("\n", "");
            while (paragraph.length() > 0){
                //测量一行占用的字节数
                int count = mTextPaint.breakText(paragraph, true, mVisibleWidth, null);
                //将一行字节，存储到lines中
                lines.add(paragraph.substring(0, count));
                //裁剪
                paragraph = paragraph.substring(count);

                //加载完一页清除当前页的数据
                if (lines.size() == mLineCount){
                    //如果为最后一段，并且最后一段已经完全显示在lines中的时候，就不清除
                    //有可能最后一页占满的情况
                    if (mBookManager.getPosition() == mBookManager.getChapterLen()
                            && paragraph.length() == 0){
                    }
                    else {
                        lines.clear();
                    }
                }
            }
        }
        //回退指针
        int size = 0;
        for (String str : lines){
            size += str.length();
        }
        mBookManager.setPosition(mBookManager.getPosition() - size);
        return lines;
    }

    //翻阅上一页
    public boolean prev(){
        if (mStatus != STATUS_FINISH){
            ToastUtils.show("正在加载中，请稍等");
            return false;
        }

        //判断是否达到章节的起始点
        if (mCurPage.begin == 0){
            //载入上一章。
            if (!prevChapter()){
                return false;
            }
            else {
                onDraw(mPageView.getCurPage(),mCurPage);
                mCancelPage = mCurPage;
                mCurPage = getPrevLastPage();
                onDraw(mPageView.getNextPage(),mCurPage);
                return true;
            }
        }

        onDraw(mPageView.getCurPage(),mCurPage);
        mCancelPage = mCurPage;
        mCurPage = getPrevPage();
        onDraw(mPageView.getNextPage(),mCurPage);//nextPage:指的是准备看的下一页，无关prev还是next
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
            boolean installSucceed = mBookManager.
                    openChapter(mBookId,mCategoryList.get(prevChapter).getTitle());

            if (installSucceed){
                mCurChapter = prevChapter;
                //提示章节改变，缓冲接下来的章节
                loadPrevChapter();
                return true;
            }
            else {
                ToastUtils.show("正在加载中，请稍等");
                return false;
            }
        }
    }

    //翻阅下一页
    public boolean next(){
        if (mStatus != STATUS_FINISH){
            ToastUtils.show("正在加载中，请稍等");
            return false;
        }

        //判断是否达到章节的终止点
        if (mBookManager.getChapterLen() == mCurPage.end){
            if (!nextChapter()){
                return false;
            }
            else {
                onDraw(mPageView.getCurPage(),mCurPage);
                mCancelPage = mCurPage;
                mCurPage = getCurPage(0);
                onDraw(mPageView.getNextPage(),mCurPage);
                return true;
            }
        }
        else {
            onDraw(mPageView.getCurPage(),mCurPage);
            mCancelPage = mCurPage;
            mCurPage = getNextPage();
            onDraw(mPageView.getNextPage(),mCurPage);
            return true;
        }
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
            boolean nextExist = mBookManager
                    .openChapter(mBookId,mCategoryList.get(nextChapter).getTitle());

            if (nextExist){
                mCurChapter = nextChapter;
                //提示章节改变，缓冲接下来的章节
                loadNextChapter();
                return true;
            }
            else {
                ToastUtils.show("正在加载中，请稍等");
                return false;
            }
        }
    }

    //取消翻页 (这个cancel有点歧义，指的是不需要看的页面)
    public void pageCancel(){
        if (mCurPage.begin == 0 && mCancelPage.begin != mCurPage.end){ //是否下翻了一章 （没有考虑到，第二页翻到第一页时候取消的情况）
            prevChapter();//取消的时候，回退到上一章
            //恢复position
            mBookManager.setPosition(mCancelPage.end);
        }
        else if (mCurPage.end == mBookManager.getChapterLen()
                && mCancelPage.end != mCurPage.begin){//是否向上翻了一章 (没有考虑到，倒数第二章翻到的最后一章，再取消)
            nextChapter();//取消的时候，回退到下一章
            //恢复position
            mBookManager.setPosition(mCancelPage.begin);
        }
        //假设加载到下一页了，又取消了。那么需要重新装载的问题
        mCurPage = mCancelPage;
    }

    //清除使用，并设定是否缓存数据
    public void clear(boolean isCache){

        if (isCache){
            saveRecord();
        }
        else {
            //清空缓存文件
            BookRepository.getInstance()
                    .deleteBook(mBookId);
        }

        //总感觉没必要用单例 0 0...算了就这样了
        isBookOpen = false;
        mBookRecord = null;
        mPageView = null;
        mCurPage = null;
        mCurPage = null;
        mCategoryList.clear();
        mBookManager.clear();
    }

    public void saveRecord(){
        mBookRecord.setBookId(mBookId);
        mBookRecord.setChapter(mCurChapter);
        //BookRecordBean的变量被替换，弃用
        /*        mBookRecord.setStart(mCurPage.begin);
        mBookRecord.setEnd(mCurPage.end);*/

        BookRepository.getInstance()
                .saveBookRecord(mBookRecord);
    }

    //跳转到指定章节
    public void skipToChapter(int pos){
        //初始化正在加载
        mStatus = STATUS_LOADING;
        //绘制当前的状态
        drawStatus(mPageView.getCurPage());
        drawStatus(mPageView.getNextPage());
        mPageView.invalidate();

        mCurChapter = pos;
        //提示章节改变，需要下载
        loadCurrentChapter();
    }

    public void skipNextChapter(){
        //判断是否达到章节的终止点
        if (nextChapter()){
            mCurPage = getCurPage(0);
            onDraw(mPageView.getNextPage(),mCurPage);
            mPageView.invalidate();
        }
    }
    //跳转到上一章
    public void skipPreChapter(){
        //载入上一章。
        if (prevChapter()){
            mCurPage = getCurPage(0);
            onDraw(mPageView.getNextPage(),mCurPage);
            mPageView.invalidate();
        }
    }

    public void updateTime(){
        if (mCurPage != null && !mPageView.isRunning()){
            onDraw(mPageView.getNextPage(),mCurPage);
            mPageView.invalidate();
        }
    }

    public void updateBattery(int level){
        mBatteryLevel = level;
        if (mCurPage != null && !mPageView.isRunning()){
            onDraw(mPageView.getNextPage(),mCurPage);
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
    public void setTextSize(int textSize){
        //设置textSize
        mTextSize = textSize;
        //设置画笔的字体大小
        mTextPaint.setTextSize(mTextSize);
        //存储状态
        mSettingManager.setTextSize(mTextSize);
        //重新计算LineCount
        calculateLineCount();
        //重新设置文章指针的位置
        mCurPage = getCurPage(mCurPage.begin);
        //绘制
        onDraw(mPageView.getNextPage(),mCurPage);
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
            mTextColor = ContextCompat.getColor(App.getContext(),R.color.nb_read_font_night);
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
            onDraw(mPageView.getNextPage(),mCurPage);
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

    public void setOnPageChangeListener(OnPageChangeListener listener){
        mPageChangeListener = listener;
    }

    public interface OnPageChangeListener{
        //pos:表示下一章节
        void onChapterChange(List<BookChapterBean> beanList,int pos);
    }

    //创建临时的页面类
    private class TPage{
        long begin;
        long end;
        float fraction;
        String title;
        List<String> lines;
    }
}
