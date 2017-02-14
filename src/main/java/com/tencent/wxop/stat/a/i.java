package com.tencent.wxop.stat.a;

import android.content.Context;

import com.tencent.wxop.stat.b.d;
import com.tencent.wxop.stat.b.l;
import com.tencent.wxop.stat.f;

import org.json.JSONObject;

public final class i extends d {
    private d bJ;
    private JSONObject bK = null;

    public i(Context context, int i, JSONObject jSONObject, f fVar) {
        super(context, i, fVar);
        this.bJ = new d(context);
        this.bK = jSONObject;
    }

    public final e ac() {
        return e.SESSION_ENV;
    }

    public final boolean b(JSONObject jSONObject) {
        if (this.bp != null) {
            jSONObject.put("ut", this.bp.as());
        }
        if (this.bK != null) {
            jSONObject.put("cfg", this.bK);
        }
        if (l.P(this.bv)) {
            jSONObject.put("ncts", 1);
        }
        this.bJ.a(jSONObject, null);
        return true;
    }
}
