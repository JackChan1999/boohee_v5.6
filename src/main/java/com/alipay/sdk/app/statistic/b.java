package com.alipay.sdk.app.statistic;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.alipay.sdk.packet.impl.d;
import com.alipay.sdk.util.h;
import java.io.IOException;

final class b implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;

    b(Context context, String str) {
        this.a = context;
        this.b = str;
    }

    public final void run() {
        d dVar = new d();
        try {
            String a = h.a(this.a, a.a);
            if (!(TextUtils.isEmpty(a) || dVar.a(this.a, a) == null)) {
                Context context = this.a;
                try {
                    PreferenceManager.getDefaultSharedPreferences(context).edit().remove(a.a).commit();
                } catch (Throwable th) {
                }
            }
        } catch (Throwable th2) {
        }
        try {
            if (!TextUtils.isEmpty(this.b)) {
                dVar.a(this.a, this.b);
            }
        } catch (IOException e) {
            h.a(this.a, a.a, this.b);
        } catch (Throwable th3) {
        }
    }
}
