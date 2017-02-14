package com.tencent.stat;

import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.k;

import org.json.JSONObject;

public class DeviceInfo {
    public static final int        NEW_USER       = 0;
    public static final int        OLD_USER       = 1;
    public static final String     TAG_ANDROID_ID = "aid";
    public static final String     TAG_FLAG       = "__MTA_DEVICE_INFO__";
    public static final String     TAG_IMEI       = "ui";
    public static final String     TAG_MAC        = "mc";
    public static final String     TAG_MID        = "mid";
    public static final String     TAG_TIMESTAMPS = "ts";
    public static final String     TAG_VERSION    = "ver";
    public static final int        UPGRADE_USER   = 2;
    private static      StatLogger h              = k.b();
    private             String     a              = null;
    private             String     b              = null;
    private             String     c              = null;
    private             String     d              = "0";
    private int e;
    private int  f = 0;
    private long g = 0;

    DeviceInfo() {
    }

    DeviceInfo(String str, String str2, int i) {
        this.a = str;
        this.b = str2;
        this.e = i;
    }

    static DeviceInfo a(String str) {
        DeviceInfo deviceInfo = new DeviceInfo();
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.isNull(TAG_IMEI)) {
                deviceInfo.d(jSONObject.getString(TAG_IMEI));
            }
            if (!jSONObject.isNull("mc")) {
                deviceInfo.e(jSONObject.getString("mc"));
            }
            if (!jSONObject.isNull(TAG_MID)) {
                deviceInfo.c(jSONObject.getString(TAG_MID));
            }
            if (!jSONObject.isNull(TAG_ANDROID_ID)) {
                deviceInfo.b(jSONObject.getString(TAG_ANDROID_ID));
            }
            if (!jSONObject.isNull(TAG_TIMESTAMPS)) {
                deviceInfo.a(jSONObject.getLong(TAG_TIMESTAMPS));
            }
            if (!jSONObject.isNull(TAG_VERSION)) {
                deviceInfo.a(jSONObject.getInt(TAG_VERSION));
            }
        } catch (Exception e) {
            h.e(e);
        }
        return deviceInfo;
    }

    int a() {
        return this.f;
    }

    int a(DeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            return 1;
        }
        String mid = getMid();
        String mid2 = deviceInfo.getMid();
        if (mid != null && mid2 != null && mid.equals(mid2)) {
            return 0;
        }
        int a = a();
        int a2 = deviceInfo.a();
        if (a > a2) {
            return 1;
        }
        if (a != a2) {
            return -1;
        }
        long b = b();
        long b2 = deviceInfo.b();
        return b <= b2 ? b == b2 ? 0 : -1 : 1;
    }

    void a(int i) {
        this.f = i;
    }

    void a(long j) {
        this.g = j;
    }

    long b() {
        return this.g;
    }

    void b(int i) {
        this.e = i;
    }

    void b(String str) {
        this.c = str;
    }

    JSONObject c() {
        JSONObject jSONObject = new JSONObject();
        try {
            k.a(jSONObject, TAG_IMEI, this.a);
            k.a(jSONObject, "mc", this.b);
            k.a(jSONObject, TAG_MID, this.d);
            k.a(jSONObject, TAG_ANDROID_ID, this.c);
            jSONObject.put(TAG_TIMESTAMPS, this.g);
            jSONObject.put(TAG_VERSION, this.f);
        } catch (Exception e) {
            h.e(e);
        }
        return jSONObject;
    }

    void c(String str) {
        this.d = str;
    }

    void d(String str) {
        this.a = str;
    }

    void e(String str) {
        this.b = str;
    }

    public String getImei() {
        return this.a;
    }

    public String getMac() {
        return this.b;
    }

    public String getMid() {
        return this.d;
    }

    public int getUserType() {
        return this.e;
    }

    public String toString() {
        return c().toString();
    }
}
