package com.meiqia.core;

import com.meiqia.core.callback.OnClientInfoCallback;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

class cc implements cw {
    final /* synthetic */ OnClientInfoCallback a;
    final /* synthetic */ bu                   b;

    cc(bu buVar, OnClientInfoCallback onClientInfoCallback) {
        this.b = buVar;
        this.a = onClientInfoCallback;
    }

    public void a(JSONObject jSONObject, Response response) {
        if (this.a != null) {
            this.a.onSuccess();
        }
    }
}
