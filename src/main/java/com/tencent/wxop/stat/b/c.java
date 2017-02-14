package com.tencent.wxop.stat.b;

import com.tencent.stat.DeviceInfo;

import org.json.JSONException;
import org.json.JSONObject;

public final class c {
    private String W  = "0";
    private String a  = null;
    private String b  = null;
    private int    bf = 0;
    private String c  = null;
    private int cu;
    private long cv = 0;

    public c(String str, String str2, int i) {
        this.a = str;
        this.b = str2;
        this.cu = i;
    }

    private JSONObject aq() {
        JSONObject jSONObject = new JSONObject();
        try {
            r.a(jSONObject, DeviceInfo.TAG_IMEI, this.a);
            r.a(jSONObject, "mc", this.b);
            r.a(jSONObject, DeviceInfo.TAG_MID, this.W);
            r.a(jSONObject, DeviceInfo.TAG_ANDROID_ID, this.c);
            jSONObject.put(DeviceInfo.TAG_TIMESTAMPS, this.cv);
            jSONObject.put(DeviceInfo.TAG_VERSION, this.bf);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public final String ar() {
        return this.b;
    }

    public final int as() {
        return this.cu;
    }

    public final String b() {
        return this.a;
    }

    public final String toString() {
        return aq().toString();
    }

    public final void z() {
        this.cu = 1;
    }
}
