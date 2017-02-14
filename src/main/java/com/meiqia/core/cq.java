package com.meiqia.core;

import com.squareup.okhttp.Response;

import org.json.JSONObject;

class cq implements cw {
    final /* synthetic */ da a;
    final /* synthetic */ bu b;

    cq(bu buVar, da daVar) {
        this.b = buVar;
        this.a = daVar;
    }

    public void a(JSONObject jSONObject, Response response) {
        this.a.a(jSONObject.optString("message_created_on"), jSONObject.optLong("message_id"));
    }
}
