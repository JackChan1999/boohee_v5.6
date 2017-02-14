package com.xiaomi.common.logger.thrift.mfs;

import com.umeng.socialize.common.SocializeConstants;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.thrift.b;
import org.apache.thrift.protocol.c;
import org.apache.thrift.protocol.f;
import org.apache.thrift.protocol.g;
import org.apache.thrift.protocol.i;
import org.apache.thrift.protocol.k;

public class h implements Serializable, Cloneable, b<h, a> {
    public static final Map<a, org.apache.thrift.meta_data.b> a;
    private static final k b = new k("PassportLandNodeInfo");
    private static final c c = new c("ip", (byte) 8, (short) 1);
    private static final c d = new c("eid", (byte) 8, (short) 2);
    private static final c e = new c("rt", (byte) 8, (short) 3);
    private int f;
    private int g;
    private int h;
    private BitSet i = new BitSet(3);

    public enum a {
        IP((short) 1, "ip"),
        EID((short) 2, "eid"),
        RT((short) 3, "rt");

        private static final Map<String, a> d = null;
        private final short  e;
        private final String f;

        static {
            d = new HashMap();
            Iterator it = EnumSet.allOf(a.class).iterator();
            while (it.hasNext()) {
                a aVar = (a) it.next();
                d.put(aVar.a(), aVar);
            }
        }

        private a(short s, String str) {
            this.e = s;
            this.f = str;
        }

        public String a() {
            return this.f;
        }
    }

    static {
        Map enumMap = new EnumMap(a.class);
        enumMap.put(a.IP, new org.apache.thrift.meta_data.b("ip", (byte) 1, new org.apache.thrift
                .meta_data.c((byte) 8)));
        enumMap.put(a.EID, new org.apache.thrift.meta_data.b("eid", (byte) 1, new org.apache
                .thrift.meta_data.c((byte) 8)));
        enumMap.put(a.RT, new org.apache.thrift.meta_data.b("rt", (byte) 1, new org.apache.thrift
                .meta_data.c((byte) 8)));
        a = Collections.unmodifiableMap(enumMap);
        org.apache.thrift.meta_data.b.a(h.class, a);
    }

    public void a(f fVar) {
        fVar.g();
        while (true) {
            c i = fVar.i();
            if (i.b == (byte) 0) {
                fVar.h();
                if (!a()) {
                    throw new g("Required field 'ip' was not found in serialized data! Struct: "
                            + toString());
                } else if (!b()) {
                    throw new g("Required field 'eid' was not found in serialized data! Struct: "
                            + toString());
                } else if (c()) {
                    d();
                    return;
                } else {
                    throw new g("Required field 'rt' was not found in serialized data! Struct: "
                            + toString());
                }
            }
            switch (i.c) {
                case (short) 1:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.f = fVar.t();
                    a(true);
                    break;
                case (short) 2:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.g = fVar.t();
                    b(true);
                    break;
                case (short) 3:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.h = fVar.t();
                    c(true);
                    break;
                default:
                    i.a(fVar, i.b);
                    break;
            }
            fVar.j();
        }
    }

    public void a(boolean z) {
        this.i.set(0, z);
    }

    public boolean a() {
        return this.i.get(0);
    }

    public boolean a(h hVar) {
        return hVar != null && this.f == hVar.f && this.g == hVar.g && this.h == hVar.h;
    }

    public int b(h hVar) {
        if (!getClass().equals(hVar.getClass())) {
            return getClass().getName().compareTo(hVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a()) {
            compareTo = org.apache.thrift.c.a(this.f, hVar.f);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hVar.b()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (b()) {
            compareTo = org.apache.thrift.c.a(this.g, hVar.g);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hVar.c()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (c()) {
            compareTo = org.apache.thrift.c.a(this.h, hVar.h);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }

    public void b(f fVar) {
        d();
        fVar.a(b);
        fVar.a(c);
        fVar.a(this.f);
        fVar.b();
        fVar.a(d);
        fVar.a(this.g);
        fVar.b();
        fVar.a(e);
        fVar.a(this.h);
        fVar.b();
        fVar.c();
        fVar.a();
    }

    public void b(boolean z) {
        this.i.set(1, z);
    }

    public boolean b() {
        return this.i.get(1);
    }

    public void c(boolean z) {
        this.i.set(2, z);
    }

    public boolean c() {
        return this.i.get(2);
    }

    public /* synthetic */ int compareTo(Object obj) {
        return b((h) obj);
    }

    public void d() {
    }

    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof h)) ? a((h) obj) : false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("PassportLandNodeInfo(");
        stringBuilder.append("ip:");
        stringBuilder.append(this.f);
        stringBuilder.append(", ");
        stringBuilder.append("eid:");
        stringBuilder.append(this.g);
        stringBuilder.append(", ");
        stringBuilder.append("rt:");
        stringBuilder.append(this.h);
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }
}
