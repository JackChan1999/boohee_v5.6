package com.huewu.pla.lib.internal;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.ViewConfiguration;

final class PLA_AbsListView$CheckForTap implements Runnable {
    final /* synthetic */ PLA_AbsListView this$0;

    PLA_AbsListView$CheckForTap(PLA_AbsListView this$0) {
        this.this$0 = this$0;
    }

    public void run() {
        if (this.this$0.mTouchMode == 0) {
            this.this$0.mTouchMode = 1;
            View child = this.this$0.getChildAt(this.this$0.mMotionPosition - this.this$0
                    .mFirstPosition);
            if (child != null && !child.hasFocusable()) {
                this.this$0.mLayoutMode = 0;
                if (this.this$0.mDataChanged) {
                    this.this$0.mTouchMode = 2;
                    return;
                }
                this.this$0.layoutChildren();
                child.setPressed(true);
                this.this$0.positionSelector(child);
                this.this$0.setPressed(true);
                int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                boolean longClickable = this.this$0.isLongClickable();
                if (this.this$0.mSelector != null) {
                    Drawable d = this.this$0.mSelector.getCurrent();
                    if (d != null && (d instanceof TransitionDrawable)) {
                        if (longClickable) {
                            ((TransitionDrawable) d).startTransition(longPressTimeout);
                        } else {
                            ((TransitionDrawable) d).resetTransition();
                        }
                    }
                }
                if (!longClickable) {
                    this.this$0.mTouchMode = 2;
                }
            }
        }
    }
}
