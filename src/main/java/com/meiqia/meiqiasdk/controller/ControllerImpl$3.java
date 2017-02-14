package com.meiqia.meiqiasdk.controller;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.meiqiasdk.callback.OnGetMessageListCallBack;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.util.MQUtils;

import java.util.List;

class ControllerImpl$3 implements OnGetMessageListCallback {
    final /* synthetic */ ControllerImpl           this$0;
    final /* synthetic */ OnGetMessageListCallBack val$onGetMessageListCallBack;

    ControllerImpl$3(ControllerImpl this$0, OnGetMessageListCallBack onGetMessageListCallBack) {
        this.this$0 = this$0;
        this.val$onGetMessageListCallBack = onGetMessageListCallBack;
    }

    public void onSuccess(List<MQMessage> mqMessageList) {
        List<BaseMessage> messageList = MQUtils.parseMQMessageToChatBaseList(mqMessageList);
        if (this.val$onGetMessageListCallBack != null) {
            this.val$onGetMessageListCallBack.onSuccess(messageList);
        }
    }

    public void onFailure(int code, String message) {
        if (this.val$onGetMessageListCallBack != null) {
            this.val$onGetMessageListCallBack.onFailure(code, message);
        }
    }
}
