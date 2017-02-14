package com.boohee.circleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.tencent.tinker.android.dx.instruction.Opcodes;

public class CircleProgress extends BaseCircle {
    private static final int CIRCLE_STROKE_WIDTH = 56;
    private int     mCircleWhite;
    private float[] mDividerIndicator;
    private float   mProgress;
    private String  mProgressAlert;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCircleWhite = getResources().getColor(R.color.circle_white);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutSideText(canvas);
        drawBackground(canvas);
        drawCircle(canvas);
        drawContent(canvas);
        drawIndicator(canvas);
    }

    private void drawOutSideText(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(this.mNormalGray);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimension(R.dimen.out_indicator_size));
        int radius = getViewRadius() - this.mDividerWidth;
        Path path = new Path();
        path.addCircle((float) getCenterX(), (float) getCenterY(), (float) radius, Direction.CW);
        String content = BaseCircle.formatNumber(this.mStartIndicator);
        canvas.drawTextOnPath(content, path, ViewUtils.getCirclePathLength((float) radius, 135
        .0f) - ((float) (ViewUtils.getTextWidth(textPaint, content) / 2)), 0.0f, textPaint);
        content = BaseCircle.formatNumber(this.mEndIndicator);
        canvas.drawTextOnPath(content, path, ViewUtils.getCirclePathLength((float) radius, 405
        .0f) - ((float) (ViewUtils.getTextWidth(textPaint, content) / 2)), 0.0f, textPaint);
        if (this.mDividerIndicator != null && this.mDividerIndicator.length != 0) {
            int sizeOfDivider = this.mDividerIndicator.length;
            int perAngle = 270 / (sizeOfDivider + 1);
            for (int i = 1; i <= sizeOfDivider; i++) {
                content = BaseCircle.formatNumber(this.mDividerIndicator[i - 1]);
                canvas.drawTextOnPath(content, path, ViewUtils.getCirclePathLength((float)
                        radius, (float) ((perAngle * i) + Opcodes.FLOAT_TO_INT)) - ((float)
                        (ViewUtils.getTextWidth(textPaint, content) / 2)), 0.0f, textPaint);
            }
        }
    }

    private void drawCircle(Canvas canvas) {
        int radius = (int) (((float) getViewRadius()) - (2.6f * ((float) this.mDividerWidth)));
        RectF oval = new RectF((float) (getCenterX() - radius), (float) (getCenterY() - radius),
                (float) (getCenterX() + radius), (float) (getCenterY() + radius));
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(56.0f);
        circlePaint.setStyle(Style.STROKE);
        circlePaint.setStrokeCap(Cap.ROUND);
        circlePaint.setColor(this.mCircleGray);
        canvas.drawArc(oval, 135.0f, 270.0f, false, circlePaint);
        if (this.mEndIndicator > this.mStartIndicator && this.mProgress >= this.mStartIndicator) {
            circlePaint.setColor(this.mCircleGreen);
            float angle = ((this.mProgress - this.mStartIndicator) * 270.0f) / (this
                    .mEndIndicator - this.mStartIndicator);
            canvas.drawArc(oval, 135.0f, angle, false, circlePaint);
            if (!TextUtils.isEmpty(this.mProgressAlert)) {
                drawAlert(canvas, radius, angle);
            }
        }
    }

    private void drawAlert(Canvas canvas, int radius, float angle) {
        Paint textPaint = new Paint();
        textPaint.setColor(this.mCircleWhite);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimension(R.dimen.out_indicator_size));
        FontMetricsInt fmi = textPaint.getFontMetricsInt();
        float textRadius = (float) (radius - (Math.abs(fmi.bottom + fmi.top) / 2));
        Path path = new Path();
        path.addCircle((float) getCenterX(), (float) getCenterY(), textRadius, Direction.CW);
        float circlePathLength = ViewUtils.getCirclePathLength(textRadius, 135.0f + (angle / 2
        .0f)) - ((float) (ViewUtils.getTextWidth(textPaint, this.mProgressAlert) / 2));
        canvas.drawTextOnPath(String.valueOf(this.mProgressAlert), path, circlePathLength, 0.0f,
                textPaint);
    }

    public void setIndicatorValue(float start, float end, float progress, float indicator,
                                  float... divider) {
        setIndicatorValue(start, end, progress, "", indicator, divider);
    }

    public void setIndicatorValue(float start, float end, float progress, String progressAlert,
                                  float indicator, float... divider) {
        if (start < end && progress <= end && progress >= start) {
            this.mStartIndicator = start;
            this.mEndIndicator = end;
            this.mProgress = progress;
            this.mProgressAlert = progressAlert;
            this.mDividerIndicator = divider;
            int i = 0;
            while (i < divider.length) {
                if (divider[i] > end || divider[i] < start) {
                    divider[i] = start;
                }
                i++;
            }
            animateIndicator(indicator);
        }
    }
}
