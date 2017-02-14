package com.xiaomi.smack;

import android.support.v4.os.EnvironmentCompat;
import android.util.Pair;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.channel.commonutils.string.c;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.w;
import com.xiaomi.push.service.y;
import com.xiaomi.smack.packet.d;
import com.xiaomi.smack.packet.f;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class a {
    public static boolean a;
    private static final AtomicInteger n = new AtomicInteger(0);
    protected            int           b = 0;
    protected            long          c = -1;
    protected int d;
    protected final Map<f, a>                   e = new ConcurrentHashMap();
    protected final Map<f, a>                   f = new ConcurrentHashMap();
    protected       com.xiaomi.smack.debugger.a g = null;
    protected Reader h;
    protected Writer i;
    protected       String j = "";
    protected final int    k = n.getAndIncrement();
    protected b             l;
    protected XMPushService m;
    private       LinkedList<Pair<Integer, Long>> o = new LinkedList();
    private final Collection<d>                   p = new CopyOnWriteArrayList();
    private       int                             q = 2;
    private       long                            r = 0;

    protected static class a {
        private f                         a;
        private com.xiaomi.smack.filter.a b;

        public a(f fVar, com.xiaomi.smack.filter.a aVar) {
            this.a = fVar;
            this.b = aVar;
        }

        public void a(d dVar) {
            if (this.b == null || this.b.a(dVar)) {
                this.a.a(dVar);
            }
        }
    }

    static {
        a = false;
        try {
            a = Boolean.getBoolean("smack.debugEnabled");
        } catch (Exception e) {
        }
        j.a();
    }

    protected a(XMPushService xMPushService, b bVar) {
        this.l = bVar;
        this.m = xMPushService;
    }

    private String a(int i) {
        return i == 1 ? "connected" : i == 0 ? "connecting" : i == 2 ? "disconnected" :
                EnvironmentCompat.MEDIA_UNKNOWN;
    }

    private void b(int i) {
        synchronized (this.o) {
            if (i == 1) {
                this.o.clear();
            } else {
                this.o.add(new Pair(Integer.valueOf(i), Long.valueOf(System.currentTimeMillis())));
                if (this.o.size() > 6) {
                    this.o.remove(0);
                }
            }
        }
    }

    public b a() {
        return this.l;
    }

    public void a(int i, int i2, Exception exception) {
        if (i != this.q) {
            b.a(String.format("update the connection status. %1$s -> %2$s : %3$s ", new
                    Object[]{a(this.q), a(i), y.a(i2)}));
        }
        if (com.xiaomi.channel.commonutils.network.d.d(this.m)) {
            b(i);
        }
        if (i == 1) {
            this.m.a(10);
            if (this.q != 0) {
                b.a("try set connected while not connecting.");
            }
            this.q = i;
            for (d a : this.p) {
                a.a(this);
            }
        } else if (i == 0) {
            this.m.h();
            if (this.q != 2) {
                b.a("try set connecting while not disconnected.");
            }
            this.q = i;
            for (d a2 : this.p) {
                a2.b(this);
            }
        } else if (i == 2) {
            this.m.a(10);
            if (this.q == 0) {
                for (d a22 : this.p) {
                    a22.a(this, exception == null ? new CancellationException("disconnect while " +
                            "connecting") : exception);
                }
            } else if (this.q == 1) {
                for (d a222 : this.p) {
                    a222.a(this, i2, exception);
                }
            }
            this.q = i;
        }
    }

    public abstract void a(w.b bVar);

    public void a(d dVar) {
        if (dVar != null && !this.p.contains(dVar)) {
            this.p.add(dVar);
        }
    }

    public void a(f fVar, com.xiaomi.smack.filter.a aVar) {
        if (fVar == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        this.e.put(fVar, new a(fVar, aVar));
    }

    public abstract void a(d dVar);

    public abstract void a(f fVar, int i, Exception exception);

    public void a(String str) {
        b.a("setChallenge hash = " + c.a(str).substring(0, 8));
        this.j = str;
        a(1, 0, null);
    }

    public abstract void a(String str, String str2);

    public abstract void a(d[] dVarArr);

    public boolean a(long j) {
        return this.r >= j;
    }

    public String b() {
        return this.l.c();
    }

    public void b(d dVar) {
        this.p.remove(dVar);
    }

    public void b(f fVar, com.xiaomi.smack.filter.a aVar) {
        if (fVar == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        this.f.put(fVar, new a(fVar, aVar));
    }

    protected void b(d dVar) {
        for (a a : this.f.values()) {
            a.a(dVar);
        }
    }

    public String c() {
        return this.l.f();
    }

    public String d() {
        return this.l.d();
    }

    public int e() {
        return this.d;
    }

    protected void f() {
        Class cls = null;
        if (this.h != null && this.i != null && this.l.g()) {
            if (this.g == null) {
                String property;
                try {
                    property = System.getProperty("smack.debuggerClass");
                } catch (Throwable th) {
                    Object obj = cls;
                }
                if (property != null) {
                    try {
                        cls = Class.forName(property);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (cls == null) {
                    this.g = new com.xiaomi.measite.smack.a(this, this.i, this.h);
                    this.h = this.g.a();
                    this.i = this.g.b();
                    return;
                }
                try {
                    this.g = (com.xiaomi.smack.debugger.a) cls.getConstructor(new Class[]{a
                            .class, Writer.class, Reader.class}).newInstance(new Object[]{this,
                            this.i, this.h});
                    this.h = this.g.a();
                    this.i = this.g.b();
                    return;
                } catch (Throwable e2) {
                    throw new IllegalArgumentException("Can't initialize the configured " +
                            "debugger!", e2);
                }
            }
            this.h = this.g.a(this.h);
            this.i = this.g.a(this.i);
        }
    }

    public boolean g() {
        return this.q == 0;
    }

    public boolean h() {
        return this.q == 1;
    }

    public int i() {
        return this.b;
    }

    public void j() {
        this.b = 0;
    }

    public long k() {
        return this.c;
    }

    public void l() {
        this.c = -1;
    }

    public abstract void m();

    public int n() {
        return this.q;
    }

    public void o() {
        this.r = System.currentTimeMillis();
    }

    public boolean p() {
        return System.currentTimeMillis() - this.r < ((long) j.b());
    }

    public void q() {
        synchronized (this.o) {
            this.o.clear();
        }
    }
}
