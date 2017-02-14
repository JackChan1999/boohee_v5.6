package com.meiqia.core;

import com.squareup.okhttp.Response;

import org.json.JSONObject;

class cp implements cw {
    final /* synthetic */ da a;
    final /* synthetic */ bu b;

    cp(bu buVar, da daVar) {
        this.b = buVar;
        this.a = daVar;
    }

    public void a(JSONObject jSONObject, Response response) {
        JSONObject optJSONObject = jSONObject.optJSONObject("msg");
        this.a.a(optJSONObject.optString("created_on"), optJSONObject.optLong("id"));
    }
}
