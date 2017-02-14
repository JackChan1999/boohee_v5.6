package com.tencent.a.a.a.a;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class g {
    private static g               i = null;
    private        Map<Integer, f> f = null;
    private        int             g = 0;
    private        Context         h = null;

    private g(Context context) {
        this.h = context.getApplicationContext();
        this.f = new HashMap(3);
        this.f.put(Integer.valueOf(1), new e(context));
        this.f.put(Integer.valueOf(2), new b(context));
        this.f.put(Integer.valueOf(4), new d(context));
    }

    private c a(List<Integer> list) {
        if (list != null && list.size() >= 0) {
            for (Integer num : list) {
                f fVar = (f) this.f.get(num);
                if (fVar != null) {
                    c e = fVar.e();
                    if (e != null && h.e(e.c)) {
                        return e;
                    }
                }
            }
        }
        return new c();
    }

    public static synchronized g a(Context context) {
        g gVar;
        synchronized (g.class) {
            if (i == null) {
                i = new g(context);
            }
            gVar = i;
        }
        return gVar;
    }

    public final void b(String str) {
        c f = f();
        f.c = str;
        if (!h.d(f.a)) {
            f.a = h.b(this.h);
        }
        if (!h.d(f.b)) {
            f.b = h.c(this.h);
        }
        f.d = System.currentTimeMillis();
        for (Entry value : this.f.entrySet()) {
            ((f) value.getValue()).a(f);
        }
    }

    public final c f() {
        return a(new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(4)})));
    }
}
