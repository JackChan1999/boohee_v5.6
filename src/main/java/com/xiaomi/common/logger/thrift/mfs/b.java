package com.xiaomi.common.logger.thrift.mfs;

import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.meta_data.f;
import org.apache.thrift.meta_data.g;
import org.apache.thrift.protocol.c;
import org.apache.thrift.protocol.i;
import org.apache.thrift.protocol.j;
import org.apache.thrift.protocol.k;

import u.aly.df;

public class b implements Serializable, Cloneable, org.apache.thrift.b<b, a> {
    public static final Map<a, org.apache.thrift.meta_data.b> a;
    private static final k      b = new k("HttpApi");
    private static final c      c = new c("category", (byte) 11, (short) 1);
    private static final c      d = new c("uuid", (byte) 11, (short) 2);
    private static final c      e = new c("version", (byte) 11, (short) 3);
    private static final c      f = new c("network", (byte) 11, (short) 4);
    private static final c      g = new c("client_ip", (byte) 11, (short) 5);
    private static final c      h = new c("location", (byte) 12, (short) 6);
    private static final c      i = new c("host_info", df.l, (short) 7);
    private static final c      j = new c("version_type", (byte) 11, (short) 8);
    private static final c      k = new c(SocializeProtocolConstants.PROTOCOL_KEY_APP_NAME,
            (byte) 11, (short) 9);
    private static final c      l = new c("app_version", (byte) 11, (short) 10);
    private              String m = "";
    private String n;
    private String o;
    private String p;
    private String q;
    private e      r;
    private Set<a> s;
    private String t = "";
    private String u = "";
    private String v = "";

    public enum a {
        CATEGORY((short) 1, "category"),
        UUID((short) 2, "uuid"),
        VERSION((short) 3, "version"),
        NETWORK((short) 4, "network"),
        CLIENT_IP((short) 5, "client_ip"),
        LOCATION((short) 6, "location"),
        HOST_INFO((short) 7, "host_info"),
        VERSION_TYPE((short) 8, "version_type"),
        APP_NAME((short) 9, SocializeProtocolConstants.PROTOCOL_KEY_APP_NAME),
        APP_VERSION((short) 10, "app_version");

        private static final Map<String, a> k = null;
        private final short  l;
        private final String m;

        static {
            k = new HashMap();
            Iterator it = EnumSet.allOf(a.class).iterator();
            while (it.hasNext()) {
                a aVar = (a) it.next();
                k.put(aVar.a(), aVar);
            }
        }

        private a(short s, String str) {
            this.l = s;
            this.m = str;
        }

        public String a() {
            return this.m;
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
        enumMap.put(a.CLIENT_IP, new org.apache.thrift.meta_data.b("client_ip", (byte) 2, new org
                .apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.LOCATION, new org.apache.thrift.meta_data.b("location", (byte) 2, new g(
                (byte) 12, e.class)));
        enumMap.put(a.HOST_INFO, new org.apache.thrift.meta_data.b("host_info", (byte) 2, new f
                (df.l, new g((byte) 12, a.class))));
        enumMap.put(a.VERSION_TYPE, new org.apache.thrift.meta_data.b("version_type", (byte) 2,
                new org.apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.APP_NAME, new org.apache.thrift.meta_data.b(SocializeProtocolConstants
                .PROTOCOL_KEY_APP_NAME, (byte) 2, new org.apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.APP_VERSION, new org.apache.thrift.meta_data.b("app_version", (byte) 2, new
                org.apache.thrift.meta_data.c((byte) 11)));
        a = Collections.unmodifiableMap(enumMap);
        org.apache.thrift.meta_data.b.a(b.class, a);
    }

    public b a(e eVar) {
        this.r = eVar;
        return this;
    }

    public b a(String str) {
        this.m = str;
        return this;
    }

    public void a(a aVar) {
        if (this.s == null) {
            this.s = new HashSet();
        }
        this.s.add(aVar);
    }

    public void a(org.apache.thrift.protocol.f fVar) {
        fVar.g();
        while (true) {
            c i = fVar.i();
            if (i.b == (byte) 0) {
                fVar.h();
                l();
                return;
            }
            switch (i.c) {
                case (short) 1:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.m = fVar.w();
                        break;
                    }
                case (short) 2:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.n = fVar.w();
                        break;
                    }
                case (short) 3:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.o = fVar.w();
                        break;
                    }
                case (short) 4:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.p = fVar.w();
                        break;
                    }
                case (short) 5:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.q = fVar.w();
                        break;
                    }
                case (short) 6:
                    if (i.b != (byte) 12) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.r = new e();
                    this.r.a(fVar);
                    break;
                case (short) 7:
                    if (i.b != df.l) {
                        i.a(fVar, i.b);
                        break;
                    }
                    j o = fVar.o();
                    this.s = new HashSet(o.b * 2);
                    for (int i2 = 0; i2 < o.b; i2++) {
                        a aVar = new a();
                        aVar.a(fVar);
                        this.s.add(aVar);
                    }
                    fVar.p();
                    break;
                case (short) 8:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.t = fVar.w();
                        break;
                    }
                case (short) 9:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.u = fVar.w();
                        break;
                    }
                case (short) 10:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.v = fVar.w();
                        break;
                    }
                default:
                    i.a(fVar, i.b);
                    break;
            }
            fVar.j();
        }
    }

    public boolean a() {
        return this.m != null;
    }

    public boolean a(b bVar) {
        if (bVar == null) {
            return false;
        }
        boolean a = a();
        boolean a2 = bVar.a();
        if ((a || a2) && (!a || !a2 || !this.m.equals(bVar.m))) {
            return false;
        }
        a = b();
        a2 = bVar.b();
        if ((a || a2) && (!a || !a2 || !this.n.equals(bVar.n))) {
            return false;
        }
        a = c();
        a2 = bVar.c();
        if ((a || a2) && (!a || !a2 || !this.o.equals(bVar.o))) {
            return false;
        }
        a = d();
        a2 = bVar.d();
        if ((a || a2) && (!a || !a2 || !this.p.equals(bVar.p))) {
            return false;
        }
        a = e();
        a2 = bVar.e();
        if ((a || a2) && (!a || !a2 || !this.q.equals(bVar.q))) {
            return false;
        }
        a = f();
        a2 = bVar.f();
        if ((a || a2) && (!a || !a2 || !this.r.a(bVar.r))) {
            return false;
        }
        a = h();
        a2 = bVar.h();
        if ((a || a2) && (!a || !a2 || !this.s.equals(bVar.s))) {
            return false;
        }
        a = i();
        a2 = bVar.i();
        if ((a || a2) && (!a || !a2 || !this.t.equals(bVar.t))) {
            return false;
        }
        a = j();
        a2 = bVar.j();
        if ((a || a2) && (!a || !a2 || !this.u.equals(bVar.u))) {
            return false;
        }
        a = k();
        a2 = bVar.k();
        return !(a || a2) || (a && a2 && this.v.equals(bVar.v));
    }

    public int b(b bVar) {
        if (!getClass().equals(bVar.getClass())) {
            return getClass().getName().compareTo(bVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(bVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a()) {
            compareTo = org.apache.thrift.c.a(this.m, bVar.m);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(b()).compareTo(Boolean.valueOf(bVar.b()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (b()) {
            compareTo = org.apache.thrift.c.a(this.n, bVar.n);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(c()).compareTo(Boolean.valueOf(bVar.c()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (c()) {
            compareTo = org.apache.thrift.c.a(this.o, bVar.o);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(d()).compareTo(Boolean.valueOf(bVar.d()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (d()) {
            compareTo = org.apache.thrift.c.a(this.p, bVar.p);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(e()).compareTo(Boolean.valueOf(bVar.e()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (e()) {
            compareTo = org.apache.thrift.c.a(this.q, bVar.q);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(f()).compareTo(Boolean.valueOf(bVar.f()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (f()) {
            compareTo = org.apache.thrift.c.a(this.r, bVar.r);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(h()).compareTo(Boolean.valueOf(bVar.h()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (h()) {
            compareTo = org.apache.thrift.c.a(this.s, bVar.s);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(i()).compareTo(Boolean.valueOf(bVar.i()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (i()) {
            compareTo = org.apache.thrift.c.a(this.t, bVar.t);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(j()).compareTo(Boolean.valueOf(bVar.j()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (j()) {
            compareTo = org.apache.thrift.c.a(this.u, bVar.u);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(k()).compareTo(Boolean.valueOf(bVar.k()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (k()) {
            compareTo = org.apache.thrift.c.a(this.v, bVar.v);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }

    public b b(String str) {
        this.n = str;
        return this;
    }

    public void b(org.apache.thrift.protocol.f fVar) {
        l();
        fVar.a(b);
        if (this.m != null) {
            fVar.a(c);
            fVar.a(this.m);
            fVar.b();
        }
        if (this.n != null) {
            fVar.a(d);
            fVar.a(this.n);
            fVar.b();
        }
        if (this.o != null) {
            fVar.a(e);
            fVar.a(this.o);
            fVar.b();
        }
        if (this.p != null) {
            fVar.a(f);
            fVar.a(this.p);
            fVar.b();
        }
        if (this.q != null && e()) {
            fVar.a(g);
            fVar.a(this.q);
            fVar.b();
        }
        if (this.r != null && f()) {
            fVar.a(h);
            this.r.b(fVar);
            fVar.b();
        }
        if (this.s != null && h()) {
            fVar.a(i);
            fVar.a(new j((byte) 12, this.s.size()));
            for (a b : this.s) {
                b.b(fVar);
            }
            fVar.f();
            fVar.b();
        }
        if (this.t != null && i()) {
            fVar.a(j);
            fVar.a(this.t);
            fVar.b();
        }
        if (this.u != null && j()) {
            fVar.a(k);
            fVar.a(this.u);
            fVar.b();
        }
        if (this.v != null && k()) {
            fVar.a(l);
            fVar.a(this.v);
            fVar.b();
        }
        fVar.c();
        fVar.a();
    }

    public boolean b() {
        return this.n != null;
    }

    public b c(String str) {
        this.o = str;
        return this;
    }

    public boolean c() {
        return this.o != null;
    }

    public /* synthetic */ int compareTo(Object obj) {
        return b((b) obj);
    }

    public b d(String str) {
        this.p = str;
        return this;
    }

    public boolean d() {
        return this.p != null;
    }

    public b e(String str) {
        this.q = str;
        return this;
    }

    public boolean e() {
        return this.q != null;
    }

    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof b)) ? a((b) obj) : false;
    }

    public b f(String str) {
        this.t = str;
        return this;
    }

    public boolean f() {
        return this.r != null;
    }

    public int g() {
        return this.s == null ? 0 : this.s.size();
    }

    public b g(String str) {
        this.u = str;
        return this;
    }

    public b h(String str) {
        this.v = str;
        return this;
    }

    public boolean h() {
        return this.s != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.t != null;
    }

    public boolean j() {
        return this.u != null;
    }

    public boolean k() {
        return this.v != null;
    }

    public void l() {
        if (this.m == null) {
            throw new org.apache.thrift.protocol.g("Required field 'category' was not present! " +
                    "Struct: " + toString());
        } else if (this.n == null) {
            throw new org.apache.thrift.protocol.g("Required field 'uuid' was not present! " +
                    "Struct: " + toString());
        } else if (this.o == null) {
            throw new org.apache.thrift.protocol.g("Required field 'version' was not present! " +
                    "Struct: " + toString());
        } else if (this.p == null) {
            throw new org.apache.thrift.protocol.g("Required field 'network' was not present! " +
                    "Struct: " + toString());
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("HttpApi(");
        stringBuilder.append("category:");
        if (this.m == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.m);
        }
        stringBuilder.append(", ");
        stringBuilder.append("uuid:");
        if (this.n == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.n);
        }
        stringBuilder.append(", ");
        stringBuilder.append("version:");
        if (this.o == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.o);
        }
        stringBuilder.append(", ");
        stringBuilder.append("network:");
        if (this.p == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.p);
        }
        if (e()) {
            stringBuilder.append(", ");
            stringBuilder.append("client_ip:");
            if (this.q == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.q);
            }
        }
        if (f()) {
            stringBuilder.append(", ");
            stringBuilder.append("location:");
            if (this.r == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.r);
            }
        }
        if (h()) {
            stringBuilder.append(", ");
            stringBuilder.append("host_info:");
            if (this.s == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.s);
            }
        }
        if (i()) {
            stringBuilder.append(", ");
            stringBuilder.append("version_type:");
            if (this.t == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.t);
            }
        }
        if (j()) {
            stringBuilder.append(", ");
            stringBuilder.append("app_name:");
            if (this.u == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.u);
            }
        }
        if (k()) {
            stringBuilder.append(", ");
            stringBuilder.append("app_version:");
            if (this.v == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.v);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }
}
