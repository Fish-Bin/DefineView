package com.liubin.fenghui.binview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.liubin.fenghui.binview.Util.L;

/**
 * Created by LiuBin on 2017/12/18.
 */

public class ArcView extends View {
    private int arcHeight;
    private int backColor;
    private Paint mPaint;
    private int dx;
    private int dy;
    private int radius;
//    private int left,top,right,bottom;

    public ArcView(Context context) {
        this(context, null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
        if (typedArray != null) {
            arcHeight=typedArray.getDimensionPixelSize(R.styleable.ArcView_arc_height,20);
            backColor = typedArray.getColor(R.styleable.ArcView_backColor, 0xffffffff);
            typedArray.recycle();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(backColor);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(600, 900);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 400);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dx=getWidth()/2;
        radius=(int) ((Math.pow(getWidth()/2,2)+Math.pow(arcHeight,2))/2/arcHeight);
        dy=radius;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {//拿控件的边界
        super.onLayout(changed, left, top, right, bottom);
        L.i("left="+left+"\nright="+right+"\ntop="+top+"\nbottom="+bottom);
//        this.left=left;
//        this.top=top;
//        this.right=right;
//        this.bottom=bottom;
//        dx = (right-left) / 2;//此处要以本控件自身左上角为原点，而不是以屏幕左上角为原点
//        radius = (int) ((Math.pow((right-left)/2,2)+Math.pow(arcHeight,2))/2/arcHeight);
//        dy = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        L.i("left="+left+"\nright="+right+"\ntop="+top+"\nbottom="+bottom);
//        Paint paint=new Paint();
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.RED);
//        canvas.drawRect(new Rect(0,0,300,400),paint);

        L.i("dx=" + dx + "  dy=" + dy + "  radius=" + radius);
        canvas.drawCircle(dx, dy, radius, mPaint);
    }
}

