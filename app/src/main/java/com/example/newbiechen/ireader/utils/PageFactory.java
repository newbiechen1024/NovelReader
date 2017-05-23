package com.example.newbiechen.ireader.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.BookRecordBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.local.ReadSettingManager;
import com.example.newbiechen.ireader.widget.PageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by newbiechen on 17-5-19.
 */

public class PageFactory {
    private static final String TAG = "PageFactory";
    private static final int DEFAULT_LINE_DIVIDER = 12;
    private static final int DEFAULT_PAGE_MARGIN_TOP_BOTTOM = 12;
    private static final int DEFAULT_PAGE_MARGIN_LEFT_RIGHT = 20;

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_FINISH = 2;
    public static final int STATUS_ERROR = 3;

    private static PageFactory sInstance;

    private PageView mPageView;
    private BookManager mBookManager;
    //绘制文字的画笔
    private Paint mTextPaint;
    private ReadSettingManager mSettingManager;
    private OnPageChangeListener mPageChangeListener;
    private TPage mCancelPage;
    private TPage mCurPage;
    private DecimalFormat mPerFormat = new DecimalFormat("#00.00");

    /*****************params**************************/
    private int mStatus = STATUS_LOADING;
    private int mAppWidth;
    private int mAppHeight;
    private int mVisibleWidth;
    private int mVisibleHeight;
    private int mTextColor;
    private int mTextSize;
    private int mPageMode;
    private int mPageBg;
    private int mLineCount;
    //书籍信息
    private String mBookId;
    private List<BookChapterBean> mCategoryList;
    private BookRecordBean mBookRecord;

    private int mCurChapter = 0;

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
        setBgColor(mSettingManager.getReadBgTheme());

        //获取当前App的宽高
        int[] size = ScreenUtils.getAppSize();
        mAppWidth = size[0];
        mAppHeight = size[1];
        mVisibleWidth = mAppWidth - ScreenUtils.dpToPx(DEFAULT_PAGE_MARGIN_LEFT_RIGHT) * 2;
        mVisibleHeight = mAppHeight - ScreenUtils.dpToPx(DEFAULT_PAGE_MARGIN_TOP_BOTTOM) * 2;
        calculateLineCount();
    }

    //计算每页的行数
    private void calculateLineCount(){
        mLineCount = (mVisibleHeight /(ScreenUtils.dpToPx(DEFAULT_LINE_DIVIDER) + mTextSize));
    }

    private void initPaint(){
        //绘制TextPaint
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.LEFT);//绘制的起始点
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setSubpixelText(true);
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
        drawBackground(canvas);
        //
        String tip = "";
        switch (mStatus){
            case STATUS_LOADING:
                tip = "正在拼命加载中...";
                break;
            case STATUS_ERROR:
                tip = "很抱歉,加载失败...";
                break;
        }
        //将提示语句放到正中间
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.top - fontMetrics.bottom;
        float textWidth = mTextPaint.measureText(tip);
        float pivotX = (mAppWidth - textWidth)/2;
        float pivotY = (mAppHeight - textHeight)/2;
        canvas.drawText(tip,pivotX,pivotY,mTextPaint);
    }

    private void drawBackground(Canvas canvas){
        canvas.drawColor(ContextCompat.getColor(App.getContext(),mPageBg));
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

        //提示章节改变
        if (mPageChangeListener != null){
            mPageChangeListener.onChapterChange(mCurChapter);
        }
    }

    public void startRead(){
        mStatus = STATUS_FINISH;

        //打开具体章节，提供章节读取功能。
        mBookManager.openChapter(mBookId,
                mCategoryList.get(mBookRecord.getChapter()).getTitle(),
                mBookRecord.getStart());

        //创建currentPage对象
        mCurPage = getCurPage(mBookRecord.getStart());

        onDraw(mPageView.getNextPage(),mCurPage.lines);
        //重绘
        mPageView.invalidate();
    }

    private void onDraw(Bitmap bitmap,List<String> lines){
        Canvas canvas = new Canvas(bitmap);
        //绘制背景
        drawBackground(canvas);
        //绘制头部

        //绘制内容
        drawContent(canvas,lines);
        //绘制底部
        drawBottom(canvas);
    }

    private void drawContent(Canvas canvas, List<String> lines){
        int top = 0;
        int left = ScreenUtils.dpToPx(DEFAULT_PAGE_MARGIN_LEFT_RIGHT);
        int interval = ScreenUtils.dpToPx(DEFAULT_LINE_DIVIDER) + mTextSize;

        //绘制内容
        for (int i=0; i<lines.size(); ++i){
            top += interval;
            canvas.drawText(lines.get(i),left,top,mTextPaint);
        }

    }

    private void drawBottom(Canvas canvas){
        //绘制当前进度
        int x = ScreenUtils.dpToPx(DEFAULT_PAGE_MARGIN_LEFT_RIGHT);
        int y = mAppHeight - mTextSize;
        float fraction = (mCurPage.begin / (1.0f * mBookManager.getChapterLen())) * 100;
        String percent = mPerFormat.format(fraction) + "%";
        canvas.drawText(percent, x, y, mTextPaint);
    }

    /**
     * @param begin:书的起始点。
     * @return:获取初始显示的页面
     */
    private TPage getCurPage(long begin){
        TPage tPage = new TPage();
        tPage.begin = begin; //起始字节
        tPage.lines = getNextLines();
        tPage.end = mBookManager.getPosition(); //下个位置的起始点
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
        return tPage;
    }

    //获取上一页的内容
    private List<String> getPrevLines(){
        List<String> lines = new ArrayList<>();
        String paragraph = null;
        while((paragraph = mBookManager.getPrevPara()) != null){
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
        //判断是否达到章节的起始点
        if (mCurPage.begin == 0){
            //载入上一章。
            if (!preChapter()){
                return false;
            }
            else {
                onDraw(mPageView.getCurPage(),mCurPage.lines);
                mCancelPage = mCurPage;
                mCurPage = getPrevLastPage();
                onDraw(mPageView.getNextPage(),mCurPage.lines);
                return true;
            }
        }

        onDraw(mPageView.getCurPage(),mCurPage.lines);
        mCancelPage = mCurPage;
        mCurPage = getPrevPage();
        onDraw(mPageView.getNextPage(),mCurPage.lines);//nextPage:指的是准备看的下一页，无关prev还是next
        return true;
    }

    //装载上一章节的内容
    private boolean preChapter(){
        if (mCurChapter - 1 < 0){
            return false;
        }
        else {
            mCurChapter -= 1;
            mBookManager.openChapter(mBookId,mCategoryList.get(mCurChapter).getTitle());

            if (mPageChangeListener != null){
                mPageChangeListener.onChapterChange(mCurChapter);
            }
            return true;
        }
    }

    //翻阅下一页
    public boolean next(){
        //判断是否达到章节的终止点
        if (mBookManager.getChapterLen() == mCurPage.end){
            if (!nextChapter()){
                return false;
            }
            else {
                onDraw(mPageView.getCurPage(),mCurPage.lines);
                mCancelPage = mCurPage;
                mCurPage = getCurPage(0);
                onDraw(mPageView.getNextPage(),mCurPage.lines);
                return true;
            }
        }
        else {
            onDraw(mPageView.getCurPage(),mCurPage.lines);
            mCancelPage = mCurPage;
            mCurPage = getNextPage();
            onDraw(mPageView.getNextPage(),mCurPage.lines);
            return true;
        }
    }

    //装载下一章节的内容
    private boolean nextChapter(){
        if (mCurChapter + 1 >= mCategoryList.size()){
            return false;
        }
        else {
            mCurChapter += 1;
            //装载
            mBookManager.openChapter(mBookId,mCategoryList.get(mCurChapter).getTitle());
            if (mPageChangeListener != null){
                mPageChangeListener.onChapterChange(mCurChapter);
            }
            return true;
        }
    }

    //取消翻页 (这个cancel有点歧义，指的是不需要看的页面)
    public void cancel(){
        if (mCurPage.begin == 0 && mCancelPage.begin != mCurPage.end){ //是否下翻了一章 （没有考虑到，第二页翻到第一页时候取消的情况）
            preChapter();//取消的时候，回退到上一章
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

    //跳转到指定章节
    public void skipToChapter(int pos){
        //进行绘制解析
    }

    public void setTextColor(){
    }

    public void setTextSize(int textSize){
    }

    //绘制背景
    public void setBgColor(int theme){
        switch (theme){
            case ReadSettingManager.READ_BG_DEFAULT:
                mTextColor = R.color.nb_read_font_1;
                mPageBg = R.color.nb_read_bg_1;
                break;
            case ReadSettingManager.READ_BG_1:
                mTextColor = R.color.nb_read_font_2;
                mPageBg = R.color.nb_read_bg_2;
                break;
            case ReadSettingManager.READ_BG_2:
                mTextColor = R.color.nb_read_font_3;
                mPageBg = R.color.nb_read_bg_3;
                break;
            case ReadSettingManager.READ_BG_3:
                mTextColor = R.color.nb_read_font_4;
                mPageBg = R.color.nb_read_bg_4;
                break;
            case ReadSettingManager.READ_BG_4:
                mTextColor = R.color.nb_read_font_5;
                mPageBg = R.color.nb_read_bg_5;
                break;
        }
    }

    public void setPageMode(int pageMode){
    }

    public int getPageStatus(){
        return mStatus;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener){
        mPageChangeListener = listener;
    }

    public interface OnPageChangeListener{
        //pos:表示下一章节
        void onChapterChange(int pos);
    }

    //创建临时的页面
    private class TPage{
        long begin;
        long end;
        List<String> lines;
    }
}
