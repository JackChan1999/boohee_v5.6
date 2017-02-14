package com.boohee.circleview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;

import uk.co.senab.photoview.IPhotoView;

public class BaseCircle extends View {
    protected static final float CIRCLE_GAP         = 2.6f;
    protected static final int   CIRCLE_START_ANGLE = 135;
    protected static final int   CIRCLE_SWEEP_ANGLE = 270;
    protected String mAlert;
    protected int    mAlertColor;
    protected int    mAlertSize;
    protected int    mCenterCircleInside;
    protected int    mCenterCircleMiddle;
    protected int    mCenterCircleOut;
    protected int    mCircleBackground;
    protected int    mCircleGray;
    protected int    mCircleGreen;
    protected String mContent;
    protected int    mContentColor;
    protected int    mContentSize;
    protected int    mDividerWidth;
    protected float  mEndIndicator;
    private   int    mHeight;
    protected float  mIndicator;
    protected int    mIndicatorCenter;
    protected int    mIndicatorGray;
    protected int    mIndicatorLight;
    protected int    mNormalGray;
    protected int    mOutIndicatorSize;
    protected float  mStartIndicator;
    protected String mTitle;
    protected int    mTitleColor;
    protected int    mTitleSize;
    protected String mUnit;
    protected int    mUnitColor;
    protected int    mUnitSize;
    private   int    mWidth;

    public BaseCircle(Context context) {
        this(context, null);
    }

    public BaseCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDividerWidth = 32;
        this.mNormalGray = Color.parseColor("#FFDFDFDF");
        this.mWidth = 0;
        this.mHeight = 0;
        this.mStartIndicator = 10.0f;
        this.mEndIndicator = 60.0f;
        this.mIndicator = 10.0f;
        this.mTitle = " ";
        this.mContent = " ";
        this.mUnit = " ";
        this.mAlert = " ";
        this.mCircleBackground = getResources().getColor(R.color.circle_background);
        this.mCircleGray = getResources().getColor(R.color.circle_gray);
        this.mCircleGreen = getResources().getColor(R.color.circle_green);
        this.mDividerWidth = (int) getResources().getDimension(R.dimen.divider_width);
        this.mOutIndicatorSize = (int) getResources().getDimension(R.dimen.out_indicator_size);
        this.mTitleSize = (int) getResources().getDimension(R.dimen.title_indicator_size);
        this.mContentSize = (int) getResources().getDimension(R.dimen.content_indicator_size);
        this.mUnitSize = (int) getResources().getDimension(R.dimen.unit_indicator_size);
        this.mAlertSize = (int) getResources().getDimension(R.dimen.alert_indicator_size);
        this.mCenterCircleOut = (int) getResources().getDimension(R.dimen.center_circle_out);
        this.mCenterCircleMiddle = (int) getResources().getDimension(R.dimen.center_circle_middle);
        this.mCenterCircleInside = (int) getResources().getDimension(R.dimen.center_circle_inside);
        this.mNormalGray = getResources().getColor(R.color.normal_gray);
        this.mIndicatorCenter = getResources().getColor(R.color.indicator_center);
        this.mIndicatorGray = getResources().getColor(R.color.indicator_gray);
        this.mIndicatorLight = getResources().getColor(R.color.indicator_light);
        this.mTitleColor = getResources().getColor(R.color.circle_title);
        this.mAlertColor = getResources().getColor(R.color.circle_alert);
        this.mContentColor = getResources().getColor(R.color.circle_content);
        this.mUnitColor = getResources().getColor(R.color.circle_unit);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleView);
        this.mDividerWidth = (int) typedArray.getDimension(R.styleable
                .CircleView_circle_out_indicator_size, (float) this.mDividerWidth);
        this.mOutIndicatorSize = (int) typedArray.getDimension(R.styleable
                .CircleView_circle_out_indicator_size, (float) this.mOutIndicatorSize);
        this.mTitleSize = (int) typedArray.getDimension(R.styleable
                .CircleView_circle_title_indicator_size, (float) this.mTitleSize);
        this.mContentSize = (int) typedArray.getDimension(R.styleable
                .CircleView_circle_content_indicator_size, (float) this.mContentSize);
        this.mUnitSize = (int) typedArray.getDimension(R.styleable
                .CircleView_circle_unit_indicator_size, (float) this.mUnitSize);
        this.mAlertSize = (int) typedArray.getDimension(R.styleable
                .CircleView_circle_alert_indicator_size, (float) this.mAlertSize);
        this.mCenterCircleOut = (int) typedArray.getDimension(R.styleable
                .CircleView_circle_center_out, (float) this.mCenterCircleOut);
        this.mCenterCircleMiddle = (int) typedArray.getDimension(R.styleable
                .CircleView_circle_center_middle, (float) this.mCenterCircleMiddle);
        this.mCenterCircleInside = (int) typedArray.getDimension(R.styleable
                .CircleView_circle_center_inside, (float) this.mCenterCircleInside);
        this.mNormalGray = typedArray.getColor(R.styleable.CircleView_circle_normal_gray, this
                .mNormalGray);
        this.mIndicatorCenter = typedArray.getColor(R.styleable
                .CircleView_circle_indicator_center, this.mIndicatorCenter);
        this.mIndicatorGray = typedArray.getColor(R.styleable.CircleView_circle_indicator_gray,
                this.mIndicatorGray);
        this.mIndicatorLight = typedArray.getColor(R.styleable.CircleView_circle_indicator_light,
                this.mIndicatorLight);
        this.mTitleColor = typedArray.getColor(R.styleable.CircleView_circle_title, this
                .mTitleColor);
        this.mAlertColor = typedArray.getColor(R.styleable.CircleView_circle_alert, this
                .mAlertColor);
        this.mContentColor = typedArray.getColor(R.styleable.CircleView_circle_content, this
                .mContentColor);
        this.mUnitColor = typedArray.getColor(R.styleable.CircleView_circle_unit, this.mUnitColor);
        typedArray.recycle();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(specSize, specSize);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    protected void drawBackground(Canvas canvas) {
        Paint bgPaint = new Paint();
        bgPaint.setColor(this.mCircleBackground);
        int radius = getViewRadius() - ((this.mDividerWidth * 3) / 2);
        canvas.drawCircle((float) getCenterX(), (float) getCenterY(), (float) radius, bgPaint);
        Path path = new Path();
        path.addCircle((float) getCenterX(), (float) getCenterY(), (float) radius, Direction.CW);
        Paint paint = new Paint();
        paint.setColor(this.mCircleGray);
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(1.0f);
        canvas.drawPath(path, paint);
    }

    protected void drawContent(Canvas canvas) {
        int radius = (int) (((float) getViewRadius()) - (CIRCLE_GAP * ((float) this
                .mDividerWidth)));
        Paint titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(getResources().getDimension(R.dimen.title_indicator_size));
        titlePaint.setColor(this.mTitleColor);
        int titleX = getCenterX() - (ViewUtils.getTextWidth(titlePaint, this.mTitle) / 2);
        int titleY = getCenterY() - ((radius * 3) / 5);
        canvas.drawText(this.mTitle, (float) titleX, (float) titleY, titlePaint);
        Paint line = new Paint();
        line.setStrokeWidth(IPhotoView.DEFAULT_MAX_SCALE);
        line.setColor(this.mCircleGray);
        int lineY = (getCenterY() + ((int) (((double) radius) * Math.sin(0.7853981633974483d))))
                + (this.mDividerWidth / 2);
        Canvas canvas2 = canvas;
        canvas2.drawLine((float) ((getCenterX() - ((int) (((double) radius) * Math.sin(0
        .7853981633974483d)))) + this.mDividerWidth), (float) lineY, (float) ((getCenterX() + (
                (int) (((double) radius) * Math.sin(0.7853981633974483d)))) - this.mDividerWidth)
                , (float) lineY, line);
        Paint alertPaint = new Paint();
        alertPaint.setAntiAlias(true);
        alertPaint.setTextSize(getResources().getDimension(R.dimen.alert_indicator_size));
        alertPaint.setColor(this.mAlertColor);
        Canvas canvas3 = canvas;
        canvas3.drawText(this.mAlert, (float) (getCenterX() - (ViewUtils.getTextWidth(alertPaint,
                this.mAlert) / 2)), (float) (lineY + ((this.mDividerWidth * 3) / 2)), alertPaint);
        Paint contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        contentPaint.setTextSize(getResources().getDimension(R.dimen.content_indicator_size));
        contentPaint.setColor(this.mContentColor);
        Paint unitPaint = new Paint();
        unitPaint.setAntiAlias(true);
        unitPaint.setTextSize(getResources().getDimension(R.dimen.unit_indicator_size));
        unitPaint.setColor(this.mContentColor);
        int contentWidth = ViewUtils.getTextWidth(contentPaint, this.mContent);
        if (!TextUtils.isEmpty(this.mUnit)) {
            contentWidth += ViewUtils.getTextWidth(unitPaint, this.mUnit);
        }
        int lineMargin = this.mDividerWidth / 2;
        int contentX = getCenterX() - (contentWidth / 2);
        canvas3 = canvas;
        canvas3.drawText(this.mContent, (float) contentX, (float) (lineY - lineMargin),
                contentPaint);
        int unitY = lineY - lineMargin;
        int unitX = contentX + ViewUtils.getTextWidth(contentPaint, this.mContent);
        canvas.drawText(this.mUnit, (float) unitX, (float) unitY, unitPaint);
    }

    protected void drawIndicator(Canvas canvas) {
        Paint centerPaint = new Paint();
        centerPaint.setColor(this.mIndicatorCenter);
        canvas.drawCircle((float) getCenterX(), (float) getCenterY(), (float) this
                .mCenterCircleOut, centerPaint);
        RectF oval = new RectF((float) (getCenterX() - this.mCenterCircleMiddle), (float)
                (getCenterY() - this.mCenterCircleMiddle), (float) (getCenterX() + this
                .mCenterCircleMiddle), (float) (getCenterY() + this.mCenterCircleMiddle));
        float angle = (270.0f * (this.mIndicator - this.mStartIndicator)) / (this.mEndIndicator -
                this.mStartIndicator);
        Paint lightPaint = new Paint();
        lightPaint.setColor(this.mIndicatorLight);
        canvas.drawArc(oval, 90.0f + (135.0f + angle), 90.0f, true, lightPaint);
        Paint grayPaint = new Paint();
        grayPaint.setColor(this.mIndicatorGray);
        canvas.drawArc(oval, (135.0f + angle) + 180.0f, 90.0f, true, grayPaint);
        drawArrow(canvas, angle, this.mCenterCircleMiddle, grayPaint, false);
        drawArrow(canvas, angle, this.mCenterCircleMiddle, lightPaint, true);
        canvas.drawArc(new RectF((float) (getCenterX() - this.mCenterCircleInside), (float)
                (getCenterY() - this.mCenterCircleInside), (float) (getCenterX() + this
                .mCenterCircleInside), (float) (getCenterY() + this.mCenterCircleInside)), 0.0f,
                360.0f, true, centerPaint);
    }

    private void drawArrow(Canvas canvas, float angle, int radius, Paint paint, boolean isLight) {
        double oneAngle = (((double) ((135.0f + angle) - 90.0f)) * 3.141592653589793d) / 180.0d;
        if (isLight) {
            oneAngle = (((double) ((135.0f + angle) + 90.0f)) * 3.141592653589793d) / 180.0d;
        }
        int oneX = ((int) (((double) radius) * Math.cos(oneAngle))) + getCenterX();
        int oneY = ((int) (((double) radius) * Math.sin(oneAngle))) + getCenterY();
        double twoAngle = (((double) (135.0f + angle)) * 3.141592653589793d) / 180.0d;
        int middleRadius = (int) (((float) getViewRadius()) - (CIRCLE_GAP * ((float) this
                .mDividerWidth)));
        int twoX = ((int) (((double) middleRadius) * Math.cos(twoAngle))) + getCenterX();
        int twoY = ((int) (((double) middleRadius) * Math.sin(twoAngle))) + getCenterY();
        Path path = new Path();
        path.moveTo((float) getCenterX(), (float) getCenterY());
        path.lineTo((float) oneX, (float) oneY);
        path.lineTo((float) twoX, (float) twoY);
        path.close();
        canvas.drawPath(path, paint);
    }

    protected int getCenterX() {
        return this.mWidth / 2;
    }

    protected int getCenterY() {
        return this.mHeight / 2;
    }

    protected int getViewRadius() {
        return getCenterY() > getCenterX() ? getCenterX() : getCenterY();
    }

    public void setContent(String title, String content, String unit, String alert) {
        this.mTitle = title;
        this.mContent = content;
        this.mUnit = unit;
        this.mAlert = alert;
        postInvalidate();
    }

    public void setContentColor(int contentColor, int unitColor) {
        this.mContentColor = contentColor;
        this.mUnitColor = unitColor;
    }

    public void setIndicator(float indicator) {
        if (indicator <= this.mStartIndicator) {
            this.mIndicator = this.mStartIndicator;
        } else if (indicator > this.mEndIndicator) {
            this.mIndicator = this.mEndIndicator;
        } else {
            this.mIndicator = indicator;
        }
        postInvalidate();
    }

    public float getIndicator() {
        return this.mStartIndicator;
    }

    public void animateIndicator(float indicator) {
        Interpolator interpolator = new AnticipateOvershootInterpolator(1.8f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(this, "indicator", new
                float[]{indicator});
        animation.setDuration(2000);
        animation.setInterpolator(interpolator);
        animation.start();
    }

    public static String formatNumber(float value) {
        int temp = (int) value;
        if (value > ((float) temp)) {
            return String.valueOf(value);
        }
        return String.valueOf(temp);
    }
}
