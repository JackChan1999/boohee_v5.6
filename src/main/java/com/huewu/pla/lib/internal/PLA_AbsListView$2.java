package com.huewu.pla.lib.internal;

class PLA_AbsListView$2 implements Runnable {
    final /* synthetic */ PLA_AbsListView this$0;

    PLA_AbsListView$2(PLA_AbsListView this$0) {
        this.this$0 = this$0;
    }

    public void run() {
        if (this.this$0.mCachingStarted) {
            this.this$0.mCachingStarted = false;
            PLA_AbsListView.access$600(this.this$0, false);
            if ((this.this$0.getPersistentDrawingCache() & 2) == 0) {
                PLA_AbsListView.access$700(this.this$0, false);
            }
            if (!this.this$0.isAlwaysDrawnWithCacheEnabled()) {
                this.this$0.invalidate();
            }
        }
    }
}
