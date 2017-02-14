package com.meiqia.core;

import com.meiqia.core.callback.SimpleCallback;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

class cf implements cw {
    final /* synthetic */ SimpleCallback a;
    final /* synthetic */ bu             b;

    cf(bu buVar, SimpleCallback simpleCallback) {
        this.b = buVar;
        this.a = simpleCallback;
    }

    public void a(JSONObject jSONObject, Response response) {
        this.a.onSuccess();
    }
}
