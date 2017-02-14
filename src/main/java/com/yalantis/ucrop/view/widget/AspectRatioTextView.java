package com.yalantis.ucrop.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yalantis.ucrop.R;

public class AspectRatioTextView extends TextView {
    private       float  mAspectRatio;
    private       String mAspectRatioTitle;
    private       float  mAspectRatioX;
    private       float  mAspectRatioY;
    private final Rect   mCanvasClipBounds;
    private       Paint  mDotPaint;
    private       int    mDotSize;

    public AspectRatioTextView(Context context) {
        this(context, null);
    }

    public AspectRatioTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCanvasClipBounds = new Rect();
        init(context.obtainStyledAttributes(attrs, R.styleable.ucrop_AspectRatioTextView));
    }

    @TargetApi(21)
    public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCanvasClipBounds = new Rect();
        init(context.obtainStyledAttributes(attrs, R.styleable.ucrop_AspectRatioTextView));
    }

    public void setActiveColor(@ColorInt int activeColor) {
        applyActiveColor(activeColor);
        invalidate();
    }

    public float getAspectRatio(boolean toggleRatio) {
        if (toggleRatio) {
            toggleAspectRatio();
            setTitle();
        }
        return this.mAspectRatio;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelected()) {
            canvas.getClipBounds(this.mCanvasClipBounds);
            canvas.drawCircle(((float) (this.mCanvasClipBounds.right - this.mCanvasClipBounds
                    .left)) / 2.0f, (float) (this.mCanvasClipBounds.bottom - this.mDotSize),
                    (float) (this.mDotSize / 2), this.mDotPaint);
        }
    }

    private void init(@NonNull TypedArray a) {
        setGravity(1);
        this.mAspectRatioTitle = a.getString(R.styleable
                .ucrop_AspectRatioTextView_ucrop_artv_ratio_title);
        this.mAspectRatioX = a.getFloat(R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_x,
                0.0f);
        this.mAspectRatioY = a.getFloat(R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_y,
                0.0f);
        if (this.mAspectRatioX == 0.0f || this.mAspectRatioY == 0.0f) {
            this.mAspectRatio = 0.0f;
        } else {
            this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
        }
        this.mDotSize = getContext().getResources().getDimensionPixelSize(R.dimen
                .ucrop_size_dot_scale_text_view);
        this.mDotPaint = new Paint(1);
        this.mDotPaint.setStyle(Style.FILL);
        setTitle();
        applyActiveColor(getResources().getColor(R.color.ucrop_color_widget_active));
        a.recycle();
    }

    private void applyActiveColor(@ColorInt int activeColor) {
        if (this.mDotPaint != null) {
            this.mDotPaint.setColor(activeColor);
        }
        r1 = new int[2][];
        r1[0] = new int[]{16842913};
        r1[1] = new int[]{0};
        setTextColor(new ColorStateList(r1, new int[]{activeColor, ContextCompat.getColor
                (getContext(), R.color.ucrop_color_widget)}));
    }

    private void toggleAspectRatio() {
        if (this.mAspectRatio != 0.0f) {
            float tempRatioW = this.mAspectRatioX;
            this.mAspectRatioX = this.mAspectRatioY;
            this.mAspectRatioY = tempRatioW;
            this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
        }
    }

    private void setTitle() {
        if (TextUtils.isEmpty(this.mAspectRatioTitle)) {
            setText(String.format("%d:%d", new Object[]{Integer.valueOf((int) this.mAspectRatioX)
                    , Integer.valueOf((int) this.mAspectRatioY)}));
            return;
        }
        setText(this.mAspectRatioTitle);
    }
}
