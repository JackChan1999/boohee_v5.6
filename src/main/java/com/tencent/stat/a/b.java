package com.tencent.stat.a;

import android.content.Context;

import java.util.Map.Entry;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

public class b extends e {
    protected c    a = new c();
    private   long l = -1;

    public b(Context context, int i, String str) {
        super(context, i);
        this.a.a = str;
    }

    public f a() {
        return f.CUSTOM;
    }

    public void a(long j) {
        this.l = j;
    }

    public void a(Properties properties) {
        if (properties != null) {
            this.a.c = (Properties) properties.clone();
        }
    }

    public void a(String[] strArr) {
        if (strArr != null) {
            this.a.b = (String[]) strArr.clone();
        }
    }

    public boolean a(JSONObject jSONObject) {
        jSONObject.put("ei", this.a.a);
        if (this.l > 0) {
            jSONObject.put("du", this.l);
        }
        if (this.a.c == null && this.a.b == null) {
            jSONObject.put("kv", new JSONObject());
        }
        if (this.a.b != null) {
            JSONArray jSONArray = new JSONArray();
            for (Object put : this.a.b) {
                jSONArray.put(put);
            }
            jSONObject.put("ar", jSONArray);
        }
        if (this.a.c != null) {
            Object jSONObject2;
            JSONObject jSONObject3 = new JSONObject();
            try {
                for (Entry entry : this.a.c.entrySet()) {
                    jSONObject3.put(entry.getKey().toString(), entry.getValue().toString());
                }
                JSONObject jSONObject4 = jSONObject3;
            } catch (Exception e) {
                jSONObject2 = new JSONObject(this.a.c);
            }
            jSONObject.put("kv", jSONObject2);
        }
        return true;
    }
}
