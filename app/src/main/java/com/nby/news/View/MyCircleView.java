package com.nby.news.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import static android.graphics.Shader.TileMode.CLAMP;

//还有问题,getDrawable为null
public class MyCircleView extends android.support.v7.widget.AppCompatImageView{

    private Drawable drawable;
    private Paint mPaint;
    private Context mContext;
    private Canvas mCanvas;
    private int height,width,mRadius;
    private float mScale;
    private Matrix matrix;

    public MyCircleView(Context context) {
        this(context,null);
    }

    public MyCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        matrix = new Matrix();
        mContext = getContext();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        height = getHeight() - getPaddingTop() - getPaddingBottom();
        width = getWidth() - getPaddingLeft() - getPaddingRight();
        int size = Math.min(height ,width);
        mRadius = size/2;
        mCanvas = canvas;
        drawable= getDrawable();
        if(drawable != null){
            int cx = mRadius;
            int cy = mRadius;
            Bitmap bitmap =((BitmapDrawable)drawable).getBitmap();
            BitmapShader bitmapShader = new BitmapShader(bitmap , CLAMP , CLAMP);
            mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight() ,bitmap.getWidth());
            matrix.setScale(mScale,mScale);
            bitmapShader.setLocalMatrix(matrix);
            mPaint.setShader(bitmapShader);

            Path path = new Path();
            path.addCircle(cx,cy,mRadius, Path.Direction.CW); //cw
            canvas.clipPath(path);
            canvas.setMatrix(matrix);
            canvas.drawARGB(0,0,0,0);

            Log.i("Bitmap",bitmap.getWidth() +","+bitmap.getHeight());
        }else{
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams lp =getLayoutParams();
        if(lp.width == ViewGroup.LayoutParams.WRAP_CONTENT){
            lp.width = dip2px(50);
        }
        if(lp.height == ViewGroup.LayoutParams.WRAP_CONTENT){
            lp.height = dip2px(50);
        }
    }

    public int dip2px(int px){
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(px*scale +0.5f);
    }
}
