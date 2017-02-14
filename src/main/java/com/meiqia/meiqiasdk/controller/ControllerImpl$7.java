package com.meiqia.meiqiasdk.controller;

import com.meiqia.core.callback.SimpleCallback;

class ControllerImpl$7 implements SimpleCallback {
    final /* synthetic */ ControllerImpl                               this$0;
    final /* synthetic */ com.meiqia.meiqiasdk.callback.SimpleCallback val$simpleCallback;

    ControllerImpl$7(ControllerImpl this$0, com.meiqia.meiqiasdk.callback.SimpleCallback
            simpleCallback) {
        this.this$0 = this$0;
        this.val$simpleCallback = simpleCallback;
    }

    public void onFailure(int code, String message) {
        if (this.val$simpleCallback != null) {
            this.val$simpleCallback.onFailure(code, message);
        }
    }

    public void onSuccess() {
        if (this.val$simpleCallback != null) {
            this.val$simpleCallback.onSuccess();
        }
    }
}
