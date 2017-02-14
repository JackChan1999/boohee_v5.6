package com.tencent.a.a.a.a;

import android.util.Log;

import com.tencent.stat.DeviceInfo;

import org.json.JSONObject;

public final class c {
    String a = null;
    String b = null;
    String c = "0";
    long   d = 0;

    static c c(String str) {
        c cVar = new c();
        if (h.d(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (!jSONObject.isNull(DeviceInfo.TAG_IMEI)) {
                    cVar.a = jSONObject.getString(DeviceInfo.TAG_IMEI);
                }
                if (!jSONObject.isNull("mc")) {
                    cVar.b = jSONObject.getString("mc");
                }
                if (!jSONObject.isNull(DeviceInfo.TAG_MID)) {
                    cVar.c = jSONObject.getString(DeviceInfo.TAG_MID);
                }
                if (!jSONObject.isNull(DeviceInfo.TAG_TIMESTAMPS)) {
                    cVar.d = jSONObject.getLong(DeviceInfo.TAG_TIMESTAMPS);
                }
            } catch (Throwable e) {
                Log.w("MID", e);
            }
        }
        return cVar;
    }

    private JSONObject d() {
        JSONObject jSONObject = new JSONObject();
        try {
            h.a(jSONObject, DeviceInfo.TAG_IMEI, this.a);
            h.a(jSONObject, "mc", this.b);
            h.a(jSONObject, DeviceInfo.TAG_MID, this.c);
            jSONObject.put(DeviceInfo.TAG_TIMESTAMPS, this.d);
        } catch (Throwable e) {
            Log.w("MID", e);
        }
        return jSONObject;
    }

    public final String c() {
        return this.c;
    }

    public final String toString() {
        return d().toString();
    }
}
