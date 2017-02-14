package com.meiqia.core;

import com.meiqia.core.b.i;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.SimpleCallback;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

class aa implements db {
    final /* synthetic */ List           a;
    final /* synthetic */ Map            b;
    final /* synthetic */ SimpleCallback c;
    final /* synthetic */ b              d;

    aa(b bVar, List list, Map map, SimpleCallback simpleCallback) {
        this.d = bVar;
        this.a = list;
        this.b = map;
        this.c = simpleCallback;
    }

    public void a(JSONArray jSONArray) {
        for (int i = 0; i < this.a.size(); i++) {
            MQMessage mQMessage = (MQMessage) this.a.get(i);
            JSONObject optJSONObject = jSONArray.optJSONObject(i);
            if (optJSONObject != null) {
                mQMessage.setCreated_on(i.a(optJSONObject.optString("created_on")));
                mQMessage.setId(optJSONObject.optLong("id"));
                mQMessage.setStatus("arrived");
                mQMessage.setFrom_type("client");
                mQMessage.setType("message");
                mQMessage.setTrack_id(b.a.getTrackId());
                if (!(this.d.g == null || this.d.f == null)) {
                    mQMessage.setAgent_nickname(this.d.f.getNickname());
                    mQMessage.setConversation_id(this.d.g.getId());
                    mQMessage.setAgent_id(this.d.g.getAgent_id());
                    mQMessage.setEnterprise_id(this.d.g.getEnterprise_id());
                }
                this.d.d.a(mQMessage);
            }
        }
        if (this.b != null && this.b.keySet().size() != 0) {
            this.d.a(this.b, this.c);
        } else if (this.c != null) {
            this.d.a(new ab(this));
        }
    }

    public void onFailure(int i, String str) {
        if (this.c != null) {
            this.c.onFailure(i, str);
        }
    }
}
