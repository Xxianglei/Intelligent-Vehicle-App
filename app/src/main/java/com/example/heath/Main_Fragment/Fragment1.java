package com.example.heath.Main_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.heath.R;
import com.example.heath.utils.LoadWeb;

/**
 * Created by Administrator on 2017/8/8.
 */
public class Fragment1 extends Fragment{


    private WebView webView;
    private ProgressBar progressBar;
    private LoadWeb loadWeb;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment1, container, false);
        initView(view);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.setOnKeyListener(backListener);
        return view;
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.progressbar);
        webView = view.findViewById(R.id.webview);
        loadWeb = new LoadWeb(webView,progressBar,getActivity());
        loadWeb.load("http://muzhi.baidu.com/");
    }
    private View.OnKeyListener backListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
                webView.goBack(); // goBack()表示返回webView的上一页面
                return true;
            }
            return false;
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放资源
        webView.destroy();
        webView = null;
        super.onDestroy();
    }
}

