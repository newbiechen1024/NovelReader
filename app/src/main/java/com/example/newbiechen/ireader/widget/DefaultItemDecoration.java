package com.example.newbiechen.ireader.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by newbiechen on 17-4-19.
 * 默认为灰色的间距
 * 1. 间距有这几种类型horizon,vertical,Grid
 * 2. 每种类型有这几种方式边缘处理，与边缘不处理
 * 3. 如果是边缘处理则无视RecyclerView的padding
 * 4. 如果边缘不处理则根据RecyclerView的padding做出边缘上的改变
 * 5. 可以选择颜色，距离
 */

public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //获取类型，然后根据类型选择哪一種绘制方式

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
