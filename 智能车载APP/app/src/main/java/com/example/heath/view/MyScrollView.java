package com.example.heath.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public  class MyScrollView extends ScrollView {
    /**
     * 是否可以滚动
     */
    private boolean isCanScroll;
    /**
     * 手势滑动对象
     */
    private GestureDetector mGestureDetector;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (isCanScroll) {
                    // 根据当前滑动的X,Y距离来判断手势滑动的主要方向
                    isCanScroll = Math.abs(distanceY) >= Math.abs(distanceX);
                }
                return isCanScroll;
            }
        });
        isCanScroll = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            isCanScroll = true;
        }
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }
}