package com.example.heath.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by 丽丽超可爱 on 2018/5/10.
 */

public class LoadWeb {

    private static ProgressBar progressBar;
    private static WebView webView;
    private static  Context context;
    public LoadWeb(WebView webView,ProgressBar progressBar,Context context) {
        this.progressBar=progressBar;
        this.webView=webView;
        this.context=context;
    }
    public LoadWeb() {

    }
    public static void load(String url) {

        webView.loadUrl(url);
        webView.addJavascriptInterface(context, "android");//添加js监听 这样html就能调用客户端
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        //允许浏览器保存doom原型,这样js就可以调用这个方法了
        webView.getSettings().setDomStorageEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);   //不使用缓存，只从网络获取数据.

    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private static WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {  //页面加载完成
            webView.loadUrl("javascript:document.getElementById(\"header\").style.display=\"none\";");
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }


    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private static WebChromeClient webChromeClient = new WebChromeClient() {
          //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };
    /**
     * JS调用android的方法
     *
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void getClient(String str) {
        Log.i("ansen", "html调用客户端:" + str);
    }


}
