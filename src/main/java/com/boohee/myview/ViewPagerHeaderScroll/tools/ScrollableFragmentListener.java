package com.boohee.myview.ViewPagerHeaderScroll.tools;

public interface ScrollableFragmentListener {
    void onFragmentAttached(ScrollableListener scrollableListener, int i);

    void onFragmentDetached(ScrollableListener scrollableListener, int i);
}
