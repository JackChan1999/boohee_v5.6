package com.nostra13.universalimageloader.core;

class LoadAndDisplayImageTask$1 implements Runnable {
    final /* synthetic */ LoadAndDisplayImageTask this$0;
    final /* synthetic */ int                     val$current;
    final /* synthetic */ int                     val$total;

    LoadAndDisplayImageTask$1(LoadAndDisplayImageTask loadAndDisplayImageTask, int i, int i2) {
        this.this$0 = loadAndDisplayImageTask;
        this.val$current = i;
        this.val$total = i2;
    }

    public void run() {
        this.this$0.progressListener.onProgressUpdate(this.this$0.uri, this.this$0.imageAware
                .getWrappedView(), this.val$current, this.val$total);
    }
}
