package com.meiqia.meiqiasdk.controller;

import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnClientOnlineCallback;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.util.MQUtils;

import java.util.List;

class ControllerImpl$5 implements OnClientOnlineCallback {
    final /* synthetic */ ControllerImpl                                       this$0;
    final /* synthetic */ com.meiqia.meiqiasdk.callback.OnClientOnlineCallback
            val$onClientOnlineCallback;

    ControllerImpl$5(ControllerImpl this$0, com.meiqia.meiqiasdk.callback.OnClientOnlineCallback
            onClientOnlineCallback) {
        this.this$0 = this$0;
        this.val$onClientOnlineCallback = onClientOnlineCallback;
    }

    public void onSuccess(MQAgent mqAgent, String conversationId, List<MQMessage>
            conversationMessageList) {
        Agent agent = MQUtils.parseMQAgentToAgent(mqAgent);
        List<BaseMessage> messageList = MQUtils.parseMQMessageToChatBaseList
                (conversationMessageList);
        if (this.val$onClientOnlineCallback != null) {
            this.val$onClientOnlineCallback.onSuccess(agent, conversationId, messageList);
        }
    }

    public void onFailure(int code, String message) {
        if (this.val$onClientOnlineCallback != null) {
            this.val$onClientOnlineCallback.onFailure(code, message);
        }
    }
}
