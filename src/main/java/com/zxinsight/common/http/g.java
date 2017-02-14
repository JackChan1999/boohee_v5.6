package com.zxinsight.common.http;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView.ScaleType;

import java.util.HashMap;

public class g {
    private final v a;
    private int b = 100;
    private final k c;
    private final HashMap<String, j> d = new HashMap();
    private final HashMap<String, j> e = new HashMap();
    private final Handler            f = new Handler(Looper.getMainLooper());
    private Runnable g;

    public g(v vVar, k kVar) {
        this.a = vVar;
        this.c = kVar;
    }

    public l a(String str, m mVar) {
        return a(str, mVar, 0, 0);
    }

    public l a(String str, m mVar, int i, int i2) {
        return a(str, mVar, i, i2, ScaleType.CENTER_INSIDE);
    }

    public l a(String str, m mVar, int i, int i2, ScaleType scaleType) {
        a();
        String a = a(str, i, i2, scaleType);
        Bitmap a2 = this.c.a(a);
        if (a2 != null) {
            l lVar = new l(this, a2, str, null, null);
            mVar.a(lVar, true);
            return lVar;
        }
        lVar = new l(this, null, str, a, mVar);
        mVar.a(lVar, true);
        j jVar = (j) this.d.get(a);
        if (jVar != null) {
            jVar.a(lVar);
            return lVar;
        }
        Request a3 = a(str, i, i2, scaleType, a);
        this.a.a(a3);
        this.d.put(a, new j(this, a3, lVar));
        return lVar;
    }

    protected Request a(String str, int i, int i2, ScaleType scaleType, String str2) {
        return new n(str, new h(this, str2), i, i2, scaleType, Config.RGB_565);
    }

    protected void a(String str, Bitmap bitmap) {
        this.c.a(str, bitmap);
        j jVar = (j) this.d.remove(str);
        if (jVar != null) {
            jVar.c = bitmap;
            a(str, jVar);
        }
    }

    protected void a(String str, Exception exception) {
        j jVar = (j) this.d.remove(str);
        if (jVar != null) {
            jVar.a(exception);
            a(str, jVar);
        }
    }

    private void a(String str, j jVar) {
        this.e.put(str, jVar);
        if (this.g == null) {
            this.g = new i(this);
            this.f.postDelayed(this.g, (long) this.b);
        }
    }

    private void a() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("ImageLoader must be invoked from the main thread.");
        }
    }

    private static String a(String str, int i, int i2, ScaleType scaleType) {
        return new StringBuilder(str.length() + 12).append("#W").append(i).append("#H").append
                (i2).append("#S").append(scaleType.ordinal()).append(str).toString();
    }
}
