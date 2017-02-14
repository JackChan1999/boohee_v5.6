package com.boohee.myview.ViewPagerHeaderScroll.tools;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class ViewPagerHeaderHelper {
    private boolean mHandlingTouchEventFromDown;
    private int     mHeaderHeight;
    private float   mInitialMotionX;
    private float   mInitialMotionY;
    private boolean mIsBeingMove;
    private boolean mIsHeaderExpand = true;
    private float                    mLastMotionY;
    private OnViewPagerTouchListener mListener;
    private int                      mMaximumFlingVelocity;
    private int                      mMinimumFlingVelocity;
    private int                      mTouchSlop;
    private VelocityTracker          mVelocityTracker;

    public interface OnViewPagerTouchListener {
        boolean isViewBeingDragged(MotionEvent motionEvent);

        void onMove(float f, float f2);

        void onMoveEnded(boolean z, float f);

        void onMoveStarted(float f);
    }

    private ViewPagerHeaderHelper() {
    }

    public ViewPagerHeaderHelper(Context context, OnViewPagerTouchListener listener) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mListener = listener;
    }

    public boolean onLayoutInterceptTouchEvent(MotionEvent event, int headerHeight) {
        this.mHeaderHeight = headerHeight;
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                if ((this.mListener.isViewBeingDragged(event) && !this.mIsHeaderExpand) || this
                        .mIsHeaderExpand) {
                    if (!this.mIsHeaderExpand || y >= ((float) headerHeight)) {
                        this.mInitialMotionX = x;
                        this.mInitialMotionY = y;
                        break;
                    }
                    return this.mIsBeingMove;
                }
                break;
            case 1:
            case 3:
                if (this.mIsBeingMove) {
                    this.mListener.onMoveEnded(false, 0.0f);
                }
                resetTouch();
                break;
            case 2:
                if (this.mInitialMotionY > 0.0f && !this.mIsBeingMove) {
                    float yDiff = y - this.mInitialMotionY;
                    float xDiff = x - this.mInitialMotionX;
                    if (((!this.mIsHeaderExpand && yDiff > ((float) this.mTouchSlop)) || (this
                            .mIsHeaderExpand && yDiff < 0.0f)) && Math.abs(yDiff) > Math.abs
                            (xDiff)) {
                        this.mIsBeingMove = true;
                        this.mListener.onMoveStarted(y);
                        break;
                    }
                }
        }
        return this.mIsBeingMove;
    }

    public boolean onLayoutTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            this.mHandlingTouchEventFromDown = true;
        }
        if (this.mHandlingTouchEventFromDown) {
            if (this.mIsBeingMove) {
                this.mLastMotionY = event.getY();
            } else {
                onLayoutInterceptTouchEvent(event, this.mHeaderHeight);
                return true;
            }
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
        int action = event.getAction();
        int count = event.getPointerCount();
        switch (action) {
            case 1:
            case 3:
                if (this.mIsBeingMove) {
                    boolean isFling = false;
                    float velocityY = 0.0f;
                    if (action == 1) {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        int pointerId = event.getPointerId(0);
                        velocityTracker.computeCurrentVelocity(1000, (float) this
                                .mMaximumFlingVelocity);
                        velocityY = velocityTracker.getYVelocity(pointerId);
                        if (Math.abs(velocityY) > ((float) this.mMinimumFlingVelocity)) {
                            isFling = true;
                        }
                    }
                    this.mListener.onMoveEnded(isFling, velocityY);
                }
                resetTouch();
                break;
            case 2:
                float y = event.getY();
                if (this.mIsBeingMove && y != this.mLastMotionY) {
                    this.mListener.onMove(y, this.mLastMotionY == -1.0f ? 0.0f : y - this
                            .mLastMotionY);
                    this.mLastMotionY = y;
                    break;
                }
            case 6:
                this.mVelocityTracker.computeCurrentVelocity(1000, (float) this
                        .mMaximumFlingVelocity);
                int upIndex = event.getActionIndex();
                int id1 = event.getPointerId(upIndex);
                float x1 = this.mVelocityTracker.getXVelocity(id1);
                float y1 = this.mVelocityTracker.getYVelocity(id1);
                for (int i = 0; i < count; i++) {
                    if (i != upIndex) {
                        int id2 = event.getPointerId(i);
                        if ((x1 * this.mVelocityTracker.getXVelocity(id2)) + (y1 * this
                                .mVelocityTracker.getYVelocity(id2)) < 0.0f) {
                            this.mVelocityTracker.clear();
                            break;
                        }
                    }
                }
                break;
        }
        return true;
    }

    private void resetTouch() {
        this.mIsBeingMove = false;
        this.mHandlingTouchEventFromDown = false;
        this.mLastMotionY = -1.0f;
        this.mInitialMotionY = -1.0f;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void setHeaderExpand(boolean isHeaderExpand) {
        this.mIsHeaderExpand = isHeaderExpand;
    }

    public float getInitialMotionY() {
        return this.mInitialMotionY;
    }

    public float getLastMotionY() {
        return this.mLastMotionY;
    }
}
