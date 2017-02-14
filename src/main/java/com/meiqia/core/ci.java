package com.meiqia.core;

import com.squareup.okhttp.Response;

import org.json.JSONObject;

class ci implements cw {
    final /* synthetic */ cz a;
    final /* synthetic */ bu b;

    ci(bu buVar, cz czVar) {
        this.b = buVar;
        this.a = czVar;
    }

    public void a(JSONObject jSONObject, Response response) {
        this.a.a(jSONObject.optJSONObject("ticket_config"));
    }
}
