package com.meiqia.core;

import com.meiqia.core.callback.OnGetClientCallback;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

class by implements cw {
    final /* synthetic */ OnGetClientCallback a;
    final /* synthetic */ bu                  b;

    by(bu buVar, OnGetClientCallback onGetClientCallback) {
        this.b = buVar;
        this.a = onGetClientCallback;
    }

    public void a(JSONObject jSONObject, Response response) {
        String optString = jSONObject.optString("track_id");
        String optString2 = jSONObject.optString("enterprise_id");
        String optString3 = jSONObject.optString("browser_id");
        String optString4 = jSONObject.optString("visit_page_id");
        String optString5 = jSONObject.optString("visit_id");
        this.a.onSuccess(true, jSONObject.optString("aes_key"), optString, optString2,
                optString3, optString4, optString5);
    }
}
