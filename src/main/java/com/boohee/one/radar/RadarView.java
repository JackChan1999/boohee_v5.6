package com.boohee.one.radar;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.baidu.location.aj;
import com.boohee.utility.DensityUtil;

import java.util.Arrays;

public class RadarView extends View {
    private final int[]              BACKGROUND_COLOR;
    private final int                CIRCLE_NUMBER;
    private final int                DEFAULT_DURATION;
    private final int[]              FILL_COLOR;
    private final int                LINE_NUMBER;
    private       float              MIN_RADIUS;
    private final int                START_ANGLE;
    private final int                STROKE_COLOR;
    private final int                STROKE_WIDTH;
    private       PointF[]           centerPoints;
    private       int                height;
    private       AnimFinishListener mAnimFinishListener;
    private       Paint              mBackgroundPaint;
    private       Path               mPath;
    private       Paint              mRazorPaint;
    private       Paint              mStrokePaint;
    private       float              maxRadius;
    private       float[]            origin;
    private       float[]            percent;
    private       PointF[]           points;
    private       float[]            target;
    private       ValueAnimator      valueAnimator;
    private       int                width;

    public interface AnimFinishListener {
        void onAnimFinish();
    }

    public RadarView(Context context) {
        super(context);
        this.BACKGROUND_COLOR = new int[]{-3283459, -2692611, -1641730, -853763, -1};
        this.FILL_COLOR = new int[]{-14581016, -15501602, -13858067, -12477447, -13397517};
        this.STROKE_COLOR = -3417355;
        this.CIRCLE_NUMBER = this.BACKGROUND_COLOR.length;
        this.LINE_NUMBER = this.FILL_COLOR.length;
        this.DEFAULT_DURATION = 1000;
        this.STROKE_WIDTH = 1;
        this.START_ANGLE = -90;
        this.MIN_RADIUS = 36.0f;
        this.points = new PointF[this.LINE_NUMBER];
        this.centerPoints = new PointF[this.LINE_NUMBER];
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        this.target = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        init(context);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.BACKGROUND_COLOR = new int[]{-3283459, -2692611, -1641730, -853763, -1};
        this.FILL_COLOR = new int[]{-14581016, -15501602, -13858067, -12477447, -13397517};
        this.STROKE_COLOR = -3417355;
        this.CIRCLE_NUMBER = this.BACKGROUND_COLOR.length;
        this.LINE_NUMBER = this.FILL_COLOR.length;
        this.DEFAULT_DURATION = 1000;
        this.STROKE_WIDTH = 1;
        this.START_ANGLE = -90;
        this.MIN_RADIUS = 36.0f;
        this.points = new PointF[this.LINE_NUMBER];
        this.centerPoints = new PointF[this.LINE_NUMBER];
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        this.target = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        init(context);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.BACKGROUND_COLOR = new int[]{-3283459, -2692611, -1641730, -853763, -1};
        this.FILL_COLOR = new int[]{-14581016, -15501602, -13858067, -12477447, -13397517};
        this.STROKE_COLOR = -3417355;
        this.CIRCLE_NUMBER = this.BACKGROUND_COLOR.length;
        this.LINE_NUMBER = this.FILL_COLOR.length;
        this.DEFAULT_DURATION = 1000;
        this.STROKE_WIDTH = 1;
        this.START_ANGLE = -90;
        this.MIN_RADIUS = 36.0f;
        this.points = new PointF[this.LINE_NUMBER];
        this.centerPoints = new PointF[this.LINE_NUMBER];
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        this.target = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        init(context);
    }

    private void init(Context context) {
        this.MIN_RADIUS = (float) DensityUtil.dip2px(context, this.MIN_RADIUS);
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setStyle(Style.FILL);
        this.mBackgroundPaint.setAntiAlias(true);
        this.mStrokePaint = new Paint();
        this.mStrokePaint.setStyle(Style.STROKE);
        this.mStrokePaint.setStrokeWidth(1.0f);
        this.mStrokePaint.setAntiAlias(true);
        this.mStrokePaint.setColor(-3417355);
        this.mRazorPaint = new Paint();
        this.mRazorPaint.setStyle(Style.FILL_AND_STROKE);
        this.mRazorPaint.setStrokeWidth(1.0f);
        this.mRazorPaint.setAntiAlias(true);
        this.mPath = new Path();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.maxRadius = Math.min(((float) this.width) / 2.0f, ((float) this.height) / 2.0f) - 1.0f;
        for (int i = 0; i < this.LINE_NUMBER; i++) {
            float radians = (float) Math.toRadians((double) (-90.0f + (((float) i) * (360.0f / (
                    (float) this.LINE_NUMBER)))));
            PointF p = new PointF(((float) Math.cos((double) radians)) * this.maxRadius, ((float)
                    Math.sin((double) radians)) * this.maxRadius);
            this.points[i] = p;
            this.centerPoints[i] = new PointF((p.x * this.MIN_RADIUS) / this.maxRadius, (p.y *
                    this.MIN_RADIUS) / this.maxRadius);
        }
    }

    public void setTarget(float[] target) {
        this.target = target;
    }

    public void startAnim() {
        if (this.valueAnimator != null && this.valueAnimator.isRunning()) {
            this.valueAnimator.cancel();
        }
        if (this.target != null) {
            this.origin = Arrays.copyOf(this.percent, this.percent.length);
            this.valueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(1000);
            this.valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) animation.getAnimatedValue()).floatValue();
                    for (int i = 0; i < RadarView.this.percent.length; i++) {
                        RadarView.this.percent[i] = RadarView.this.origin[i] + ((RadarView.this
                                .target[i] - RadarView.this.origin[i]) * value);
                    }
                    RadarView.this.invalidate();
                }
            });
            this.valueAnimator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    if (RadarView.this.mAnimFinishListener != null) {
                        RadarView.this.mAnimFinishListener.onAnimFinish();
                    }
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
            this.valueAnimator.start();
        }
    }

    protected void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        canvas.translate(((float) this.width) / 2.0f, ((float) this.height) / 2.0f);
        float radiusDiff = (this.maxRadius - this.MIN_RADIUS) / aj.hA;
        for (i = this.CIRCLE_NUMBER - 1; i >= 0; i--) {
            this.mBackgroundPaint.setColor(this.BACKGROUND_COLOR[i]);
            canvas.drawCircle(0.0f, 0.0f, this.MIN_RADIUS + (((float) i) * radiusDiff), this
                    .mBackgroundPaint);
        }
        canvas.drawCircle(0.0f, 0.0f, this.maxRadius, this.mStrokePaint);
        for (PointF point : this.points) {
            canvas.drawLine(0.0f, 0.0f, point.x, point.y, this.mStrokePaint);
        }
        i = 0;
        while (i < this.points.length) {
            this.mPath.moveTo(0.0f, 0.0f);
            this.mPath.lineTo(length(this.centerPoints[i].x, this.points[i].x, this.percent[i]),
                    length(this.centerPoints[i].y, this.points[i].y, this.percent[i]));
            int j = i + 1 < this.points.length ? i + 1 : (i + 1) - this.points.length;
            this.mPath.lineTo(length(this.centerPoints[j].x, this.points[j].x, this.percent[j]),
                    length(this.centerPoints[j].y, this.points[j].y, this.percent[j]));
            this.mPath.close();
            this.mRazorPaint.setColor(this.FILL_COLOR[i]);
            canvas.drawPath(this.mPath, this.mRazorPaint);
            this.mPath.reset();
            i++;
        }
    }

    private float length(float center, float max, float percent) {
        if (((double) percent) <= 0.2d) {
            return (center * percent) / 0.2f;
        }
        return (((max - center) * (percent - 0.2f)) / 0.8f) + center;
    }

    public void setAnimFinishListener(AnimFinishListener animFinishListener) {
        this.mAnimFinishListener = animFinishListener;
    }
}
