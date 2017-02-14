package com.huewu.pla.lib.internal;

class PLA_AbsListView$WindowRunnnable {
    private               int             mOriginalAttachCount;
    final /* synthetic */ PLA_AbsListView this$0;

    private PLA_AbsListView$WindowRunnnable(PLA_AbsListView pLA_AbsListView) {
        this.this$0 = pLA_AbsListView;
    }

    public void rememberWindowAttachCount() {
        this.mOriginalAttachCount = PLA_AbsListView.access$100(this.this$0);
    }

    public boolean sameWindow() {
        return this.this$0.hasWindowFocus() && PLA_AbsListView.access$200(this.this$0) == this.mOriginalAttachCount;
    }
}
