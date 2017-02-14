package com.tencent.stat.a;

import android.content.Context;

import com.tencent.stat.common.k;

import org.json.JSONObject;

public class i extends e {
    private static String a = null;
    private        String l = null;
    private        String m = null;

    public i(Context context, int i) {
        super(context, i);
        this.l = k.p(context);
        if (a == null) {
            a = k.m(context);
        }
    }

    public f a() {
        return f.NETWORK_MONITOR;
    }

    public void a(String str) {
        this.m = str;
    }

    public boolean a(JSONObject jSONObject) {
        k.a(jSONObject, "op", a);
        k.a(jSONObject, "cn", this.l);
        jSONObject.put("sp", this.m);
        return true;
    }
}
