package com.nby.news.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class MyCircleView extends android.support.v7.widget.AppCompatImageView{

    private Drawable drawable;
    private Bitmap bitmap;
    private Paint mPaint;

    public MyCircleView(Context context) {
        this(context,null);
    }

    public MyCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable){
        if(drawable == null)
            return null;
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        try{
            if(drawable instanceof ColorDrawable){
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            }else{
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            return bitmap;
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        drawable = getDrawable();
        mPaint.setAntiAlias(true);
        Bitmap mBitmap = Bitmap.createBitmap(bitmap.getWidth()
                ,bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(mBitmap);
        super.onDraw(bitmapCanvas);
        //drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        //drawable.draw(canvas1);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));


        bitmapCanvas.drawCircle(getWidth()/2,getHeight()/2,90, mPaint);
        bitmapCanvas.drawBitmap(bitmap,0,0,mPaint);

    }
}
