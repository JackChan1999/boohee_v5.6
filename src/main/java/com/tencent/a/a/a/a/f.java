package com.tencent.a.a.a.a;

import android.content.Context;

public abstract class f {
    protected Context e = null;

    protected f(Context context) {
        this.e = context;
    }

    public final void a(c cVar) {
        if (cVar != null) {
            String cVar2 = cVar.toString();
            if (a()) {
                b(h.g(cVar2));
            }
        }
    }

    protected abstract boolean a();

    protected abstract String b();

    protected abstract void b(String str);

    public final c e() {
        String f = a() ? h.f(b()) : null;
        return f != null ? c.c(f) : null;
    }
}
