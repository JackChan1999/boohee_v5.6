package com.nostra13.universalimageloader.core;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.FailReason$FailType;

class LoadAndDisplayImageTask$2 implements Runnable {
    final /* synthetic */ LoadAndDisplayImageTask this$0;
    final /* synthetic */ Throwable               val$failCause;
    final /* synthetic */ FailReason$FailType     val$failType;

    LoadAndDisplayImageTask$2(LoadAndDisplayImageTask loadAndDisplayImageTask,
                              FailReason$FailType failReason$FailType, Throwable th) {
        this.this$0 = loadAndDisplayImageTask;
        this.val$failType = failReason$FailType;
        this.val$failCause = th;
    }

    public void run() {
        if (this.this$0.options.shouldShowImageOnFail()) {
            this.this$0.imageAware.setImageDrawable(this.this$0.options.getImageOnFail
                    (LoadAndDisplayImageTask.access$000(this.this$0).resources));
        }
        this.this$0.listener.onLoadingFailed(this.this$0.uri, this.this$0.imageAware
                .getWrappedView(), new FailReason(this.val$failType, this.val$failCause));
    }
}
