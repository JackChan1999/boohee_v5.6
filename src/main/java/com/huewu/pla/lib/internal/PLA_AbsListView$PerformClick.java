package com.huewu.pla.lib.internal;

import android.view.View;
import android.widget.ListAdapter;

class PLA_AbsListView$PerformClick extends PLA_AbsListView$WindowRunnnable implements Runnable {
    View mChild;
    int  mClickMotionPosition;
    final /* synthetic */ PLA_AbsListView this$0;

    private PLA_AbsListView$PerformClick(PLA_AbsListView pLA_AbsListView) {
        this.this$0 = pLA_AbsListView;
        super(pLA_AbsListView);
    }

    public void run() {
        if (!this.this$0.mDataChanged) {
            ListAdapter adapter = this.this$0.mAdapter;
            int motionPosition = this.mClickMotionPosition;
            if (adapter != null && this.this$0.mItemCount > 0 && motionPosition != -1 &&
                    motionPosition < adapter.getCount() && sameWindow()) {
                this.this$0.performItemClick(this.mChild, motionPosition, adapter.getItemId
                        (motionPosition));
            }
        }
    }
}
