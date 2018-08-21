package com.example.heath.view;

import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.example.heath.R;


@SuppressLint("AppCompatCustomView")
public class ThemometerView extends ImageView {

	private Paint mPaint;
	private int height, width, circle_radius;
	private int COLOR_BROWN = Color.parseColor("#A5937B");
	private int COLOR_YELLOW = Color.parseColor("#F7AF1F");
	private int COLOR_GRAY = Color.parseColor("#C1CDCD");
	private int COLOR_TEXT = Color.parseColor("#49BDCC");
	private float temperature = 0f;
	private float temp_temperature = 0f;

	public void setTemperature(float value) {

		Log.e(this.getClass().getSimpleName(), "setTemperature " + value);

		if (value < 0f) {
			value = 0;
		} else if (value > 41.5f) {
			value = 41.5f;
		}

		if (value <= 34f) {
			value = new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
		} else {
			value = new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
		}
		if (temp_temperature > value) {
			temp_temperature = 0f;
		}
		temperature = value;
		invalidate();
	}

	public ThemometerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ThemometerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mPaint = new Paint();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.height = getMeasuredHeight();
		this.width = getMeasuredWidth();
		circle_radius = width / 4;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBG(canvas);
		drawScale(canvas);
		drawFG(canvas);

		if (temp_temperature < temperature && temp_temperature < 41.5) {
			if (temp_temperature < 34) {
				BigDecimal b1 = new BigDecimal(Float.toString(temp_temperature));
				BigDecimal b2 = new BigDecimal(Float.toString(1f));
				temp_temperature = b1.add(b2).floatValue();
			} else {
				BigDecimal b1 = new BigDecimal(Float.toString(temp_temperature));
				BigDecimal b2 = new BigDecimal(Float.toString(0.1f));
				temp_temperature = b1.add(b2).floatValue();
			}
			invalidate();
		}

	}


	private void drawScale(Canvas canvas) {
		drawLines(canvas);
		drawText(canvas);
	}

	private void drawText(Canvas canvas) {
		mPaint.reset();
		mPaint.setColor(COLOR_TEXT);
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(getResources().getDimension(R.dimen.scale_text_size));
		Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);
		mPaint.setTypeface(font);
		mPaint.setTextAlign(Paint.Align.CENTER);

		FontMetrics fontMetrics = mPaint.getFontMetrics();
		// �������ָ߶�
		float fontHeight = fontMetrics.bottom - fontMetrics.top;

		int line_width = circle_radius * 2 / 3 - 10;
		int text_x = this.width - getPaddingRight() - (circle_radius - 5 + circle_radius) - line_width - line_width / 2
				- 15;

		int begin = 41;
		float recH = this.height - getPaddingBottom() - circle_radius - circle_radius + 15 - (getPaddingTop() + 5);

		while (begin >= 35) {

			float addition = 41.5f - begin;
			float addition_h = addition / (41.5f - 34.5f) * recH;

			// ��������baseline
			canvas.drawText(String.valueOf(begin), text_x, getPaddingTop() + addition_h + fontHeight / 3, mPaint);
			begin--;
		}
	}

	private void drawFG(Canvas canvas) {
		drawYellowArc(canvas, temp_temperature);
		drawYellowRec(canvas, temp_temperature);
		drawRecShadow(canvas);
		drawCircleShadow(canvas);
	}

	private void drawCircleShadow(Canvas canvas) {
		mPaint.reset();
		mPaint.setColor(Color.WHITE);
		mPaint.setAntiAlias(true);
		mPaint.setAlpha(80);

		int radius = (circle_radius - 5) * 2 / 5;

		int circle_x = this.width - getPaddingRight() - circle_radius * 3 / 4;
		int circle_y = this.height - getPaddingBottom() - circle_radius * 5 / 4;

		canvas.drawCircle(circle_x, circle_y, radius, mPaint);
	}

	private void drawRecShadow(Canvas canvas) {
		// mPaint.reset();
		// mPaint.setColor(Color.WHITE);
		// mPaint.setAntiAlias(true);
		// mPaint.setAlpha(50);
		//
		float begin = 41.2f;
		float end = 34.8f;
		float totalH = this.height - getPaddingBottom() - circle_radius - circle_radius + 15 - (getPaddingTop() + 5);
		float recH = totalH * (begin - end) / (41.5f - 34.5f);
		float beginY = getPaddingTop() + (41.5f - begin) / (41.5f - 34.5f) * totalH;
		//
		Rect r2 = new Rect(); // RectF����
		r2.left = this.width - getPaddingRight() - circle_radius - circle_radius / 3 + 5 + circle_radius / 4; // ���
		r2.top = (int) beginY; // �ϱ�
		r2.right = this.width - getPaddingRight() - circle_radius + circle_radius / 3 - 5 - circle_radius / 10; // �ұ�
		r2.bottom = (int) (beginY + recH); // �±�
		// canvas.drawRect(r2, mPaint);

		Drawable d = getResources().getDrawable(R.drawable.bg_tiwenji_line);
		d.setBounds(r2);
		d.draw(canvas);
	}

	private void drawLines(Canvas canvas) {
		mPaint.reset();
		mPaint.setColor(COLOR_GRAY);
		mPaint.setAntiAlias(true);

		int line_width = circle_radius * 2 / 3 - 10;
		int line_x = this.width - getPaddingRight() - (circle_radius - 5 + circle_radius) - line_width;

		float begin = 41.0f;
		float recH = this.height - getPaddingBottom() - circle_radius - circle_radius + 15 - (getPaddingTop() + 5);

		while (begin >= 35) {

			float addition = 41.5f - begin;
			float addition_h = addition / (41.5f - 34.5f) * recH;
			int temp = (int) begin;
			if (begin - temp > 0) {
				canvas.drawLine(line_x + line_width * 3f / 4, getPaddingTop() + addition_h, line_x + line_width,
						getPaddingTop() + addition_h, mPaint);
			} else {
				canvas.drawLine(line_x, getPaddingTop() + addition_h, line_x + line_width, getPaddingTop() + addition_h,
						mPaint);
			}

			BigDecimal b1 = new BigDecimal(Float.toString(begin));
			BigDecimal b2 = new BigDecimal(Float.toString(0.1f));
			begin = b1.subtract(b2).floatValue();
		}

	}

	private void drawYellowRec(Canvas canvas, float temperature2) {
		if (temperature2 <= 34.5f)
			return;

		float addition = temperature2 - 34.5f;
		double rate = addition / (41.5d - 34.5d);
		if (rate > 1f) {
			rate = 1f;
		}

		rate = 1 - rate;

		mPaint.reset();
		mPaint.setColor(COLOR_YELLOW);
		mPaint.setAntiAlias(true);

		float recH = this.height - getPaddingBottom() - circle_radius - circle_radius + 15 - (getPaddingTop() + 5);
		RectF r2 = new RectF(); // RectF����
		r2.left = this.width - getPaddingRight() - circle_radius - circle_radius / 3 + 5; // ���
		r2.top = (float) (getPaddingTop() + 5 + recH * rate); // �ϱ�
		r2.right = this.width - getPaddingRight() - circle_radius + circle_radius / 3 - 5; // �ұ�
		r2.bottom = this.height - getPaddingBottom() - circle_radius - 5; // �±�

		if ((float) (getPaddingTop() + 5 + recH * rate) - 30 >= (getPaddingTop() + 5)) {
			canvas.drawRoundRect(r2, 0, 0, mPaint);
			RectF r3 = new RectF(); // RectF����
			r3.left = this.width - getPaddingRight() - circle_radius - circle_radius / 3 + 5; // ���
			r3.top = (float) (getPaddingTop() + 5 + recH * rate) - 20; // �ϱ�
			r3.right = this.width - getPaddingRight() - circle_radius + circle_radius / 3 - 5; // �ұ�
			r3.bottom = (float) (getPaddingTop() + 5 + recH * rate); // �±�
			canvas.drawRoundRect(r3, 0, 0, mPaint);

			mPaint.setColor(COLOR_BROWN);
			RectF r4 = new RectF(); // RectF����
			r4.left = this.width - getPaddingRight() - circle_radius - circle_radius / 3 + 5; // ���
			r4.top = (float) (getPaddingTop() + 5 + recH * rate) - 30; // �ϱ�
			r4.right = this.width - getPaddingRight() - circle_radius + circle_radius / 3 - 5; // �ұ�
			r4.bottom = (float) (getPaddingTop() + 5 + recH * rate); // �±�
			canvas.drawRoundRect(r4, 10, 10, mPaint);
		} else {
			if ((float) (getPaddingTop() + 5 + recH * rate) - 10 >= (getPaddingTop() + 5)) {
				canvas.drawRoundRect(r2, 0, 0, mPaint);
			} else
				canvas.drawRoundRect(r2, 20, 20, mPaint);
		}
	}

	private void drawYellowArc(Canvas canvas, float value) {
		mPaint.reset();
		mPaint.setColor(COLOR_YELLOW);
		mPaint.setAntiAlias(true);

		RectF r2 = new RectF(); // RectF����
		r2.left = this.width - getPaddingRight() - (circle_radius - 5 + circle_radius); // ���
		r2.top = this.height - getPaddingBottom() - (circle_radius - 5 + circle_radius); // �ϱ�
		r2.right = this.width - getPaddingRight() - 5; // �ұ�
		r2.bottom = this.height - getPaddingBottom() - 5;

		if (value <= 34.5f) {
			if (value >= 0) {
				float angle = (value / 34.5f) * 360;
				float start_angle = 90 - angle / 2;
				canvas.drawArc(r2, start_angle, angle, false, mPaint);
			}
		} else {
			canvas.drawArc(r2, -90, 360, false, mPaint);
		}

	}

	private void drawBG(Canvas canvas) {
		drawWhiteCircle(canvas);
		drawWhiteRec(canvas);
		drawBrownCircle(canvas);
		drawBrownRec(canvas);
	}

	private void drawBrownRec(Canvas canvas) {
		mPaint.reset();
		mPaint.setColor(COLOR_BROWN);
		mPaint.setAntiAlias(true);

		RectF r2 = new RectF(); // RectF����
		r2.left = this.width - getPaddingRight() - circle_radius - circle_radius / 3 + 5; // ���
		r2.top = getPaddingTop() + 5; // �ϱ�
		r2.right = this.width - getPaddingRight() - circle_radius + circle_radius / 3 - 5; // �ұ�
		r2.bottom = this.height - getPaddingBottom() - circle_radius - 5; // �±�
		canvas.drawRoundRect(r2, 20, 20, mPaint);
	}

	private void drawBrownCircle(Canvas canvas) {
		mPaint.reset();
		mPaint.setColor(COLOR_BROWN);
		mPaint.setAntiAlias(true);

		int brown_radius = circle_radius - 5;

		int circle_x = this.width - getPaddingRight() - circle_radius;
		int circle_y = this.height - getPaddingBottom() - circle_radius;

		canvas.drawCircle(circle_x, circle_y, brown_radius, mPaint);
	}

	private void drawWhiteRec(Canvas canvas) {
		mPaint.reset();
		mPaint.setColor(Color.WHITE);
		mPaint.setAntiAlias(true);

		RectF r2 = new RectF(); // RectF����
		r2.left = this.width - getPaddingRight() - circle_radius - circle_radius / 3; // ���
		r2.top = getPaddingTop(); // �ϱ�
		r2.right = this.width - getPaddingRight() - circle_radius + circle_radius / 3; // �ұ�
		r2.bottom = this.height - circle_radius; // �±�
		canvas.drawRoundRect(r2, 20, 20, mPaint);
	}

	private void drawWhiteCircle(Canvas canvas) {
		mPaint.reset();
		mPaint.setColor(Color.WHITE);
		mPaint.setAntiAlias(true);

		int circle_x = this.width - getPaddingRight() - circle_radius;
		int circle_y = this.height - getPaddingBottom() - circle_radius;

		canvas.drawCircle(circle_x, circle_y, circle_radius, mPaint);
	}

}
