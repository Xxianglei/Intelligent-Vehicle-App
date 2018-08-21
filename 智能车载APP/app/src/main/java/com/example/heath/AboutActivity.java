package com.example.heath;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 丽丽超可爱 on 2018/4/4.
 */

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//继承AppCompatActivity使用
        setContentView(R.layout.myabout);
        MyApplication.getInstance().addActivity(this);
        initview();
    }

    private void initview() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("关于");
        toolbar.setTitleTextColor(Color.WHITE);
        // 副标题
        //  toolbar.setSubtitle("Sub title");
        // toolbar.setSubtitleTextColor(Color.parseColor("#80ff0000"));
        //侧边栏的按钮
        toolbar.setNavigationIcon(R.mipmap.back);
        //取代原本的actionbar
        setSupportActionBar(toolbar);
        //设置NavigationIcon的点击事件,需要放在setSupportActionBar之后设置才会生效,
        //因为setSupportActionBar里面也会setNavigationOnClickListener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
