package com.liubin.fenghui.binview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.liubin.fenghui.binview.Util.L;

/**
 * Created by LiuBin on 2017/12/18.
 */

public class TimeLineView extends View{
    private int mMarkerSize=12;//结点半径大小，单位dp
    private int mLineSize=2;//线段粗细，dp
    private Drawable mBeginLine;//上面线，颜色或者图片
    private Drawable mEndLine;//下面线，颜色或者图片
    private Drawable mMarketDrawable;//结点，图片或者颜色
    private boolean oritation;//是否横屏

    public TimeLineView(Context context) {
       this(context,null);
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeLineView);
        if(typedArray!=null){
            mMarkerSize=typedArray.getDimensionPixelSize(R.styleable.TimeLineView_mMarketSize,mMarkerSize);//不管单位是什么都默认乘以denstiy
            mLineSize=typedArray.getDimensionPixelSize(R.styleable.TimeLineView_mLineSize,mLineSize);
            mBeginLine=typedArray.getDrawable(R.styleable.TimeLineView_mBeginLine);
            mEndLine=typedArray.getDrawable(R.styleable.TimeLineView_mEndLine);
            mMarketDrawable=typedArray.getDrawable(R.styleable.TimeLineView_mMarketDrawable);
            oritation=typedArray.getBoolean(R.styleable.TimeLineView_oritation,false);
            typedArray.recycle();
            L.i("mMarkerSize="+mMarkerSize+"   mLinSize="+mLineSize);
        }
        if(mBeginLine!=null){
            mBeginLine.setCallback(this);
        }
        if(mEndLine!=null){
            mEndLine.setCallback(this);
        }
        if(mMarketDrawable!=null){
            mMarketDrawable.setCallback(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        L.i("");
        int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        if(oritation){//横屏
            if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
                setMeasuredDimension(120,80);
            }else if(widthSpecMode==MeasureSpec.AT_MOST){
                setMeasuredDimension(120,heightSpecSize);
            }else if(heightSpecMode==MeasureSpec.AT_MOST){
                setMeasuredDimension(widthMeasureSpec,80);
            }
        }else {
            if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
                setMeasuredDimension(80,120);
            }else if(widthSpecMode==MeasureSpec.AT_MOST){
                setMeasuredDimension(80,heightSpecSize);
            }else if(heightSpecMode==MeasureSpec.AT_MOST){
                setMeasuredDimension(widthSpecSize,120);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {//此时拿到的是控件的宽度和高度
        super.onSizeChanged(w, h, oldw, oldh);
        L.i("w="+w+"  h="+h+"   oldw="+oldh+"   oldh="+oldh);
        initDrawableSize();
    }

    private void initDrawableSize() {
        int pLeft=getPaddingLeft();
        int pRight=getPaddingRight();
        int pTop=getPaddingTop();
        int pBottom=getPaddingBottom();
        int width=getWidth();
        int height=getHeight();
        int cWidth=width-pLeft-pRight;
        int cHeight=height-pBottom-pTop;
        Rect rect;
        int marksize=Math.min(Math.min(cWidth,cHeight),mMarkerSize);
        if(oritation){//横屏
            //结点
            mMarketDrawable.setBounds(cWidth/2-mMarkerSize,pTop,cWidth/2+mMarkerSize,pTop+2*mMarkerSize);
            rect=mMarketDrawable.getBounds();
            //开始线
            mBeginLine.setBounds(pLeft,rect.centerY()-mLineSize/2,rect.left,rect.centerY()+mLineSize/2);
            //结束线
            mEndLine.setBounds(rect.right,rect.centerY()-mLineSize/2,width-pRight,rect.centerY()+mLineSize/2);
        }else {//竖屏
            //结点
            mMarketDrawable.setBounds(pLeft,cHeight/2-mMarkerSize,pLeft+2*mMarkerSize,cHeight/2+mMarkerSize);
            rect=mMarketDrawable.getBounds();
            //开始线
            mBeginLine.setBounds(rect.centerX()-mLineSize/2,pTop,rect.centerX()+mLineSize/2,rect.top);
            //结束线
            mEndLine.setBounds(rect.centerX()-mLineSize/2,rect.bottom,rect.centerX()+mLineSize/2,height-pBottom);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {//拿到的是控件的位置
        super.onLayout(changed, left, top, right, bottom);
        L.i("left="+left+"  top="+top+"   right="+right+"  bottom="+bottom);
        L.i("width="+getWidth()+" height="+getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        L.i("");
        if(mBeginLine!=null){
            mBeginLine.draw(canvas);
        }
        if(mMarketDrawable!=null){
            mMarketDrawable.draw(canvas);
        }
        if(mEndLine!=null){
            mEndLine.draw(canvas);
        }
    }
    public void setmBeginLine(Drawable beginLine){
        if(this.mBeginLine!=beginLine){
            this.mBeginLine=beginLine;
            if(mBeginLine!=null){
                mBeginLine.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }
    public void setmMarketDrawable(Drawable marketDrawable){
        if(this.mMarketDrawable!=marketDrawable){
            this.mMarketDrawable=marketDrawable;
            if(mMarketDrawable!=null){
                mMarketDrawable.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

}
