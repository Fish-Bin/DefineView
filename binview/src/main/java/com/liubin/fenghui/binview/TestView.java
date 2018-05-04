package com.liubin.fenghui.binview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;

import com.liubin.fenghui.binview.Util.L;

/**
 * Created by LiuBin on 2017/12/21
 */

public class TestView extends View {
    private Paint borderPaint;
    private Paint valuePaint;
    private Path borderPath;
    private Path inBorderPath;
    private Path valuePath;
    private PathMeasure pathMeasure;
    private ValueAnimator valueAnimator;
    private float w = 800, h = 1200;
    private float offSize = 100;//多出的距离
    private float per = 0.2f;//夹角的百分比
    private float xPer = 0.8f;//夹角剩余的百分比
    private float length;//总长
    private float addPer = 0;//每次增加总长的比例，默认每次增加0.05
    private float lineA;//第一段线段的长度
    private float lineB;//第二段线段的长度
    private float lineC;//第三段线段的长度
    private float lineD;//第四段线段的长度
    private float lineE;//第五段线段的长度
    private float AB;//第一段线段和第二段线段的距离
    private float BC;//第二段线段和第三段线段的距离
    private float animatedValue;

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initAnimator();
    }

    private void initAnimator() {
//        L.i("");
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new CycleInterpolator(0.5f));
//        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                invalidate();
                if(mOnFinishListener!=null){
                    mOnFinishListener.onFinish();
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void start() {
        valueAnimator.start();
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        loadPath();
        canvas.drawPath(borderPath, borderPaint);
        canvas.drawPath(inBorderPath, borderPaint);
//        valuePaint.setMaskFilter(null);
//        canvas.drawPath(valuePath, valuePaint);
//        valuePath.offset(20, 20);
//        BlurMaskFilter maskFilter = new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID);
//        valuePaint.setMaskFilter(maskFilter);
//        canvas.drawPath(valuePath, valuePaint);
    }


    private void init() {
//        L.i("");
        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(6);
        borderPaint.setColor(Color.WHITE);

        valuePaint = new Paint();
        valuePaint.setStyle(Paint.Style.STROKE);
        valuePaint.setStrokeWidth(12);
        valuePaint.setColor(Color.WHITE);//4faeff

        borderPath = new Path();
        inBorderPath=new Path();
        valuePath = new Path();

        borderPath.reset();
        borderPath.moveTo(0, h * per);
        borderPath.lineTo(w * per, 0);
        borderPath.lineTo(w, 0);
        borderPath.lineTo(w, h * xPer);
        borderPath.lineTo(w * xPer, h);
        borderPath.lineTo(0, h);
        borderPath.lineTo(0, h * per);
        borderPath.offset(4, 4);
        borderPath.close();

        h=h-40;
        w=w-40;
        inBorderPath.reset();
        inBorderPath.moveTo(0, h * per);
        inBorderPath.lineTo(w * per, 0);
        inBorderPath.lineTo(w, 0);
        inBorderPath.lineTo(w, h * xPer);
        inBorderPath.lineTo(w * xPer, h);
        inBorderPath.lineTo(0, h);
        inBorderPath.lineTo(0, h * per);
        inBorderPath.offset(4, 4);
        inBorderPath.close();

        pathMeasure = new PathMeasure(borderPath, true);
        length = pathMeasure.getLength();
        lineA = (float) (offSize + Math.sqrt(Math.pow(w * per, 2) + Math.pow(h * per, 2)));
        lineB = offSize * 2;
        lineC = lineA + offSize;
        lineD = lineB;
        lineE = offSize;
        AB = w * xPer - 2 * offSize;
        BC = h * xPer - 2 * offSize;
    }


    private void loadPath() {
        float distance = length * animatedValue;
        L.i("distance="+distance+"animatedValue="+animatedValue);
        valuePath.reset();
        float startA = 0 + distance;
        float endA = startA + lineA;
        float startB = endA + AB + distance;
        float endB = startB + lineB;
        float startC = endB + BC + distance;
        float endC = startC + lineC;
        float startD = endC + AB + distance;
        float endD = startD + lineD;
        float startE = endD + BC + distance;
        float endE = startE + lineE;
        pathMeasure.getSegment(startA, endA, valuePath, true);
        pathMeasure.getSegment(startB, endB, valuePath, true);
        pathMeasure.getSegment(startC, endC, valuePath, true);
        pathMeasure.getSegment(startD, endD, valuePath, true);
        pathMeasure.getSegment(startE, endE, valuePath, true);
        }

    private OnFinishListener mOnFinishListener;

    public interface OnFinishListener {
        void onFinish();
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.mOnFinishListener = onFinishListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start();
                return true;
        }
        return super.onTouchEvent(event);
    }
}