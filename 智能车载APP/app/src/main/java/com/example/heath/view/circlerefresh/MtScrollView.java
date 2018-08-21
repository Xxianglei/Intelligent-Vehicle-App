package com.example.heath.view.circlerefresh;

/**
 * Created by 丽丽超可爱 on 2018/4/14.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by 丽丽超可爱 on 2017/7/30.
 */
//   使 ScrollView 具有弹性
public class MtScrollView  extends ScrollView {


    public MtScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MtScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MtScrollView(Context context) {
        super(context);
    }

    /**
     * 滑动事件
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 2);
    }
}