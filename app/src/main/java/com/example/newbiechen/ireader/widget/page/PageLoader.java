package com.example.newbiechen.ireader.widget.page;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.BookRecordBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.local.ReadSettingManager;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.IOUtils;
import com.example.newbiechen.ireader.utils.ScreenUtils;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-7-1.
 */

public abstract class PageLoader{
    private static final String TAG = "PageLoader";

    //默认的显示参数配置
    private static final int DEFAULT_INTERVAL = 10;
    private static final int DEFAULT_MARGIN_HEIGHT = 30;
    private static final int DEFAULT_MARGIN_WIDTH = 15;
    private static final int DEFAULT_TIP_SIZE = 12;

    //当前页面的状态
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_FINISH = 2;
    public static final int STATUS_ERROR = 3;
    public static final int STATUS_EMPTY = 4;
    public static final int STATUS_PARSE = 5;
    public static final int STATUS_PARSE_ERROR = 6;

    //页面显示类
    protected PageView mPageView;
    //绘制小说内容的画笔
    protected Paint mTextPaint;
    protected TxtPage mCurPage;
    protected List<TxtChapter> mChapterList;
    protected List<TxtPage> mPageList;
    protected CollBookBean mCollBook;
    //监听器
    protected OnPageChangeListener mPageChangeListener;

    private Paint mBatteryPaint;
    private Paint mBgPaint;
    private ReadSettingManager mSettingManager;

    //被遮盖的页，或者认为被取消显示的页
    private TxtPage mCancelPage;
    private BookRecordBean mBookRecord;
    /*****************params**************************/
    //当前的状态
    protected int mStatus = STATUS_LOADING;
    //当前章
    protected int mCurChapterPos = 0;
    //上一章
    protected int mLastChapter = 0;
    protected int mLineCount;
    protected boolean isBookOpen = false;
    //书籍绘制区域的宽高
    protected int mVisibleWidth;
    protected int mVisibleHeight;
    //应用的宽高
    private int mDisplayWidth;
    private int mDisplayHeight;
    //间距
    private int mMarginWidth;
    private int mMarginHeight;

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


    /*****************************init params*******************************/
    public PageLoader(PageView pageView){
        mPageView = pageView;

        //初始化数据
        initData();
        //初始化画笔
        initPaint();
        //初始化PageView
        initPageView();
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

        //初始化参数
        mMarginWidth = ScreenUtils.dpToPx(DEFAULT_MARGIN_WIDTH);
        mMarginHeight = ScreenUtils.dpToPx(DEFAULT_MARGIN_HEIGHT);
        mIntervalSize = ScreenUtils.dpToPx(DEFAULT_INTERVAL);
    }

    private void initPaint(){
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.LEFT);//绘制的起始点
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setSubpixelText(true);
        mBgPaint = new Paint();
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

    private void initPageView(){
        //配置参数
        mPageView.setPageMode(mPageMode);
        mPageView.setBgColor(mPageBg);
    }

    //计算每页的行数
    private void calculateLineCount(){
        //这里需要加上intervalSize 是为保证当达到最后一行的时候没有间距
        mLineCount = (mVisibleHeight+mIntervalSize) /(mIntervalSize + mTextSize);
    }

    /****************************** public method***************************/

    //跳转到上一章
    public int skipPreChapter(){
        //载入上一章。
        if (prevChapter()){
            mCurPage = getCurPage(0);
            onDraw(mPageView.getNextPage(),false);
        }
        return mCurChapterPos;
    }
    //跳转到下一章
    public int skipNextChapter(){

        //判断是否达到章节的终止点
        if (nextChapter()){
            mCurPage = getCurPage(0);
            onDraw(mPageView.getNextPage(),false);
        }
        return mCurChapterPos;
    }
    //跳转到具体的页
    public void skipToPage(int pos){
        mCurPage = getCurPage(pos);
        onDraw(mPageView.getNextPage(),false);
    }
    //自动翻到上一章
    public void autoPrevPage(){
        mPageView.autoPrevPage();
    }
    //自动翻到下一章
    public void autoNextPage(){
        mPageView.autoNextPage();
    }
    //更新时间
    public void updateTime(){
        if (!mPageView.isRunning() && mPageView.getNextPage() != null){
            onDraw(mPageView.getNextPage(),true);
        }
    }
    //更新电量
    public void updateBattery(int level){
        mBatteryLevel = level;
        if (!mPageView.isRunning() && mPageView.getNextPage() != null){
            onDraw(mPageView.getNextPage(),true);
        }
    }

    //TODO:需要限制TextSize的大小，一直长按可能会造成重绘的卡顿问题
    //设置文字大小
    public void setTextSize(int textSize){
        if (!isBookOpen) return;

        //设置textSize
        mTextSize = textSize;
        //设置画笔的字体大小
        mTextPaint.setTextSize(mTextSize);
        //存储状态
        mSettingManager.setTextSize(mTextSize);
        //重新计算LineCount
        calculateLineCount();
        //重新计算页面
        mPageList = loadPageList(mCurChapterPos);

        //防止在最后一页，通过修改字体，以至于页面数减少导致崩溃的问题
        if (mCurPage.position >= mPageList.size()){
            mCurPage.position = mPageList.size() - 1;
        }

        //重新设置文章指针的位置
        mCurPage = getCurPage(mCurPage.position);
        //绘制
        onDraw(mPageView.getNextPage(),false);
        //通知重绘
    }
    //设置夜间模式
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
            onDraw(mPageView.getNextPage(),false);
        }
    }
    //翻页动画
    public void setPageMode(int pageMode){
        mPageMode = pageMode;
        mPageView.setPageMode(mPageMode);
        mSettingManager.setPageMode(mPageMode);
    }
    //设置页面切换监听
    public void setOnPageChangeListener(OnPageChangeListener listener){
        mPageChangeListener = listener;
    }
    //获取当前页的状态
    public int getPageStatus(){
        return mStatus;
    }
    //获取当前章节的章节位置
    public int getChapterPos(){
        return mCurChapterPos;
    }
    //获取当前页的页码
    public int getPagePos(){
        return mCurPage.position;
    }
    //保存阅读记录
    public void saveRecord(){
        //书没打开，就没有记录
        if (!isBookOpen) return;

        mBookRecord.setBookId(mCollBook.get_id());
        mBookRecord.setChapter(mCurChapterPos);
        mBookRecord.setPagePos(mCurPage.position);

        //存储到数据库
        BookRepository.getInstance()
                .saveBookRecord(mBookRecord);
    }
    //打开书本，初始化书籍
    public void openBook(CollBookBean collBook){
        mCollBook = collBook;
        //init book record
        mBookRecord = BookRepository.getInstance()
                .getBookRecord(mCollBook.get_id());
        if (mBookRecord == null){
            mBookRecord = new BookRecordBean();
        }

        mCurChapterPos = mBookRecord.getChapter();
        mLastChapter = mCurChapterPos;
    }
    //打开具体章节
    public void openChapter(){
        //加载完成
        mStatus = STATUS_FINISH;

        mPageList = loadPageList(mCurChapterPos);
        //获取制定页面
        if (!isBookOpen){
            isBookOpen = true;
            //可能会出现当前页的大小大于记录页的情况。
            int position = mBookRecord.getPagePos();
            if (position >= mPageList.size()){
                position = mPageList.size() - 1;
            }
            mCurPage = getCurPage(position);

            if (mPageChangeListener != null){
                mPageChangeListener.onChapterChange(mCurChapterPos);
            }
        }
        else {
            mCurPage = getCurPage(0);
        }
        onDraw(mPageView.getNextPage(),false);
    }
    //清除记录，并设定是否缓存数据
    public void closeBook(){
        isBookOpen = false;
        mPageView = null;
    }

    /*******************************abstract method***************************************/

    //跳转到指定章节
    public abstract void skipToChapter(int pos);
    //设置章节
    public abstract void setChapterList(List<BookChapterBean> bookChapters);
    //章节加载错误
    public abstract void chapterError();
    //TODO:十分耗时的操作，需要进行优化
    @Nullable
    protected abstract List<TxtPage> loadPageList(int chapter);

    protected abstract boolean prevChapter();

    protected abstract boolean nextChapter();


    /***********************************default method***********************************************/
    //通过流获取Page的方法
    List<TxtPage> loadPages(TxtChapter chapter, BufferedReader br){
        //读取数据段
        List<TxtPage> pages = new ArrayList<>();
        //使用流的方式加载
        List<String> lines = new ArrayList<>();
        Reader reader = null;
        String paragraph = null;
        try {
            while ((paragraph = br.readLine()) != null){
                paragraph = paragraph.replaceAll("\\s", "");
                //如果只有换行符，那么就不执行
                if (paragraph.equals("")) continue;

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
                        TxtPage page = new TxtPage();
                        page.position = pages.size();
                        page.title = chapter.getTitle();
                        page.lines = new ArrayList<>(lines);
                        pages.add(page);
                        //重置Lines
                        lines.clear();
                    }
                }
            }

            if (lines.size() != 0){
                //创建Page
                TxtPage page = new TxtPage();
                page.position = pages.size();
                page.title = chapter.getTitle();
                page.lines = new ArrayList<>(lines);
                pages.add(page);
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
            TxtPage page = new TxtPage();
            page.lines = new ArrayList<>(1);
            pages.add(page);

            mStatus = STATUS_EMPTY;
        }

        //提示章节数量改变了。
        if (mPageChangeListener != null){
            mPageChangeListener.onPageCountChange(pages.size());
        }
        return pages;
    }

    void onDraw(Bitmap bitmap,boolean isUpdate){
        Canvas canvas = new Canvas(bitmap);
        int tipMarginHeight = ScreenUtils.dpToPx(3);
        if (!isUpdate){
            /****绘制背景****/
            canvas.drawColor(mPageBg);
            /******绘制内容****/
            //内容比标题先绘制的原因是，内容的字体大小和标题还有底部的字体大小不相同
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
                    case STATUS_PARSE:
                        tip = "正在排版请等待...";
                        break;
                    case STATUS_PARSE_ERROR:
                        tip = "文件解析错误";
                        break;
                }

                //将提示语句放到正中间
                Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                float textHeight = fontMetrics.top - fontMetrics.bottom;
                float textWidth = mTextPaint.measureText(tip);
                float pivotX = (mDisplayWidth - textWidth)/2;
                float pivotY = (mDisplayHeight - textHeight)/2;
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

            /*****初始化标题的参数********/
            //需要注意的是:绘制text的y的起始点是text的基准线的位置，而不是从text的头部的位置
            mTextPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_TIP_SIZE));
            float tipTop = tipMarginHeight - mTextPaint.getFontMetrics().top;
            //根据状态不一样，数据不一样
            if (mStatus != STATUS_FINISH){
                if(mChapterList != null && mChapterList.size() != 0){
                    canvas.drawText(mChapterList.get(mCurChapterPos).getTitle()
                            , mMarginWidth, tipTop, mTextPaint);
                }
            }
            else {
                canvas.drawText(mCurPage.title, mMarginWidth, tipTop, mTextPaint);
            }
            /******绘制页码********/
            //底部的字显示的位置Y
            float y = mDisplayHeight - mTextPaint.getFontMetrics().bottom - tipMarginHeight;
            //只有finish的时候采用页码
            if (mStatus == STATUS_FINISH){
                String percent = (mCurPage.position+1) + "/" + mPageList.size();
                canvas.drawText(percent, mMarginWidth, y, mTextPaint);
            }
        }
        else {
            //擦除区域
            mBgPaint.setColor(mPageBg);
            canvas.drawRect(mDisplayWidth/2,mDisplayHeight - mMarginHeight,mDisplayWidth,mDisplayHeight,mBgPaint);
        }
        mTextPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_TIP_SIZE));

        /******绘制电池********/
        int visibleRight = mDisplayWidth - mMarginWidth;
        int visibleBottom = mDisplayHeight - tipMarginHeight;

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

        //底部的字显示的位置Y
        float y = mDisplayHeight - mTextPaint.getFontMetrics().bottom - tipMarginHeight;
        String time = StringUtils.dateConvert(System.currentTimeMillis(), Constant.FORMAT_TIME);
        float x = outFrameLeft - mTextPaint.measureText(time) - ScreenUtils.dpToPx(4);
        canvas.drawText(time,x,y,mTextPaint);
        //重置字体大小
        mTextPaint.setTextSize(mTextSize);

        //更新绘制
        mPageView.invalidate();
    }

    void setDisplaySize(int w,int h){
        //获取PageView的宽高
        mDisplayWidth = w;
        mDisplayHeight = h;
        //获取内容显示位置的大小
        mVisibleWidth = mDisplayWidth - mMarginWidth * 2;
        mVisibleHeight = mDisplayHeight - mMarginHeight * 2;

        //计算行数
        calculateLineCount();

        //如果章节已显示，那么就重新计算页面
        if (mStatus == STATUS_FINISH){
            mPageList = loadPageList(mCurChapterPos);
            //重新设置文章指针的位置
            mCurPage = getCurPage(mCurPage.position);
        }

        //绘制
        onDraw(mPageView.getNextPage(),false);
    }

    //翻阅上一页
    boolean prev(){
        if (!checkStatus()) return false;

        //判断是否达到章节的起始点
        TxtPage prevPage = getPrevPage();
        if (prevPage == null){
            //载入上一章。
            if (!prevChapter()){
                return false;
            }
            else {
                onDraw(mPageView.getCurPage(),false);
                mCancelPage = mCurPage;
                mCurPage = getPrevLastPage();
                onDraw(mPageView.getNextPage(),false);
                return true;
            }
        }

        onDraw(mPageView.getCurPage(),false);
        mCancelPage = mCurPage;
        mCurPage = prevPage;
        onDraw(mPageView.getNextPage(),false);//nextPage:指的是准备看的下一页，无关prev还是next
        return true;
    }
    //翻阅下一页
    boolean next(){
        if (!checkStatus()) return false;

        //判断是否达到章节的终止点
        TxtPage nextPage = getNextPage();
        if (nextPage == null){
            if (!nextChapter()){
                return false;
            }
            else {
                onDraw(mPageView.getCurPage(),false);
                mCancelPage = mCurPage;
                mCurPage = getCurPage(0);
                onDraw(mPageView.getNextPage(),false);
                return true;
            }
        }

        onDraw(mPageView.getCurPage(),false);
        mCancelPage = mCurPage;
        mCurPage = nextPage;
        onDraw(mPageView.getNextPage(),false);
        return true;
    }

    //取消翻页 (这个cancel有点歧义，指的是不需要看的页面)
    void pageCancel(){
        //加载到下一章取消了
        if (mCurPage.position == 0 && mCurChapterPos > mLastChapter){
            prevChapter();
        }
        //加载上一章取消了
        else if (mCurPage.position == mPageList.size()-1 && mCurChapterPos < mLastChapter){
            nextChapter();
        }
        //假设加载到下一页了，又取消了。那么需要重新装载的问题
        mCurPage = mCancelPage;
    }

    /**
     * @return:获取初始显示的页面
     */
    TxtPage getCurPage(int pos){
        if (mPageChangeListener != null){
            mPageChangeListener.onPageChange(pos);
        }
        return mPageList.get(pos);
    }


    /**************************************private method********************************************/

    /**
     * @return:获取上一个页面
     */
    private TxtPage getPrevPage(){
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
     * @return:获取下一的页面
     */
    private TxtPage getNextPage(){
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
     * @return:获取上一个章节的最后一页
     */
    private TxtPage getPrevLastPage(){
        int pos = mPageList.size() - 1;
        return mPageList.get(pos);
    }

    private boolean checkStatus(){
        if (mStatus == STATUS_LOADING){
            ToastUtils.show("正在加载中，请稍等");
            return false;
        }
        else if (mStatus == STATUS_ERROR){
            //点击重试
            mStatus = STATUS_LOADING;
            onDraw(mPageView.getNextPage(),false);
            return false;
        }
        //由于解析失败，让其退出
        return true;
    }
    //翻阅下一页

    /*****************************************ineterface*****************************************/

    public interface OnPageChangeListener{
        void onChapterChange(int pos);
        //请求加载回调
        void onLoadChapter(List<TxtChapter> chapters,int pos);
        //当目录加载完成的回调(必须要在创建的时候，就要存在了)
        void onCategoryFinish(List<TxtChapter> chapters);
        //页码改变
        void onPageCountChange(int count);
        //页面改变
        void onPageChange(int pos);
    }
}
