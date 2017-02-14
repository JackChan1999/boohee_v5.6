package com.meiqia.core;

import com.squareup.okhttp.Response;

import org.json.JSONObject;

class ax implements cw {
    final /* synthetic */ dc a;
    final /* synthetic */ b  b;

    ax(b bVar, dc dcVar) {
        this.b = bVar;
        this.a = dcVar;
    }

    public void a(JSONObject jSONObject, Response response) {
        String optString = jSONObject.optString("photo_url");
        this.b.a(new ay(this, jSONObject.optString("photo_key"), optString));
    }
}
