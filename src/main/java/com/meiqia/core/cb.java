package com.meiqia.core;

import com.meiqia.core.callback.OnRegisterDeviceTokenCallback;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

class cb implements cw {
    final /* synthetic */ OnRegisterDeviceTokenCallback a;
    final /* synthetic */ bu                            b;

    cb(bu buVar, OnRegisterDeviceTokenCallback onRegisterDeviceTokenCallback) {
        this.b = buVar;
        this.a = onRegisterDeviceTokenCallback;
    }

    public void a(JSONObject jSONObject, Response response) {
        this.a.onSuccess();
    }
}
