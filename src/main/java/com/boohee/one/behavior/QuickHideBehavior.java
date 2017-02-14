package com.boohee.one.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.boohee.one.R;

public class QuickHideBehavior extends Behavior<View> {
    private static final int DIRECTION_DOWN = -1;
    private static final int DIRECTION_UP   = 1;
    private ObjectAnimator mAnimator;
    private int            mScrollDistance;
    private int            mScrollThreshold;
    private int            mScrollTrigger;
    private int            mScrollingDirection;

    public QuickHideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.bk});
        this.mScrollThreshold = a.getDimensionPixelSize(0, 0) / 2;
        a.recycle();
    }

    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View
            directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & 2) != 0;
    }

    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                                  int dx, int dy, int[] consumed) {
        if (dy > 0 && this.mScrollingDirection != 1) {
            this.mScrollingDirection = 1;
            this.mScrollDistance = 0;
        } else if (dy < 0 && this.mScrollingDirection != -1) {
            this.mScrollingDirection = -1;
            this.mScrollDistance = 0;
        }
    }

    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int
            dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        this.mScrollDistance += dyConsumed;
        if (this.mScrollDistance > this.mScrollThreshold && this.mScrollTrigger != 1) {
            this.mScrollTrigger = 1;
            restartAnimator(child, getTargetHideValue(coordinatorLayout, child));
        } else if (this.mScrollDistance < (-this.mScrollThreshold) && this.mScrollTrigger != -1) {
            this.mScrollTrigger = -1;
            restartAnimator(child, 0.0f);
        }
    }

    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target,
                                 float velocityX, float velocityY, boolean consumed) {
        if (consumed) {
            if (velocityY > 0.0f && this.mScrollTrigger != 1) {
                this.mScrollTrigger = 1;
                restartAnimator(child, getTargetHideValue(coordinatorLayout, child));
            } else if (velocityY < 0.0f && this.mScrollTrigger != -1) {
                this.mScrollTrigger = -1;
                restartAnimator(child, 0.0f);
            }
        }
        return false;
    }

    private void restartAnimator(View target, float value) {
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        this.mAnimator = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, new float[]{value})
                .setDuration(250);
        this.mAnimator.start();
    }

    private float getTargetHideValue(ViewGroup parent, View target) {
        if (target instanceof AppBarLayout) {
            return (float) (-target.getHeight());
        }
        if (target instanceof FloatingActionButton) {
            return (float) (parent.getHeight() - target.getTop());
        }
        return (float) target.getHeight();
    }
}
