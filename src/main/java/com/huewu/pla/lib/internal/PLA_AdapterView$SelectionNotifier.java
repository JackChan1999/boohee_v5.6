package com.huewu.pla.lib.internal;

class PLA_AdapterView$SelectionNotifier implements Runnable {
    final /* synthetic */ PLA_AdapterView this$0;

    private PLA_AdapterView$SelectionNotifier(PLA_AdapterView pLA_AdapterView) {
        this.this$0 = pLA_AdapterView;
    }

    public void run() {
        if (!this.this$0.mDataChanged) {
            PLA_AdapterView.access$200(this.this$0);
            PLA_AdapterView.access$300(this.this$0);
        } else if (this.this$0.getAdapter() != null) {
            this.this$0.post(this);
        }
    }
}
