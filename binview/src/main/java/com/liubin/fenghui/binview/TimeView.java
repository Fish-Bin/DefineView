package com.liubin.fenghui.binview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.liubin.fenghui.binview.Util.L;

/**
 * 作者：liu bin
 * 时间：2018.5.4
 * 说明：时间控件，倒计时，可以翻转
 */

public class TimeView extends RelativeLayout {
    private static final boolean DEBUG = true;

    private TextSwitcher hourTensSwitcher, hourDigitsSwitcher;
    private TextSwitcher minuteTensSwitcher, minuteDigitsSwitcher;
    private TextSwitcher secondTensSwitcher, secondDigitsSwitcher;
    private TextSwitcher mSeparatorOneSwitcher, mSeparatorTwoSwitcher;
    private int lastHourTens = -1, lastHourDigits = -1;
    private int lastMinuteTens = -1, lastMinuteDigits = -1;
    private int lastSecondTens = -1, lastSecondDigits = -1;
    private int textColor;
    private float textSize = 16; // Default Text Size 18
    private Handler mHandler;

    private long time;
    public int hour, minute, second;

    public TimeView(Context context) {
        super(context);
        init(context, null);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeView);
        textColor = array.getColor(R.styleable.TimeView_textColor, getResources().getColor(android.R.color.white));
        textSize = array.getDimension(R.styleable.TimeView_textSize, textSize);
        array.recycle();
        View clockView = LayoutInflater.from(context).inflate(R.layout.view_clock, this, true);
        mSeparatorOneSwitcher = (TextSwitcher) clockView.findViewById(R.id.switcher_separator_1);
        mSeparatorOneSwitcher.setFactory(mFactory);

        mSeparatorTwoSwitcher = (TextSwitcher) clockView.findViewById(R.id.switcher_separator_2);
        mSeparatorTwoSwitcher.setFactory(mFactory);

        hourTensSwitcher = (TextSwitcher) clockView.findViewById(R.id.switcher_hour_tens);
        hourTensSwitcher.setFactory(mFactory0);

        hourDigitsSwitcher = (TextSwitcher) clockView.findViewById(R.id.switcher_hour_digits);
        hourDigitsSwitcher.setFactory(mFactory0);

        minuteTensSwitcher = (TextSwitcher) clockView.findViewById(R.id.switcher_minute_tens);
        minuteTensSwitcher.setFactory(mFactory0);

        minuteDigitsSwitcher = (TextSwitcher) clockView.findViewById(R.id.switcher_minute_digits);
        minuteDigitsSwitcher.setFactory(mFactory0);

        secondTensSwitcher = (TextSwitcher) clockView.findViewById(R.id.switcher_second_tens);
        secondTensSwitcher.setFactory(mFactory0);

        secondDigitsSwitcher = (TextSwitcher) clockView.findViewById(R.id.switcher_second_digits);
        secondDigitsSwitcher.setFactory(mFactory0);
    }

    /*冒号view*/
    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {
            TextView view = new TextView(getContext());
            view.setTextSize(textSize);
            view.setTextColor(textColor);
            return view;
        }
    };


    /*时间*/
    private ViewSwitcher.ViewFactory mFactory0 = new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {
            TextView view = new TextView(getContext());
            view.setTextSize(textSize);
            view.setTextColor(textColor);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_text_bg_blue));
            return view;
        }
    };

    // 开始同步时间
    public void start(long time) {
        this.time = time;
        startLoopTask();
    }

    /*初始化时间*/
    public void formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = ms / dd;
        hour = (int) ((ms - day * dd) / hh);
        minute = (int) ((ms - day * dd - hour * hh) / mi);
        second = (int) ((ms - day * dd - hour * hh - minute * mi) / ss);
        if (DEBUG) L.i(hour + ":" + minute + ":" + second);
    }

    // 停止同步时间
    public void stop() {
        stopLoopTask();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            resetSecond();
            if(mHandler!=null){
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    private void startLoopTask() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(mRunnable);
    }

    private void stopLoopTask() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
    }


    private void resetSecond() {
        if (time / 1000 == 0) {
            if (mOnTimeFinish != null) {
                mOnTimeFinish.onTimeFinish();
            }
            stop();
        }
        formatTime(time);
        time = time - 1000;
        int hourOne = getTimeOne(hour);//1
        int hourTwo = getTimeTwo(hour);//6
        int minuteOne = getTimeOne(minute);//2
        int minuteTwo = getTimeTwo(minute);//0
        int secondOne = getTimeOne(second);//2
        int secondTwo = getTimeTwo(second);//2
        L.i("&" + hourOne + "&" + hourTwo + "&" + minuteOne + "&" + minuteTwo + "&" + secondOne + "&" + secondTwo);
        updateView(hourOne, hourTwo, minuteOne, minuteTwo, secondOne, secondTwo);

        // 临时缓存
        lastHourTens = hourOne;
        lastHourDigits = hourTwo;
        lastMinuteTens = minuteOne;
        lastMinuteDigits = minuteTwo;
        lastSecondTens = secondOne;
        lastSecondDigits = secondTwo;
    }

    /*1*/
    private void updateView(int hourOne, int hourTwo, int minuteOne,
                            int minuteTwo, int secondOne, int secondTwo) {
        // 时间中间点，跳动效果
        if (secondTwo % 2 != 0) {
            mSeparatorOneSwitcher.setText("");
            mSeparatorTwoSwitcher.setText("");
        } else {
            mSeparatorOneSwitcher.setText(":");
            mSeparatorTwoSwitcher.setText(":");
        }

        if (lastHourTens != hourOne) {
            hourTensSwitcher.setText("" + hourOne);
        }

        if (lastHourDigits != hourTwo) {
            hourDigitsSwitcher.setText("" + hourTwo);
        }

        if (lastMinuteTens != minuteOne) {
            minuteTensSwitcher.setText("" + minuteOne);
        }

        if (lastMinuteDigits != minuteTwo) {
            minuteDigitsSwitcher.setText("" + minuteTwo);
        }

        if (lastSecondTens != secondOne) {
            secondTensSwitcher.setText("" + secondOne);
        }

        if (lastSecondDigits != secondTwo) {
            secondDigitsSwitcher.setText("" + secondTwo);
        }
    }

    private int getTimeOne(int time) {
        return time / 10;
    }

    private int getTimeTwo(int second) {
        return second % 10;
    }

    private OnTimeFinish mOnTimeFinish;

    public interface OnTimeFinish {
        void onTimeFinish();
    }

    public void setOnTimeFinish(OnTimeFinish onTimeFinish) {
        this.mOnTimeFinish = onTimeFinish;
    }
}
