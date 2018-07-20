package com.example.heath.utils;

/**
 * Created by Administrator on 2018/7/20.
 * 优化Json数据解析  解决UTF-8的BOM头
 */
public class JsonUtil {
    public static String JSONTokener(String in) {
        // consume an optional byte order mark (BOM) if it exists
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }
}
