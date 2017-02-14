package com.meiqia.core;

import com.squareup.okhttp.Response;

import org.json.JSONObject;

class d implements cw {
    final /* synthetic */ dc a;
    final /* synthetic */ b  b;

    d(b bVar, dc dcVar) {
        this.b = bVar;
        this.a = dcVar;
    }

    public void a(JSONObject jSONObject, Response response) {
        String optString = jSONObject.optString("audio_url");
        this.b.a(new e(this, jSONObject.optString("audio_key"), optString));
    }
}
