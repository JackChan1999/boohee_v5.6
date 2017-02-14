package com.boohee.myview.swipeback;

public interface SwipeBackActivityBase {
    SwipeBackLayout getSwipeBackLayout();

    void scrollToFinishActivity();

    void setSwipeBackEnable(boolean z);
}
