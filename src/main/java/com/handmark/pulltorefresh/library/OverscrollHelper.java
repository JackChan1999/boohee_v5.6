package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;

@TargetApi(9)
public final class OverscrollHelper {
    static final float  DEFAULT_OVERSCROLL_SCALE = 1.0f;
    static final String LOG_TAG                  = "OverscrollHelper";

    public static void overScrollBy(PullToRefreshBase<?> view, int deltaX, int scrollX, int
            deltaY, int scrollY, boolean isTouchEvent) {
        overScrollBy(view, deltaX, scrollX, deltaY, scrollY, 0, isTouchEvent);
    }

    public static void overScrollBy(PullToRefreshBase<?> view, int deltaX, int scrollX, int
            deltaY, int scrollY, int scrollRange, boolean isTouchEvent) {
        overScrollBy(view, deltaX, scrollX, deltaY, scrollY, scrollRange, 0, 1.0f, isTouchEvent);
    }

    public static void overScrollBy(PullToRefreshBase<?> view, int deltaX, int scrollX, int
            deltaY, int scrollY, int scrollRange, int fuzzyThreshold, float scaleFactor, boolean
            isTouchEvent) {
        int deltaValue;
        int scrollValue;
        int currentScrollValue;
        switch (view.getPullToRefreshScrollDirection()) {
            case HORIZONTAL:
                deltaValue = deltaX;
                scrollValue = scrollX;
                currentScrollValue = view.getScrollX();
                break;
            default:
                deltaValue = deltaY;
                scrollValue = scrollY;
                currentScrollValue = view.getScrollY();
                break;
        }
        if (view.isPullToRefreshOverScrollEnabled() && !view.isRefreshing()) {
            Mode mode = view.getMode();
            if (mode.permitsPullToRefresh() && !isTouchEvent && deltaValue != 0) {
                int newScrollValue = deltaValue + scrollValue;
                if (newScrollValue < 0 - fuzzyThreshold) {
                    if (mode.showHeaderLoadingLayout()) {
                        if (currentScrollValue == 0) {
                            view.setState(State.OVERSCROLLING, new boolean[0]);
                        }
                        view.setHeaderScroll((int) (((float) (currentScrollValue +
                                newScrollValue)) * scaleFactor));
                    }
                } else if (newScrollValue > scrollRange + fuzzyThreshold) {
                    if (mode.showFooterLoadingLayout()) {
                        if (currentScrollValue == 0) {
                            view.setState(State.OVERSCROLLING, new boolean[0]);
                        }
                        view.setHeaderScroll((int) (((float) ((currentScrollValue +
                                newScrollValue) - scrollRange)) * scaleFactor));
                    }
                } else if (Math.abs(newScrollValue) <= fuzzyThreshold || Math.abs(newScrollValue
                        - scrollRange) <= fuzzyThreshold) {
                    view.setState(State.RESET, new boolean[0]);
                }
            } else if (isTouchEvent && State.OVERSCROLLING == view.getState()) {
                view.setState(State.RESET, new boolean[0]);
            }
        }
    }

    static boolean isAndroidOverScrollEnabled(View view) {
        return view.getOverScrollMode() != 2;
    }
}
