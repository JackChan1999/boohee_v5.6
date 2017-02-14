package com.boohee.myview.hybrid;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Scroller;

public class HomeView extends LinearLayout {
    private static final int DELAY = 300;
    private int          mActivePointerId;
    private ChildView    mChildHeader;
    private float        mLastMotionY;
    private int          mMove;
    private Scroller     mScroller;
    private Status       mStatus;
    private ChildWebView mWebView;

    private enum Status {
        one,
        two
    }

    public HomeView(Context context) {
        this(context, null);
    }

    public HomeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mStatus = Status.one;
        this.mScroller = new Scroller(context);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(1);
        setLayoutParams(new LayoutParams(-1, -1));
        this.mChildHeader = new ChildView(context);
        this.mWebView = new ChildWebView(context);
        addView(this.mChildHeader);
        addView(this.mWebView);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
    }

    public void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.mWebView.loadUrl(url);
        }
    }

    public void setHeaderView(View header) {
        if (header != null) {
            this.mChildHeader.removeAllViews();
            this.mChildHeader.addView(header);
        }
    }

    public WebView getWebView() {
        return this.mWebView;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWebView.getLayoutParams().height = h;
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            invalidate();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mActivePointerId = event.getPointerId(0);
                this.mLastMotionY = event.getY();
                break;
            case 2:
                int activePointerIndex = event.findPointerIndex(this.mActivePointerId);
                if (activePointerIndex != -1) {
                    int move = (int) (event.getY(activePointerIndex) - this.mLastMotionY);
                    switch (this.mStatus) {
                        case one:
                            if (this.mChildHeader.isScrollBottom() && move < 0) {
                                return true;
                            }
                        case two:
                            if (this.mChildHeader.isScrollBottom() && this.mWebView.isScrollTop()
                                    && move > 0) {
                                return true;
                            }
                        default:
                            break;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mActivePointerId = event.getPointerId(0);
                this.mLastMotionY = event.getY();
                break;
            case 1:
            case 3:
                if (((float) Math.abs(this.mMove)) > 10.0f) {
                    if (this.mMove <= 0) {
                        scrollToTwo();
                        break;
                    }
                    scrollToOne();
                    break;
                }
                break;
            case 2:
                int activePointerIndex = event.findPointerIndex(this.mActivePointerId);
                if (activePointerIndex != -1) {
                    float y = event.getY(activePointerIndex);
                    int move = (int) (y - this.mLastMotionY);
                    if (((float) (getScrollY() - getHeight())) > 10.0f) {
                        return true;
                    }
                    this.mMove += move;
                    this.mLastMotionY = y;
                    scrollBy(0, -move);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void scrollToOne() {
        this.mStatus = Status.one;
        this.mMove = 0;
        this.mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 300);
        invalidate();
    }

    private void scrollToTwo() {
        this.mStatus = Status.two;
        this.mMove = 0;
        this.mScroller.startScroll(0, getScrollY(), 0, this.mChildHeader.getMeasuredHeight() -
                getScrollY(), 300);
        invalidate();
    }
}
