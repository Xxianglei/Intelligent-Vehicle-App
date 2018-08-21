package com.example.heath.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * download 指定 url 的文本信息的工具类
 */

public class HttpDownloader {

    /**
     * @param urlstr
     * @return
     */
    public String download(String urlstr) {
        StringBuffer sb = new StringBuffer();
        String line;
        BufferedReader buffer = null;
        try {
            Log.e("天气", "123456");
            URL url = new URL(urlstr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setConnectTimeout(8000);
            urlConn.setReadTimeout(8000);
            buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
