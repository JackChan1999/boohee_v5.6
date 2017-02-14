package com.nostra13.universalimageloader.core;

class LoadAndDisplayImageTask$3 implements Runnable {
    final /* synthetic */ LoadAndDisplayImageTask this$0;

    LoadAndDisplayImageTask$3(LoadAndDisplayImageTask loadAndDisplayImageTask) {
        this.this$0 = loadAndDisplayImageTask;
    }

    public void run() {
        this.this$0.listener.onLoadingCancelled(this.this$0.uri, this.this$0.imageAware
                .getWrappedView());
    }
}
