package com.meiqia.meiqiasdk.controller;

import com.meiqia.core.callback.OnProgressCallback;
import com.meiqia.meiqiasdk.callback.OnDownloadFileCallback;

class ControllerImpl$8 implements OnProgressCallback {
    final /* synthetic */ ControllerImpl         this$0;
    final /* synthetic */ OnDownloadFileCallback val$onDownloadFileCallback;

    ControllerImpl$8(ControllerImpl this$0, OnDownloadFileCallback onDownloadFileCallback) {
        this.this$0 = this$0;
        this.val$onDownloadFileCallback = onDownloadFileCallback;
    }

    public void onSuccess() {
        if (this.val$onDownloadFileCallback != null) {
            this.val$onDownloadFileCallback.onSuccess(null);
        }
    }

    public void onProgress(int progress) {
        if (this.val$onDownloadFileCallback != null) {
            this.val$onDownloadFileCallback.onProgress(progress);
        }
    }

    public void onFailure(int code, String message) {
        if (this.val$onDownloadFileCallback != null) {
            this.val$onDownloadFileCallback.onFailure(code, message);
        }
    }
}
