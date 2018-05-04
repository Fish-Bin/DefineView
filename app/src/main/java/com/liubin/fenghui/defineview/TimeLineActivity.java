package com.liubin.fenghui.defineview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.liubin.fenghui.binview.TimeLineView;

/**
 * Created by LiuBin on 2017/12/18.
 */

public class TimeLineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        final TimeLineView timeLineView=findViewById(R.id.time_line);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timeLineView.setmMarketDrawable(getResources().getDrawable(R.color.colorAccent));
                timeLineView.setmBeginLine(getResources().getDrawable(R.color.gray));
            }
        });
    }
}
