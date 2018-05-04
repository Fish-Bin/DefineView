package com.liubin.fenghui.defineview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.liubin.fenghui.binview.TimeView;
import com.liubin.fenghui.binview.Util.L;

/**
 * Created by LiuBin on 2017/12/18.
 */

public class EditTextViewActivity extends AppCompatActivity {
    private TimeView mTimeView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_view);
        mTimeView = (TimeView) findViewById(R.id.time_clock);
        mTimeView.setOnTimeFinish(new TimeView.OnTimeFinish() {
            @Override
            public void onTimeFinish() {
                L.i("finish");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mTimeView.start(10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimeView.stop();
    }
}
