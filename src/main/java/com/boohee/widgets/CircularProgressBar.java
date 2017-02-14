package com.boohee.widgets;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.boohee.one.R;

public class CircularProgressBar extends View {
    private static final int     MAX_PROGRESS        = 100;
    private static final int     PADDING             = 20;
    private              Paint   circlePaint         = new Paint();
    private              Paint   fillPaint           = new Paint();
    private              int     layout_height       = 0;
    private              int     layout_width        = 0;
    private              int     mBackgroundColor    = Color.parseColor("#DFDFDF");
    private              boolean mIsCapRound         = false;
    private              boolean mIsIndicatorEnabled = false;
    private              int     mProgress           = 0;
    private              int     mProgressColor      = Color.parseColor("#03A9F5");
    private              int     mStartAngle         = 270;
    private              int     mStrokeWidth        = 4;
    private              int     mSweepAngle         = 360;
    private              RectF   oval                = new RectF();

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar));
    }

    private void parseAttributes(TypedArray a) {
        this.mBackgroundColor = a.getColor(1, this.mBackgroundColor);
        this.mProgressColor = a.getColor(0, this.mProgressColor);
        this.mStrokeWidth = (int) a.getDimension(6, (float) this.mStrokeWidth);
        this.mStartAngle = a.getInt(3, this.mStartAngle);
        this.mSweepAngle = a.getInt(4, this.mSweepAngle);
        this.mIsCapRound = a.getBoolean(5, this.mIsCapRound);
        a.recycle();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.layout_width = w;
        this.layout_height = h;
        setupPaints();
        setupBounds();
    }

    private void setupPaints() {
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setStrokeWidth((float) this.mStrokeWidth);
        this.circlePaint.setStyle(Style.STROKE);
        this.circlePaint.setColor(this.mBackgroundColor);
        this.fillPaint.setAntiAlias(true);
        this.fillPaint.setStrokeWidth((float) this.mStrokeWidth);
        this.fillPaint.setStyle(Style.STROKE);
        this.fillPaint.setStrokeJoin(Join.ROUND);
        this.fillPaint.setColor(this.mProgressColor);
        if (this.mIsCapRound) {
            this.circlePaint.setStrokeCap(Cap.ROUND);
            this.fillPaint.setStrokeCap(Cap.ROUND);
        }
    }

    private void setupBounds() {
        int minValue = Math.min(this.layout_width, this.layout_height);
        this.oval = new RectF((float) (this.mStrokeWidth / 2), (float) (this.mStrokeWidth / 2),
                (float) (minValue - (this.mStrokeWidth / 2)), (float) (minValue - (this
                .mStrokeWidth / 2)));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(this.oval, (float) this.mStartAngle, (float) this.mSweepAngle, false, this
                .circlePaint);
        Canvas canvas2 = canvas;
        canvas2.drawArc(this.oval, (float) this.mStartAngle, ((float) this.mSweepAngle) * ((
                (float) this.mProgress) / 100.0f), false, this.fillPaint);
    }

    public void setProgress(int progress) {
        if (progress > 100) {
            progress = 100;
        }
        this.mProgress = progress;
        postInvalidate();
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void animateProgess(int progress) {
        animateProgess(progress, new AccelerateDecelerateInterpolator());
    }

    public void animateProgess(int progress, Interpolator interpolator) {
        ObjectAnimator animation = ObjectAnimator.ofInt(this, "progress", new int[]{progress});
        animation.setDuration(2000);
        animation.setInterpolator(interpolator);
        animation.start();
    }

    private float getMarkerRotation() {
        return (((float) this.mProgress) / 100.0f) * 360.0f;
    }

    public void setProgressColor(int color) {
        this.mProgressColor = color;
        invalidate();
    }
}
