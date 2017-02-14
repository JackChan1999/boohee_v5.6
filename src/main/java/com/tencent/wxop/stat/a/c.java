package com.tencent.wxop.stat.a;

import android.content.Context;

import com.tencent.wxop.stat.b.d;
import com.tencent.wxop.stat.b.r;
import com.tencent.wxop.stat.f;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.json.JSONObject;

public final class c extends d {
    private String a;
    private int    ay;
    private int    bn = 100;
    private Thread bo = null;

    public c(Context context, int i, Throwable th, f fVar) {
        super(context, i, fVar);
        a(99, th);
    }

    public c(Context context, int i, Throwable th, Thread thread) {
        super(context, i, null);
        a(2, th);
        this.bo = thread;
    }

    private void a(int i, Throwable th) {
        if (th != null) {
            Writer stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            this.a = stringWriter.toString();
            this.ay = i;
            printWriter.close();
        }
    }

    public final e ac() {
        return e.ERROR;
    }

    public final boolean b(JSONObject jSONObject) {
        r.a(jSONObject, "er", this.a);
        jSONObject.put("ea", this.ay);
        if (this.ay == 2 || this.ay == 3) {
            new d(this.bv).a(jSONObject, this.bo);
        }
        return true;
    }
}
