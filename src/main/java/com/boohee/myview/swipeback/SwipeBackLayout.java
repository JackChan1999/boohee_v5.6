package com.boohee.myview.swipeback;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.boohee.myview.swipeback.ViewDragHelper.Callback;
import com.boohee.one.R;

import java.util.ArrayList;
import java.util.List;

public class SwipeBackLayout extends FrameLayout {
    private static final int   DEFAULT_SCRIM_COLOR      = -1728053248;
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;
    public static final  int   EDGE_ALL                 = 11;
    public static final  int   EDGE_BOTTOM              = 8;
    private static final int[] EDGE_FLAGS               = new int[]{1, 2, 8, 11};
    public static final  int   EDGE_LEFT                = 1;
    public static final  int   EDGE_RIGHT               = 2;
    private static final int   FULL_ALPHA               = 255;
    private static final int   MIN_FLING_VELOCITY       = 400;
    private static final int   OVERSCROLL_DISTANCE      = 10;
    public static final  int   STATE_DRAGGING           = 1;
    public static final  int   STATE_IDLE               = 0;
    public static final  int   STATE_SETTLING           = 2;
    private Activity            mActivity;
    private int                 mContentLeft;
    private int                 mContentTop;
    private View                mContentView;
    private ViewDragHelper      mDragHelper;
    private int                 mEdgeFlag;
    private boolean             mEnable;
    private boolean             mInLayout;
    private List<SwipeListener> mListeners;
    private int                 mScrimColor;
    private float               mScrimOpacity;
    private float               mScrollPercent;
    private float               mScrollThreshold;
    private Drawable            mShadowBottom;
    private Drawable            mShadowLeft;
    private Drawable            mShadowRight;
    private Rect                mTmpRect;
    private int                 mTrackingEdge;

    public interface SwipeListener {
        void onEdgeTouch(int i);

        void onScrollOverThreshold();

        void onScrollStateChange(int i, float f);
    }

    private class ViewDragCallback extends Callback {
        private boolean mIsScrollOverValid;

        private ViewDragCallback() {
        }

        public boolean tryCaptureView(View view, int i) {
            boolean ret = SwipeBackLayout.this.mDragHelper.isEdgeTouched(SwipeBackLayout.this
                    .mEdgeFlag, i);
            if (ret) {
                if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(1, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 1;
                } else if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(2, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 2;
                } else if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(8, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 8;
                }
                if (!(SwipeBackLayout.this.mListeners == null || SwipeBackLayout.this.mListeners
                        .isEmpty())) {
                    for (SwipeListener listener : SwipeBackLayout.this.mListeners) {
                        listener.onEdgeTouch(SwipeBackLayout.this.mTrackingEdge);
                    }
                }
                this.mIsScrollOverValid = true;
            }
            return ret;
        }

        public int getViewHorizontalDragRange(View child) {
            return SwipeBackLayout.this.mEdgeFlag & 3;
        }

        public int getViewVerticalDragRange(View child) {
            return SwipeBackLayout.this.mEdgeFlag & 8;
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if ((SwipeBackLayout.this.mTrackingEdge & 1) != 0) {
                SwipeBackLayout.this.mScrollPercent = Math.abs(((float) left) / ((float)
                        (SwipeBackLayout.this.mContentView.getWidth() + SwipeBackLayout.this
                                .mShadowLeft.getIntrinsicWidth())));
            } else if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                SwipeBackLayout.this.mScrollPercent = Math.abs(((float) left) / ((float)
                        (SwipeBackLayout.this.mContentView.getWidth() + SwipeBackLayout.this
                                .mShadowRight.getIntrinsicWidth())));
            } else if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                SwipeBackLayout.this.mScrollPercent = Math.abs(((float) top) / ((float)
                        (SwipeBackLayout.this.mContentView.getHeight() + SwipeBackLayout.this
                                .mShadowBottom.getIntrinsicHeight())));
            }
            SwipeBackLayout.this.mContentLeft = left;
            SwipeBackLayout.this.mContentTop = top;
            SwipeBackLayout.this.invalidate();
            if (SwipeBackLayout.this.mScrollPercent < SwipeBackLayout.this.mScrollThreshold &&
                    !this.mIsScrollOverValid) {
                this.mIsScrollOverValid = true;
            }
            if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners
                    .isEmpty() && SwipeBackLayout.this.mDragHelper.getViewDragState() == 1 &&
                    SwipeBackLayout.this.mScrollPercent >= SwipeBackLayout.this.mScrollThreshold
                    && this.mIsScrollOverValid) {
                this.mIsScrollOverValid = false;
                for (SwipeListener listener : SwipeBackLayout.this.mListeners) {
                    listener.onScrollOverThreshold();
                }
            }
            if (SwipeBackLayout.this.mScrollPercent >= 1.0f) {
                if (!SwipeBackLayout.this.mActivity.isFinishing()) {
                    SwipeBackLayout.this.mActivity.finish();
                }
                SwipeBackLayout.this.mActivity.overridePendingTransition(0, 0);
            }
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int childWidth = releasedChild.getWidth();
            int childHeight = releasedChild.getHeight();
            int left = 0;
            int top = 0;
            if ((SwipeBackLayout.this.mTrackingEdge & 1) != 0) {
                left = (xvel > 0.0f || (xvel == 0.0f && SwipeBackLayout.this.mScrollPercent >
                        SwipeBackLayout.this.mScrollThreshold)) ? (SwipeBackLayout.this
                        .mShadowLeft.getIntrinsicWidth() + childWidth) + 10 : 0;
            } else if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                left = (xvel < 0.0f || (xvel == 0.0f && SwipeBackLayout.this.mScrollPercent >
                        SwipeBackLayout.this.mScrollThreshold)) ? -((SwipeBackLayout.this
                        .mShadowLeft.getIntrinsicWidth() + childWidth) + 10) : 0;
            } else if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                top = (yvel < 0.0f || (yvel == 0.0f && SwipeBackLayout.this.mScrollPercent >
                        SwipeBackLayout.this.mScrollThreshold)) ? -((SwipeBackLayout.this
                        .mShadowBottom.getIntrinsicHeight() + childHeight) + 10) : 0;
            }
            SwipeBackLayout.this.mDragHelper.settleCapturedViewAt(left, top);
            SwipeBackLayout.this.invalidate();
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if ((SwipeBackLayout.this.mTrackingEdge & 1) != 0) {
                return Math.min(child.getWidth(), Math.max(left, 0));
            }
            if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                return Math.min(0, Math.max(left, -child.getWidth()));
            }
            return 0;
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                return Math.min(0, Math.max(top, -child.getHeight()));
            }
            return 0;
        }

        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners
                    .isEmpty()) {
                for (SwipeListener listener : SwipeBackLayout.this.mListeners) {
                    listener.onScrollStateChange(state, SwipeBackLayout.this.mScrollPercent);
                }
            }
        }
    }

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.a);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;
        this.mEnable = true;
        this.mScrimColor = DEFAULT_SCRIM_COLOR;
        this.mTmpRect = new Rect();
        this.mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeBackLayout,
                defStyle, R.style.fq);
        int edgeSize = a.getDimensionPixelSize(0, -1);
        if (edgeSize > 0) {
            setEdgeSize(edgeSize);
        }
        setEdgeTrackingEnabled(EDGE_FLAGS[a.getInt(1, 0)]);
        int shadowLeft = a.getResourceId(2, R.drawable.vn);
        int shadowRight = a.getResourceId(3, R.drawable.vo);
        int shadowBottom = a.getResourceId(4, R.drawable.vm);
        setShadow(shadowLeft, 1);
        setShadow(shadowRight, 2);
        setShadow(shadowBottom, 8);
        a.recycle();
        this.mDragHelper.setMinVelocity(400.0f * getResources().getDisplayMetrics().density);
    }

    public void setSensitivity(Context context, float sensitivity) {
        this.mDragHelper.setSensitivity(context, sensitivity);
    }

    private void setContentView(View view) {
        this.mContentView = view;
    }

    public void setEnableGesture(boolean enable) {
        this.mEnable = enable;
    }

    public void setEdgeTrackingEnabled(int edgeFlags) {
        this.mEdgeFlag = edgeFlags;
        this.mDragHelper.setEdgeTrackingEnabled(this.mEdgeFlag);
    }

    public void setScrimColor(int color) {
        this.mScrimColor = color;
        invalidate();
    }

    public void setEdgeSize(int size) {
        this.mDragHelper.setEdgeSize(size);
    }

    @Deprecated
    public void setSwipeListener(SwipeListener listener) {
        addSwipeListener(listener);
    }

    public void addSwipeListener(SwipeListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(listener);
    }

    public void removeSwipeListener(SwipeListener listener) {
        if (this.mListeners != null) {
            this.mListeners.remove(listener);
        }
    }

    public void setScrollThresHold(float threshold) {
        if (threshold >= 1.0f || threshold <= 0.0f) {
            throw new IllegalArgumentException("Threshold value should be between 0 and 1.0");
        }
        this.mScrollThreshold = threshold;
    }

    public void setShadow(Drawable shadow, int edgeFlags) {
        if ((edgeFlags & 1) != 0) {
            this.mShadowLeft = shadow;
        } else if ((edgeFlags & 2) != 0) {
            this.mShadowRight = shadow;
        } else if ((edgeFlags & 8) != 0) {
            this.mShadowBottom = shadow;
        }
        invalidate();
    }

    public void setShadow(int resId, int edgeFlags) {
        setShadow(getResources().getDrawable(resId), edgeFlags);
    }

    public void scrollToFinishActivity() {
        int childWidth = this.mContentView.getWidth();
        int childHeight = this.mContentView.getHeight();
        int left = 0;
        int top = 0;
        if ((this.mEdgeFlag & 1) != 0) {
            left = (this.mShadowLeft.getIntrinsicWidth() + childWidth) + 10;
            this.mTrackingEdge = 1;
        } else if ((this.mEdgeFlag & 2) != 0) {
            left = ((-childWidth) - this.mShadowRight.getIntrinsicWidth()) - 10;
            this.mTrackingEdge = 2;
        } else if ((this.mEdgeFlag & 8) != 0) {
            top = ((-childHeight) - this.mShadowBottom.getIntrinsicHeight()) - 10;
            this.mTrackingEdge = 8;
        }
        this.mDragHelper.smoothSlideViewTo(this.mContentView, left, top);
        invalidate();
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean z = false;
        if (this.mEnable) {
            try {
                z = this.mDragHelper.shouldInterceptTouchEvent(event);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return z;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mEnable) {
            return false;
        }
        this.mDragHelper.processTouchEvent(event);
        return true;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mInLayout = true;
        if (this.mContentView != null) {
            this.mContentView.layout(this.mContentLeft, this.mContentTop, this.mContentLeft +
                    this.mContentView.getMeasuredWidth(), this.mContentTop + this.mContentView
                    .getMeasuredHeight());
        }
        this.mInLayout = false;
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean drawContent = child == this.mContentView;
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (this.mScrimOpacity > 0.0f && drawContent && this.mDragHelper.getViewDragState() != 0) {
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return ret;
    }

    private void drawScrim(Canvas canvas, View child) {
        int color = (((int) (((float) ((this.mScrimColor & -16777216) >>> 24)) * this
                .mScrimOpacity)) << 24) | (this.mScrimColor & ViewCompat.MEASURED_SIZE_MASK);
        if ((this.mTrackingEdge & 1) != 0) {
            canvas.clipRect(0, 0, child.getLeft(), getHeight());
        } else if ((this.mTrackingEdge & 2) != 0) {
            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
        } else if ((this.mTrackingEdge & 8) != 0) {
            canvas.clipRect(child.getLeft(), child.getBottom(), getRight(), getHeight());
        }
        canvas.drawColor(color);
    }

    private void drawShadow(Canvas canvas, View child) {
        Rect childRect = this.mTmpRect;
        child.getHitRect(childRect);
        if ((this.mEdgeFlag & 1) != 0) {
            this.mShadowLeft.setBounds(childRect.left - this.mShadowLeft.getIntrinsicWidth(),
                    childRect.top, childRect.left, childRect.bottom);
            this.mShadowLeft.setAlpha((int) (this.mScrimOpacity * 255.0f));
            this.mShadowLeft.draw(canvas);
        }
        if ((this.mEdgeFlag & 2) != 0) {
            this.mShadowRight.setBounds(childRect.right, childRect.top, childRect.right + this
                    .mShadowRight.getIntrinsicWidth(), childRect.bottom);
            this.mShadowRight.setAlpha((int) (this.mScrimOpacity * 255.0f));
            this.mShadowRight.draw(canvas);
        }
        if ((this.mEdgeFlag & 8) != 0) {
            this.mShadowBottom.setBounds(childRect.left, childRect.bottom, childRect.right,
                    childRect.bottom + this.mShadowBottom.getIntrinsicHeight());
            this.mShadowBottom.setAlpha((int) (this.mScrimOpacity * 255.0f));
            this.mShadowBottom.draw(canvas);
        }
    }

    public void attachToActivity(Activity activity) {
        this.mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{16842836});
        int background = a.getResourceId(0, 0);
        a.recycle();
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    public void computeScroll() {
        this.mScrimOpacity = 1.0f - this.mScrollPercent;
        if (this.mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
