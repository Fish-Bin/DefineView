package com.liubin.fenghui.defineview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.liubin.fenghui.binview.BorderImageView;
import com.liubin.fenghui.binview.TestView;
import com.liubin.fenghui.binview.Util.L;

/**
 * Created by LiuBin on 2017/12/18.
 */

public class BorderImageViewActivity extends AppCompatActivity {
    private BorderImageView borderImageView;
    private TestView testView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_imageview);
        borderImageView = findViewById(R.id.my_imageview);
        borderImageView.setOnFinishListener(new BorderImageView.OnFinishListener() {
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
        borderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));

    }
}
