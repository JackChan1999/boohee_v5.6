package com.meiqia.meiqiasdk.controller;

import com.meiqia.core.callback.OnClientInfoCallback;
import com.meiqia.meiqiasdk.callback.SimpleCallback;

class ControllerImpl$6 implements OnClientInfoCallback {
    final /* synthetic */ ControllerImpl this$0;
    final /* synthetic */ SimpleCallback val$onClientInfoCallback;

    ControllerImpl$6(ControllerImpl this$0, SimpleCallback simpleCallback) {
        this.this$0 = this$0;
        this.val$onClientInfoCallback = simpleCallback;
    }

    public void onSuccess() {
        if (this.val$onClientInfoCallback != null) {
            this.val$onClientInfoCallback.onSuccess();
        }
    }

    public void onFailure(int code, String message) {
        if (this.val$onClientInfoCallback != null) {
            this.val$onClientInfoCallback.onFailure(code, message);
        }
    }
}
