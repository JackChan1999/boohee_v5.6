package com.zxinsight.common.http;

import android.content.Context;

import com.zxinsight.MWConfiguration;

public class d {
    private static volatile d c;
    private v a = a();
    private g b = new g(this.a, new e(this));

    private d(Context context) {
        MWConfiguration.initContext(context);
    }

    public static d a(Context context) {
        if (c == null) {
            synchronized (d.class) {
                if (c == null) {
                    c = new d(context.getApplicationContext());
                }
            }
        }
        return c;
    }

    public v a() {
        if (this.a == null) {
            this.a = q.a();
        }
        return this.a;
    }

    public void a(Request request) {
        a().a(request);
    }

    public g b() {
        return this.b;
    }
}
