package com.liubin.fenghui.binview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class LightTextView extends View {

    private final Paint mTextPaint = new Paint();//文字画笔
    private final Paint mLightPaint = new Paint();//发光画笔

    private int mTextColor = Color.parseColor("#ffffff");
    private int mLightColor = Color.parseColor("#e6ff8a00");
    private String text="# 199";

    public LightTextView(Context context) {
        this(context, null);
    }

    public LightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LightTextView);
        if (typedArray != null) {
            text=typedArray.getString(R.styleable.LightTextView_text);
            typedArray.recycle();
        }
        initPaint();
    }

    private void initPaint() {
        mLightPaint.setStyle(Paint.Style.STROKE);
        mLightPaint.setStrokeWidth(4);
        mLightPaint.setTextSize(80);
        mLightPaint.setColor(mLightColor);
        mLightPaint.setTextAlign(Paint.Align.LEFT);

        mTextPaint.setStrokeWidth(3);
        mTextPaint.setTextSize(80);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // 设置光源的方向
        float[] direction = new float[]{1, 1, 1};
        //设置环境光亮度
        float light = 6f;
        // 选择要应用的反射等级
        float specular = 6;
        // 向mask应用一定级别的模糊
        float blur = 3.5f;
        EmbossMaskFilter emboss = new EmbossMaskFilter(direction, light, specular, blur);
        mTextPaint.setMaskFilter(emboss);
        mTextPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));//给动画画笔加闪光
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text,getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mLightPaint);
        canvas.drawText(text,getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mTextPaint);
    }
}
