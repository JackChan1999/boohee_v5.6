package com.boohee.myview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;

import com.boohee.one.R;
import com.boohee.utils.ArithmeticUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;

public class BooheeRulerView extends View {
    public static final int   DEFAULT_BG_COLOR                = Color.parseColor("#FAE40B");
    public static final int   DEFAULT_BOTTOM_PADDING          = 8;
    public static final int   DEFAULT_INDICATOR_HEIGHT        = 18;
    public static final int   DEFAULT_LONG_LINE_HEIGHT        = 38;
    public static final int   DEFAULT_LONG_LINE_STROKE_WIDTH  = 2;
    public static final int   DEFAULT_SHORT_LINE_STROKE_WIDTH = 1;
    public static final int   DEFAULT_TEXT_SIZE               = 17;
    public static final float DEFAULT_UNIT                    = 10.0f;
    public static final int   DEFAULT_WIDTH_PER_UNIT          = 100;
    public static final int   INDICATOR_LINE                  = 1;
    public static final int   INDICATOR_TRIANGLE              = 0;
    public static final int   MICRO_UNIT_FIVE                 = 5;
    public static final int   MICRO_UNIT_ONE                  = 2;
    public static final int   MICRO_UNIT_TEN                  = 10;
    public static final int   MICRO_UNIT_ZERO                 = 0;
    private ValueAnimator         animator;
    private int                   bgColor;
    private Paint                 bgPaint;
    private float                 bottomPadding;
    private float                 currentValue;
    private float                 density;
    private DecimalFormat         df;
    private float                 endValue;
    private boolean               f;
    private int                   height;
    private int                   indicatorColor;
    private float                 indicatorHeight;
    private Paint                 indicatorPaint;
    private int                   indicatorType;
    private boolean               isActionUp;
    private boolean               isCancel;
    private int                   lineColor;
    private Paint                 linePaint;
    private OnValueChangeListener listener;
    private float                 longLineHeight;
    private float                 mLastX;
    private float                 maxLeftOffset;
    private float                 maxRightOffset;
    private float                 microUnit;
    private int                   microUnitCount;
    private int                   minvelocity;
    private float                 moveX;
    private float                 offset;
    private float                 originValue;
    private Path                  path;
    private PaintFlagsDrawFilter  pfdf;
    private OverScroller          scroller;
    private float                 shortLineHeight;
    private float                 startValue;
    private int                   textColor;
    private Paint                 textPaint;
    private float                 textSize;
    private float                 unit;
    private VelocityTracker       velocityTracker;
    private int                   width;
    private float                 widthPerMicroUnit;
    private float                 widthPerUnit;

    public interface OnValueChangeListener {
        void onValueChange(float f);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MicroUnitMode {
    }

    public BooheeRulerView(Context context) {
        this(context, null);
    }

    public BooheeRulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BooheeRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.df = new DecimalFormat("#.##");
        this.isActionUp = false;
        this.isCancel = false;
        this.f = true;
        this.scroller = new OverScroller(context);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        this.density = getResources().getDisplayMetrics().density;
        this.longLineHeight = 38.0f * this.density;
        this.shortLineHeight = 19.0f * this.density;
        this.bottomPadding = 8.0f * this.density;
        this.indicatorHeight = 18.0f * this.density;
        this.textSize = 17.0f * this.density;
        this.widthPerUnit = 100.0f * this.density;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable
                .BooheeRulerView);
        if (typedArray != null) {
            this.bgColor = typedArray.getColor(0, DEFAULT_BG_COLOR);
            this.textSize = typedArray.getDimension(2, this.textSize);
            this.textColor = typedArray.getColor(3, -1);
            this.widthPerUnit = typedArray.getDimension(4, this.widthPerUnit);
            this.lineColor = typedArray.getColor(1, -1);
            this.indicatorColor = typedArray.getColor(5, -1);
            this.indicatorType = typedArray.getInt(6, 0);
            typedArray.recycle();
        }
        this.minvelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        initPaint();
    }

    public void init(float startValue, float endValue, float currentValue, OnValueChangeListener
            listener) {
        init(startValue, endValue, currentValue, 10.0f, 0, listener);
    }

    public void init(float startValue, float endValue, float currentValue, float unit, int
            microUnitCount, OnValueChangeListener listener) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.currentValue = currentValue;
        if (currentValue < startValue) {
            this.currentValue = startValue;
        }
        if (currentValue > endValue) {
            this.currentValue = endValue;
        }
        this.originValue = this.currentValue;
        this.unit = unit;
        this.microUnit = microUnitCount == 0 ? 0.0f : unit / ((float) microUnitCount);
        this.microUnitCount = microUnitCount;
        this.listener = listener;
        caculate();
    }

    private void initPaint() {
        this.pfdf = new PaintFlagsDrawFilter(0, 3);
        this.bgPaint = new Paint();
        this.bgPaint.setColor(this.bgColor);
        this.indicatorPaint = new Paint();
        this.indicatorPaint.setColor(this.indicatorColor);
        this.textPaint = new Paint();
        this.textPaint.setColor(this.textColor);
        this.textPaint.setTextSize(this.textSize);
        this.linePaint = new Paint();
        this.linePaint.setColor(this.lineColor);
        this.path = new Path();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int measureMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureSize = MeasureSpec.getSize(widthMeasureSpec);
        int result = getSuggestedMinimumWidth();
        switch (measureMode) {
            case Integer.MIN_VALUE:
            case 1073741824:
                result = measureSize;
                break;
        }
        this.width = result;
        return result;
    }

    private int measureHeight(int heightMeasure) {
        int measureMode = MeasureSpec.getMode(heightMeasure);
        int measureSize = MeasureSpec.getSize(heightMeasure);
        int result = (int) (this.bottomPadding + (this.longLineHeight * 2.0f));
        switch (measureMode) {
            case Integer.MIN_VALUE:
                result = Math.min(result, measureSize);
                break;
            case 1073741824:
                result = Math.max(result, measureSize);
                break;
        }
        this.height = result;
        return result;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(this.pfdf);
        drawBg(canvas);
        drawRuler(canvas);
        drawIndicator(canvas);
    }

    private void drawBg(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, (float) this.width, (float) this.height, this.bgPaint);
    }

    private void drawIndicator(Canvas canvas) {
        if (this.indicatorType == 0) {
            this.path.reset();
            this.path.moveTo(((float) (this.width / 2)) - (this.indicatorHeight / 2.0f), 0.0f);
            this.path.lineTo((float) (this.width / 2), this.indicatorHeight / 2.0f);
            this.path.lineTo(((float) (this.width / 2)) + (this.indicatorHeight / 2.0f), 0.0f);
            canvas.drawPath(this.path, this.indicatorPaint);
            return;
        }
        this.indicatorPaint.setStrokeWidth((this.density * 2.0f) * 2.0f);
        this.indicatorPaint.setStrokeCap(Cap.ROUND);
        canvas.drawLine((float) (this.width / 2), 0.0f, (float) (this.width / 2), 5.0f + this
                .longLineHeight, this.indicatorPaint);
    }

    private void drawRuler(Canvas canvas) {
        if (this.moveX < this.maxRightOffset) {
            this.moveX = this.maxRightOffset;
        }
        if (this.moveX > this.maxLeftOffset) {
            this.moveX = this.maxLeftOffset;
        }
        int halfCount = (int) (((float) (this.width / 2)) / getBaseUnitWidth());
        this.currentValue = this.originValue - (((float) ((int) (this.moveX / getBaseUnitWidth())
        )) * getBaseUnit());
        this.offset = this.moveX - (((float) ((int) (this.moveX / getBaseUnitWidth()))) *
                getBaseUnitWidth());
        for (int i = (-halfCount) - 1; i <= halfCount + 1; i++) {
            float value = (float) ArithmeticUtil.roundOff((double) (this.currentValue + (((float)
                    i) * getBaseUnit())), 2);
            if (value >= this.startValue && value <= this.endValue) {
                float startx = (((float) (this.width / 2)) + this.offset) + (((float) i) *
                        getBaseUnitWidth());
                if (startx > 0.0f && startx < ((float) this.width)) {
                    if (this.microUnitCount == 0) {
                        drawLongLine(canvas, i, value);
                    } else if (ArithmeticUtil.remainder(value, this.unit) == 0) {
                        drawLongLine(canvas, i, value);
                    } else {
                        drawShortLine(canvas, i);
                    }
                }
            }
        }
        notifyValueChange();
    }

    private void notifyValueChange() {
        if (this.listener != null) {
            this.currentValue = ArithmeticUtil.round(this.currentValue, 2);
            this.listener.onValueChange(this.currentValue);
        }
    }

    private void drawShortLine(Canvas canvas, int i) {
        this.linePaint.setStrokeWidth(1.0f * this.density);
        Canvas canvas2 = canvas;
        canvas2.drawLine((((float) i) * getBaseUnitWidth()) + (((float) (this.width / 2)) + this
                .offset), 0.0f, (((float) i) * getBaseUnitWidth()) + (((float) (this.width / 2))
                + this.offset), 0.0f + this.shortLineHeight, this.linePaint);
    }

    private void drawLongLine(Canvas canvas, int i, float value) {
        this.linePaint.setStrokeWidth(this.density * 2.0f);
        Canvas canvas2 = canvas;
        canvas2.drawLine((((float) i) * getBaseUnitWidth()) + (((float) (this.width / 2)) + this
                .offset), 0.0f, (((float) i) * getBaseUnitWidth()) + (((float) (this.width / 2))
                + this.offset), 0.0f + this.longLineHeight, this.linePaint);
        String text = this.df.format((double) value);
        canvas.drawText(text, ((((float) (this.width / 2)) + this.offset) + (((float) i) *
                getBaseUnitWidth())) - (this.textPaint.measureText(text) / 2.0f), (((float)
                getHeight()) - this.bottomPadding) - ((float) (calcTextHeight(this.textPaint,
                text) / 2)), this.textPaint);
    }

    private float getBaseUnitWidth() {
        if (this.microUnitCount != 0) {
            return this.widthPerMicroUnit;
        }
        return this.widthPerUnit;
    }

    private float getBaseUnit() {
        if (this.microUnitCount != 0) {
            return this.microUnit;
        }
        return this.unit;
    }

    private void caculate() {
        this.startValue = format(this.startValue);
        this.endValue = format(this.endValue);
        this.currentValue = format(this.currentValue);
        this.originValue = this.currentValue;
        if (this.unit == 0.0f) {
            this.unit = 10.0f;
        }
        if (this.microUnitCount != 0) {
            this.widthPerMicroUnit = ArithmeticUtil.div(this.widthPerUnit, (float) this
                    .microUnitCount, 2);
        }
        this.maxRightOffset = -1.0f * (((this.endValue - this.originValue) * getBaseUnitWidth())
                / getBaseUnit());
        this.maxLeftOffset = ((this.originValue - this.startValue) * getBaseUnitWidth()) /
                getBaseUnit();
        invalidate();
    }

    private float format(float vallue) {
        float result = vallue;
        if (((double) getBaseUnit()) < 0.1d) {
            return ArithmeticUtil.remainder(result, getBaseUnit()) != 0 ? (float) ArithmeticUtil
                    .roundOff((double) result, 2) : result;
        } else {
            if (getBaseUnit() < 1.0f) {
                if (ArithmeticUtil.remainder(result, getBaseUnit()) != 0) {
                    return (float) ArithmeticUtil.roundOff((double) result, 1);
                }
                return result;
            } else if (getBaseUnit() >= 10.0f || ArithmeticUtil.remainder(result, getBaseUnit())
                    == 0) {
                return result;
            } else {
                return ArithmeticUtil.round(result, 0);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float xPosition = event.getX();
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(event);
        switch (action) {
            case 0:
                this.isActionUp = false;
                this.scroller.forceFinished(true);
                if (this.animator != null) {
                    this.animator.cancel();
                    break;
                }
                break;
            case 1:
            case 3:
                this.isActionUp = true;
                this.f = true;
                countVelocityTracker(event);
                return false;
            case 2:
                this.isActionUp = false;
                float off = xPosition - this.mLastX;
                if ((this.moveX > this.maxRightOffset || off >= 0.0f) && (this.moveX < this
                        .maxLeftOffset || off <= 0.0f)) {
                    this.moveX += off;
                    postInvalidate();
                    break;
                }
        }
        this.mLastX = xPosition;
        return true;
    }

    private void startAnim() {
        this.isCancel = false;
        float neededMoveX = ArithmeticUtil.mul(ArithmeticUtil.div(this.moveX, getBaseUnitWidth(),
                0), getBaseUnitWidth());
        ValueAnimator valueAnimator = new ValueAnimator();
        this.animator = ValueAnimator.ofFloat(new float[]{this.moveX, neededMoveX});
        this.animator.setDuration(300);
        this.animator.setInterpolator(new DecelerateInterpolator());
        this.animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!BooheeRulerView.this.isCancel) {
                    BooheeRulerView.this.moveX = ((Float) animation.getAnimatedValue())
                            .floatValue();
                    BooheeRulerView.this.postInvalidate();
                }
            }
        });
        this.animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animation) {
                BooheeRulerView.this.isCancel = true;
            }
        });
        this.animator.start();
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.scroller.computeScrollOffset()) {
            float off = ((float) (this.scroller.getFinalX() - this.scroller.getCurrX())) *
                    functionSpeed();
            if (this.moveX <= this.maxRightOffset && off < 0.0f) {
                this.moveX = this.maxRightOffset;
            } else if (this.moveX < this.maxLeftOffset || off <= 0.0f) {
                this.moveX += off;
                if (this.scroller.isFinished()) {
                    startAnim();
                    return;
                }
                postInvalidate();
                this.mLastX = (float) this.scroller.getFinalX();
            } else {
                this.moveX = this.maxLeftOffset;
            }
        } else if (this.isActionUp && this.f) {
            startAnim();
            this.f = false;
        }
    }

    private float functionSpeed() {
        return 0.2f;
    }

    private void countVelocityTracker(MotionEvent event) {
        this.velocityTracker.computeCurrentVelocity(1000, 3000.0f);
        float xVelocity = this.velocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > ((float) this.minvelocity)) {
            this.scroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, 0, 0);
        }
    }

    private int calcTextHeight(Paint paint, String demoText) {
        Rect r = new Rect();
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return r.height();
    }
}
