package com.liubin.fenghui.defineview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.liubin.fenghui.binview.ClockTimeView;
import com.liubin.fenghui.binview.Util.L;

/**
 * Created by LiuBin on 2017/12/18.
 */

public class ClockTimeViewActivity extends AppCompatActivity {
    private ClockTimeView mClockTimeView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_view);
        mClockTimeView = (ClockTimeView) findViewById(R.id.time_clock);
        mClockTimeView.setOnTimeFinish(new ClockTimeView.OnTimeFinish() {
            @Override
            public void onTimeFinish() {
                L.i("finish");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mClockTimeView.start(10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mClockTimeView.stop();
    }
}
