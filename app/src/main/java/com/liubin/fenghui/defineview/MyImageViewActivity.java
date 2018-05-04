package com.liubin.fenghui.defineview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.liubin.fenghui.binview.MyImageView;
import com.liubin.fenghui.binview.TestView;
import com.liubin.fenghui.binview.Util.L;

import junit.framework.Test;

/**
 * Created by LiuBin on 2017/12/18.
 */

public class MyImageViewActivity extends AppCompatActivity {
    private MyImageView myImageView;
    private TestView testView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_imageview);
        myImageView = findViewById(R.id.my_imageview);
        myImageView.setOnFinishListener(new MyImageView.OnFinishListener() {
            @Override
            public void onFinish() {
                L.i("finish");
            }
        });
        testView = findViewById(R.id.testView);
        testView.setOnFinishListener(new TestView.OnFinishListener() {
            @Override
            public void onFinish() {
                L.i("finish");
            }
        });
        myImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));

    }
}
