package com.mob.tools.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class PullToRefreshView extends RelativeLayout {
    private static final long MIN_REF_TIME = 1000;
    private PullToRefreshAdatper adapter;
    private View                 bodyView;
    private float                downY;
    private int                  headerHeight;
    private View                 headerView;
    private boolean              pullingLock;
    private long                 refreshTime;
    private boolean              requesting;
    private Runnable             stopAct;
    private int                  top;

    public PullToRefreshView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PullToRefreshView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private boolean canPull() {
        return !this.pullingLock && this.adapter.isPullReady();
    }

    private MotionEvent getCancelEvent(MotionEvent motionEvent) {
        return MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getEventTime(), 3,
                motionEvent.getX(), motionEvent.getY(), motionEvent.getMetaState());
    }

    private void init() {
        this.stopAct = new Runnable() {
            public void run() {
                PullToRefreshView.this.reversePulling();
                PullToRefreshView.this.stopRequest();
            }
        };
    }

    private void performRequest() {
        this.refreshTime = System.currentTimeMillis();
        this.requesting = true;
        if (this.adapter != null) {
            this.adapter.onRequest();
        }
    }

    private void reversePulling() {
        this.top = 0;
        scrollTo(0, 0);
        if (this.adapter != null) {
            this.adapter.onReversed();
        }
    }

    private void stopRequest() {
        this.requesting = false;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.downY = motionEvent.getY();
                break;
            case 1:
            case 3:
                if (!this.requesting) {
                    if (this.top <= this.headerHeight) {
                        if (this.top != 0) {
                            reversePulling();
                            if (this.adapter != null) {
                                this.adapter.onPullDown(0);
                                break;
                            }
                        }
                    }
                    this.top = this.headerHeight;
                    scrollTo(0, -this.top);
                    if (this.adapter != null) {
                        this.adapter.onPullDown(100);
                    }
                    performRequest();
                    motionEvent = getCancelEvent(motionEvent);
                    break;
                }
                this.top = this.headerHeight;
                scrollTo(0, -this.top);
                break;
            break;
            case 2:
                float y = motionEvent.getY();
                if (this.requesting || canPull()) {
                    this.top = (int) (((float) this.top) + ((y - this.downY) / 2.0f));
                    if (this.top > 0) {
                        scrollTo(0, -this.top);
                        if (!(this.requesting || this.adapter == null)) {
                            this.adapter.onPullDown((this.top * 100) / this.headerHeight);
                        }
                        motionEvent = getCancelEvent(motionEvent);
                    } else {
                        this.top = 0;
                        scrollTo(0, 0);
                    }
                }
                this.downY = y;
                break;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void lockPulling() {
        this.pullingLock = true;
    }

    public void performPulling(boolean z) {
        this.top = this.headerHeight;
        scrollTo(0, -this.top);
        if (z) {
            performRequest();
        }
    }

    public void releaseLock() {
        this.pullingLock = false;
    }

    public void setAdapter(PullToRefreshAdatper pullToRefreshAdatper) {
        this.adapter = pullToRefreshAdatper;
        removeAllViews();
        this.bodyView = (View) pullToRefreshAdatper.getBodyView();
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(9, -1);
        layoutParams.addRule(11, -1);
        layoutParams.addRule(10, -1);
        addView(this.bodyView, layoutParams);
        this.headerView = pullToRefreshAdatper.getHeaderView();
        this.headerView.measure(0, 0);
        this.headerHeight = this.headerView.getMeasuredHeight();
        layoutParams = new RelativeLayout.LayoutParams(-2, this.headerHeight);
        layoutParams.addRule(9, -1);
        layoutParams.addRule(11, -1);
        layoutParams.addRule(10, -1);
        layoutParams.topMargin = -this.headerHeight;
        addView(this.headerView, layoutParams);
    }

    public void stopPulling() {
        long currentTimeMillis = System.currentTimeMillis() - this.refreshTime;
        if (currentTimeMillis < MIN_REF_TIME) {
            postDelayed(this.stopAct, MIN_REF_TIME - currentTimeMillis);
        } else {
            post(this.stopAct);
        }
    }
}
