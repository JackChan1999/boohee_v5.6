package com.tencent.stat.a;

import android.content.Context;

import com.tencent.stat.common.a;

import org.json.JSONObject;

public class k extends e {
    private a a;
    private JSONObject l = null;

    public k(Context context, int i, JSONObject jSONObject) {
        super(context, i);
        this.a = new a(context);
        this.l = jSONObject;
    }

    public f a() {
        return f.SESSION_ENV;
    }

    public boolean a(JSONObject jSONObject) {
        if (this.e != null) {
            jSONObject.put("ut", this.e.getUserType());
        }
        if (this.l != null) {
            jSONObject.put("cfg", this.l);
        }
        this.a.a(jSONObject);
        return true;
    }
}
