package com.alipay.sdk.data;

import android.content.Context;
import com.alipay.sdk.util.h;
import org.json.JSONObject;

final class b implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ a b;

    b(a aVar, Context context) {
        this.b = aVar;
        this.a = context;
    }

    public final void run() {
        try {
            com.alipay.sdk.packet.b a = new com.alipay.sdk.packet.impl.b().a(this.a);
            if (a != null) {
                this.b.a(a.b);
                a aVar = this.b;
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(a.f, aVar.a());
                    jSONObject.put(a.h, aVar.i);
                    h.a(com.alipay.sdk.sys.b.a().a, a.e, jSONObject.toString());
                } catch (Exception e) {
                }
            }
        } catch (Throwable th) {
        }
    }
}
