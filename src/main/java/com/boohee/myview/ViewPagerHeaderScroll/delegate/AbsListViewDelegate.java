package com.boohee.myview.ViewPagerHeaderScroll.delegate;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

public class AbsListViewDelegate implements ViewDelegate {
    private final Rect  mRect               = new Rect();
    private final int[] mViewLocationResult = new int[2];

    public boolean isViewBeingDragged(MotionEvent event, AbsListView view) {
        if (view.getAdapter() == null || ((ListAdapter) view.getAdapter()).isEmpty()) {
            return true;
        }
        view.getLocationOnScreen(this.mViewLocationResult);
        int viewLeft = this.mViewLocationResult[0];
        int viewTop = this.mViewLocationResult[1];
        this.mRect.set(viewLeft, viewTop, view.getWidth() + viewLeft, view.getHeight() + viewTop);
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        return this.mRect.contains(rawX, rawY) ? isReadyForPull(view, (float) (rawX - this.mRect
                .left), (float) (rawY - this.mRect.top)) : false;
    }

    public boolean isReadyForPull(View view, float x, float y) {
        AbsListView absListView = (AbsListView) view;
        if (absListView.getCount() == 0) {
            return true;
        }
        if (absListView.getFirstVisiblePosition() != 0) {
            return false;
        }
        View firstVisibleChild = absListView.getChildAt(0);
        boolean ready = firstVisibleChild != null && firstVisibleChild.getTop() >= absListView
                .getPaddingTop();
        return ready;
    }
}
