package com.boohee.circleview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;

import java.text.NumberFormat;

public class LineIndicator extends View {
    private static final int   GAP          = 6;
    private static final int   MARGIN       = 24;
    public static final  float MAX          = 0.942f;
    public static final  float MIN          = 0.065f;
    private static final int   STROKE_WIDTH = 28;
    private String mAlert;
    private int    mAlertColor;
    private int    mAlertSize;
    private int    mHeight;
    private int    mIndicatorBgColor;
    private int    mIndicatorProgressColor;
    private int    mIndicatorSize;
    private int    mIndicatorTextColor;
    private String mLeftAlert;
    private String mLeftContent;
    private float  mProgress;
    private String mRightAlert;
    private String mRightContent;
    private Paint  mTextPaint;
    private int    mWidth;

    public LineIndicator(Context context) {
        this(context, null);
    }

    public LineIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mWidth = 0;
        this.mHeight = 0;
        this.mProgress = 0.0f;
        this.mLeftAlert = " ";
        this.mLeftContent = " ";
        this.mRightAlert = " ";
        this.mRightContent = " ";
        this.mAlertSize = (int) getResources().getDimension(R.dimen.line_alert_size);
        this.mIndicatorSize = (int) getResources().getDimension(R.dimen.line_indicator_size);
        this.mAlertColor = getResources().getColor(R.color.line_alert);
        this.mIndicatorBgColor = getResources().getColor(R.color.line_indicator_bg);
        this.mIndicatorProgressColor = getResources().getColor(R.color.line_indicator_progress);
        this.mIndicatorTextColor = getResources().getColor(R.color.line_indicator_text);
        this.mTextPaint = getAlertPaint();
    }

    private Paint getAlertPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(this.mAlertColor);
        paint.setTextSize((float) this.mAlertSize);
        return paint;
    }

    private Paint getProgressPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(this.mAlertColor);
        paint.setStrokeWidth(28.0f);
        paint.setStyle(Style.STROKE);
        paint.setStrokeCap(Cap.ROUND);
        return paint;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int textHeight = ViewUtils.getTextHeight(this.mTextPaint);
        setMeasuredDimension(width, ((((0 + textHeight) + 24) + 56) + 24) + textHeight);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(this.mLeftAlert, 6.0f, (float) ViewUtils.getTextHeight(this.mTextPaint),
                this.mTextPaint);
        canvas.drawText(this.mLeftContent, 6.0f, (float) (this.mHeight - 6), this.mTextPaint);
        int rightContentX = (this.mWidth - ViewUtils.getTextWidth(this.mTextPaint, this
                .mRightContent)) - 6;
        canvas.drawText(this.mRightAlert, (float) ((this.mWidth - ViewUtils.getTextWidth(this
                .mTextPaint, this.mRightAlert)) - 6), (float) ViewUtils.getTextHeight(this
                .mTextPaint), this.mTextPaint);
        canvas.drawText(this.mRightContent, (float) rightContentX, (float) (this.mHeight - 6),
                this.mTextPaint);
        drawProgress(canvas);
    }

    private void drawProgress(Canvas canvas) {
        Paint bgPaint = getProgressPaint();
        bgPaint.setColor(this.mIndicatorBgColor);
        canvas.drawLine(14.0f, (float) (this.mHeight / 2), (float) (this.mWidth - 14), (float)
                (this.mHeight / 2), bgPaint);
        if (this.mProgress != 0.0f) {
            Paint progressPaint = getProgressPaint();
            progressPaint.setColor(this.mIndicatorProgressColor);
            int stopX = (int) (((float) (this.mWidth - 28)) * this.mProgress);
            canvas.drawLine(14.0f, (float) (this.mHeight / 2), (float) stopX, (float) (this
                    .mHeight / 2), progressPaint);
            Paint textPain = getAlertPaint();
            textPain.setTextSize((float) this.mIndicatorSize);
            textPain.setColor(this.mIndicatorTextColor);
            int textWidth = ViewUtils.getTextWidth(textPain, this.mAlert);
            Paint alertPaint = getProgressPaint();
            alertPaint.setColor(this.mIndicatorProgressColor);
            alertPaint.setStrokeWidth(78.4f);
            canvas.drawLine((float) (stopX - (textWidth / 2)), (float) (this.mHeight / 2),
                    (float) ((textWidth / 2) + stopX), (float) (this.mHeight / 2), alertPaint);
            FontMetricsInt fmi = textPain.getFontMetricsInt();
            Canvas canvas2 = canvas;
            canvas2.drawText(this.mAlert, (float) (stopX - (textWidth / 2)), (float) ((this
                    .mHeight / 2) + (Math.abs(fmi.bottom + fmi.top) / 2)), textPain);
        }
    }

    public void setContent(String leftAlert, String leftContent, String rightAlert, String
            rightContent) {
        this.mLeftAlert = leftAlert;
        this.mLeftContent = leftContent;
        this.mRightAlert = rightAlert;
        this.mRightContent = rightContent;
        postInvalidate();
    }

    public void setIndicator(float start, float end, float progress) {
        setIndicator(start, end, progress, "");
    }

    public void setIndicator(float start, float end, float progress, String alert) {
        float value = Math.abs((progress - start) / (end - start));
        if (TextUtils.isEmpty(alert)) {
            this.mAlert = NumberFormat.getPercentInstance().format((double) value);
        } else {
            this.mAlert = alert;
        }
        animateIndicator(value);
    }

    public void setProgress(float progress) {
        if (progress > MAX) {
            this.mProgress = MAX;
        } else if (progress < MIN) {
            this.mProgress = MIN;
        } else {
            this.mProgress = progress;
        }
        postInvalidate();
    }

    public float getProgress() {
        return this.mProgress;
    }

    public void animateIndicator(float progress) {
        Interpolator interpolator = new AnticipateOvershootInterpolator(1.8f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(this, "progress", new float[]{progress});
        animation.setDuration(3000);
        animation.setInterpolator(interpolator);
        animation.start();
    }
}
