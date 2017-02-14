package com.meiqia.core;

import com.meiqia.core.b.c;
import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQConversation;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

class bw implements cw {
    final /* synthetic */ cv a;
    final /* synthetic */ bu b;

    bw(bu buVar, cv cvVar) {
        this.b = buVar;
        this.a = cvVar;
    }

    public void a(JSONObject jSONObject, Response response) {
        MQAgent b = c.b(jSONObject.optJSONObject("agent"));
        MQConversation c = c.c(jSONObject.optJSONObject("conv"));
        c.setAgent_id(b.getId());
        this.a.a(b, c, c.a(jSONObject.optJSONArray("messages")));
    }
}
