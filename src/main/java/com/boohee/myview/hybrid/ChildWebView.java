package com.boohee.myview.hybrid;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ChildWebView extends WebView implements IScrollStatus {
    private boolean isScrollBottom;

    public ChildWebView(Context context) {
        this(context, null);
    }

    public ChildWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChildWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if ((((float) getContentHeight()) * getScale()) - ((float) (getHeight() + getScrollY()))
                <= 10.0f) {
            this.isScrollBottom = true;
        } else {
            this.isScrollBottom = false;
        }
    }

    public boolean isScrollTop() {
        return getScrollY() == 0;
    }

    public boolean isScrollBottom() {
        return this.isScrollBottom;
    }
}
