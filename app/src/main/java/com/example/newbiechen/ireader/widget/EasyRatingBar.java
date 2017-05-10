package com.example.newbiechen.ireader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.utils.ScreenUtils;

import java.lang.ref.WeakReference;

/**
 * Created by newbiechen on 17-4-30.
 * 简单实现RatingBar效果
 */
public class EasyRatingBar extends View {
    private static final String TAG = "EasyRatingBar";
    private static final int DEFAULT_MAX_HEIGHT = 48;
    private Context mContext;
    //默认等级的数量
    private int mRateCount;
    //当前的等级
    private int mCurrentRate = 0;
    //未选中的图片的id
    private int mNormalRes;
    //选中的图片的id
    private int mSelectRes;
    //间距
    private int mInterval;
    //是否可选中
    private boolean isIndicator;

    //每个格子的宽、高
    private int mRoomWidth;
    private int mRoomHeight;

    private WeakReference<Drawable> mNormalWeak = null;
    private WeakReference<Drawable> mSelectWeak = null;
    private Paint mPaint = null;
    public EasyRatingBar(Context context) {
        this(context,null);
    }

    public EasyRatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EasyRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.EasyRatingBar);
        mRateCount = a.getInteger(R.styleable.EasyRatingBar_rateNum,5);
        mNormalRes = a.getResourceId(R.styleable.EasyRatingBar_rateNormal,R.drawable.rating_star_nor);
        mSelectRes = a.getResourceId(R.styleable.EasyRatingBar_rateSelect,R.drawable.rating_star_sel);
        mInterval = (int) a.getDimension(R.styleable.EasyRatingBar_rateInterval,ScreenUtils.dpToPx(4));
        isIndicator = a.getBoolean(R.styleable.EasyRatingBar_isIndicator,true);

        int currentRate =a.getInteger(R.styleable.EasyRatingBar_rating,0);
        if (currentRate < mRateCount){
            mCurrentRate = currentRate;
        }
        a.recycle();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRoomHeight = h;
        mRoomWidth = w/mRateCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            //宽度 × 大小
            int viewHeight = getInitRoomHeight();
            int viewWidth = viewHeight * mRateCount;
            widthSize = viewWidth;
            heightSize = viewHeight;
        }
        else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY){
            int viewWidth = heightSize * mRateCount;
            widthSize = viewWidth;
        }
        else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST){
            heightSize = widthSize / mRateCount;
        }
        setMeasuredDimension(widthSize,heightSize);
    }

    //返回空间的高度
    private int getInitRoomHeight(){
        Drawable normal= getDrawable(mNormalWeak,mNormalRes);
        Drawable select= getDrawable(mSelectWeak,mSelectRes);
        int normalMin = Math.min(normal.getIntrinsicWidth(),normal.getIntrinsicHeight());
        int selectMin = Math.min(select.getIntrinsicWidth(),select.getIntrinsicHeight());
        int drawableMin = Math.min(normalMin,selectMin);
        return Math.min(ScreenUtils.dpToPx(DEFAULT_MAX_HEIGHT),drawableMin);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable normalDrawable = getDrawable(mNormalWeak,mNormalRes);
        Drawable selectDrawable = getDrawable(mSelectWeak,mSelectRes);
        //绘制的半径
        int radius = Math.min(mRoomWidth, mRoomHeight)/2 - mInterval;
        //进行绘制
        for (int i=0; i<mRateCount; ++i){
            int roomWidthCenter = 0;
            if (i == 0){
                roomWidthCenter = mRoomWidth/2-mInterval;
            }
            else if (i==mRateCount-1){
                roomWidthCenter = mRoomWidth/2 + mRoomWidth*i + mInterval;
            }
            else {
                roomWidthCenter = mRoomWidth/2 + mRoomWidth*i;
            }
            int roomHeightCenter = mRoomHeight /2;
            canvas.save();
            canvas.translate(roomWidthCenter,roomHeightCenter);
            //绘制正常图片
            normalDrawable.setBounds(-radius,-radius,radius,radius);
            normalDrawable.draw(canvas);
            //绘制选中图片
            if (i < mCurrentRate){
                selectDrawable.setBounds(-radius,-radius,radius,radius);
                selectDrawable.draw(canvas);
            }
            canvas.restore();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //根据当前位置判断点击到哪个区域之后重置
        //暂时不做咯～～

        return super.onTouchEvent(event);

    }

    private Drawable getDrawable(WeakReference<Drawable> weak, int res){
        Drawable drawable = null;
        if (weak == null || weak.get() == null){
            drawable = ContextCompat.getDrawable(mContext,res);
            weak = new WeakReference<Drawable>(drawable);
        }
        else {
            drawable = weak.get();
        }
        return  drawable;
    }

    public void setRating(int currentRate){
        mCurrentRate = currentRate;
        invalidate();
    }

    public void setRateCount(int rateCount){
        mRateCount = rateCount;
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superParcel = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superParcel);
        savedState.rateCount = mRateCount;
        savedState.currentRate = mCurrentRate;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mRateCount = savedState.rateCount;
        mCurrentRate = savedState.currentRate;
    }

    static class SavedState extends BaseSavedState {
        int rateCount;
        int currentRate;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            rateCount = in.readInt();
            currentRate = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(rateCount);
            out.writeInt(currentRate);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
