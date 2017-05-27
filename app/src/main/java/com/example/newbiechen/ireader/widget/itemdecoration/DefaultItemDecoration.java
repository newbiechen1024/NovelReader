package com.example.newbiechen.ireader.widget.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;

import java.lang.ref.WeakReference;

/**
 * Created by newbiechen on 17-4-19.
 * 默认为灰色的间距
 * 1. 间距有这几种类型horizon,vertical,Grid (完成)
 * 2. 每种类型有这几种方式边缘处理，与边缘不处理
 * 3. 如果是边缘处理则无视RecyclerView的padding
 * 4. 如果边缘不处理则根据RecyclerView的padding做出边缘上的改变 (完成)
 * 5. 可以选择颜色，距离
 */

public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "DefaultItemDecoration";

    private static final int TYPE_HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    private static final int TYPE_VERTICAL = LinearLayoutManager.VERTICAL;
    private static final int TYPE_GRID = 3;

    private Context mContext;
    private int type;

    private WeakReference<Drawable> mHorizontalDrawable;
    private WeakReference<Drawable> mVerticalDrawable;

    public DefaultItemDecoration(Context context){
        mContext = context;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //获取类型，然后根据类型选择哪一種绘制方式
        type = getLayoutType(parent);
        switch (type){
            case TYPE_HORIZONTAL:
                drawHorizontal(c,parent);
                break;
            case TYPE_VERTICAL:
                drawVertical(c,parent);
                break;
            case  TYPE_GRID:
                drawGrid(c,parent);
                break;
            default:
                break;
        }
    }

    private int getLayoutType(RecyclerView rv){
        RecyclerView.LayoutManager manager = rv.getLayoutManager();
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager){
            return TYPE_GRID;
        }
        else {
            LinearLayoutManager linearManager = (LinearLayoutManager) manager;
            if (linearManager.getOrientation() == LinearLayoutManager.HORIZONTAL){
                return TYPE_HORIZONTAL;
            }
            else {
                return TYPE_VERTICAL;
            }
        }
    }

    private void drawHorizontal(Canvas c,RecyclerView rv){
        int count = rv.getChildCount();
        Drawable horizontalDrawable = getHorizontalDrawable();
        for (int i=0; i<count; ++i){
            View child = rv.getChildAt(i);
            int left = child.getRight();
            int right = left + horizontalDrawable.getIntrinsicWidth();
            horizontalDrawable.setBounds(left,child.getTop(),right,child.getBottom());
            horizontalDrawable.draw(c);
        }
    }

    private void drawVertical(Canvas c,RecyclerView rv){
        int count = rv.getChildCount();
        Drawable verticalDrawable = getVerticalDrawable();
        for (int i=0; i<count; ++i){
            View child = rv.getChildAt(i);
            int top = child.getBottom();
            int bottom = top + verticalDrawable.getIntrinsicHeight();
            verticalDrawable.setBounds(child.getLeft(),top,child.getRight(),bottom);
            verticalDrawable.draw(c);
        }
    }

    private void drawGrid(Canvas c,RecyclerView rv){
        int count = rv.getChildCount();
        int span = getSpanCount(rv);
        Drawable horizontalDrawable = getHorizontalDrawable();
        Drawable verticalDrawable = getVerticalDrawable();
        for (int i=0; i<count; ++i){
            if(i % span == span - 1){
                continue;
            }
            View child = rv.getChildAt(i);
            int horLeft = child.getRight();
            int horRight = horLeft + horizontalDrawable.getIntrinsicWidth();
            int horTop = child.getTop();
            int horBottom = child.getBottom() + verticalDrawable.getIntrinsicHeight();
            horizontalDrawable.setBounds(horLeft,horTop,horRight,horBottom);
            horizontalDrawable.draw(c);
        }

        int lastCount = count % span;
        for (int i=0; i<count; ++i){
            if (i >= count - lastCount){
                break;
            }
            View child = rv.getChildAt(i);
            int verLeft = child.getLeft();
            int verRight = child.getRight() + horizontalDrawable.getIntrinsicWidth();
            int verTop = child.getBottom();
            int verBottom = verTop + verticalDrawable.getIntrinsicHeight();
            verticalDrawable.setBounds(verLeft,verTop,verRight,verBottom);
            verticalDrawable.draw(c);
        }
    }

    private int getSpanCount(RecyclerView rv){
        RecyclerView.LayoutManager manager = rv.getLayoutManager();
        int span = -1;
        if (manager instanceof GridLayoutManager){
            GridLayoutManager gridManager = (GridLayoutManager) rv.getLayoutManager();
            span =  gridManager.getSpanCount();
        }
        else if (manager instanceof StaggeredGridLayoutManager){
            StaggeredGridLayoutManager staggerManager = (StaggeredGridLayoutManager) rv.getLayoutManager();
            span = staggerManager.getSpanCount();
        }
        return span;
    }

    private Drawable getHorizontalDrawable(){
        Drawable drawable = null;
        if (mHorizontalDrawable != null){
            drawable = mHorizontalDrawable.get();
        }
        if (drawable == null){
            drawable = ContextCompat.getDrawable(mContext,R.drawable.shape_divider_horizon);
            mHorizontalDrawable = new WeakReference<Drawable>(drawable);
        }
        return drawable;
    }

    private Drawable getVerticalDrawable(){
        Drawable drawable = null;
        if (mVerticalDrawable != null){
            drawable = mVerticalDrawable.get();
        }
        if (drawable == null){
            drawable = ContextCompat.getDrawable(App.getContext(),R.drawable.shape_divider_vertical);
            mVerticalDrawable = new WeakReference<Drawable>(drawable);
        }
        return drawable;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int type = getLayoutType(parent);
        switch (type){
            case TYPE_HORIZONTAL:
                offsetHorizontal(outRect);
                break;
            case TYPE_VERTICAL:
                offsetVertical(outRect);
                break;
            case TYPE_GRID:
                offsetGrid(outRect);
                break;
            default:
                break;
        }
    }

    private void offsetHorizontal(Rect outRect){
        outRect.set(0, 0, getHorizontalDrawable().getIntrinsicWidth(), 0);
    }

    public void offsetVertical(Rect outRect){
        outRect.set(0,0,0, getVerticalDrawable().getIntrinsicHeight());
    }

    public void offsetGrid(Rect outRect){
        outRect.set(0,0, getHorizontalDrawable().getIntrinsicWidth(), getVerticalDrawable().getIntrinsicHeight());
    }
}
