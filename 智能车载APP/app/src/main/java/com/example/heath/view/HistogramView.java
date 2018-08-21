package com.example.heath.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heath.R;
import com.example.heath.utils.DisplayUtil;

import java.util.Arrays;

public class HistogramView extends HorizontalScrollView {
    private static final int DEFAULT_COLUMN_PER_SCREEN = 7;
    private static final String[] DEFAULT_DATE_TEXT = new String[]{"一", "二", "三", "四", "五", "六", "日"};
    private static final int DEFAULT_COLOR = 0XFF3F51B5;
    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int PLAY = 0;
    private String[] mDefaultDateText = DEFAULT_DATE_TEXT;
    private int mColumnPerScreen = DEFAULT_COLUMN_PER_SCREEN;
    private int mColumnWid = 0;
    private int mDateTextColor = DEFAULT_COLOR;
    private int mHistogramColor = DEFAULT_COLOR;
    private int mDateTextSize = DEFAULT_TEXT_SIZE;
    private LinearLayout llHistogram;
    private LinearLayout llTime;
    private LinearLayout parent;
    private int mIndex = 0;
    private boolean isPlaying = false;
    private int mLastSelected = 0;
    private OnSelectListener mSelectListener;
    private OnClickListener mColumnListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setCheck(v.getId());
        }
    };
    private AnimationListener mAnimationListener;
    private Handler mPlayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY:
                    if (mIndex >= llHistogram.getChildCount()) {
                        // 滑动到最右侧
                        fullScroll(FOCUS_RIGHT);
                        // 默认选择最右边的那个
                       /* ColumnView v = (ColumnView) llHistogram.getChildAt(llHistogram.getChildCount() - 1);
                        v.performClick();*/
                        isPlaying = false;
                        mIndex = 0;
                        if (mAnimationListener != null)
                            mAnimationListener.onAnimationDone();
                        break;
                    }
                    ColumnView v = (ColumnView) llHistogram.getChildAt(mIndex);
                    v.startAnim();
                    mIndex++;
                    sendEmptyMessageDelayed(PLAY, 50);
                    break;
            }
        }
    };

    public HistogramView(Context context) {
        super(context);

        init(context, null);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 隐藏滑动条
        setHorizontalScrollBarEnabled(false);

        parent = new LinearLayout(context);
        parent.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        addView(parent);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HistogramView);

            int columnsPer = a.getInteger(R.styleable.HistogramView_column_per_screen,
                    DEFAULT_COLUMN_PER_SCREEN);
            mDateTextColor = a.getColor(R.styleable.HistogramView_date_text_color, DEFAULT_COLOR);
            mHistogramColor = a.getColor(R.styleable.HistogramView_histogram_color, DEFAULT_COLOR);
            int textSizeSp = a.getDimensionPixelSize(R.styleable.HistogramView_date_text_size, -1);

            setColumnPerScreen(columnsPer);
            setDateTextColor(mDateTextColor);
            setDateTextSize(textSizeSp);

            a.recycle();
        }
    }

    public void setData(HistogramEntity[] data) {

        if (isPlaying) {
            return;
        }
        if (data == null || data.length == 0) {
            return;
        }

        isPlaying = true;

        mColumnWid = getMeasuredWidth() / mColumnPerScreen;

        mLastSelected = 0;

        int max = maxInArray(data);

        llHistogram.removeAllViews();
        llTime.removeAllViews();

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(mColumnWid,
                ViewGroup.LayoutParams.WRAP_CONTENT);

//        param.leftMargin = mColumnWid;

        for (int i = 0; i < data.length; i++) {
            int d = data[i].count;
            ColumnView view = new ColumnView(getContext());
            view.setLayoutParams(param);
            if (max != 0) {
                view.setRatio((float) d / (float) max);
//                if (d != 0)
                view.setShowText(String.valueOf(d));
            } else {
                view.setRatio(0);
                // 全部为0则不显示数字
                // view.setShowText(String.valueOf(0));
            }
            view.setId(i);
            view.setColumnColor(mHistogramColor);
            view.setOnClickListener(mColumnListener);
            llHistogram.addView(view);
        }

        for (int i = 0; i < data.length; i++) {
            TextView view = new TextView(getContext());
            view.setGravity(Gravity.CENTER);
            view.setTextSize(mDateTextSize);
            view.setTextColor(mDateTextColor);
            view.setLayoutParams(param);
            view.setText(data[i].time);
            view.setId(i);
            view.setOnClickListener(mColumnListener);
            llTime.addView(view);
        }
        requestLayout();

        play();
    }

    private void play() {
        mPlayHandler.sendEmptyMessage(PLAY);
    }

    private void initHistogram() {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        param.weight = 1;
        llHistogram = new LinearLayout(getContext());
        llHistogram.setOrientation(LinearLayout.HORIZONTAL);
        llHistogram.setLayoutParams(param);
        parent.addView(llHistogram);
    }

    private void initTime() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp2px(5);
        llTime = new LinearLayout(getContext());
        llTime.setOrientation(LinearLayout.HORIZONTAL);
        llTime.setLayoutParams(params);
        parent.addView(llTime);
        LinearLayout.LayoutParams childParam = new LinearLayout.LayoutParams(mColumnWid,
                ViewGroup.LayoutParams.WRAP_CONTENT);
   //        childParam.leftMargin = mColumnWid;
        for (int i = 0; i < mDefaultDateText.length; i++) {
            TextView view = new TextView(getContext());
            view.setGravity(Gravity.CENTER);
            view.setTextSize(mDateTextSize);
            view.setTextColor(mDateTextColor);
            view.setLayoutParams(childParam);
            view.setText(mDefaultDateText[i]);
            llTime.addView(view);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mColumnWid = w / mColumnPerScreen;

        initHistogram();
        initTime();
    }

    private int maxInArray(HistogramEntity[] array) {
        int[] temp = new int[array.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = array[i].count;
        }
        Arrays.sort(temp);
        return temp[temp.length - 1];
    }

    protected int dp2px(int dpValue) {
        if (!isInEditMode())
            return DisplayUtil.dip2px(getContext(), dpValue);
        else
            return 20;
    }

    protected int px2dp(int pxValue) {
        if (!isInEditMode())
            return DisplayUtil.px2dip(getContext(), pxValue);
        else
            return 20;
    }

    public void setDefaultDateTextArray(String[] defaultDateTextArray) {
        if (defaultDateTextArray == null || defaultDateTextArray.length == 0)
            return;
        mDefaultDateText = defaultDateTextArray;
    }

    public void setCheck(int position) {
        if (isPlaying || llHistogram == null)
            return;
        if (position < 0 || position > llHistogram.getChildCount())
            return;
        ColumnView columnOld = (ColumnView) llHistogram.getChildAt(mLastSelected);
        columnOld.setSelect(false);
        ColumnView columnNew = (ColumnView) llHistogram.getChildAt(position);
        columnNew.setSelect(true);
        mLastSelected = position;
        if (mSelectListener != null)
            mSelectListener.onSelected(position);
    }

    public void setColumnPerScreen(int columnPerScreen) {
        if (columnPerScreen < 1 || columnPerScreen > 10) {
            return;
        }
        mColumnPerScreen = columnPerScreen;
    }

    public void setDateTextColor(int color) {
        mDateTextColor = color;
    }

    public void setDateTextSize(int size) {
        if (size < 0 || size > 20) {
            return;
        }
        mDateTextSize = size;
    }

    public void setSelectListener(OnSelectListener listener) {
        mSelectListener = listener;
    }

    public void setAnimationListener(AnimationListener listener) {
        mAnimationListener = listener;
    }

    public interface OnSelectListener {
        void onSelected(int index);
    }

    public interface AnimationListener {
        void onAnimationDone();
    }

    public static class HistogramEntity {
        public String time;
        public int count;

        public HistogramEntity() {
        }

        public HistogramEntity(String time, int count) {
            this.time = time;
            this.count = count;
        }
    }
}
