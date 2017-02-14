package com.boohee.one.radar;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.baidu.location.aj;
import com.boohee.utility.DensityUtil;

import uk.co.senab.photoview.IPhotoView;

public class BarChart extends View {
    private       int COLUMN           = 4;
    private       int COLUMN_WIDTH     = 25;
    private final int DEFAULT_DURATION = 800;
    private       int LINE             = 4;
    private final int STROKE_WIDTH     = 2;
    private int           height;
    private Paint         mBackgroundPaint;
    private RectF         mBackgroundRect;
    private Paint         mColumnPaint;
    private Context       mContext;
    private Paint         mLinePaint;
    private float[]       percent;
    private float[]       startPoint;
    private float[]       target;
    private ValueAnimator valueAnimator;
    private int           width;

    public BarChart(Context context) {
        super(context);
        init(context);
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setPercent(float[] percent) {
        this.percent = percent;
        invalidate();
    }

    public void setTarget(float[] target) {
        this.target = target;
    }

    public void startAnim() {
        if (this.target != null && this.valueAnimator == null) {
            this.valueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(800);
            this.valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) animation.getAnimatedValue()).floatValue();
                    for (int i = 0; i < BarChart.this.percent.length; i++) {
                        BarChart.this.percent[i] = BarChart.this.target[i] * value;
                    }
                    BarChart.this.invalidate();
                }
            });
            this.valueAnimator.start();
        }
    }

    public void setColumn(int count, int columnWidth) {
        this.COLUMN = count;
        this.COLUMN_WIDTH = DensityUtil.dip2px(this.mContext, (float) columnWidth);
    }

    public void setLine(int number) {
        this.LINE = number;
    }

    private void init(Context context) {
        setLayerType(1, null);
        this.mContext = context;
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setStrokeWidth(2.0f);
        this.mBackgroundPaint.setAntiAlias(true);
        this.mBackgroundPaint.setColor(-5056771);
        this.mBackgroundPaint.setStyle(Style.STROKE);
        this.mColumnPaint = new Paint();
        this.mColumnPaint.setAntiAlias(true);
        this.mColumnPaint.setStyle(Style.FILL);
        this.mColumnPaint.setColor(-12477447);
        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setStyle(Style.STROKE);
        this.mLinePaint.setColor(-5056771);
        this.mLinePaint.setStrokeWidth(1.0f);
        this.mLinePaint.setPathEffect(new DashPathEffect(new float[]{aj.hA, aj.hA}, 0.0f));
        this.mBackgroundRect = new RectF();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.startPoint = new float[this.COLUMN];
        float COLUMN_INTERVAL = ((float) ((w - (this.COLUMN * this.COLUMN_WIDTH)) / (this.COLUMN
                + 1))) * 1.0f;
        for (int i = 0; i < this.COLUMN; i++) {
            this.startPoint[i] = (((float) (i + 1)) * COLUMN_INTERVAL) + ((float) (this
                    .COLUMN_WIDTH * i));
        }
    }

    protected void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        this.mBackgroundRect.set(1.0f, 1.0f, ((float) this.width) - 1.0f, ((float) this.height) -
                1.0f);
        this.mBackgroundPaint.setStrokeWidth(2.0f);
        canvas.drawRoundRect(this.mBackgroundRect, 5.0f, 5.0f, this.mBackgroundPaint);
        float interval = (((float) this.height) * 1.0f) / ((float) (this.LINE + 1));
        for (i = 0; i < this.LINE; i++) {
            canvas.drawLine(0.0f, interval * ((float) (i + 1)), (float) this.width, interval * (
                    (float) (i + 1)), this.mLinePaint);
        }
        if (this.percent != null && this.percent.length == this.COLUMN) {
            for (i = 0; i < this.COLUMN; i++) {
                this.mBackgroundRect.set(this.startPoint[i], ((float) this.height) * (1.0f - this
                        .percent[i]), this.startPoint[i] + ((float) this.COLUMN_WIDTH), (float)
                        this.height);
                canvas.drawRoundRect(this.mBackgroundRect, IPhotoView.DEFAULT_MAX_SCALE,
                        IPhotoView.DEFAULT_MAX_SCALE, this.mColumnPaint);
            }
        }
    }

    public float[] getStartPoint() {
        return this.startPoint;
    }
}
