package com.liubin.fenghui.binview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.CycleInterpolator;

public class MyImageView extends android.support.v7.widget.AppCompatImageView {

    private final Paint mOutBorderPaint = new Paint();//外框画笔
    private final Paint mInBorderPaint = new Paint();//内框画笔
    private final Paint mValuePaint = new Paint();//动画画笔
    private final Paint mBitmapPaint = new Paint();//图片画笔

    /*外框画笔的配置*/
    private int mOutBorderColor = Color.parseColor("#335394fe");
    private int mOutBorderWidth = 1;
    /*内框画笔的配置*/
    private int mInBorderColor = Color.parseColor("#99bad3fd");
    private int mInBorderWidth = 1;
    /*动画画笔的配置*/
    private int mValueColor = Color.parseColor("#4faeff");
    private int mValueWidth = 6;

    /*路径*/
    private final Path outPath = new Path();
    private final Path inPath = new Path();
    private final Path valuePath = new Path();
    private final Path bitmapPath = new Path();

    private PathMeasure pathMeasure;

    private Bitmap mBitmap;//图片对象
    private final Matrix mShaderMatrix = new Matrix();//用于缩放图片
    private BitmapShader mBitmapShader;//图片画笔的填充物

    private float mWidth;//图片的宽
    private float mHeight;//图片的高
    private float mBitmapWidth = 200;//图片的宽
    private float mBitmapHeight = 300;//图片的高
    private float per = 0.2f;//夹角的百分比
    private float xPer = 0.8f;//夹角剩余的百分比
    private float offSize = 30;//多出的距离
    private float length;//总长
    private float lineA;//第一段线段的长度
    private float lineB;//第二段线段的长度
    private float lineC;//第三段线段的长度
    private float lineD;//第四段线段的长度
    private float lineE;//第五段线段的长度
    private float AB;//第一段线段和第二段线段的距离
    private float BC;//第二段线段和第三段线段的距离

    private ValueAnimator valueAnimator;//动画
    private float animatedValue;//动画变化值

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setScaleType(ScaleType.CENTER_CROP);
        setLayerType(LAYER_TYPE_SOFTWARE, null);//针对此控件关闭硬件加速
        initAnimator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mBitmapWidth<250||mBitmapHeight<300){
            setMeasuredDimension((int)mBitmapWidth,(int)mBitmapHeight);
        }else {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
            int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(223, 213);
            } else if (widthMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(223, heightSize);
            } else if (heightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(widthSize, 213);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        canvas.scale(0.98f, 0.98f, mWidth / 2, mHeight / 2);
        //画外边框
        canvas.drawPath(outPath, mOutBorderPaint);
        //画动画
        loadPath();
        canvas.scale(0.98f, 0.98f, mWidth / 2, mHeight / 2);
        canvas.drawPath(valuePath, mValuePaint);
        //画内边框
        canvas.scale(0.95f, 0.95f, mWidth / 2, mHeight / 2);
        canvas.drawPath(outPath, mInBorderPaint);
        //画图片
        canvas.drawPath(outPath, mBitmapPaint);
    }

    private void loadPath() {
        float distance = length * animatedValue;
        valuePath.reset();
        valuePath.rLineTo(0, 0);//
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


    private void init() {
        if (mBitmap == null) {
            return;
        }
        initPaint();
        initPath();
        initData();
        initBitmapPaint();
        invalidate();
    }

    private void initData() {
        length = pathMeasure.getLength();
        lineA = (float) (offSize + Math.sqrt(Math.pow(mWidth * per, 2) + Math.pow(mHeight * per, 2)));
        lineB = offSize * 2;
        lineC = lineA + offSize;
        lineD = lineB;
        lineE = offSize;
        AB = mWidth * xPer - 2 * offSize;
        BC = mHeight * xPer - 2 * offSize;
    }

    private void initAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new CycleInterpolator(0.5f));
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
                /*结束后还需要重绘一次初始状态*/
                invalidate();
                if (mOnFinishListener != null) {
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
                if (valueAnimator.isStarted()) {
                    return true;
                } else {
                    valueAnimator.start();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void initPaint() {
        //外框画笔
        mOutBorderPaint.setStyle(Paint.Style.STROKE);
        mOutBorderPaint.setAntiAlias(true);
        mOutBorderPaint.setColor(mOutBorderColor);
        mOutBorderPaint.setStrokeWidth(mOutBorderWidth);
        //内框画笔
        mInBorderPaint.setStyle(Paint.Style.STROKE);
        mInBorderPaint.setAntiAlias(true);
        mInBorderPaint.setColor(mInBorderColor);
        mInBorderPaint.setStrokeWidth(mInBorderWidth);
        //动画画笔
        mValuePaint.setStyle(Paint.Style.STROKE);
        mValuePaint.setAntiAlias(true);
        mValuePaint.setColor(mValueColor);
        mValuePaint.setStrokeWidth(mValueWidth);

        // 设置光源的方向
        float[] direction = new float[]{1, 1, 1};
        //设置环境光亮度
        float light = 1f;
        // 选择要应用的反射等级
        float specular = 6;
        // 向mask应用一定级别的模糊
        float blur = 3.5f;
        EmbossMaskFilter emboss = new EmbossMaskFilter(direction, light, specular, blur);
        mValuePaint.setMaskFilter(emboss);

//        mValuePaint.setMaskFilter(new BlurMaskFilter(80, BlurMaskFilter.Blur.SOLID));//给动画画笔加闪光
    }

    private void initPath() {
        outPath.reset();
        outPath.moveTo(0, mHeight * per);
        outPath.lineTo(mWidth * per, 0);
        outPath.lineTo(mWidth, 0);
        outPath.lineTo(mWidth, mHeight * xPer);
        outPath.lineTo(mWidth * xPer, mHeight);
        outPath.lineTo(0, mHeight);
        outPath.lineTo(0, mHeight * per);
        pathMeasure = new PathMeasure(outPath, true);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        if (mBitmap != null) {
            mBitmapWidth = mBitmap.getWidth();
            mBitmapHeight = mBitmap.getHeight();
        }
        init();
    }

    /*获取bitmap*/
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }


    private void initBitmapPaint() {
        mShaderMatrix.set(null);//相当于reset();
        float scale = Math.min(mHeight / mBitmapHeight, mWidth / mBitmapWidth);
//        L.i("scale="+scale);
//        mShaderMatrix.setScale(scale, scale);
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setAntiAlias(true);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
        mBitmapPaint.setShader(mBitmapShader);
    }
}
