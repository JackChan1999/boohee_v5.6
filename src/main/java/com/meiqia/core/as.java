package com.meiqia.core;

import android.content.Intent;

import com.boohee.utils.Utils;
import com.meiqia.core.b.j;
import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQConversation;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnMessageSendCallback;
import com.meiqia.meiqiasdk.util.ErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class as implements cv {
    final /* synthetic */ MQMessage             a;
    final /* synthetic */ OnMessageSendCallback b;
    final /* synthetic */ b                     c;

    as(b bVar, MQMessage mQMessage, OnMessageSendCallback onMessageSendCallback) {
        this.c = bVar;
        this.a = mQMessage;
        this.b = onMessageSendCallback;
    }

    public void a(MQAgent mQAgent, MQConversation mQConversation, List<MQMessage> list) {
        MQMessageManager.getInstance(this.c.e).setCurrentAgent(mQAgent);
        Intent intent = new Intent("agent_change_action");
        intent.putExtra("conversation_id", String.valueOf(mQConversation.getId()));
        j.a(this.c.e, intent);
        this.c.a(mQAgent);
        this.c.a(this.a, new at(this));
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case ErrorCode.NO_AGENT_ONLINE /*19998*/:
                this.c.a(null);
                this.a.setType("reply");
                Map hashMap = new HashMap();
                hashMap.put("track_id", b.a.getTrackId());
                hashMap.put("enterprise_id", b.a.getEnterpriseId());
                hashMap.put("visit_id", b.a.getVisitId());
                hashMap.put(Utils.RESPONSE_CONTENT, this.a.getContent());
                hashMap.put("content_type", this.a.getContent_type());
                this.c.h.a(hashMap, new au(this));
                return;
            default:
                this.a.setStatus("failed");
                this.c.d.a(this.a);
                if (this.b != null) {
                    this.b.onFailure(this.a, i, str);
                    return;
                }
                return;
        }
    }
}
