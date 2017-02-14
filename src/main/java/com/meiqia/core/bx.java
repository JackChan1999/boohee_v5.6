package com.meiqia.core;

import com.meiqia.core.b.c;
import com.meiqia.core.b.g;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.squareup.okhttp.Response;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

class bx implements cw {
    final /* synthetic */ OnGetMessageListCallback a;
    final /* synthetic */ bu                       b;

    bx(bu buVar, OnGetMessageListCallback onGetMessageListCallback) {
        this.b = buVar;
        this.a = onGetMessageListCallback;
    }

    public void a(JSONObject jSONObject, Response response) {
        List a = c.a(jSONObject.optJSONArray("messages"));
        Collections.sort(a, new g());
        this.a.onSuccess(a);
    }
}
