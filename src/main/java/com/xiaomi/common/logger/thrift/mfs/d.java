package com.xiaomi.common.logger.thrift.mfs;

import com.boohee.modeldao.SportRecordDao;
import com.umeng.socialize.common.SocializeConstants;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.thrift.b;
import org.apache.thrift.meta_data.e;
import org.apache.thrift.protocol.c;
import org.apache.thrift.protocol.f;
import org.apache.thrift.protocol.g;
import org.apache.thrift.protocol.i;
import org.apache.thrift.protocol.k;

public class d implements Serializable, Cloneable, b<d, a> {
    public static final Map<a, org.apache.thrift.meta_data.b> a;
    private static final k b = new k("LandNodeInfo");
    private static final c c = new c("ip", (byte) 11, (short) 1);
    private static final c d = new c("failed_count", (byte) 8, (short) 2);
    private static final c e = new c("success_count", (byte) 8, (short) 3);
    private static final c f = new c(SportRecordDao.DURATION, (byte) 10, (short) 4);
    private static final c g = new c("size", (byte) 8, (short) 5);
    private static final c h = new c("exp_info", (byte) 13, (short) 6);
    private static final c i = new c("http_info", (byte) 13, (short) 7);
    private String                j;
    private int                   k;
    private int                   l;
    private long                  m;
    private int                   n;
    private Map<String, Integer>  o;
    private Map<Integer, Integer> p;
    private BitSet q = new BitSet(4);

    public enum a {
        IP((short) 1, "ip"),
        FAILED_COUNT((short) 2, "failed_count"),
        SUCCESS_COUNT((short) 3, "success_count"),
        DURATION((short) 4, SportRecordDao.DURATION),
        SIZE((short) 5, "size"),
        EXP_INFO((short) 6, "exp_info"),
        HTTP_INFO((short) 7, "http_info");

        private static final Map<String, a> h = null;
        private final short  i;
        private final String j;

        static {
            h = new HashMap();
            Iterator it = EnumSet.allOf(a.class).iterator();
            while (it.hasNext()) {
                a aVar = (a) it.next();
                h.put(aVar.a(), aVar);
            }
        }

        private a(short s, String str) {
            this.i = s;
            this.j = str;
        }

        public String a() {
            return this.j;
        }
    }

    static {
        Map enumMap = new EnumMap(a.class);
        enumMap.put(a.IP, new org.apache.thrift.meta_data.b("ip", (byte) 1, new org.apache.thrift
                .meta_data.c((byte) 11)));
        enumMap.put(a.FAILED_COUNT, new org.apache.thrift.meta_data.b("failed_count", (byte) 1,
                new org.apache.thrift.meta_data.c((byte) 8)));
        enumMap.put(a.SUCCESS_COUNT, new org.apache.thrift.meta_data.b("success_count", (byte) 1,
                new org.apache.thrift.meta_data.c((byte) 8)));
        enumMap.put(a.DURATION, new org.apache.thrift.meta_data.b(SportRecordDao.DURATION, (byte)
                1, new org.apache.thrift.meta_data.c((byte) 10)));
        enumMap.put(a.SIZE, new org.apache.thrift.meta_data.b("size", (byte) 1, new org.apache
                .thrift.meta_data.c((byte) 8)));
        enumMap.put(a.EXP_INFO, new org.apache.thrift.meta_data.b("exp_info", (byte) 2, new e(
                (byte) 13, new org.apache.thrift.meta_data.c((byte) 11), new org.apache.thrift
                .meta_data.c((byte) 8))));
        enumMap.put(a.HTTP_INFO, new org.apache.thrift.meta_data.b("http_info", (byte) 2, new e(
                (byte) 13, new org.apache.thrift.meta_data.c((byte) 8), new org.apache.thrift
                .meta_data.c((byte) 8))));
        a = Collections.unmodifiableMap(enumMap);
        org.apache.thrift.meta_data.b.a(d.class, a);
    }

    public d a(int i) {
        this.k = i;
        a(true);
        return this;
    }

    public d a(long j) {
        this.m = j;
        c(true);
        return this;
    }

    public d a(String str) {
        this.j = str;
        return this;
    }

    public d a(Map<String, Integer> map) {
        this.o = map;
        return this;
    }

    public void a(f fVar) {
        fVar.g();
        while (true) {
            c i = fVar.i();
            if (i.b == (byte) 0) {
                fVar.h();
                if (!b()) {
                    throw new g("Required field 'failed_count' was not found in serialized data! " +
                            "Struct: " + toString());
                } else if (!c()) {
                    throw new g("Required field 'success_count' was not found in serialized data!" +
                            " Struct: " + toString());
                } else if (!d()) {
                    throw new g("Required field 'duration' was not found in serialized data! " +
                            "Struct: " + toString());
                } else if (e()) {
                    h();
                    return;
                } else {
                    throw new g("Required field 'size' was not found in serialized data! Struct: " +
                            "" + toString());
                }
            }
            org.apache.thrift.protocol.e k;
            int i2;
            switch (i.c) {
                case (short) 1:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.j = fVar.w();
                        break;
                    }
                case (short) 2:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.k = fVar.t();
                    a(true);
                    break;
                case (short) 3:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.l = fVar.t();
                    b(true);
                    break;
                case (short) 4:
                    if (i.b != (byte) 10) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.m = fVar.u();
                    c(true);
                    break;
                case (short) 5:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.n = fVar.t();
                    d(true);
                    break;
                case (short) 6:
                    if (i.b != (byte) 13) {
                        i.a(fVar, i.b);
                        break;
                    }
                    k = fVar.k();
                    this.o = new HashMap(k.c * 2);
                    for (i2 = 0; i2 < k.c; i2++) {
                        this.o.put(fVar.w(), Integer.valueOf(fVar.t()));
                    }
                    fVar.l();
                    break;
                case (short) 7:
                    if (i.b != (byte) 13) {
                        i.a(fVar, i.b);
                        break;
                    }
                    k = fVar.k();
                    this.p = new HashMap(k.c * 2);
                    for (i2 = 0; i2 < k.c; i2++) {
                        this.p.put(Integer.valueOf(fVar.t()), Integer.valueOf(fVar.t()));
                    }
                    fVar.l();
                    break;
                default:
                    i.a(fVar, i.b);
                    break;
            }
            fVar.j();
        }
    }

    public void a(boolean z) {
        this.q.set(0, z);
    }

    public boolean a() {
        return this.j != null;
    }

    public boolean a(d dVar) {
        if (dVar == null) {
            return false;
        }
        boolean a = a();
        boolean a2 = dVar.a();
        if (((a || a2) && (!a || !a2 || !this.j.equals(dVar.j))) || this.k != dVar.k || this.l !=
                dVar.l || this.m != dVar.m || this.n != dVar.n) {
            return false;
        }
        a = f();
        a2 = dVar.f();
        if ((a || a2) && (!a || !a2 || !this.o.equals(dVar.o))) {
            return false;
        }
        a = g();
        a2 = dVar.g();
        return !(a || a2) || (a && a2 && this.p.equals(dVar.p));
    }

    public int b(d dVar) {
        if (!getClass().equals(dVar.getClass())) {
            return getClass().getName().compareTo(dVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(dVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a()) {
            compareTo = org.apache.thrift.c.a(this.j, dVar.j);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(b()).compareTo(Boolean.valueOf(dVar.b()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (b()) {
            compareTo = org.apache.thrift.c.a(this.k, dVar.k);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(c()).compareTo(Boolean.valueOf(dVar.c()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (c()) {
            compareTo = org.apache.thrift.c.a(this.l, dVar.l);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(d()).compareTo(Boolean.valueOf(dVar.d()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (d()) {
            compareTo = org.apache.thrift.c.a(this.m, dVar.m);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(e()).compareTo(Boolean.valueOf(dVar.e()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (e()) {
            compareTo = org.apache.thrift.c.a(this.n, dVar.n);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(f()).compareTo(Boolean.valueOf(dVar.f()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (f()) {
            compareTo = org.apache.thrift.c.a(this.o, dVar.o);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(g()).compareTo(Boolean.valueOf(dVar.g()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (g()) {
            compareTo = org.apache.thrift.c.a(this.p, dVar.p);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }

    public d b(int i) {
        this.l = i;
        b(true);
        return this;
    }

    public void b(f fVar) {
        h();
        fVar.a(b);
        if (this.j != null) {
            fVar.a(c);
            fVar.a(this.j);
            fVar.b();
        }
        fVar.a(d);
        fVar.a(this.k);
        fVar.b();
        fVar.a(e);
        fVar.a(this.l);
        fVar.b();
        fVar.a(f);
        fVar.a(this.m);
        fVar.b();
        fVar.a(g);
        fVar.a(this.n);
        fVar.b();
        if (this.o != null && f()) {
            fVar.a(h);
            fVar.a(new org.apache.thrift.protocol.e((byte) 11, (byte) 8, this.o.size()));
            for (Entry entry : this.o.entrySet()) {
                fVar.a((String) entry.getKey());
                fVar.a(((Integer) entry.getValue()).intValue());
            }
            fVar.d();
            fVar.b();
        }
        if (this.p != null && g()) {
            fVar.a(i);
            fVar.a(new org.apache.thrift.protocol.e((byte) 8, (byte) 8, this.p.size()));
            for (Entry entry2 : this.p.entrySet()) {
                fVar.a(((Integer) entry2.getKey()).intValue());
                fVar.a(((Integer) entry2.getValue()).intValue());
            }
            fVar.d();
            fVar.b();
        }
        fVar.c();
        fVar.a();
    }

    public void b(boolean z) {
        this.q.set(1, z);
    }

    public boolean b() {
        return this.q.get(0);
    }

    public d c(int i) {
        this.n = i;
        d(true);
        return this;
    }

    public void c(boolean z) {
        this.q.set(2, z);
    }

    public boolean c() {
        return this.q.get(1);
    }

    public /* synthetic */ int compareTo(Object obj) {
        return b((d) obj);
    }

    public void d(boolean z) {
        this.q.set(3, z);
    }

    public boolean d() {
        return this.q.get(2);
    }

    public boolean e() {
        return this.q.get(3);
    }

    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof d)) ? a((d) obj) : false;
    }

    public boolean f() {
        return this.o != null;
    }

    public boolean g() {
        return this.p != null;
    }

    public void h() {
        if (this.j == null) {
            throw new g("Required field 'ip' was not present! Struct: " + toString());
        }
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("LandNodeInfo(");
        stringBuilder.append("ip:");
        if (this.j == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.j);
        }
        stringBuilder.append(", ");
        stringBuilder.append("failed_count:");
        stringBuilder.append(this.k);
        stringBuilder.append(", ");
        stringBuilder.append("success_count:");
        stringBuilder.append(this.l);
        stringBuilder.append(", ");
        stringBuilder.append("duration:");
        stringBuilder.append(this.m);
        stringBuilder.append(", ");
        stringBuilder.append("size:");
        stringBuilder.append(this.n);
        if (f()) {
            stringBuilder.append(", ");
            stringBuilder.append("exp_info:");
            if (this.o == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.o);
            }
        }
        if (g()) {
            stringBuilder.append(", ");
            stringBuilder.append("http_info:");
            if (this.p == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.p);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }
}
