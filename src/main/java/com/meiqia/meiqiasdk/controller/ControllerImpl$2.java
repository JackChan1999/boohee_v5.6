package com.meiqia.meiqiasdk.controller;

import com.meiqia.core.MQManager;
import com.meiqia.meiqiasdk.callback.OnMessageSendCallback;
import com.meiqia.meiqiasdk.model.BaseMessage;

class ControllerImpl$2 implements OnMessageSendCallback {
    final /* synthetic */ ControllerImpl        this$0;
    final /* synthetic */ OnMessageSendCallback val$onMessageSendCallback;
    final /* synthetic */ long                  val$preId;

    ControllerImpl$2(ControllerImpl this$0, OnMessageSendCallback onMessageSendCallback, long j) {
        this.this$0 = this$0;
        this.val$onMessageSendCallback = onMessageSendCallback;
        this.val$preId = j;
    }

    public void onSuccess(BaseMessage message, int state) {
        if (this.val$onMessageSendCallback != null) {
            this.val$onMessageSendCallback.onSuccess(message, state);
        }
        MQManager.getInstance(this.this$0.context).deleteMessage(this.val$preId);
    }

    public void onFailure(BaseMessage failureMessage, int code, String failureInfo) {
        if (this.val$onMessageSendCallback != null) {
            this.val$onMessageSendCallback.onFailure(failureMessage, code, failureInfo);
        }
        MQManager.getInstance(this.this$0.context).deleteMessage(this.val$preId);
    }
}
