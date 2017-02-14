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
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CircleIndicator extends BaseCircle {
    private static final int CIRCLE_STROKE_WIDTH = 16;
    private int                 mCircleWhite;
    private List<IndicatorItem> mDividerIndicator;

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDividerIndicator = new ArrayList();
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
        if (this.mDividerIndicator.size() != 0) {
            String content = BaseCircle.formatNumber(this.mStartIndicator);
            canvas.drawTextOnPath(content, path, ViewUtils.getCirclePathLength((float) radius,
                    135.0f) - ((float) (ViewUtils.getTextWidth(textPaint, content) / 2)), 0.0f,
                    textPaint);
            float perAngle = 270.0f / (this.mEndIndicator - this.mStartIndicator);
            for (IndicatorItem item : this.mDividerIndicator) {
                content = BaseCircle.formatNumber(item.end);
                canvas.drawTextOnPath(content, path, ViewUtils.getCirclePathLength((float)
                        radius, ((item.end - this.mStartIndicator) * perAngle) + 135.0f) - (
                        (float) (ViewUtils.getTextWidth(textPaint, content) / 2)), 0.0f, textPaint);
            }
        }
    }

    private void drawCircle(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(this.mCircleWhite);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimension(R.dimen.out_indicator_size));
        float textHeight = (float) ViewUtils.getTextHeight(textPaint);
        float perAngle = 270.0f / (this.mEndIndicator - this.mStartIndicator);
        int radius = (int) (((float) getViewRadius()) - (2.6f * ((float) this.mDividerWidth)));
        FontMetricsInt fmi = textPaint.getFontMetricsInt();
        float textRadius = (float) (radius - (Math.abs(fmi.bottom + fmi.top) / 2));
        RectF oval = new RectF((float) (getCenterX() - radius), (float) (getCenterY() - radius),
                (float) (getCenterX() + radius), (float) (getCenterY() + radius));
        Path path = new Path();
        path.addCircle((float) getCenterX(), (float) getCenterY(), textRadius, Direction.CW);
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(16.0f + textHeight);
        circlePaint.setStyle(Style.STROKE);
        if (this.mDividerIndicator.size() == 0) {
            circlePaint.setStrokeCap(Cap.ROUND);
            circlePaint.setColor(this.mCircleGray);
            canvas.drawArc(oval, 135.0f, 270.0f, false, circlePaint);
            return;
        }
        circlePaint.setStrokeCap(Cap.ROUND);
        drawCircleContent(canvas, (IndicatorItem) this.mDividerIndicator.get(0), oval, perAngle,
                textRadius, path, textPaint, circlePaint);
        Canvas canvas2 = canvas;
        drawCircleContent(canvas2, (IndicatorItem) this.mDividerIndicator.get(this
                .mDividerIndicator.size() - 1), oval, perAngle, textRadius, path, textPaint,
                circlePaint);
        circlePaint.setStrokeCap(Cap.BUTT);
        for (int i = 1; i < this.mDividerIndicator.size() - 1; i++) {
            drawCircleContent(canvas, (IndicatorItem) this.mDividerIndicator.get(i), oval,
                    perAngle, textRadius, path, textPaint, circlePaint);
        }
    }

    private void drawCircleContent(Canvas canvas, IndicatorItem item, RectF oval, float perAngle,
                                   float textRadius, Path path, Paint textPaint, Paint
                                           circlePaint) {
        circlePaint.setColor(item.color);
        float startAngle = 135.0f + ((item.start - this.mStartIndicator) * perAngle);
        float endAngle = perAngle * (item.end - item.start);
        canvas.drawArc(oval, startAngle, endAngle, false, circlePaint);
        Canvas canvas2 = canvas;
        canvas2.drawTextOnPath(String.valueOf(item.value), path, ViewUtils.getCirclePathLength
                (textRadius, (endAngle / 2.0f) + startAngle) - ((float) (ViewUtils.getTextWidth
                (textPaint, item.value) / 2)), 0.0f, textPaint);
    }

    public void setIndicatorValue(List<IndicatorItem> data, float indicator) {
        if (data != null && data.size() != 0) {
            Collections.sort(data, new Comparator<IndicatorItem>() {
                public int compare(IndicatorItem lhs, IndicatorItem rhs) {
                    return lhs.start - rhs.start > 0.0f ? 1 : -1;
                }
            });
            this.mStartIndicator = ((IndicatorItem) data.get(0)).start;
            this.mEndIndicator = ((IndicatorItem) data.get(data.size() - 1)).end;
            this.mDividerIndicator.clear();
            this.mDividerIndicator.addAll(data);
            if (indicator < this.mStartIndicator || indicator > this.mEndIndicator) {
                this.mIndicator = this.mStartIndicator;
                postInvalidate();
                return;
            }
            animateIndicator(indicator);
        }
    }
}
