package com.xiaomi.common.logger.thrift.mfs;

import com.umeng.socialize.common.SocializeConstants;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.b;
import org.apache.thrift.meta_data.g;
import org.apache.thrift.protocol.c;
import org.apache.thrift.protocol.i;
import org.apache.thrift.protocol.j;
import org.apache.thrift.protocol.k;

import u.aly.df;

public class f implements Serializable, Cloneable, b<f, a> {
    public static final Map<a, org.apache.thrift.meta_data.b> a;
    private static final k      b = new k("Passport");
    private static final c      c = new c("category", (byte) 11, (short) 1);
    private static final c      d = new c("uuid", (byte) 11, (short) 2);
    private static final c      e = new c("version", (byte) 11, (short) 3);
    private static final c      f = new c("network", (byte) 11, (short) 4);
    private static final c      g = new c("rid", (byte) 11, (short) 5);
    private static final c      h = new c("location", (byte) 12, (short) 6);
    private static final c      i = new c("host_info", df.l, (short) 7);
    private              String j = "";
    private String k;
    private String l;
    private String m;
    private String n;
    private e      o;
    private Set<g> p;

    public enum a {
        CATEGORY((short) 1, "category"),
        UUID((short) 2, "uuid"),
        VERSION((short) 3, "version"),
        NETWORK((short) 4, "network"),
        RID((short) 5, "rid"),
        LOCATION((short) 6, "location"),
        HOST_INFO((short) 7, "host_info");

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
        enumMap.put(a.CATEGORY, new org.apache.thrift.meta_data.b("category", (byte) 1, new org
                .apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.UUID, new org.apache.thrift.meta_data.b("uuid", (byte) 1, new org.apache
                .thrift.meta_data.c((byte) 11)));
        enumMap.put(a.VERSION, new org.apache.thrift.meta_data.b("version", (byte) 1, new org
                .apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.NETWORK, new org.apache.thrift.meta_data.b("network", (byte) 1, new org
                .apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.RID, new org.apache.thrift.meta_data.b("rid", (byte) 1, new org.apache
                .thrift.meta_data.c((byte) 11)));
        enumMap.put(a.LOCATION, new org.apache.thrift.meta_data.b("location", (byte) 2, new g(
                (byte) 12, e.class)));
        enumMap.put(a.HOST_INFO, new org.apache.thrift.meta_data.b("host_info", (byte) 2, new org
                .apache.thrift.meta_data.f(df.l, new g((byte) 12, g.class))));
        a = Collections.unmodifiableMap(enumMap);
        org.apache.thrift.meta_data.b.a(f.class, a);
    }

    public void a(org.apache.thrift.protocol.f fVar) {
        fVar.g();
        while (true) {
            c i = fVar.i();
            if (i.b == (byte) 0) {
                fVar.h();
                h();
                return;
            }
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
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.k = fVar.w();
                        break;
                    }
                case (short) 3:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.l = fVar.w();
                        break;
                    }
                case (short) 4:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.m = fVar.w();
                        break;
                    }
                case (short) 5:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.n = fVar.w();
                        break;
                    }
                case (short) 6:
                    if (i.b != (byte) 12) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.o = new e();
                    this.o.a(fVar);
                    break;
                case (short) 7:
                    if (i.b != df.l) {
                        i.a(fVar, i.b);
                        break;
                    }
                    j o = fVar.o();
                    this.p = new HashSet(o.b * 2);
                    for (int i2 = 0; i2 < o.b; i2++) {
                        g gVar = new g();
                        gVar.a(fVar);
                        this.p.add(gVar);
                    }
                    fVar.p();
                    break;
                default:
                    i.a(fVar, i.b);
                    break;
            }
            fVar.j();
        }
    }

    public boolean a() {
        return this.j != null;
    }

    public boolean a(f fVar) {
        if (fVar == null) {
            return false;
        }
        boolean a = a();
        boolean a2 = fVar.a();
        if ((a || a2) && (!a || !a2 || !this.j.equals(fVar.j))) {
            return false;
        }
        a = b();
        a2 = fVar.b();
        if ((a || a2) && (!a || !a2 || !this.k.equals(fVar.k))) {
            return false;
        }
        a = c();
        a2 = fVar.c();
        if ((a || a2) && (!a || !a2 || !this.l.equals(fVar.l))) {
            return false;
        }
        a = d();
        a2 = fVar.d();
        if ((a || a2) && (!a || !a2 || !this.m.equals(fVar.m))) {
            return false;
        }
        a = e();
        a2 = fVar.e();
        if ((a || a2) && (!a || !a2 || !this.n.equals(fVar.n))) {
            return false;
        }
        a = f();
        a2 = fVar.f();
        if ((a || a2) && (!a || !a2 || !this.o.a(fVar.o))) {
            return false;
        }
        a = g();
        a2 = fVar.g();
        return !(a || a2) || (a && a2 && this.p.equals(fVar.p));
    }

    public int b(f fVar) {
        if (!getClass().equals(fVar.getClass())) {
            return getClass().getName().compareTo(fVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(fVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a()) {
            compareTo = org.apache.thrift.c.a(this.j, fVar.j);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(b()).compareTo(Boolean.valueOf(fVar.b()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (b()) {
            compareTo = org.apache.thrift.c.a(this.k, fVar.k);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(c()).compareTo(Boolean.valueOf(fVar.c()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (c()) {
            compareTo = org.apache.thrift.c.a(this.l, fVar.l);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(d()).compareTo(Boolean.valueOf(fVar.d()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (d()) {
            compareTo = org.apache.thrift.c.a(this.m, fVar.m);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(e()).compareTo(Boolean.valueOf(fVar.e()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (e()) {
            compareTo = org.apache.thrift.c.a(this.n, fVar.n);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(f()).compareTo(Boolean.valueOf(fVar.f()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (f()) {
            compareTo = org.apache.thrift.c.a(this.o, fVar.o);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(g()).compareTo(Boolean.valueOf(fVar.g()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (g()) {
            compareTo = org.apache.thrift.c.a(this.p, fVar.p);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }

    public void b(org.apache.thrift.protocol.f fVar) {
        h();
        fVar.a(b);
        if (this.j != null) {
            fVar.a(c);
            fVar.a(this.j);
            fVar.b();
        }
        if (this.k != null) {
            fVar.a(d);
            fVar.a(this.k);
            fVar.b();
        }
        if (this.l != null) {
            fVar.a(e);
            fVar.a(this.l);
            fVar.b();
        }
        if (this.m != null) {
            fVar.a(f);
            fVar.a(this.m);
            fVar.b();
        }
        if (this.n != null) {
            fVar.a(g);
            fVar.a(this.n);
            fVar.b();
        }
        if (this.o != null && f()) {
            fVar.a(h);
            this.o.b(fVar);
            fVar.b();
        }
        if (this.p != null && g()) {
            fVar.a(i);
            fVar.a(new j((byte) 12, this.p.size()));
            for (g b : this.p) {
                b.b(fVar);
            }
            fVar.f();
            fVar.b();
        }
        fVar.c();
        fVar.a();
    }

    public boolean b() {
        return this.k != null;
    }

    public boolean c() {
        return this.l != null;
    }

    public /* synthetic */ int compareTo(Object obj) {
        return b((f) obj);
    }

    public boolean d() {
        return this.m != null;
    }

    public boolean e() {
        return this.n != null;
    }

    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof f)) ? a((f) obj) : false;
    }

    public boolean f() {
        return this.o != null;
    }

    public boolean g() {
        return this.p != null;
    }

    public void h() {
        if (this.j == null) {
            throw new org.apache.thrift.protocol.g("Required field 'category' was not present! " +
                    "Struct: " + toString());
        } else if (this.k == null) {
            throw new org.apache.thrift.protocol.g("Required field 'uuid' was not present! " +
                    "Struct: " + toString());
        } else if (this.l == null) {
            throw new org.apache.thrift.protocol.g("Required field 'version' was not present! " +
                    "Struct: " + toString());
        } else if (this.m == null) {
            throw new org.apache.thrift.protocol.g("Required field 'network' was not present! " +
                    "Struct: " + toString());
        } else if (this.n == null) {
            throw new org.apache.thrift.protocol.g("Required field 'rid' was not present! Struct:" +
                    " " + toString());
        }
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Passport(");
        stringBuilder.append("category:");
        if (this.j == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.j);
        }
        stringBuilder.append(", ");
        stringBuilder.append("uuid:");
        if (this.k == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.k);
        }
        stringBuilder.append(", ");
        stringBuilder.append("version:");
        if (this.l == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.l);
        }
        stringBuilder.append(", ");
        stringBuilder.append("network:");
        if (this.m == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.m);
        }
        stringBuilder.append(", ");
        stringBuilder.append("rid:");
        if (this.n == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.n);
        }
        if (f()) {
            stringBuilder.append(", ");
            stringBuilder.append("location:");
            if (this.o == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.o);
            }
        }
        if (g()) {
            stringBuilder.append(", ");
            stringBuilder.append("host_info:");
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
