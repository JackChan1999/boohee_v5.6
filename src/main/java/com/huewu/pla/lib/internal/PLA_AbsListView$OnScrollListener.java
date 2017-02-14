package com.huewu.pla.lib.internal;

public interface PLA_AbsListView$OnScrollListener {
    public static final int SCROLL_STATE_FLING        = 2;
    public static final int SCROLL_STATE_IDLE         = 0;
    public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

    void onScroll(PLA_AbsListView pLA_AbsListView, int i, int i2, int i3);

    void onScrollStateChanged(PLA_AbsListView pLA_AbsListView, int i);
}
