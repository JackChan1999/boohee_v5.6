package com.meiqia.meiqiasdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

import com.meiqia.meiqiasdk.R;

public class MQImageView extends ImageView {
    private int                       mBorderColor;
    private Paint                     mBorderPaint;
    private int                       mBorderWidth;
    private int                       mCornerRadius;
    private int                       mDefaultImageId;
    private boolean                   mIsCircle;
    private boolean                   mIsSquare;
    private OnDrawableChangedCallback mOnDrawableChangedCallback;
    private RectF                     mRect;

    public MQImageView(Context context) {
        this(context, null);
    }

    public MQImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MQImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCornerRadius = 0;
        this.mIsCircle = false;
        this.mIsSquare = false;
        this.mBorderWidth = 0;
        this.mBorderColor = -1;
        initCustomAttrs(context, attrs);
        initBorderPaint();
        setDefaultImage();
        this.mRect = new RectF();
    }

    private void initBorderPaint() {
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setAntiAlias(true);
        this.mBorderPaint.setStyle(Style.STROKE);
        this.mBorderPaint.setColor(this.mBorderColor);
        this.mBorderPaint.setStrokeWidth((float) this.mBorderWidth);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MQImageView);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.MQImageView_android_src) {
            this.mDefaultImageId = typedArray.getResourceId(attr, 0);
        } else if (attr == R.styleable.MQImageView_mq_iv_isCircle) {
            this.mIsCircle = typedArray.getBoolean(attr, this.mIsCircle);
        } else if (attr == R.styleable.MQImageView_mq_iv_cornerRadius) {
            this.mCornerRadius = typedArray.getDimensionPixelSize(attr, this.mCornerRadius);
        } else if (attr == R.styleable.MQImageView_mq_iv_isSquare) {
            this.mIsSquare = typedArray.getBoolean(attr, this.mIsSquare);
        } else if (attr == R.styleable.MQImageView_mq_iv_borderWidth) {
            this.mBorderWidth = typedArray.getDimensionPixelSize(attr, this.mBorderWidth);
        } else if (attr == R.styleable.MQImageView_mq_iv_borderColor) {
            this.mBorderColor = typedArray.getColor(attr, this.mBorderColor);
        }
    }

    private void setDefaultImage() {
        if (this.mDefaultImageId != 0) {
            setImageResource(this.mDefaultImageId);
        }
    }

    public void setImageResource(@DrawableRes int resId) {
        if (resId != 0 && this.mCornerRadius > 0) {
            setImageDrawable(getRoundedDrawable(getContext(), resId, (float) this.mCornerRadius));
        } else if (resId == 0 || !this.mIsCircle) {
            super.setImageResource(resId);
            notifyDrawableChanged();
        } else {
            setImageDrawable(getCircleDrawable(getContext(), resId));
        }
    }

    public void setImageDrawable(@Nullable Drawable drawable) {
        Bitmap bitmap;
        if ((drawable instanceof BitmapDrawable) && this.mCornerRadius > 0) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                super.setImageDrawable(getRoundedDrawable(getContext(), bitmap, (float) this
                        .mCornerRadius));
            } else {
                super.setImageDrawable(drawable);
            }
        } else if ((drawable instanceof BitmapDrawable) && this.mIsCircle) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                super.setImageDrawable(getCircleDrawable(getContext(), bitmap));
            } else {
                super.setImageDrawable(drawable);
            }
        } else {
            super.setImageDrawable(drawable);
        }
        notifyDrawableChanged();
    }

    private void notifyDrawableChanged() {
        if (this.mOnDrawableChangedCallback != null) {
            this.mOnDrawableChangedCallback.onDrawableChanged();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mIsCircle || this.mIsSquare) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0,
                    heightMeasureSpec));
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
            heightMeasureSpec = widthMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mBorderWidth <= 0) {
            return;
        }
        if (this.mIsCircle) {
            canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) (
                    (getWidth() / 2) - (this.mBorderWidth / 2)), this.mBorderPaint);
            return;
        }
        this.mRect.left = 0.0f;
        this.mRect.top = 0.0f;
        this.mRect.right = (float) getWidth();
        this.mRect.bottom = (float) getHeight();
        canvas.drawRoundRect(this.mRect, (float) this.mCornerRadius, (float) this.mCornerRadius,
                this.mBorderPaint);
    }

    public void setDrawableChangedCallback(OnDrawableChangedCallback onDrawableChangedCallback) {
        this.mOnDrawableChangedCallback = onDrawableChangedCallback;
    }

    public static RoundedBitmapDrawable getCircleDrawable(Context context, Bitmap bitmap) {
        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(context
                .getResources(), bitmap);
        circleDrawable.setAntiAlias(true);
        circleDrawable.setCornerRadius(((float) Math.min(bitmap.getWidth(), bitmap.getHeight()))
                / 2.0f);
        return circleDrawable;
    }

    public static RoundedBitmapDrawable getCircleDrawable(Context context, @DrawableRes int resId) {
        return getCircleDrawable(context, BitmapFactory.decodeResource(context.getResources(),
                resId));
    }

    public static RoundedBitmapDrawable getRoundedDrawable(Context context, Bitmap bitmap, float
            cornerRadius) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context
                .getResources(), bitmap);
        roundedBitmapDrawable.setAntiAlias(true);
        roundedBitmapDrawable.setCornerRadius(cornerRadius);
        return roundedBitmapDrawable;
    }

    public static RoundedBitmapDrawable getRoundedDrawable(Context context, @DrawableRes int resId, float cornerRadius) {
        return getRoundedDrawable(context, BitmapFactory.decodeResource(context.getResources(), resId), cornerRadius);
    }
}
