package com.yalantis.ucrop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.yalantis.ucrop.util.RotationGestureDetector;

public class GestureCropImageView extends CropImageView {
    private static final int DOUBLE_TAP_ZOOM_DURATION = 200;
    private int                     mDoubleTapScaleSteps;
    private GestureDetector         mGestureDetector;
    private boolean                 mIsRotateEnabled;
    private boolean                 mIsScaleEnabled;
    private float                   mMidPntX;
    private float                   mMidPntY;
    private RotationGestureDetector mRotateDetector;
    private ScaleGestureDetector    mScaleDetector;

    public GestureCropImageView(Context context) {
        super(context);
        this.mIsRotateEnabled = true;
        this.mIsScaleEnabled = true;
        this.mDoubleTapScaleSteps = 5;
    }

    public GestureCropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureCropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsRotateEnabled = true;
        this.mIsScaleEnabled = true;
        this.mDoubleTapScaleSteps = 5;
    }

    public void setScaleEnabled(boolean scaleEnabled) {
        this.mIsScaleEnabled = scaleEnabled;
    }

    public boolean isScaleEnabled() {
        return this.mIsScaleEnabled;
    }

    public void setRotateEnabled(boolean rotateEnabled) {
        this.mIsRotateEnabled = rotateEnabled;
    }

    public boolean isRotateEnabled() {
        return this.mIsRotateEnabled;
    }

    public void setDoubleTapScaleSteps(int doubleTapScaleSteps) {
        this.mDoubleTapScaleSteps = doubleTapScaleSteps;
    }

    public int getDoubleTapScaleSteps() {
        return this.mDoubleTapScaleSteps;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getAction() & 255) == 0) {
            cancelAllAnimations();
        }
        if (event.getPointerCount() > 1) {
            this.mMidPntX = (event.getX(0) + event.getX(1)) / 2.0f;
            this.mMidPntY = (event.getY(0) + event.getY(1)) / 2.0f;
        }
        this.mGestureDetector.onTouchEvent(event);
        if (this.mIsScaleEnabled) {
            this.mScaleDetector.onTouchEvent(event);
        }
        if (this.mIsRotateEnabled) {
            this.mRotateDetector.onTouchEvent(event);
        }
        if ((event.getAction() & 255) == 1) {
            setImageToWrapCropBounds();
        }
        return true;
    }

    protected void init() {
        super.init();
        setupGestureListeners();
    }

    protected float getDoubleTapTargetScale() {
        return getCurrentScale() * ((float) Math.pow((double) (getMaxScale() / getMinScale()),
                (double) (1.0f / ((float) this.mDoubleTapScaleSteps))));
    }

    private void setupGestureListeners() {
        this.mGestureDetector = new GestureDetector(getContext(), new GestureListener(this, null)
                , null, true);
        this.mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener(this, null));
        this.mRotateDetector = new RotationGestureDetector(new RotateListener(this, null));
    }
}
