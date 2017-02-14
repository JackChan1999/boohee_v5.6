package com.meiqia.meiqiasdk.controller;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnMessageSendCallback;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.util.MQUtils;

class ControllerImpl$1 implements OnMessageSendCallback {
    final /* synthetic */ ControllerImpl                                      this$0;
    final /* synthetic */ BaseMessage                                         val$message;
    final /* synthetic */ com.meiqia.meiqiasdk.callback.OnMessageSendCallback
            val$onMessageSendCallback;

    ControllerImpl$1(ControllerImpl this$0, BaseMessage baseMessage, com.meiqia.meiqiasdk
            .callback.OnMessageSendCallback onMessageSendCallback) {
        this.this$0 = this$0;
        this.val$message = baseMessage;
        this.val$onMessageSendCallback = onMessageSendCallback;
    }

    public void onSuccess(MQMessage mcMessage, int state) {
        MQUtils.parseMQMessageIntoBaseMessage(mcMessage, this.val$message);
        if (this.val$onMessageSendCallback != null) {
            this.val$onMessageSendCallback.onSuccess(this.val$message, state);
        }
    }

    public void onFailure(MQMessage failureMessage, int code, String response) {
        MQUtils.parseMQMessageIntoBaseMessage(failureMessage, this.val$message);
        if (this.val$onMessageSendCallback != null) {
            this.val$onMessageSendCallback.onFailure(this.val$message, code, response);
        }
    }
}
