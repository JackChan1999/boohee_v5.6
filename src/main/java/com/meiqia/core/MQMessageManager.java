package com.meiqia.core;

import android.content.Context;

import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQMessage;

import java.util.HashMap;
import java.util.Map;

public class MQMessageManager {
    public static final String ACTION_AGENT_CHANGE_EVENT        = "agent_change_action";
    public static final String ACTION_AGENT_INPUTTING           = "agent_inputting_action";
    public static final String ACTION_AGENT_STATUS_UPDATE_EVENT =
            "action_agent_status_update_event";
    public static final String ACTION_BLACK_ADD                 = "action_black_add";
    public static final String ACTION_INVITE_EVALUATION         = "invite_evaluation";
    public static final String ACTION_NEW_MESSAGE_RECEIVED      = "new_msg_received_action";
    private static MQMessageManager instance;
    private Map<String, MQMessage> cacheMQMessageMap = new HashMap();
    private MQAgent currentAgent;
    private String preMsgId = "";

    private MQMessageManager(Context context) {
    }

    public static MQMessageManager getInstance(Context context) {
        if (instance == null) {
            instance = new MQMessageManager(context);
        }
        return instance;
    }

    protected void addMQMessage(MQMessage mQMessage) {
        this.cacheMQMessageMap.put(mQMessage.getId() + "", mQMessage);
    }

    public MQAgent getCurrentAgent() {
        return this.currentAgent;
    }

    public MQMessage getMQMessage(String str) {
        MQMessage mQMessage = (MQMessage) this.cacheMQMessageMap.get(str);
        if (!(this.preMsgId == null || this.preMsgId.equals(str))) {
            this.cacheMQMessageMap.remove(this.preMsgId);
        }
        this.preMsgId = str;
        return mQMessage;
    }

    public void setCurrentAgent(MQAgent mQAgent) {
        this.currentAgent = mQAgent;
    }
}
