package com.meiqia.core;

import android.text.TextUtils;

import com.meiqia.core.callback.SimpleCallback;

import org.json.JSONObject;

class ae implements cz {
    final /* synthetic */ SimpleCallback a;
    final /* synthetic */ b              b;

    ae(b bVar, SimpleCallback simpleCallback) {
        this.b = bVar;
        this.a = simpleCallback;
    }

    public void a(JSONObject jSONObject) {
        if (TextUtils.equals("open", jSONObject.optString("defaultTemplate"))) {
            this.b.b.g(jSONObject.optString("defaultTemplateContent"));
        } else {
            this.b.b.g("");
        }
        if (this.a != null) {
            this.b.a(new af(this));
        }
    }

    public void onFailure(int i, String str) {
        if (this.a != null) {
            this.a.onFailure(i, str);
        }
    }
}
