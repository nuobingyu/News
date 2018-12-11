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
    private int mLeft,mTop;
    private int padding_bottom;
    private int padding_top;
    private int padding_left;
    private int padding_right;

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

        mCanvas = canvas;
        drawable= getDrawable();
        if(drawable != null){
            height = getBottom()-getTop(); //高度
            width = getRight()-getLeft();  //宽度
            Log.e("宽高","width="+width+",height="+height);
            mRadius = Math.min(height ,width)/2;
            int cx = mLeft+width/2;
            int cy = mTop+height/2-30;
            Log.e("圆心","cx="+cx+",cy="+cy);
            Bitmap bitmap =((BitmapDrawable)drawable).getBitmap();
            BitmapShader bitmapShader = new BitmapShader(bitmap , CLAMP , CLAMP);
            mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight() ,bitmap.getWidth());
            matrix.setScale(mScale,mScale);
            bitmapShader.setLocalMatrix(matrix);
            Log.e("r",mRadius+"");
            mPaint.setShader(bitmapShader);

            matrix.postTranslate(cx-mRadius,cy-mRadius);
            Path path = new Path();
            path.addCircle(cx,cy,mRadius, Path.Direction.CW); //cw
            canvas.clipPath(path);
            canvas.setMatrix(matrix);
            canvas.drawARGB(0,0,0,0);
            canvas.drawBitmap(bitmap,0, 0, mPaint);
        }else{
            Log.e("drawable为null"," ");
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams lp =getLayoutParams();
        if(lp.width == ViewGroup.LayoutParams.WRAP_CONTENT){
            Log.e("width","WRAP_CONTENT");
            lp.width = dip2px(50);
        }
        if(lp.height == ViewGroup.LayoutParams.WRAP_CONTENT){
            Log.e("height","WRAP_CONTENT");
            lp.height = dip2px(50);
        }

    }

    public int dip2px(int px){
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(px*scale +0.5f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        padding_bottom = getPaddingBottom();
        padding_top = getPaddingTop();
        padding_left = getPaddingLeft();
        padding_right = getPaddingRight();
        setLeft(left+padding_left);
        setRight(right-padding_right);
        setBottom(bottom-padding_bottom);
        setTop(top+padding_top);
        mLeft = left;
        mTop = top;
        Log.e("top",mTop+"");
    }
}
