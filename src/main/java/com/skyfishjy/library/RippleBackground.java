package com.skyfishjy.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import java.util.ArrayList;
import java.util.Iterator;

public class RippleBackground extends RelativeLayout {
    private static final int     DEFAULT_DURATION_TIME = 3000;
    private static final int     DEFAULT_FILL_TYPE     = 0;
    private static final int     DEFAULT_RIPPLE_COUNT  = 6;
    private static final float   DEFAULT_SCALE         = 6.0f;
    private              boolean animationRunning      = false;
    private ArrayList<Animator> animatorList;
    private AnimatorSet         animatorSet;
    private Paint               paint;
    private int                 rippleAmount;
    private int                 rippleColor;
    private int                 rippleDelay;
    private int                 rippleDurationTime;
    private LayoutParams        rippleParams;
    private float               rippleRadius;
    private float               rippleScale;
    private float               rippleStrokeWidth;
    private int                 rippleType;
    private ArrayList<RippleView> rippleViewList = new ArrayList();

    private class RippleView extends View {
        public RippleView(Context context) {
            super(context);
            setVisibility(4);
        }

        protected void onDraw(Canvas canvas) {
            int radius = Math.min(getWidth(), getHeight()) / 2;
            canvas.drawCircle((float) radius, (float) radius, ((float) radius) - RippleBackground
                    .this.rippleStrokeWidth, RippleBackground.this.paint);
        }
    }

    public RippleBackground(Context context) {
        super(context);
    }

    public RippleBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            if (attrs == null) {
                throw new IllegalArgumentException("Attributes should be provided to this view,");
            }
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable
                    .RippleBackground);
            this.rippleColor = typedArray.getColor(R.styleable.RippleBackground_rb_color,
                    getResources().getColor(R.color.rippelColor));
            this.rippleStrokeWidth = typedArray.getDimension(R.styleable
                    .RippleBackground_rb_strokeWidth, getResources().getDimension(R.dimen
                    .rippleStrokeWidth));
            this.rippleRadius = typedArray.getDimension(R.styleable.RippleBackground_rb_radius,
                    getResources().getDimension(R.dimen.rippleRadius));
            this.rippleDurationTime = typedArray.getInt(R.styleable.RippleBackground_rb_duration,
                    3000);
            this.rippleAmount = typedArray.getInt(R.styleable.RippleBackground_rb_rippleAmount, 6);
            this.rippleScale = typedArray.getFloat(R.styleable.RippleBackground_rb_scale,
                    DEFAULT_SCALE);
            this.rippleType = typedArray.getInt(R.styleable.RippleBackground_rb_type, 0);
            typedArray.recycle();
            this.rippleDelay = this.rippleDurationTime / this.rippleAmount;
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            if (this.rippleType == 0) {
                this.rippleStrokeWidth = 0.0f;
                this.paint.setStyle(Style.FILL);
            } else {
                this.paint.setStyle(Style.STROKE);
            }
            this.paint.setColor(this.rippleColor);
            this.rippleParams = new LayoutParams((int) (2.0f * (this.rippleRadius + this
                    .rippleStrokeWidth)), (int) (2.0f * (this.rippleRadius + this
                    .rippleStrokeWidth)));
            this.rippleParams.addRule(13, -1);
            this.animatorSet = new AnimatorSet();
            this.animatorSet.setDuration((long) this.rippleDurationTime);
            this.animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            this.animatorList = new ArrayList();
            for (int i = 0; i < this.rippleAmount; i++) {
                RippleView rippleView = new RippleView(getContext());
                addView(rippleView, this.rippleParams);
                this.rippleViewList.add(rippleView);
                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", new
                        float[]{1.0f, this.rippleScale});
                scaleXAnimator.setRepeatCount(-1);
                scaleXAnimator.setRepeatMode(1);
                scaleXAnimator.setStartDelay((long) (this.rippleDelay * i));
                this.animatorList.add(scaleXAnimator);
                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", new
                        float[]{1.0f, this.rippleScale});
                scaleYAnimator.setRepeatCount(-1);
                scaleYAnimator.setRepeatMode(1);
                scaleYAnimator.setStartDelay((long) (this.rippleDelay * i));
                this.animatorList.add(scaleYAnimator);
                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleView, "Alpha", new
                        float[]{1.0f, 0.0f});
                alphaAnimator.setRepeatCount(-1);
                alphaAnimator.setRepeatMode(1);
                alphaAnimator.setStartDelay((long) (this.rippleDelay * i));
                this.animatorList.add(alphaAnimator);
            }
            this.animatorSet.playTogether(this.animatorList);
        }
    }

    public void startRippleAnimation() {
        if (!isRippleAnimationRunning()) {
            Iterator i$ = this.rippleViewList.iterator();
            while (i$.hasNext()) {
                ((RippleView) i$.next()).setVisibility(0);
            }
            this.animatorSet.start();
            this.animationRunning = true;
        }
    }

    public void stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            this.animatorSet.end();
            this.animationRunning = false;
        }
    }

    public boolean isRippleAnimationRunning() {
        return this.animationRunning;
    }
}
