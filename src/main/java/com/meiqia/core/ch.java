package com.meiqia.core;

import com.squareup.okhttp.Response;

import org.json.JSONObject;

class ch implements cw {
    final /* synthetic */ db a;
    final /* synthetic */ bu b;

    ch(bu buVar, db dbVar) {
        this.b = buVar;
        this.a = dbVar;
    }

    public void a(JSONObject jSONObject, Response response) {
        this.a.a(jSONObject.optJSONArray("replies"));
    }
}
