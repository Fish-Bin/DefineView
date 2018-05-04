package com.liubin.fenghui.binview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.liubin.fenghui.binview.Util.L;

/**
 * Created by LiuBin on 2017/12/19.
 */

public class WaveView extends View {
    private int A = 10;//振幅
    private int K;//偏距
    private int waveColor = 0xaaFF7E37;//波形颜色
    private float Q;//初相
    private float waveSpeed = 3f;//波浪速度
    private double w;//角速度
    private double startPeriod;//开始位置相差多少周期
    private Path path;
    private Paint paint;
    private ValueAnimator valueAnimator;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        initPaint();
        initAnimator();
    }

    private void initAnimator() {
        L.i("getWidth=" + getWidth());
//        valueAnimator = ValueAnimator.ofInt(0, getWidth());
        valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0);
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private void initPaint() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(waveColor);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WaveView);
        A = typedArray.getDimensionPixelSize(R.styleable.WaveView_A, A);
        K = A;
        waveColor = typedArray.getColor(R.styleable.WaveView_waveColor, waveColor);
        waveSpeed = typedArray.getFloat(R.styleable.WaveView_waveSpeed, waveSpeed);
        startPeriod = typedArray.getFloat(R.styleable.WaveView_startPeriod, 0);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        L.i("getWidth=" + getWidth());
        this.w = 2 * Math.PI / getWidth();
        L.i("w=" + this.w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Q = waveSpeed / 100;
        L.i("Q=" + Q);
        float y;
        path.reset();
        path.moveTo(0, getHeight());
        for (float x = 0; x <= getWidth(); x += 20) {
            y = (float) (A * Math.sin(w * x + Q + Math.PI * startPeriod) + K);
            L.i("x="+x+"  y="+y);
            path.lineTo(x, y);
        }
        //填充矩形
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.close();

        canvas.drawPath(path, paint);
    }
}
