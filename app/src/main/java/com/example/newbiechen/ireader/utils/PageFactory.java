package com.example.newbiechen.ireader.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.local.ReadSettingManager;
import com.example.newbiechen.ireader.widget.PageView;

/**
 * Created by newbiechen on 17-5-19.
 */

public class PageFactory {
    private static PageFactory sInstance;

    private static final int DEFAULT_LINE_DIVIDER = 20;
    private static final int DEFAULT_PAGE_MARGIN_TOP_BOTTOM = 20;
    private static final int DEFAULT_PAGE_MARGIN_LEFT_RIGHT = 20;

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_FINISH = 2;
    public static final int STATUS_ERROR = 3;

    private PageView mPageView;
    private Bitmap mCurrentPage;
    private Bitmap mNextPage;
    //绘制文字的画笔
    private Paint mTextPaint;
    private ReadSettingManager mSettingManager;


    /*****************params**************************/
    private int mStatus = STATUS_LOADING;
    private int mAppWidth;
    private int mAppHeight;

    private int mTextColor;
    private int mTextSize;
    private int mPageMode;
    private int mPageBg;

    public static PageFactory getInstance(PageView pageView){
        if (sInstance == null){
            synchronized (PageFactory.class){
                if (sInstance == null){
                    sInstance = new PageFactory(pageView);
                }
            }
        }
        return sInstance;
    }

    private PageFactory(PageView pageView){
        mPageView = pageView;
        //初始化数据
        initData();
        //初始化PageView
        initPageView();
        //初始化画笔
        initPaint();
        onDraw(mCurrentPage);
        onDraw(mNextPage);
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
    }

    private void initPageView(){
        mCurrentPage = mPageView.getCurPage();
        mNextPage = mPageView.getNextPage();
        mPageView.setPageMode(mPageMode);
        mPageView.setBgColor(mPageBg);
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

    private void onDraw(Bitmap bitmap){
        //绘制的内容
        String text = getTipText();

        Canvas canvas = new Canvas(bitmap);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        int fontX = (int) (mAppWidth - mTextPaint.measureText(text))/2;
        int fontY = (int) (mAppHeight - (fontMetrics.top - fontMetrics.bottom)) / 2;

        canvas.drawColor(ContextCompat.getColor(App.getContext(), R.color.nb_read_bg_1));
        canvas.drawText(text,fontX,fontY,mTextPaint);
    }

    private String getTipText(){
        String tip = "";
        switch (mStatus){
            case STATUS_LOADING:
                tip = "正在加载书籍";
                break;
            case STATUS_FINISH:
                break;
            case STATUS_ERROR:
                tip = "加载书籍错误";
                break;
        }
        return tip;
    }

    public void setTextColor(){
    }

    public void setTextSize(int textSize){
    }

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
}
