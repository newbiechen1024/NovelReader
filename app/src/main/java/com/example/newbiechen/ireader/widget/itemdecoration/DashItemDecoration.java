package com.example.newbiechen.ireader.widget.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;

import java.lang.ref.WeakReference;

/**
 * Created by newbiechen on 17-4-16.
 * 1. 首先判断是否是LinearLayout
 * 2. 遍历个个child，绘制图形
 * 3. outRect
 */

public class DashItemDecoration extends RecyclerView.ItemDecoration {
    private WeakReference<Drawable> dashDrawable;
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        checkLayoutManager(parent);
        //获取Drawable
        Drawable drawable = getDrawable();
        int childCount = parent.getChildCount();
        //纵向绘制
        for (int i=0; i<childCount; ++i){
            View child = parent.getChildAt(i);
            int top = child.getBottom();
            int bottom = top + drawable.getIntrinsicHeight();
            drawable.setBounds(0,top,child.getRight(),bottom);
            drawable.draw(c);
        }
    }

    private void checkLayoutManager(RecyclerView rv){
        RecyclerView.LayoutManager manager = rv.getLayoutManager();
        if (!(manager instanceof LinearLayoutManager)){
            throw new IllegalArgumentException("only supply linearLayoutManager");
        }
    }

    private Drawable getDrawable(){
        Drawable drawable = null;
        if (dashDrawable != null){
            drawable = dashDrawable.get();
        }
        if (drawable == null){
            drawable = ContextCompat.getDrawable(App.getAppContext(),R.drawable.shape_divider_dash);
            dashDrawable = new WeakReference<Drawable>(drawable);
        }
        return drawable;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0,0,getDrawable().getIntrinsicWidth(),getDrawable().getIntrinsicHeight());
    }
}
