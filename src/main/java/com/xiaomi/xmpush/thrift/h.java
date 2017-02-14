package com.xiaomi.xmpush.thrift;

import com.umeng.socialize.common.SocializeConstants;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.thrift.b;
import org.apache.thrift.meta_data.g;
import org.apache.thrift.protocol.c;
import org.apache.thrift.protocol.f;
import org.apache.thrift.protocol.i;
import org.apache.thrift.protocol.k;

import u.aly.df;

public class h implements Serializable, Cloneable, b<h, a> {
    public static final Map<a, org.apache.thrift.meta_data.b> i;
    private static final k j = new k("XmPushActionContainer");
    private static final c k = new c("action", (byte) 8, (short) 1);
    private static final c l = new c("encryptAction", (byte) 2, (short) 2);
    private static final c m = new c("isRequest", (byte) 2, (short) 3);
    private static final c n = new c("pushAction", (byte) 11, (short) 4);
    private static final c o = new c("appid", (byte) 11, (short) 5);
    private static final c p = new c("packageName", (byte) 11, (short) 6);
    private static final c q = new c("target", (byte) 12, (short) 7);
    private static final c r = new c("metaInfo", (byte) 12, (short) 8);
    public a a;
    public boolean b = true;
    public boolean c = true;
    public ByteBuffer d;
    public String     e;
    public String     f;
    public d          g;
    public c          h;
    private BitSet s = new BitSet(2);

    public enum a {
        ACTION((short) 1, "action"),
        ENCRYPT_ACTION((short) 2, "encryptAction"),
        IS_REQUEST((short) 3, "isRequest"),
        PUSH_ACTION((short) 4, "pushAction"),
        APPID((short) 5, "appid"),
        PACKAGE_NAME((short) 6, "packageName"),
        TARGET((short) 7, "target"),
        META_INFO((short) 8, "metaInfo");

        private static final Map<String, a> i = null;
        private final short  j;
        private final String k;

        static {
            i = new HashMap();
            Iterator it = EnumSet.allOf(a.class).iterator();
            while (it.hasNext()) {
                a aVar = (a) it.next();
                i.put(aVar.a(), aVar);
            }
        }

        private a(short s, String str) {
            this.j = s;
            this.k = str;
        }

        public String a() {
            return this.k;
        }
    }

    static {
        Map enumMap = new EnumMap(a.class);
        enumMap.put(a.ACTION, new org.apache.thrift.meta_data.b("action", (byte) 1, new org
                .apache.thrift.meta_data.a(df.n, a.class)));
        enumMap.put(a.ENCRYPT_ACTION, new org.apache.thrift.meta_data.b("encryptAction", (byte)
                1, new org.apache.thrift.meta_data.c((byte) 2)));
        enumMap.put(a.IS_REQUEST, new org.apache.thrift.meta_data.b("isRequest", (byte) 1, new
                org.apache.thrift.meta_data.c((byte) 2)));
        enumMap.put(a.PUSH_ACTION, new org.apache.thrift.meta_data.b("pushAction", (byte) 1, new
                org.apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.APPID, new org.apache.thrift.meta_data.b("appid", (byte) 2, new org.apache
                .thrift.meta_data.c((byte) 11)));
        enumMap.put(a.PACKAGE_NAME, new org.apache.thrift.meta_data.b("packageName", (byte) 2,
                new org.apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.TARGET, new org.apache.thrift.meta_data.b("target", (byte) 1, new g((byte)
                12, d.class)));
        enumMap.put(a.META_INFO, new org.apache.thrift.meta_data.b("metaInfo", (byte) 2, new g(
                (byte) 12, c.class)));
        i = Collections.unmodifiableMap(enumMap);
        org.apache.thrift.meta_data.b.a(h.class, i);
    }

    public a a() {
        return this.a;
    }

    public h a(a aVar) {
        this.a = aVar;
        return this;
    }

    public h a(c cVar) {
        this.h = cVar;
        return this;
    }

    public h a(d dVar) {
        this.g = dVar;
        return this;
    }

    public h a(String str) {
        this.e = str;
        return this;
    }

    public h a(ByteBuffer byteBuffer) {
        this.d = byteBuffer;
        return this;
    }

    public h a(boolean z) {
        this.b = z;
        b(true);
        return this;
    }

    public void a(f fVar) {
        fVar.g();
        while (true) {
            c i = fVar.i();
            if (i.b == (byte) 0) {
                fVar.h();
                if (!d()) {
                    throw new org.apache.thrift.protocol.g("Required field 'encryptAction' was " +
                            "not found in serialized data! Struct: " + toString());
                } else if (e()) {
                    o();
                    return;
                } else {
                    throw new org.apache.thrift.protocol.g("Required field 'isRequest' was not " +
                            "found in serialized data! Struct: " + toString());
                }
            }
            switch (i.c) {
                case (short) 1:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.a = a.a(fVar.t());
                        break;
                    }
                case (short) 2:
                    if (i.b != (byte) 2) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.b = fVar.q();
                    b(true);
                    break;
                case (short) 3:
                    if (i.b != (byte) 2) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.c = fVar.q();
                    d(true);
                    break;
                case (short) 4:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.d = fVar.x();
                        break;
                    }
                case (short) 5:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.e = fVar.w();
                        break;
                    }
                case (short) 6:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.f = fVar.w();
                        break;
                    }
                case (short) 7:
                    if (i.b != (byte) 12) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.g = new d();
                    this.g.a(fVar);
                    break;
                case (short) 8:
                    if (i.b != (byte) 12) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.h = new c();
                    this.h.a(fVar);
                    break;
                default:
                    i.a(fVar, i.b);
                    break;
            }
            fVar.j();
        }
    }

    public boolean a(h hVar) {
        if (hVar == null) {
            return false;
        }
        boolean b = b();
        boolean b2 = hVar.b();
        if (((b || b2) && (!b || !b2 || !this.a.equals(hVar.a))) || this.b != hVar.b || this.c !=
                hVar.c) {
            return false;
        }
        b = g();
        b2 = hVar.g();
        if ((b || b2) && (!b || !b2 || !this.d.equals(hVar.d))) {
            return false;
        }
        b = i();
        b2 = hVar.i();
        if ((b || b2) && (!b || !b2 || !this.e.equals(hVar.e))) {
            return false;
        }
        b = k();
        b2 = hVar.k();
        if ((b || b2) && (!b || !b2 || !this.f.equals(hVar.f))) {
            return false;
        }
        b = l();
        b2 = hVar.l();
        if ((b || b2) && (!b || !b2 || !this.g.a(hVar.g))) {
            return false;
        }
        b = n();
        b2 = hVar.n();
        return !(b || b2) || (b && b2 && this.h.a(hVar.h));
    }

    public int b(h hVar) {
        if (!getClass().equals(hVar.getClass())) {
            return getClass().getName().compareTo(hVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hVar.b()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (b()) {
            compareTo = org.apache.thrift.c.a(this.a, hVar.a);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hVar.d()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (d()) {
            compareTo = org.apache.thrift.c.a(this.b, hVar.b);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hVar.e()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (e()) {
            compareTo = org.apache.thrift.c.a(this.c, hVar.c);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(g()).compareTo(Boolean.valueOf(hVar.g()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (g()) {
            compareTo = org.apache.thrift.c.a(this.d, hVar.d);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(i()).compareTo(Boolean.valueOf(hVar.i()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (i()) {
            compareTo = org.apache.thrift.c.a(this.e, hVar.e);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(k()).compareTo(Boolean.valueOf(hVar.k()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (k()) {
            compareTo = org.apache.thrift.c.a(this.f, hVar.f);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(l()).compareTo(Boolean.valueOf(hVar.l()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (l()) {
            compareTo = org.apache.thrift.c.a(this.g, hVar.g);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(n()).compareTo(Boolean.valueOf(hVar.n()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (n()) {
            compareTo = org.apache.thrift.c.a(this.h, hVar.h);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }

    public h b(String str) {
        this.f = str;
        return this;
    }

    public void b(f fVar) {
        o();
        fVar.a(j);
        if (this.a != null) {
            fVar.a(k);
            fVar.a(this.a.a());
            fVar.b();
        }
        fVar.a(l);
        fVar.a(this.b);
        fVar.b();
        fVar.a(m);
        fVar.a(this.c);
        fVar.b();
        if (this.d != null) {
            fVar.a(n);
            fVar.a(this.d);
            fVar.b();
        }
        if (this.e != null && i()) {
            fVar.a(o);
            fVar.a(this.e);
            fVar.b();
        }
        if (this.f != null && k()) {
            fVar.a(p);
            fVar.a(this.f);
            fVar.b();
        }
        if (this.g != null) {
            fVar.a(q);
            this.g.b(fVar);
            fVar.b();
        }
        if (this.h != null && n()) {
            fVar.a(r);
            this.h.b(fVar);
            fVar.b();
        }
        fVar.c();
        fVar.a();
    }

    public void b(boolean z) {
        this.s.set(0, z);
    }

    public boolean b() {
        return this.a != null;
    }

    public h c(boolean z) {
        this.c = z;
        d(true);
        return this;
    }

    public boolean c() {
        return this.b;
    }

    public /* synthetic */ int compareTo(Object obj) {
        return b((h) obj);
    }

    public void d(boolean z) {
        this.s.set(1, z);
    }

    public boolean d() {
        return this.s.get(0);
    }

    public boolean e() {
        return this.s.get(1);
    }

    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof h)) ? a((h) obj) : false;
    }

    public byte[] f() {
        a(org.apache.thrift.c.c(this.d));
        return this.d.array();
    }

    public boolean g() {
        return this.d != null;
    }

    public String h() {
        return this.e;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.e != null;
    }

    public String j() {
        return this.f;
    }

    public boolean k() {
        return this.f != null;
    }

    public boolean l() {
        return this.g != null;
    }

    public c m() {
        return this.h;
    }

    public boolean n() {
        return this.h != null;
    }

    public void o() {
        if (this.a == null) {
            throw new org.apache.thrift.protocol.g("Required field 'action' was not present! " +
                    "Struct: " + toString());
        } else if (this.d == null) {
            throw new org.apache.thrift.protocol.g("Required field 'pushAction' was not present! " +
                    "Struct: " + toString());
        } else if (this.g == null) {
            throw new org.apache.thrift.protocol.g("Required field 'target' was not present! " +
                    "Struct: " + toString());
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("XmPushActionContainer(");
        stringBuilder.append("action:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("encryptAction:");
        stringBuilder.append(this.b);
        stringBuilder.append(", ");
        stringBuilder.append("isRequest:");
        stringBuilder.append(this.c);
        stringBuilder.append(", ");
        stringBuilder.append("pushAction:");
        if (this.d == null) {
            stringBuilder.append("null");
        } else {
            org.apache.thrift.c.a(this.d, stringBuilder);
        }
        if (i()) {
            stringBuilder.append(", ");
            stringBuilder.append("appid:");
            if (this.e == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.e);
            }
        }
        if (k()) {
            stringBuilder.append(", ");
            stringBuilder.append("packageName:");
            if (this.f == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.f);
            }
        }
        stringBuilder.append(", ");
        stringBuilder.append("target:");
        if (this.g == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.g);
        }
        if (n()) {
            stringBuilder.append(", ");
            stringBuilder.append("metaInfo:");
            if (this.h == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.h);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }
}
