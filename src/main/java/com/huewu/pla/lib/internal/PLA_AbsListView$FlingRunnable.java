package com.huewu.pla.lib.internal;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import android.widget.Scroller;

class PLA_AbsListView$FlingRunnable implements Runnable {
    private               int             mLastFlingY;
    private final         Scroller        mScroller;
    final /* synthetic */ PLA_AbsListView this$0;

    PLA_AbsListView$FlingRunnable(PLA_AbsListView pLA_AbsListView) {
        this.this$0 = pLA_AbsListView;
        this.mScroller = new Scroller(pLA_AbsListView.getContext());
    }

    void start(int initialVelocity) {
        initialVelocity = this.this$0.modifyFlingInitialVelocity(initialVelocity);
        int initialY = initialVelocity < 0 ? ActivityChooserViewAdapter
                .MAX_ACTIVITY_COUNT_UNLIMITED : 0;
        this.mLastFlingY = initialY;
        this.mScroller.fling(0, initialY, 0, initialVelocity, 0, ActivityChooserViewAdapter
                .MAX_ACTIVITY_COUNT_UNLIMITED, 0, ActivityChooserViewAdapter
                .MAX_ACTIVITY_COUNT_UNLIMITED);
        this.this$0.mTouchMode = 4;
        this.this$0.post(this);
    }

    void startScroll(int distance, int duration) {
        int initialY;
        if (distance < 0) {
            initialY = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        } else {
            initialY = 0;
        }
        this.mLastFlingY = initialY;
        this.mScroller.startScroll(0, initialY, 0, distance, duration);
        this.this$0.mTouchMode = 4;
        this.this$0.post(this);
    }

    private void endFling() {
        this.mLastFlingY = 0;
        this.this$0.mTouchMode = -1;
        this.this$0.reportScrollStateChange(0);
        PLA_AbsListView.access$500(this.this$0);
        this.this$0.removeCallbacks(this);
        if (this.this$0.mPositionScroller != null) {
            this.this$0.removeCallbacks(this.this$0.mPositionScroller);
        }
        this.mScroller.forceFinished(true);
    }

    public void run() {
        switch (this.this$0.mTouchMode) {
            case 4:
                if (this.this$0.mItemCount == 0 || this.this$0.getChildCount() == 0) {
                    endFling();
                    return;
                }
                Scroller scroller = this.mScroller;
                boolean more = scroller.computeScrollOffset();
                int y = scroller.getCurrY();
                int delta = this.mLastFlingY - y;
                if (delta > 0) {
                    this.this$0.mMotionPosition = this.this$0.mFirstPosition;
                    this.this$0.mMotionViewOriginalTop = this.this$0.getScrollChildTop();
                    delta = Math.min(((this.this$0.getHeight() - this.this$0.getPaddingBottom())
                            - this.this$0.getPaddingTop()) - 1, delta);
                } else {
                    int offsetToLast = this.this$0.getChildCount() - 1;
                    this.this$0.mMotionPosition = this.this$0.mFirstPosition + offsetToLast;
                    this.this$0.mMotionViewOriginalTop = this.this$0.getScrollChildBottom();
                    delta = Math.max(-(((this.this$0.getHeight() - this.this$0.getPaddingBottom()
                    ) - this.this$0.getPaddingTop()) - 1), delta);
                }
                boolean atEnd = this.this$0.trackMotionScroll(delta, delta);
                if (!more || atEnd) {
                    endFling();
                    return;
                }
                this.this$0.invalidate();
                this.mLastFlingY = y;
                this.this$0.post(this);
                return;
            default:
                return;
        }
    }
}
