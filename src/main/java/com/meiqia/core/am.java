package com.meiqia.core;

import android.content.Intent;

import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQConversation;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.meiqiasdk.util.ErrorCode;

import java.util.List;

class am implements cv {
    final /* synthetic */ List a;
    final /* synthetic */ cv   b;
    final /* synthetic */ b    c;

    am(b bVar, List list, cv cvVar) {
        this.c = bVar;
        this.a = list;
        this.b = cvVar;
    }

    public void a(MQAgent mQAgent, MQConversation mQConversation, List<MQMessage> list) {
        if (this.a != null) {
            list.addAll(0, this.a);
        }
        this.c.b.a(true);
        this.c.a(mQAgent);
        this.c.a(mQConversation);
        this.c.d.b((List) list);
        this.c.a(this.b);
        this.c.c(b.a.getTrackId());
    }

    public void onFailure(int i, String str) {
        String str2;
        if ("success = false".equals(str)) {
            if (i == ErrorCode.BLACKLIST) {
                str2 = "blacklist state";
            } else {
                i = ErrorCode.NO_AGENT_ONLINE;
                str2 = "no agent online";
            }
            Intent intent = new Intent(this.c.e, MeiQiaService.class);
            intent.setAction("ACTION_OPEN_SOCKET");
            this.c.e.startService(intent);
        } else {
            str2 = str;
        }
        if (this.b != null) {
            this.b.onFailure(i, str2);
        }
    }
}
