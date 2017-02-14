package com.xiaomi.smack;

import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Base64;

import com.alipay.sdk.sys.a;
import com.xiaomi.smack.packet.d;
import com.xiaomi.smack.util.g;
import com.xiaomi.smack.util.h;
import com.xiaomi.smack.util.k;

import java.io.Writer;
import java.util.Locale;

class i {
    private Writer a;
    private l      b;

    protected i(l lVar) {
        this.b = lVar;
        this.a = lVar.i;
    }

    private void b(d dVar) {
        synchronized (this.a) {
            try {
                String a = dVar.a();
                this.a.write(a + "\r\n");
                this.a.flush();
                Object o = dVar.o();
                if (!TextUtils.isEmpty(o)) {
                    k.a(this.b.m, o, (long) k.a(a), false, System.currentTimeMillis());
                }
            } catch (Throwable e) {
                throw new p(e);
            }
        }
    }

    void a() {
        this.b.f.clear();
    }

    public void a(d dVar) {
        b(dVar);
        this.b.b(dVar);
    }

    public void b() {
        synchronized (this.a) {
            this.a.write("</stream:stream>");
            this.a.flush();
        }
    }

    void c() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<stream:stream");
        stringBuilder.append(" xmlns=\"xm\"");
        stringBuilder.append(" xmlns:stream=\"xm\"");
        stringBuilder.append(" to=\"").append(this.b.b()).append(a.e);
        stringBuilder.append(" version=\"105\"");
        stringBuilder.append(" model=\"").append(g.a(Build.MODEL)).append(a.e);
        stringBuilder.append(" os=\"").append(g.a(VERSION.INCREMENTAL)).append(a.e);
        String b = h.b();
        if (b != null) {
            stringBuilder.append(" uid=\"").append(b).append(a.e);
        }
        stringBuilder.append(" sdk=\"").append(8).append(a.e);
        stringBuilder.append(" connpt=\"").append(g.a(this.b.d())).append(a.e);
        stringBuilder.append(" host=\"").append(this.b.c()).append(a.e);
        stringBuilder.append(" locale=\"").append(g.a(Locale.getDefault().toString())).append(a.e);
        byte[] a = this.b.a().a();
        if (a != null) {
            stringBuilder.append(" ps=\"").append(Base64.encodeToString(a, 10)).append(a.e);
        }
        stringBuilder.append(">");
        this.a.write(stringBuilder.toString());
        this.a.flush();
    }

    public void d() {
        synchronized (this.a) {
            try {
                this.a.write(this.b.t() + "\r\n");
                this.a.flush();
                this.b.v();
            } catch (Throwable e) {
                throw new p(e);
            }
        }
    }
}
