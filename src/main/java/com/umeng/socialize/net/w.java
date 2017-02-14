package com.umeng.socialize.net;

import com.umeng.socialize.net.base.SocializeReseponse;

import org.json.JSONObject;

/* compiled from: UploadImageResponse */
public class w extends SocializeReseponse {
    public String a;
    public String b;

    public w(JSONObject jSONObject) {
        super(jSONObject);
    }

    public void parseJsonObject() {
        super.parseJsonObject();
        JSONObject jSONObject = this.mJsonData;
        if (jSONObject != null) {
            this.a = jSONObject.optString("large_url");
            this.b = jSONObject.optString("small_url");
        }
    }
}
