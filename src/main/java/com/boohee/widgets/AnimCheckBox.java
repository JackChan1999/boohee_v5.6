package com.boohee.widgets;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.boohee.one.R;

public class AnimCheckBox extends View {
    private final String                  TAG;
    private final int                     defaultSize;
    private       float                   mBaseLeftHookOffset;
    private       float                   mBaseRightHookOffset;
    private       boolean                 mChecked;
    private       int                     mCircleColor;
    private       boolean                 mClickable;
    private final int                     mDuration;
    private       float                   mEndLeftHookOffset;
    private       float                   mEndRightHookOffset;
    private       float                   mHookOffset;
    private       float                   mHookSize;
    private       float                   mHookStartY;
    private       int                     mInnerCircleAlpha;
    private       RectF                   mInnerRectF;
    private       OnCheckedChangeListener mOnCheckedChangeListener;
    private       Paint                   mPaint;
    private       Path                    mPath;
    private       RectF                   mRectF;
    private final double                  mSin27;
    private final double                  mSin63;
    private       int                     mStrokeColor;
    private       int                     mStrokeWidth;
    private       float                   mSweepAngle;
    private       int                     radius;
    private       int                     size;

    public interface OnCheckedChangeListener {
        void onChange(boolean z);
    }

    public AnimCheckBox(Context context) {
        this(context, null);
    }

    public AnimCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "AnimCheckBox";
        this.mPaint = new Paint(1);
        this.mRectF = new RectF();
        this.mInnerRectF = new RectF();
        this.mPath = new Path();
        this.mSin27 = Math.sin(Math.toRadians(27.0d));
        this.mSin63 = Math.sin(Math.toRadians(63.0d));
        this.mChecked = true;
        this.mInnerCircleAlpha = 255;
        this.mStrokeWidth = 2;
        this.mDuration = 500;
        this.mStrokeColor = -16776961;
        this.mCircleColor = -1;
        this.defaultSize = 40;
        this.mClickable = true;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.AnimCheckBox);
            this.mStrokeWidth = (int) array.getDimension(0, (float) dip(this.mStrokeWidth));
            this.mStrokeColor = array.getColor(1, this.mStrokeColor);
            this.mCircleColor = array.getColor(2, this.mCircleColor);
            this.mClickable = array.getBoolean(3, true);
            array.recycle();
        } else {
            this.mStrokeWidth = dip(this.mStrokeWidth);
        }
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth((float) this.mStrokeWidth);
        this.mPaint.setColor(this.mStrokeColor);
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (AnimCheckBox.this.mClickable) {
                    AnimCheckBox.this.setChecked(!AnimCheckBox.this.mChecked);
                }
            }
        });
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == Integer.MIN_VALUE && MeasureSpec.getMode
                (heightMeasureSpec) == Integer.MIN_VALUE) {
            MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
            height = Math.min((dip(40) - params.leftMargin) - params.rightMargin, (dip(40) -
                    params.bottomMargin) - params.topMargin);
            width = height;
        }
        int size = Math.min((width - getPaddingLeft()) - getPaddingRight(), (height -
                getPaddingBottom()) - getPaddingTop());
        setMeasuredDimension(size, size);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.size = getWidth();
        this.radius = (getWidth() - (this.mStrokeWidth * 2)) / 2;
        this.mRectF.set((float) this.mStrokeWidth, (float) this.mStrokeWidth, (float) (this.size
                - this.mStrokeWidth), (float) (this.size - this.mStrokeWidth));
        this.mInnerRectF.set(this.mRectF);
        this.mInnerRectF.inset((float) (this.mStrokeWidth / 2), (float) (this.mStrokeWidth / 2));
        this.mHookStartY = (float) (((double) (this.size / 2)) - ((((double) this.radius) * this
                .mSin27) + (((double) this.radius) - (((double) this.radius) * this.mSin63))));
        this.mBaseLeftHookOffset = ((float) (((double) this.radius) * (PathListView.NO_ZOOM -
                this.mSin63))) + ((float) (this.mStrokeWidth / 2));
        this.mBaseRightHookOffset = 0.0f;
        this.mEndLeftHookOffset = this.mBaseLeftHookOffset + ((((float) ((this.size * 2) / 3)) -
                this.mHookStartY) * 0.33f);
        this.mEndRightHookOffset = this.mBaseRightHookOffset + ((((float) (this.size / 3)) + this
                .mHookStartY) * 0.38f);
        this.mHookSize = ((float) this.size) - (this.mEndLeftHookOffset + this.mEndRightHookOffset);
        this.mHookOffset = this.mChecked ? (this.mHookSize + this.mEndLeftHookOffset) - this
                .mBaseLeftHookOffset : 0.0f;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawHook(canvas);
    }

    private void drawCircle(Canvas canvas) {
        initDrawStrokeCirclePaint();
        canvas.drawArc(this.mRectF, 202.0f, this.mSweepAngle, false, this.mPaint);
        initDrawAlphaStrokeCirclePaint();
        canvas.drawArc(this.mRectF, 202.0f, this.mSweepAngle - 360.0f, false, this.mPaint);
        initDrawInnerCirclePaint();
        canvas.drawArc(this.mInnerRectF, 0.0f, 360.0f, false, this.mPaint);
    }

    private void drawHook(Canvas canvas) {
        if (this.mHookOffset != 0.0f) {
            initDrawHookPaint();
            this.mPath.reset();
            if (this.mHookOffset <= (((float) ((this.size * 2) / 3)) - this.mHookStartY) - this
                    .mBaseLeftHookOffset) {
                this.mPath.moveTo(this.mBaseLeftHookOffset, this.mBaseLeftHookOffset + this
                        .mHookStartY);
                this.mPath.lineTo(this.mBaseLeftHookOffset + this.mHookOffset, (this
                        .mBaseLeftHookOffset + this.mHookStartY) + this.mHookOffset);
            } else if (this.mHookOffset <= this.mHookSize) {
                this.mPath.moveTo(this.mBaseLeftHookOffset, this.mBaseLeftHookOffset + this
                        .mHookStartY);
                this.mPath.lineTo(((float) ((this.size * 2) / 3)) - this.mHookStartY, (float) (
                        (this.size * 2) / 3));
                this.mPath.lineTo(this.mHookOffset + this.mBaseLeftHookOffset, ((float) ((this
                        .size * 2) / 3)) - (this.mHookOffset - ((((float) ((this.size * 2) / 3))
                        - this.mHookStartY) - this.mBaseLeftHookOffset)));
            } else {
                float offset = this.mHookOffset - this.mHookSize;
                this.mPath.moveTo(this.mBaseLeftHookOffset + offset, (this.mBaseLeftHookOffset +
                        this.mHookStartY) + offset);
                this.mPath.lineTo(((float) ((this.size * 2) / 3)) - this.mHookStartY, (float) (
                        (this.size * 2) / 3));
                this.mPath.lineTo((this.mHookSize + this.mBaseLeftHookOffset) + offset, ((float)
                        ((this.size * 2) / 3)) - ((this.mHookSize - ((((float) ((this.size * 2) /
                        3)) - this.mHookStartY) - this.mBaseLeftHookOffset)) + offset));
            }
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }

    private void initDrawHookPaint() {
        this.mPaint.setAlpha(255);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth((float) this.mStrokeWidth);
        this.mPaint.setColor(this.mStrokeColor);
    }

    private void initDrawStrokeCirclePaint() {
        this.mPaint.setAlpha(255);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth((float) this.mStrokeWidth);
        this.mPaint.setColor(this.mStrokeColor);
    }

    private void initDrawAlphaStrokeCirclePaint() {
        this.mPaint.setStrokeWidth((float) this.mStrokeWidth);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setColor(this.mStrokeColor);
        this.mPaint.setAlpha(64);
    }

    private void initDrawInnerCirclePaint() {
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(this.mCircleColor);
        this.mPaint.setAlpha(this.mInnerCircleAlpha);
    }

    private void startCheckedAnim() {
        ValueAnimator animator = new ValueAnimator();
        final float hookMaxValue = (this.mHookSize + this.mEndLeftHookOffset) - this
                .mBaseLeftHookOffset;
        final float circleMaxFraction = this.mHookSize / hookMaxValue;
        final float circleMaxValue = 360.0f / circleMaxFraction;
        animator.setFloatValues(new float[]{0.0f, 1.0f});
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                AnimCheckBox.this.mHookOffset = hookMaxValue * fraction;
                if (fraction <= circleMaxFraction) {
                    AnimCheckBox.this.mSweepAngle = (float) ((int) ((circleMaxFraction -
                            fraction) * circleMaxValue));
                } else {
                    AnimCheckBox.this.mSweepAngle = 0.0f;
                }
                AnimCheckBox.this.mInnerCircleAlpha = (int) (255.0f * fraction);
                AnimCheckBox.this.invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500).start();
    }

    private void startUnCheckedAnim() {
        ValueAnimator animator = new ValueAnimator();
        final float hookMaxValue = (this.mHookSize + this.mEndLeftHookOffset) - this
                .mBaseLeftHookOffset;
        final float circleMinFraction = (this.mEndLeftHookOffset - this.mBaseLeftHookOffset) /
                hookMaxValue;
        final float circleMaxValue = 360.0f / (1.0f - circleMinFraction);
        animator.setFloatValues(new float[]{0.0f, 1.0f});
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float circleFraction = animation.getAnimatedFraction();
                float fraction = 1.0f - circleFraction;
                AnimCheckBox.this.mHookOffset = hookMaxValue * fraction;
                if (circleFraction >= circleMinFraction) {
                    AnimCheckBox.this.mSweepAngle = (float) ((int) ((circleFraction -
                            circleMinFraction) * circleMaxValue));
                } else {
                    AnimCheckBox.this.mSweepAngle = 0.0f;
                }
                AnimCheckBox.this.mInnerCircleAlpha = (int) (255.0f * fraction);
                AnimCheckBox.this.invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500).start();
    }

    private void startAnim() {
        clearAnimation();
        if (this.mChecked) {
            startCheckedAnim();
        } else {
            startUnCheckedAnim();
        }
    }

    private int getAlphaColor(int color, int alpha) {
        if (alpha < 0) {
            alpha = 0;
        }
        if (alpha > 255) {
            alpha = 255;
        }
        return (ViewCompat.MEASURED_SIZE_MASK & color) | (alpha << 24);
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public void setChecked(boolean checked, boolean animation) {
        if (checked != this.mChecked) {
            this.mChecked = checked;
            if (animation) {
                startAnim();
            } else {
                if (this.mChecked) {
                    this.mInnerCircleAlpha = 255;
                    this.mSweepAngle = 0.0f;
                    this.mHookOffset = (this.mHookSize + this.mEndLeftHookOffset) - this
                            .mBaseLeftHookOffset;
                } else {
                    this.mInnerCircleAlpha = 0;
                    this.mSweepAngle = 360.0f;
                    this.mHookOffset = 0.0f;
                }
                invalidate();
            }
            if (this.mOnCheckedChangeListener != null) {
                this.mOnCheckedChangeListener.onChange(this.mChecked);
            }
        }
    }

    private int dip(int dip) {
        return ((int) getContext().getResources().getDisplayMetrics().density) * dip;
    }

    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }
}
