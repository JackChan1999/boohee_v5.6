package com.tencent.wxop.stat.a;

import android.content.Context;

import com.alipay.sdk.sys.a;
import com.tencent.a.a.a.a.h;
import com.tencent.stat.DeviceInfo;
import com.tencent.wxop.stat.b.c;
import com.tencent.wxop.stat.b.l;
import com.tencent.wxop.stat.b.r;
import com.tencent.wxop.stat.f;
import com.tencent.wxop.stat.t;

import org.json.JSONObject;

public abstract class d {
    protected static String bt = null;
    protected int  L;
    protected long aZ;
    protected String b = null;
    protected int bf;
    protected c       bp = null;
    protected String  bq = null;
    protected String  br = null;
    protected String  bs = null;
    protected boolean bu = false;
    protected Context bv;
    private f bw = null;

    d(Context context, int i, f fVar) {
        this.bv = context;
        this.aZ = System.currentTimeMillis() / 1000;
        this.L = i;
        this.br = com.tencent.wxop.stat.c.e(context);
        this.bs = l.D(context);
        this.b = com.tencent.wxop.stat.c.d(context);
        if (fVar != null) {
            this.bw = fVar;
            if (l.e(fVar.S())) {
                this.b = fVar.S();
            }
            if (l.e(fVar.T())) {
                this.br = fVar.T();
            }
            if (l.e(fVar.getVersion())) {
                this.bs = fVar.getVersion();
            }
            this.bu = fVar.U();
        }
        this.bq = com.tencent.wxop.stat.c.g(context);
        this.bp = t.s(context).t(context);
        if (ac() != e.NETWORK_DETECTOR) {
            this.bf = l.K(context).intValue();
        } else {
            this.bf = -e.NETWORK_DETECTOR.r();
        }
        if (!h.e(bt)) {
            String h = com.tencent.wxop.stat.c.h(context);
            bt = h;
            if (!l.e(h)) {
                bt = "0";
            }
        }
    }

    private boolean c(JSONObject jSONObject) {
        boolean z = false;
        try {
            r.a(jSONObject, "ky", this.b);
            jSONObject.put("et", ac().r());
            if (this.bp != null) {
                jSONObject.put(DeviceInfo.TAG_IMEI, this.bp.b());
                r.a(jSONObject, "mc", this.bp.ar());
                int as = this.bp.as();
                jSONObject.put("ut", as);
                if (as == 0 && l.N(this.bv) == 1) {
                    jSONObject.put("ia", 1);
                }
            }
            r.a(jSONObject, "cui", this.bq);
            if (ac() != e.SESSION_ENV) {
                r.a(jSONObject, a.j, this.bs);
                r.a(jSONObject, "ch", this.br);
            }
            if (this.bu) {
                jSONObject.put("impt", 1);
            }
            r.a(jSONObject, DeviceInfo.TAG_MID, bt);
            jSONObject.put("idx", this.bf);
            jSONObject.put("si", this.L);
            jSONObject.put(DeviceInfo.TAG_TIMESTAMPS, this.aZ);
            jSONObject.put("dts", l.a(this.bv, false));
            z = b(jSONObject);
        } catch (Throwable th) {
        }
        return z;
    }

    public final Context J() {
        return this.bv;
    }

    public final boolean X() {
        return this.bu;
    }

    public abstract e ac();

    public final long ad() {
        return this.aZ;
    }

    public final f ae() {
        return this.bw;
    }

    public final String af() {
        try {
            JSONObject jSONObject = new JSONObject();
            c(jSONObject);
            return jSONObject.toString();
        } catch (Throwable th) {
            return "";
        }
    }

    public abstract boolean b(JSONObject jSONObject);
}
