package me.nereo.multi_image_selector.view;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GestureImageView extends ImageView {
    private static final float MAX_SCALE_FACTOR = 3.0f;
    private static final float MIN_SCALE_FACTOR = 0.3f;
    private static final String TAG = "GestureImageView";
    private int mCenterX;
    private int mCenterY;
    private float mCurrentFactor = 1.0f;
    private float mFirstPointerX;
    private float mFirstPointerY;
    private GestureDetectorCompat mGestureDetector;
    private Matrix mImageMatrix;
    private ScaleGestureDetector mScaleGesture;
    private float mSecondPointerX;
    private float mSecondPointerY;
    private int mTouchSlop;

    public GestureImageView(Context context) {
        super(context);
        init(context);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setScaleType(ScaleType.MATRIX);
        this.mImageMatrix = new Matrix();
        this.mScaleGesture = new ScaleGestureDetector(context, new SimpleOnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector detector) {
                float factor = detector.getScaleFactor();
                GestureImageView.this.mImageMatrix.postScale(factor, factor, (float) GestureImageView.this.mCenterX, (float) GestureImageView.this.mCenterY);
                GestureImageView.this.setImageMatrix(GestureImageView.this.mImageMatrix);
                return true;
            }
        });
        this.mGestureDetector = new GestureDetectorCompat(context, new SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                GestureImageView.this.mImageMatrix.postScale(1.0f, 1.0f, (float) GestureImageView.this.mCenterX, (float) GestureImageView.this.mCenterY);
                GestureImageView.this.setImageMatrix(GestureImageView.this.mImageMatrix);
                return true;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            this.mImageMatrix.setTranslate((float) ((w - getDrawable().getIntrinsicWidth()) / 2), (float) ((h - getDrawable().getIntrinsicHeight()) / 2));
            setImageMatrix(this.mImageMatrix);
            this.mCenterX = w / 2;
            this.mCenterY = h / 2;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean retValue = this.mScaleGesture.onTouchEvent(event);
        if (this.mGestureDetector.onTouchEvent(event) || retValue) {
            retValue = true;
        } else {
            retValue = false;
        }
        if (retValue || super.onTouchEvent(event)) {
            return true;
        }
        return false;
    }
}
