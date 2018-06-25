package com.example.heath.view.ExpandView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;


import com.example.heath.R;

public class ExpandView extends FrameLayout{


    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;
    private boolean mIsExpand;
    private View view;

    public ExpandView(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }
    public ExpandView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        // TODO Auto-generated constructor stub
    }
    public ExpandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initExpandView();
    }
    public void initExpandView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_expand, this, true);

        mExpandAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand);
        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.VISIBLE);
            }
        });

        mCollapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.collapse);
        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.INVISIBLE);
            }
        });

    }

    public void collapse() {
        if (mIsExpand) {
            removeAllViews();
            mIsExpand = false;
            clearAnimation();
            startAnimation(mCollapseAnimation);

        }
    }

    public void expand() {
        if (!mIsExpand) {
            addView(view);
            mIsExpand = true;
            clearAnimation();
            startAnimation(mExpandAnimation);
        }
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    public View setContentView(int layout_expand){
        view = null;
        view = LayoutInflater.from(getContext()).inflate(layout_expand, null);
        View view1=view.getRootView();
        removeAllViews();
        return  view1;
    }

}