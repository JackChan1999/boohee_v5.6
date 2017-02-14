package com.boohee.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

import com.boohee.utils.Helper;

public class PathListView extends ListView {
    private static final int    MAX_Y_OVERSCROLL_DISTANCE = 200;
    public static final  double NO_ZOOM                   = 1.0d;
    static final         String TAG                       = PathListView.class.getSimpleName();
    public static final  double ZOOM_X2                   = 2.0d;
    private Context mContext;
    int mDrawableMaxHeight = -1;
    ImageView mImageView;
    int mImageViewHeight = -1;
    private int mMaxYOverscrollDistance;
    private OnOverScrollByListener scrollByListener = new OnOverScrollByListener() {
        public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int
                scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean
                                            isTouchEvent) {
            if (PathListView.this.mImageView.getHeight() <= PathListView.this.mDrawableMaxHeight
                    && isTouchEvent) {
                if (deltaY < 0) {
                    if (PathListView.this.mImageView.getHeight() - (deltaY / 2) >= PathListView
                            .this.mImageViewHeight) {
                        PathListView.this.mImageView.getLayoutParams().height = PathListView.this
                                .mImageView.getHeight() - (deltaY / 2) < PathListView.this
                                .mDrawableMaxHeight ? PathListView.this.mImageView.getHeight() -
                                (deltaY / 2) : PathListView.this.mDrawableMaxHeight;
                        PathListView.this.mImageView.requestLayout();
                    }
                } else if (PathListView.this.mImageView.getHeight() > PathListView.this
                        .mImageViewHeight) {
                    PathListView.this.mImageView.getLayoutParams().height = PathListView.this
                            .mImageView.getHeight() - deltaY > PathListView.this.mImageViewHeight
                            ? PathListView.this.mImageView.getHeight() - deltaY : PathListView
                            .this.mImageViewHeight;
                    PathListView.this.mImageView.requestLayout();
                    return true;
                }
            }
            return false;
        }
    };

    private interface OnOverScrollByListener {
        boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8,
                             boolean z);
    }

    public class ResetAnimimation extends Animation {
        int extraHeight = (this.targetHeight - this.originalHeight);
        View mView;
        int  originalHeight;
        int  targetHeight;

        protected ResetAnimimation(View view, int targetHeight) {
            this.mView = view;
            this.targetHeight = targetHeight;
            this.originalHeight = view.getHeight();
        }

        protected void applyTransformation(float interpolatedTime, Transformation t) {
            this.mView.getLayoutParams().height = (int) (((float) this.targetHeight) - (((float)
                    this.extraHeight) * (1.0f - interpolatedTime)));
            this.mView.requestLayout();
        }

        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }
    }

    public PathListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initListView();
    }

    public PathListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initListView();
    }

    public PathListView(Context context) {
        super(context);
        this.mContext = context;
        initListView();
    }

    private void initListView() {
        this.mMaxYOverscrollDistance = (int) (200.0f * this.mContext.getResources()
                .getDisplayMetrics().density);
    }

    public void setParallaxImageView(ImageView iv) {
        this.mImageView = iv;
    }

    public void setViewsBounds(double zoomRatio) {
        if (this.mImageViewHeight == -1) {
            this.mImageViewHeight = this.mImageView.getHeight();
            double ratio = ((double) this.mImageView.getDrawable().getIntrinsicWidth()) / (
                    (double) this.mImageView.getWidth());
            double intrinsicHeight = ((double) this.mImageView.getDrawable().getIntrinsicHeight()
            ) / ratio;
            if (zoomRatio <= NO_ZOOM) {
                zoomRatio = NO_ZOOM;
            }
            this.mDrawableMaxHeight = (int) (intrinsicHeight * zoomRatio);
            Helper.showLog(TAG, "ratio:" + ratio);
            Helper.showLog(TAG, "mDrawableMaxHeight:" + this.mDrawableMaxHeight);
        }
    }

    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int
            scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean
            isTouchEvent) {
        boolean isCollapseAnimation = this.scrollByListener.overScrollBy(deltaX, deltaY, scrollX,
                scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
                isTouchEvent) || false;
        return isCollapseAnimation ? true : super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 1 && this.mImageViewHeight - 1 < this.mImageView.getHeight()) {
            ResetAnimimation animation = new ResetAnimimation(this.mImageView, this
                    .mImageViewHeight);
            Helper.showLog(TAG, "mImageViewHeight:" + this.mImageViewHeight);
            animation.setDuration(300);
            this.mImageView.startAnimation(animation);
        }
        return super.onTouchEvent(ev);
    }
}
