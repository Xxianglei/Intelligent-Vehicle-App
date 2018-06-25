package com.example.heath.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * px与dp互相转换
 *
 */
public class DensityUtils {

    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }
    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }
}
