package com.example.newbiechen.ireader.widget.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by newbiechen on 17-4-28.
 */

public class CircleTransform extends BitmapTransformation {
    private static final String TAG = "CircleTransform";
    public CircleTransform(Context context) {
        super(context);
    }

    /**
     *
     * @param pool : 图片池，这个之后会谈到。
     * @param toTransform:需要进行处理的图片
     * @param outWidth:图片的宽
     * @param outHeight:图片的高
     * @return 返回处理完的图片
     */
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Paint paint = new Paint();
        //初始化画笔
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);

        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int size = Math.min(width,height);
        int x = (width - size)/2;
        int y = (height - size)/2;
        Bitmap result = pool.get(size,size, Bitmap.Config.ARGB_8888);
        if (result == null){
            result = Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
        }
        //
        Canvas canvas = new Canvas(result);
        int radius = size/2;
        canvas.drawCircle(radius,radius,radius,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(toTransform,-x,-y,paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
