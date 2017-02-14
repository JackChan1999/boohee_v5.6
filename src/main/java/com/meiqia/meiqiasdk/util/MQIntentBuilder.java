package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.content.Intent;

import com.meiqia.core.MQManager;
import com.meiqia.core.MQScheduleRule;
import com.meiqia.meiqiasdk.activity.MQConversationActivity;

import java.util.HashMap;

public class MQIntentBuilder {
    private String  mAgentId;
    private Context mContext;
    private String  mGroupId;
    private Intent  mIntent;
    private MQScheduleRule mScheduleRule = MQScheduleRule.REDIRECT_ENTERPRISE;

    public MQIntentBuilder(Context context) {
        this.mContext = context;
        this.mIntent = new Intent(context, MQConversationActivity.class);
    }

    public MQIntentBuilder(Context context, Class<? extends MQConversationActivity> clazz) {
        this.mContext = context;
        this.mIntent = new Intent(context, clazz);
    }

    public MQIntentBuilder setClientId(String clientId) {
        this.mIntent.putExtra("clientId", clientId);
        return this;
    }

    public MQIntentBuilder setCustomizedId(String customizedId) {
        this.mIntent.putExtra(MQConversationActivity.CUSTOMIZED_ID, customizedId);
        return this;
    }

    public MQIntentBuilder setClientInfo(HashMap<String, String> clientInfo) {
        this.mIntent.putExtra(MQConversationActivity.CLIENT_INFO, clientInfo);
        return this;
    }

    public MQIntentBuilder setScheduledAgent(String agentId) {
        this.mAgentId = agentId;
        return this;
    }

    public MQIntentBuilder setScheduledGroup(String groupId) {
        this.mGroupId = groupId;
        return this;
    }

    public MQIntentBuilder setScheduleRule(MQScheduleRule scheduleRule) {
        this.mScheduleRule = scheduleRule;
        return this;
    }

    public Intent build() {
        MQManager.getInstance(this.mContext).setScheduledAgentOrGroupWithId(this.mAgentId, this
                .mGroupId, this.mScheduleRule);
        return this.mIntent;
    }
}
