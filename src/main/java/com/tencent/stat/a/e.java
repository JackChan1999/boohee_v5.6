package com.tencent.stat.a;

import android.content.Context;

import com.alipay.sdk.sys.a;
import com.tencent.stat.DeviceInfo;
import com.tencent.stat.StatConfig;
import com.tencent.stat.common.k;
import com.tencent.stat.n;

import org.json.JSONObject;

public abstract class e {
    private static volatile boolean a = false;
    protected               String  b = null;
    protected long c;
    protected int  d;
    protected DeviceInfo e = null;
    protected int f;
    protected String g = null;
    protected String h = null;
    protected String i = null;
    protected String j = null;
    protected Context k;

    e(Context context, int i) {
        this.k = context;
        this.c = System.currentTimeMillis() / 1000;
        this.d = i;
        this.b = StatConfig.getAppKey(context);
        this.g = StatConfig.getCustomUserId(context);
        this.e = n.a(context).b(context);
        this.f = k.w(context).intValue();
        this.i = k.n(context);
        this.h = StatConfig.getInstallChannel(context);
    }

    public abstract f a();

    public abstract boolean a(JSONObject jSONObject);

    public long b() {
        return this.c;
    }

    public boolean b(JSONObject jSONObject) {
        try {
            k.a(jSONObject, "ky", this.b);
            jSONObject.put("et", a().a());
            if (this.e != null) {
                jSONObject.put(DeviceInfo.TAG_IMEI, this.e.getImei());
                k.a(jSONObject, "mc", this.e.getMac());
                jSONObject.put("ut", this.e.getUserType());
            }
            k.a(jSONObject, "cui", this.g);
            if (a() != f.SESSION_ENV) {
                k.a(jSONObject, a.j, this.i);
                k.a(jSONObject, "ch", this.h);
            }
            k.a(jSONObject, DeviceInfo.TAG_MID, StatConfig.getMid(this.k));
            jSONObject.put("idx", this.f);
            jSONObject.put("si", this.d);
            jSONObject.put(DeviceInfo.TAG_TIMESTAMPS, this.c);
            if (this.e.getUserType() == 0 && k.E(this.k) == 1) {
                jSONObject.put("ia", 1);
            }
            return a(jSONObject);
        } catch (Throwable th) {
            return false;
        }
    }

    public Context c() {
        return this.k;
    }

    public String d() {
        try {
            JSONObject jSONObject = new JSONObject();
            b(jSONObject);
            return jSONObject.toString();
        } catch (Throwable th) {
            return "";
        }
    }
}
