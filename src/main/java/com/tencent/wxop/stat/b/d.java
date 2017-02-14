package com.tencent.wxop.stat.b;

import android.content.Context;

import com.tencent.wxop.stat.g;

import org.json.JSONObject;

public final class d {
    static e cw;
    private static b          cx = l.av();
    private static JSONObject cz = new JSONObject();
    String  c  = null;
    Integer cy = null;

    public d(Context context) {
        try {
            u(context);
            this.cy = l.F(context.getApplicationContext());
            this.c = g.r(context).b();
        } catch (Throwable th) {
            cx.b(th);
        }
    }

    private static synchronized e u(Context context) {
        e eVar;
        synchronized (d.class) {
            if (cw == null) {
                cw = new e(context.getApplicationContext());
            }
            eVar = cw;
        }
        return eVar;
    }

    public final void a(JSONObject jSONObject, Thread thread) {
        JSONObject jSONObject2 = new JSONObject();
        try {
            if (cw != null) {
                cw.a(jSONObject2, thread);
            }
            r.a(jSONObject2, "cn", this.c);
            if (this.cy != null) {
                jSONObject2.put("tn", this.cy);
            }
            if (thread == null) {
                jSONObject.put("ev", jSONObject2);
            } else {
                jSONObject.put("errkv", jSONObject2.toString());
            }
            if (cz != null && cz.length() > 0) {
                jSONObject.put("eva", cz);
            }
        } catch (Throwable th) {
            cx.b(th);
        }
    }
}
