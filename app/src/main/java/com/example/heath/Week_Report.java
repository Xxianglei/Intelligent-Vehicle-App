package com.example.heath;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.XueyaModle;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.Model.Person_Bpre;
import com.example.heath.utils.LoadWeb;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

/**
 * Created by 丽丽超可爱 on 2018/4/23.
 */

public class Week_Report extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private LoadWeb loadWeb;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_report);
        MyApplication.getInstance().addActivity(this);
        initview();
    }

    private void initview() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("体检报告");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressbar);//进度条
        webView = (WebView) findViewById(R.id.webview);
        loadWeb = new LoadWeb(webView, progressBar, this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (intent.getExtras() != null) {
            loadWeb.load(bundle.getString("url"));
            System.out.println(bundle.getString("url"));

        }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen", "是否有上一个页面:" + webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        webView.destroy();
        webView = null;

    }


}