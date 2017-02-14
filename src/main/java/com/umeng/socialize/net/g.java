package com.umeng.socialize.net;

import android.text.TextUtils;

import com.umeng.socialize.net.base.SocializeReseponse;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: PlatformKeyUploadResponse */
public class g extends SocializeReseponse {
    public String a;
    public String b;

    public g(JSONObject jSONObject) {
        super(jSONObject);
    }

    public void parseJsonObject() {
        super.parseJsonObject();
        a();
        b();
    }

    private void a() {
        if (this.mJsonData != null) {
            try {
                JSONObject jSONObject = this.mJsonData.getJSONObject("tencent");
                if (jSONObject != null) {
                    Object optString = jSONObject.optString("user_id");
                    if (!TextUtils.isEmpty(optString)) {
                        this.a = optString;
                    }
                }
            } catch (JSONException e) {
            }
        }
    }

    private void b() {
        try {
            if (this.mJsonData != null) {
                JSONObject jSONObject = this.mJsonData.getJSONObject("sina");
                if (jSONObject != null) {
                    Object optString = jSONObject.optString("expires_in");
                    if (!TextUtils.isEmpty(optString)) {
                        this.b = optString;
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
