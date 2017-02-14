package com.huewu.pla.lib.internal;

import android.view.View;
import android.view.ViewConfiguration;

class PLA_AbsListView$PositionScroller implements Runnable {
    private static final int MOVE_DOWN_BOUND = 3;
    private static final int MOVE_DOWN_POS   = 1;
    private static final int MOVE_UP_BOUND   = 4;
    private static final int MOVE_UP_POS     = 2;
    private static final int SCROLL_DURATION = 400;
    private               int             mBoundPos;
    private final         int             mExtraScroll;
    private               int             mLastSeenPos;
    private               int             mMode;
    private               int             mScrollDuration;
    private               int             mTargetPos;
    final /* synthetic */ PLA_AbsListView this$0;

    PLA_AbsListView$PositionScroller(PLA_AbsListView this$0) {
        this.this$0 = this$0;
        this.mExtraScroll = ViewConfiguration.get(this$0.getContext()).getScaledFadingEdgeLength();
    }

    void start(int position) {
        int viewTravelCount;
        int firstPos = this.this$0.mFirstPosition;
        int lastPos = (this.this$0.getChildCount() + firstPos) - 1;
        if (position <= firstPos) {
            viewTravelCount = (firstPos - position) + 1;
            this.mMode = 2;
        } else if (position >= lastPos) {
            viewTravelCount = (position - lastPos) + 1;
            this.mMode = 1;
        } else {
            return;
        }
        if (viewTravelCount > 0) {
            this.mScrollDuration = 400 / viewTravelCount;
        } else {
            this.mScrollDuration = 400;
        }
        this.mTargetPos = position;
        this.mBoundPos = -1;
        this.mLastSeenPos = -1;
        this.this$0.post(this);
    }

    void start(int position, int boundPosition) {
        if (boundPosition == -1) {
            start(position);
            return;
        }
        int viewTravelCount;
        int firstPos = this.this$0.mFirstPosition;
        int lastPos = (this.this$0.getChildCount() + firstPos) - 1;
        int posTravel;
        int boundTravel;
        if (position <= firstPos) {
            int boundPosFromLast = lastPos - boundPosition;
            if (boundPosFromLast >= 1) {
                posTravel = (firstPos - position) + 1;
                boundTravel = boundPosFromLast - 1;
                if (boundTravel < posTravel) {
                    viewTravelCount = boundTravel;
                    this.mMode = 4;
                } else {
                    viewTravelCount = posTravel;
                    this.mMode = 2;
                }
            } else {
                return;
            }
        } else if (position >= lastPos) {
            int boundPosFromFirst = boundPosition - firstPos;
            if (boundPosFromFirst >= 1) {
                posTravel = (position - lastPos) + 1;
                boundTravel = boundPosFromFirst - 1;
                if (boundTravel < posTravel) {
                    viewTravelCount = boundTravel;
                    this.mMode = 3;
                } else {
                    viewTravelCount = posTravel;
                    this.mMode = 1;
                }
            } else {
                return;
            }
        } else {
            return;
        }
        if (viewTravelCount > 0) {
            this.mScrollDuration = 400 / viewTravelCount;
        } else {
            this.mScrollDuration = 400;
        }
        this.mTargetPos = position;
        this.mBoundPos = boundPosition;
        this.mLastSeenPos = -1;
        this.this$0.post(this);
    }

    void stop() {
        this.this$0.removeCallbacks(this);
    }

    public void run() {
        int listHeight = this.this$0.getHeight();
        int firstPos = this.this$0.mFirstPosition;
        int lastViewIndex;
        int lastPos;
        View lastView;
        int lastViewHeight;
        int lastViewPixelsShowing;
        int extraScroll;
        switch (this.mMode) {
            case 1:
                lastViewIndex = this.this$0.getChildCount() - 1;
                lastPos = firstPos + lastViewIndex;
                if (lastViewIndex < 0) {
                    return;
                }
                if (lastPos == this.mLastSeenPos) {
                    this.this$0.post(this);
                    return;
                }
                lastView = this.this$0.getChildAt(lastViewIndex);
                lastViewHeight = lastView.getHeight();
                lastViewPixelsShowing = listHeight - lastView.getTop();
                if (lastPos < this.this$0.mItemCount - 1) {
                    extraScroll = this.mExtraScroll;
                } else {
                    extraScroll = this.this$0.mListPadding.bottom;
                }
                this.this$0.smoothScrollBy((lastViewHeight - lastViewPixelsShowing) +
                        extraScroll, this.mScrollDuration);
                this.mLastSeenPos = lastPos;
                if (lastPos < this.mTargetPos) {
                    this.this$0.post(this);
                    return;
                }
                return;
            case 2:
                if (firstPos == this.mLastSeenPos) {
                    this.this$0.post(this);
                    return;
                }
                View firstView = this.this$0.getChildAt(0);
                if (firstView != null) {
                    int firstViewTop = firstView.getTop();
                    if (firstPos > 0) {
                        extraScroll = this.mExtraScroll;
                    } else {
                        extraScroll = this.this$0.mListPadding.top;
                    }
                    this.this$0.smoothScrollBy(firstViewTop - extraScroll, this.mScrollDuration);
                    this.mLastSeenPos = firstPos;
                    if (firstPos > this.mTargetPos) {
                        this.this$0.post(this);
                        return;
                    }
                    return;
                }
                return;
            case 3:
                int childCount = this.this$0.getChildCount();
                if (firstPos != this.mBoundPos && childCount > 1 && firstPos + childCount < this
                        .this$0.mItemCount) {
                    int nextPos = firstPos + 1;
                    if (nextPos == this.mLastSeenPos) {
                        this.this$0.post(this);
                        return;
                    }
                    View nextView = this.this$0.getChildAt(1);
                    int nextViewHeight = nextView.getHeight();
                    int nextViewTop = nextView.getTop();
                    extraScroll = this.mExtraScroll;
                    if (nextPos < this.mBoundPos) {
                        this.this$0.smoothScrollBy(Math.max(0, (nextViewHeight + nextViewTop) -
                                extraScroll), this.mScrollDuration);
                        this.mLastSeenPos = nextPos;
                        this.this$0.post(this);
                        return;
                    } else if (nextViewTop > extraScroll) {
                        this.this$0.smoothScrollBy(nextViewTop - extraScroll, this.mScrollDuration);
                        return;
                    } else {
                        return;
                    }
                }
                return;
            case 4:
                lastViewIndex = this.this$0.getChildCount() - 2;
                if (lastViewIndex >= 0) {
                    lastPos = firstPos + lastViewIndex;
                    if (lastPos == this.mLastSeenPos) {
                        this.this$0.post(this);
                        return;
                    }
                    lastView = this.this$0.getChildAt(lastViewIndex);
                    lastViewHeight = lastView.getHeight();
                    int lastViewTop = lastView.getTop();
                    lastViewPixelsShowing = listHeight - lastViewTop;
                    this.mLastSeenPos = lastPos;
                    if (lastPos > this.mBoundPos) {
                        this.this$0.smoothScrollBy(-(lastViewPixelsShowing - this.mExtraScroll),
                                this.mScrollDuration);
                        this.this$0.post(this);
                        return;
                    }
                    int bottom = listHeight - this.mExtraScroll;
                    int lastViewBottom = lastViewTop + lastViewHeight;
                    if (bottom > lastViewBottom) {
                        this.this$0.smoothScrollBy(-(bottom - lastViewBottom), this
                                .mScrollDuration);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }
}
