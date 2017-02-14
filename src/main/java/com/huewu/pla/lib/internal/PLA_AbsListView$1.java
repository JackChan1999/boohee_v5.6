package com.huewu.pla.lib.internal;

import android.view.View;

class PLA_AbsListView$1 implements Runnable {
    final /* synthetic */ PLA_AbsListView              this$0;
    final /* synthetic */ View                         val$child;
    final /* synthetic */ PLA_AbsListView$PerformClick val$performClick;

    PLA_AbsListView$1(PLA_AbsListView this$0, View view, PLA_AbsListView$PerformClick
            pLA_AbsListView$PerformClick) {
        this.this$0 = this$0;
        this.val$child = view;
        this.val$performClick = pLA_AbsListView$PerformClick;
    }

    public void run() {
        this.val$child.setPressed(false);
        this.this$0.setPressed(false);
        if (!this.this$0.mDataChanged) {
            this.this$0.post(this.val$performClick);
        }
        this.this$0.mTouchMode = -1;
    }
}
