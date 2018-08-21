package com.example.heath.utils;

import java.util.Calendar;


/**
 * Created by Salmon on 2016/6/21 0021.
 */
public class DateUtils {

    /**
     * calendar转字符串
     *
     * @param calendar
     * @return 格式如："2016-5-25 00:00:00"的字符串
     */
    public static String calendarToString(Calendar calendar) {
        String s = calendar.get(Calendar.YEAR) + "-" +
                (calendar.get(Calendar.MONTH) + 1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
        return s;
    }

    /**
     * 整形转换星期
     *
     * @param index
     * @return
     */
    public static String intToWeek(int index) {
        String result = "";
        switch (index) {
            case 1:
                result = "一";
                break;
            case 2:
                result = "二";
                break;
            case 3:
                result = "三";
                break;
            case 4:
                result = "四";
                break;
            case 5:
                result = "五";
                break;
            case 6:
                result = "六";
                break;
            case 7:
                result = "日";
                break;
        }
        return result;
    }
}
