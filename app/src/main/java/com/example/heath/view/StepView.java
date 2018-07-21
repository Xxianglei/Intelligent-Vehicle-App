package com.example.heath.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.heath.utils.DensityUtils;

import java.util.Random;

/**
 *
 * 小米运动连接时候的自定义View
 */

public class StepView extends View {

    private static final int STAGGER_ROTATE_DURATION = 2000;

    private static final int FINISH_ROTATE_DURATION = 5000;

    private static final int FINISH_SCALE_DURATION = 500;

    /**
     * 加载中，连接中
     */
    private static final int LOADING_STATUS = 1;

    /**
     * 加载成功，连接成功
     */
    private static final int FINISH_STATUS = 2;

    private static final int DOT_SIZE = 70;

    /**
     * 小圆点移动方向系数  就是 y = kx + b 的 k
     */
    public static float[] DOT_COEFFICIENTS = new float[]{0.5f, 0.4f, 0.15f, 0.1f, 0.05f};

    private Paint mPaint;

    /**
     * 交错圆环线条宽度
     */
    private float mStaggerStrokeWidth;
    /**
     * 交错圆环的半径
     */
    private int mStaggerRadius;

    private int mFinishBackStartColor = 0x66ffffff;

    private int mFinishBackEndColor = 0x00ffffff;

    private float mProgressRadius;

    private int mCenterX;

    private int mCenterY;

    /**
     * 大圆半径padding值
     */
    private int mRadiusPadding = DensityUtils.dp2px(getContext(), 70);

    /**
     * 扇形着色器
     */
    private SweepGradient mStaggerSweepGradient;

    /**
     * 交错圆环的角度
     */
    private float mStaggerDegrees;

    /**
     * 交错圆环的绘制参数
     */
    private StaggerCircle[] mStaggerCircles;

    /**
     * 交错圆环和圆点旋转动画
     */
    private ObjectAnimator mRotationAnimator;

    /**
     * 圆点的半径
     */
    private int mDotRadius;

    /**
     * 小圆点们
     */
    private Dot[] mDots;

    /**
     * 当前状态
     */
    private int mStatus = LOADING_STATUS;

    private Random mRandom = new Random(System.currentTimeMillis());

    private float mFinishStrokeWidth;

    private float mBaseCircleRadius;

    private float mFinishCircleRadius;

    private ObjectAnimator mFinishAnimator;

    private SweepGradient mFinishSweepGradient;

    /**
     * 光晕的线性渐变
     */
    private LinearGradient mFinishLinearBackGradient;

    /**
     * 光环下绘制的光晕的paint宽度
     */
    private float mFinishBackWidth;

    private int mMaxValue = 3600;

    private float mCurrentValue = 2753;

    private float mCurrentLength = 1.5f;

    private float mCalories = 34;

    private float mProgressStrokeWidth;

    private boolean isFinishAnimEnd;
    private String mValueStr;
    private String mSubStr;
    private int mValueTextSize;
    private int mSubTextSize;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);

        mRotationAnimator = ObjectAnimator.ofFloat(this, "staggerDegrees", 0f, 360f);
        mRotationAnimator.setDuration(STAGGER_ROTATE_DURATION);
        mRotationAnimator.setInterpolator(new LinearInterpolator());
        mRotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotationAnimator.start();

        mFinishAnimator = ObjectAnimator.ofFloat(this, "finishRadius", 1.0f, 1.3f, 1.1f);
        mFinishAnimator.setDuration(FINISH_SCALE_DURATION);
//        mFinishAnimator.setInterpolator(new OvershootInterpolator());

        mFinishAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isFinishAnimEnd = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isFinishAnimEnd = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isFinishAnimEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mValueTextSize = DensityUtils.sp2px(getContext(), 65);
        mSubTextSize = DensityUtils.sp2px(getContext(), 0);

        mValueStr = String.valueOf(mCurrentValue);
        mSubStr = String.valueOf("");
    }

    public void setmCurrentValue(float mCurrentValue) {
        this.mCurrentValue=mCurrentValue;
    }
    public void setmMaxValueint  (int mMaxValue) {
        this.  mMaxValue=  mMaxValue;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int mWidth = getWidth();
        int mHeight = getHeight();

        /*
      在手机内的显示范围
     */
        RectF mViewRectF = new RectF(0, 0, mWidth, mHeight);

        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;

        /*
      半径参考值
     */
        int mRadius = mCenterX > mCenterY ? mCenterY : mCenterX;

        mStaggerStrokeWidth = DensityUtils.dp2px(getContext(), 1.0f);
        mStaggerRadius = mRadius - mRadiusPadding;

        mProgressRadius = mRadius - mRadiusPadding - 30;
        mProgressStrokeWidth = DensityUtils.dp2px(getContext(), 3.0f);

        mFinishStrokeWidth = DensityUtils.dp2px(getContext(), 15.0f);
        // 起始是一样的
        mBaseCircleRadius = mStaggerRadius;
        mFinishCircleRadius = mBaseCircleRadius;

        int mStaggerEndColor = 0xffffffff;
        /*
      交错圆环的起始颜色
     */
        int mStaggerStartColor = 0x11ffffff;
        mStaggerSweepGradient = new SweepGradient(mCenterX, mCenterY,
                new int[]{mStaggerStartColor, mStaggerEndColor},
                new float[]{0.0f, 1.0f});

        int mFinishEndColor = 0x66ffffff;
        int mFinishStartColor = 0xffffffff;
        mFinishSweepGradient = new SweepGradient(mCenterX, mCenterY,
                new int[]{mFinishStartColor, mFinishEndColor, mFinishStartColor},
                new float[]{0.0f, 0.5f, 1.0f});

        mFinishBackWidth = DensityUtils.dp2px(getContext(), 16f);

        mFinishLinearBackGradient =
                new LinearGradient(mCenterX,
                        mCenterY, mCenterX + mFinishCircleRadius + mFinishStrokeWidth / 2 + 100,
                        mCenterY, 0x00ffffff, 0x33ffffff, Shader.TileMode.CLAMP);

        mStaggerCircles = new StaggerCircle[8];
        for (int i = 0; i < mStaggerCircles.length; i++) {
            StaggerCircle staggerCircle =
                    new StaggerCircle(getCloseCenter(mRandom, mCenterX),
                            getCloseCenter(mRandom, mCenterY),
                            getCloseRadius(mRandom, mStaggerRadius));
            mStaggerCircles[i] = staggerCircle;
        }

        mDotRadius = DensityUtils.dp2px(getContext(), 4.0f);

        generateDots();

    }

    private void generateDots() {
        mDots = new Dot[DOT_SIZE];
        for (int i = 0; i < DOT_SIZE; i++) {
            mDots[i] = new Dot(mRandom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (LOADING_STATUS == mStatus) {
            // 绘制交错的圆环
            drawStaggerCircle(canvas);

            // 绘制圆点
            drawDot(canvas);
        } else {
            // 绘制 完成时候的光圈
            drawFinishCircle(canvas);

            if (isFinishAnimEnd) {
                // 绘制进度条
                drawProgress(canvas);
            }

        }

        // 绘制文字和图片
        drawTextAndBitmap(canvas);
    }

    private void drawTextAndBitmap(Canvas canvas) {

        float strokeWidth = mPaint.getStrokeWidth();
        int color = mPaint.getColor();
        mPaint.setStrokeWidth(0);

        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mValueTextSize);
        Paint.FontMetrics valueFM = mPaint.getFontMetrics();
        float valueTextHeight = valueFM.bottom + valueFM.top;
        float valueTextWidth = mPaint.measureText(mValueStr);
        canvas.drawText(mValueStr, mCenterX - valueTextWidth / 2,
                mCenterY - valueTextHeight / 2, mPaint);


        mPaint.setColor(0x99ffffff);
        mPaint.setTextSize(mSubTextSize);
        Paint.FontMetrics subFM = mPaint.getFontMetrics();
        float subTextHeight = subFM.bottom + subFM.top + valueTextHeight * 2;
        float subTextWidth = mPaint.measureText(mSubStr);

        canvas.drawText(mSubStr, mCenterX - subTextWidth / 2,
                mCenterY - subTextHeight / 2, mPaint);

        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(color);
    }

    private void drawProgress(Canvas canvas) {

        int color = mPaint.getColor();
        float strokeWidth = mPaint.getStrokeWidth();
        Paint.Style style = mPaint.getStyle();
        Paint.Cap strokeCap = mPaint.getStrokeCap();

        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        mPaint.setPathEffect(effects);

        mPaint.setStrokeWidth(mStaggerStrokeWidth);
        mPaint.setColor(Color.WHITE);


        canvas.drawCircle(mCenterX, mCenterY, mProgressRadius, mPaint);
        mPaint.setPathEffect(null);

        float sweepAngle = mCurrentValue * 1.0f / mMaxValue * 360;

        float left = mCenterX - mProgressRadius;
        float top = mCenterY - mProgressRadius;
        float right = mCenterX + mProgressRadius;
        float bottom = mCenterY + mProgressRadius;


        mPaint.setStrokeWidth(mProgressStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(new RectF(left, top, right, bottom), -90, sweepAngle, false, mPaint);

        canvas.save();

        canvas.rotate(sweepAngle, mCenterX, mCenterY);

        canvas.translate(mCenterX, mCenterY);
        canvas.translate(0, -mProgressRadius);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mProgressStrokeWidth * 1.5f, mPaint);

        canvas.restore();

        mPaint.setStrokeCap(strokeCap);
        mPaint.setStyle(style);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(strokeWidth);
    }

    /**
     * 绘制完成状态下的光圈
     *
     * @param canvas
     */
    private void drawFinishCircle(Canvas canvas) {
        float strokeWidth = mPaint.getStrokeWidth();
        int color = mPaint.getColor();

        canvas.save();
        canvas.rotate(mStaggerDegrees, mCenterX, mCenterY);

        float left = mCenterX - mFinishCircleRadius;
        float top = mCenterY - mFinishCircleRadius;
        float bottom = mCenterY + mFinishCircleRadius;
        float right = mCenterX + mFinishCircleRadius;
        mPaint.setStrokeWidth(mFinishBackWidth);
        mPaint.setShader(mFinishLinearBackGradient);
        for (int i = 0; i < 4; i++) {
            right = right + 6;
            canvas.drawArc(new RectF(left, top, right, bottom), 0, 360, false, mPaint);
        }

        mPaint.setMaskFilter(null);
        mPaint.setShader(mFinishSweepGradient);
        mPaint.setStrokeWidth(mFinishStrokeWidth);
        canvas.drawCircle(mCenterX, mCenterY, mFinishCircleRadius, mPaint);


        canvas.restore();

        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(color);
        mPaint.setShader(null);
    }

    /**
     * 不断生成的圆点
     *
     * @param canvas
     */
    private void drawDot(Canvas canvas) {
        Paint.Style style = mPaint.getStyle();
        int color = mPaint.getColor();
        int alpha = mPaint.getAlpha();

        mPaint.setStyle(Paint.Style.FILL);
        /*
      圆点的颜色
     */
        int mDotColor = Color.WHITE;
        mPaint.setColor(mDotColor);

        canvas.save();
        // 先旋转角度
        canvas.rotate(mStaggerDegrees, mCenterX, mCenterY);
        // 再切换坐标系的0点
        canvas.translate(mCenterX + mStaggerRadius, mCenterY);

//        canvas.drawLine(0, 0, 50, -100, mPaint);
//        canvas.drawLine(0, 0, -50, -100, mPaint);
//        canvas.drawCircle(0, 0, mDotRadius, mPaint);

        for (int i = 0; i < mDots.length; i++) {
            Dot dot = mDots[i];
            mPaint.setAlpha(dot.alpha);
            canvas.drawCircle(dot.centerX, dot.centerY, dot.radius, mPaint);
            dot.refresh();
        }

        canvas.translate(-(mCenterX + mStaggerRadius), mCenterY);

        canvas.restore();
        mPaint.setAlpha(alpha);
        mPaint.setStyle(style);
        mPaint.setColor(color);
    }

    /**
     * 交错的圆环
     *
     * @param canvas
     */
    private void drawStaggerCircle(Canvas canvas) {
//        canvas.save();
//        canvas.rotate(mStaggerDegrees, mCenterX, mCenterY);
        float strokeWidth = mPaint.getStrokeWidth();
        int color = mPaint.getColor();


        mPaint.setShader(mStaggerSweepGradient);
        mPaint.setStrokeWidth(mStaggerStrokeWidth);
        for (int i = 0; i < mStaggerCircles.length; i++) {
            canvas.save();
            canvas.rotate(getCloseRotate(mRandom, mStaggerDegrees), mCenterX, mCenterY);
            canvas.drawCircle(mStaggerCircles[i].centerX,
                    mStaggerCircles[i].centerY, mStaggerCircles[i].radius, mPaint);
            canvas.restore();
        }


        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(color);
        mPaint.setShader(null);
//        canvas.restore();
    }

    private float getCloseRotate(Random random, float value) {
        // -12~13
        float randomValue = (random.nextFloat()) * value * 0.05f;
        return value - randomValue;
    }

    private int getCloseCenter(Random random, int value) {
        // -12~13
        int randomValue = random.nextInt(20) - 10;
        return value + randomValue;
    }

    private int getCloseRadius(Random random, int value) {
        // -5~5
        int randomValue = random.nextInt(40) - 20;
        return value + randomValue;
    }

    public float getStaggerDegrees() {
        return mStaggerDegrees;
    }

    public void setStaggerDegrees(float staggerDegrees) {
        this.mStaggerDegrees = staggerDegrees;
        this.invalidate();

    }

    private static class StaggerCircle {
        public int centerX;
        public int centerY;
        public int radius;

        public StaggerCircle(int centerX, int centerY, int radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }
    }

    private class Dot {

        /**
         * 每次圆点移动的距离，
         */
        public float moveLength = mDotRadius * 0.8f;

        /**
         * 圆点y轴移动总距离
         */
        public float totalYLength = -mDotRadius * 20;

        /**
         * 圆点x轴上初始可设置的范围
         * 就是 y = kx + b 中的b的大小范围
         */
        public float totalXLength = mDotRadius * 2;

        public float centerX;
        public float centerY;
        /**
         * 半径是一直递减的，递减的值就是 COEFFICIENT * radius
         */
        public float radius;

        /**
         * 透明度也一直是递减的，减到0的时候代表圆点消失，此时圆点则重新刷新数据，复活成一个新的点
         */
        public int alpha;

        /**
         * 判断x轴的方向的正方向还是负方向
         */
        public boolean isPositive;

        /**
         * 系数，通过系数，y轴左边的变化算出x轴的坐标
         */
        public float coefficient;

        /**
         * 就是 y = kx + b 中的b
         */
        public float offset;

        public Dot(Random random) {
            this.centerY = random.nextFloat() * totalYLength;
            this.radius = mDotRadius;
            this.isPositive = random.nextBoolean();
            int nextCoefficientInt = random.nextInt(DOT_COEFFICIENTS.length);

            // 这里是因为圆形是有角度的，所以正方向的系数不能过大，否则会显得左右不均匀
            if (nextCoefficientInt == 0) {
                isPositive = false;
            }
            this.coefficient = DOT_COEFFICIENTS[nextCoefficientInt];

            float x = Math.abs(random.nextFloat() * totalXLength);
            this.offset = isPositive ? x : -x;


            // 当前百分比
            float percent = getPercent();

            calculateAlpha(percent);
            calculateX(percent);
            calculateRadius(percent);
        }

        private void calculateX(float percent) {
            float centerX = Math.abs((centerY - offset) * coefficient);
            this.centerX = isPositive ? centerX : -centerX;
        }

        /**
         * 根据y轴移动的距离判断透明度
         *
         * @param percent
         */
        private void calculateAlpha(float percent) {
            this.alpha = (int) (percent * 255);
        }

        public void refresh() {

            // y轴
            calculateY();

            if (centerY == totalYLength) { // 圆点走到头了
                reload();
                return;
            }

            // 当前百分比
            float percent = getPercent();

            // 透明度
            calculateAlpha(percent);

            // y轴
            calculateX(percent);

            // 半径
            calculateRadius(percent);
        }

        private float getPercent() {
            return 1 - Math.abs(centerY / totalYLength);
        }

        private void calculateRadius(float percent) {
            this.radius = mDotRadius * percent;
        }

        private void reload() {
            this.centerY = 0;
            calculateAlpha(1.0f);
            calculateX(1.0f);
            calculateRadius(1.0f);
        }

        /**
         * 计算y轴变化
         */
        private void calculateY() {
            this.centerY = this.centerY - moveLength;
            if (this.centerY < totalYLength) {
                this.centerY = totalYLength;
            } else if (this.centerY > 0) {
                this.centerY = 0;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {

        if (mRotationAnimator != null && mRotationAnimator.isStarted()) {
            mRotationAnimator.cancel();
            mRotationAnimator = null;
        }

        if (mFinishAnimator != null && mFinishAnimator.isStarted()) {
            mFinishAnimator.cancel();
            mFinishAnimator = null;
        }

        super.onDetachedFromWindow();
    }

    public void loading() {
        mStatus = LOADING_STATUS;

        mRotationAnimator.setDuration(STAGGER_ROTATE_DURATION);

    }

    public void finish() {
        mValueStr = String.valueOf(mCurrentValue);
        mStatus = FINISH_STATUS;

//        mRotationAnimator.cancel();
        mRotationAnimator.setDuration(FINISH_ROTATE_DURATION);

        mFinishAnimator.start();
    }

    float getFinishRadius() {
        return mFinishCircleRadius;
    }

    void setFinishRadius(float percent) {
        this.mFinishCircleRadius = percent * mBaseCircleRadius;
        this.invalidate();
    }
}
