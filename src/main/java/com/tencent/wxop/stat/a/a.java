package com.tencent.wxop.stat.a;

import android.content.Context;

import com.tencent.wxop.stat.e;
import com.tencent.wxop.stat.f;

import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public final class a extends d {
    protected b    bj = new b();
    private   long bk = -1;

    public a(Context context, int i, String str, f fVar) {
        super(context, i, fVar);
        this.bj.a = str;
    }

    public final b ab() {
        return this.bj;
    }

    public final e ac() {
        return e.CUSTOM;
    }

    public final boolean b(JSONObject jSONObject) {
        jSONObject.put("ei", this.bj.a);
        if (this.bk > 0) {
            jSONObject.put("du", this.bk);
        }
        if (this.bj.bl == null) {
            if (this.bj.a != null) {
                Map p = e.p(this.bj.a);
                if (p != null && p.size() > 0) {
                    if (this.bj.bm == null || this.bj.bm.length() == 0) {
                        this.bj.bm = new JSONObject(p);
                    } else {
                        for (Entry entry : p.entrySet()) {
                            try {
                                this.bj.bm.put(entry.getKey().toString(), entry.getValue());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            jSONObject.put("kv", this.bj.bm);
        } else {
            jSONObject.put("ar", this.bj.bl);
        }
        return true;
    }
}
