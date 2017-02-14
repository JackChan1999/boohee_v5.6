package com.tencent.wxop.stat.a;

import android.content.Context;

import com.tencent.wxop.stat.b.r;
import com.tencent.wxop.stat.f;

import org.json.JSONObject;

public final class h extends d {
    String aR;
    String aS;
    Long bI = null;

    public h(Context context, String str, String str2, int i, Long l, f fVar) {
        super(context, i, fVar);
        this.aS = str;
        this.aR = str2;
        this.bI = l;
    }

    public final e ac() {
        return e.PAGE_VIEW;
    }

    public final boolean b(JSONObject jSONObject) {
        r.a(jSONObject, "pi", this.aR);
        r.a(jSONObject, "rf", this.aS);
        if (this.bI != null) {
            jSONObject.put("du", this.bI);
        }
        return true;
    }
}
