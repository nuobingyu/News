package com.nby.news.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyCircleView extends android.support.v7.widget.AppCompatImageView{

    private Drawable drawable;
    private Bitmap bitmap;
    private Paint mPaint;
    private Context mContext;

    public MyCircleView(Context context) {
        this(context,null);
    }

    public MyCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mContext = getContext();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        drawable = getDrawable();
        float r = Math.min(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Path path = new Path();
        path.addCircle(getLayoutParams().width/2,getHeight()/2,r, Path.Direction.CW);
        canvas.clipPath(path);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        super.onDraw(canvas);
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
