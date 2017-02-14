package com.meiqia.meiqiasdk.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.meiqia.core.MQMessageManager;
import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.util.MQUtils;

public abstract class MessageReceiver extends BroadcastReceiver {
    private String mConversationId;

    public abstract void addDirectAgentMessageTip(String str);

    public abstract void blackAdd();

    public abstract void changeTitleToAgentName(String str);

    public abstract void changeTitleToInputting();

    public abstract void inviteEvaluation();

    public abstract void receiveNewMsg(BaseMessage baseMessage);

    public abstract void setCurrentAgent(Agent agent);

    public abstract void setNewConversationId(String str);

    public abstract void updateAgentOnlineOfflineStatus();

    public void setConversationId(String conversationId) {
        this.mConversationId = conversationId;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        MQMessageManager messageManager = MQMessageManager.getInstance(context);
        if ("new_msg_received_action".equals(action)) {
            MQMessage message = messageManager.getMQMessage(intent.getStringExtra("msgId"));
            if (message != null) {
                receiveNewMsg(MQUtils.parseMQMessageToBaseMessage(message));
            }
        } else if ("agent_inputting_action".equals(action)) {
            changeTitleToInputting();
        } else if ("agent_change_action".equals(action)) {
            MQAgent mqAgent = messageManager.getCurrentAgent();
            if (intent.getBooleanExtra("client_is_redirected", false)) {
                addDirectAgentMessageTip(mqAgent.getNickname());
            }
            changeTitleToAgentName(mqAgent.getNickname());
            setCurrentAgent(MQUtils.parseMQAgentToAgent(mqAgent));
            String conversationId = intent.getStringExtra("conversation_id");
            if (!TextUtils.isEmpty(conversationId)) {
                this.mConversationId = conversationId;
                setNewConversationId(conversationId);
            }
        } else if ("invite_evaluation".equals(action)) {
            if (intent.getStringExtra("conversation_id").equals(this.mConversationId)) {
                inviteEvaluation();
            }
        } else if ("action_agent_status_update_event".equals(action)) {
            updateAgentOnlineOfflineStatus();
        } else if ("action_black_add".equals(action)) {
            blackAdd();
        }
    }
}
