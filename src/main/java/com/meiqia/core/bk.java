package com.meiqia.core;

import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQConversation;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnClientOnlineCallback;

import java.util.List;

class bk implements cv {
    final /* synthetic */ OnClientOnlineCallback a;
    final /* synthetic */ MQManager              b;

    bk(MQManager mQManager, OnClientOnlineCallback onClientOnlineCallback) {
        this.b = mQManager;
        this.a = onClientOnlineCallback;
    }

    public void a(MQAgent mQAgent, MQConversation mQConversation, List<MQMessage> list) {
        String str = null;
        if (mQConversation != null) {
            str = String.valueOf(mQConversation.getId());
        }
        this.a.onSuccess(mQAgent, str, list);
    }

    public void onFailure(int i, String str) {
        this.a.onFailure(i, str);
    }
}
