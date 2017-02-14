package com.meiqia.meiqiasdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.meiqia.meiqiasdk.R;

import lecho.lib.hellocharts.model.PieChartData;

public class CircularProgressBar extends View {
    private int backgroundColor = -7829368;
    private Paint backgroundPaint;
    private Paint backgroundRectPaint;
    private float backgroundStrokeWidth = 8.0f;
    private int   color                 = -16777216;
    private Paint foregroundPaint;
    private float progress = 0.0f;
    private RectF rectF;
    private RectF rectFRect;
    private int   startAngle  = -90;
    private float strokeWidth = 8.0f;

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.backgroundColor = getResources().getColor(R.color.mq_circle_progress_bg);
        this.color = getResources().getColor(R.color.mq_circle_progress_color);
        this.rectF = new RectF();
        this.rectFRect = new RectF();
        this.backgroundPaint = new Paint(1);
        this.backgroundPaint.setColor(this.backgroundColor);
        this.backgroundPaint.setStyle(Style.STROKE);
        this.backgroundPaint.setStrokeWidth(this.backgroundStrokeWidth);
        this.backgroundRectPaint = new Paint(1);
        this.backgroundRectPaint.setColor(this.backgroundColor);
        this.backgroundRectPaint.setStyle(Style.STROKE);
        this.backgroundRectPaint.setStrokeWidth(this.strokeWidth);
        this.backgroundRectPaint.setStyle(Style.FILL);
        this.foregroundPaint = new Paint(1);
        this.foregroundPaint.setColor(this.color);
        this.foregroundPaint.setStyle(Style.STROKE);
        this.foregroundPaint.setStrokeWidth(this.strokeWidth);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(this.rectF, this.backgroundPaint);
        Canvas canvas2 = canvas;
        canvas2.drawArc(this.rectF, (float) this.startAngle, (360.0f * this.progress) / 100.0f,
                false, this.foregroundPaint);
        canvas.drawRect(this.rectFRect, this.backgroundRectPaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = this.strokeWidth > this.backgroundStrokeWidth ? this.strokeWidth :
                this.backgroundStrokeWidth;
        this.rectF.set((highStroke / 2.0f) + 0.0f, (highStroke / 2.0f) + 0.0f, ((float) min) -
                (highStroke / 2.0f), ((float) min) - (highStroke / 2.0f));
        this.rectFRect.set(((float) height) * 0.4f, ((float) width) * 0.4f, ((float) height) *
                PieChartData.DEFAULT_CENTER_CIRCLE_SCALE, ((float) width) * PieChartData
                .DEFAULT_CENTER_CIRCLE_SCALE);
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float progress) {
        this.progress = progress <= 100.0f ? progress : 100.0f;
        invalidate();
        if (progress >= 100.0f) {
            this.progress = 0.0f;
        }
    }

    public float getProgressBarWidth() {
        return this.strokeWidth;
    }

    public void setProgressBarWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.foregroundPaint.setStrokeWidth(strokeWidth);
        requestLayout();
        invalidate();
    }

    public float getBackgroundProgressBarWidth() {
        return this.backgroundStrokeWidth;
    }

    public void setBackgroundProgressBarWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        this.backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();
        invalidate();
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
        this.foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }
}
