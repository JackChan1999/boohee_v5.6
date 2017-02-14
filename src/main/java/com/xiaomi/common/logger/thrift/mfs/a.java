package com.xiaomi.common.logger.thrift.mfs;

import com.umeng.socialize.common.SocializeConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.thrift.b;
import org.apache.thrift.meta_data.d;
import org.apache.thrift.meta_data.g;
import org.apache.thrift.protocol.c;
import org.apache.thrift.protocol.f;
import org.apache.thrift.protocol.i;
import org.apache.thrift.protocol.k;

import u.aly.df;

public class a implements Serializable, Cloneable, b<a, a> {
    public static final Map<a, org.apache.thrift.meta_data.b> a;
    private static final k b = new k("HostInfo");
    private static final c c = new c(com.alipay.sdk.cons.c.f, (byte) 11, (short) 1);
    private static final c d = new c("land_node_info", df.m, (short) 2);
    private String  e;
    private List<d> f;

    public enum a {
        HOST((short) 1, com.alipay.sdk.cons.c.f),
        LAND_NODE_INFO((short) 2, "land_node_info");

        private static final Map<String, a> c = null;
        private final short  d;
        private final String e;

        static {
            c = new HashMap();
            Iterator it = EnumSet.allOf(a.class).iterator();
            while (it.hasNext()) {
                a aVar = (a) it.next();
                c.put(aVar.a(), aVar);
            }
        }

        private a(short s, String str) {
            this.d = s;
            this.e = str;
        }

        public String a() {
            return this.e;
        }
    }

    static {
        Map enumMap = new EnumMap(a.class);
        enumMap.put(a.HOST, new org.apache.thrift.meta_data.b(com.alipay.sdk.cons.c.f, (byte) 1,
                new org.apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.LAND_NODE_INFO, new org.apache.thrift.meta_data.b("land_node_info", (byte)
                1, new d(df.m, new g((byte) 12, d.class))));
        a = Collections.unmodifiableMap(enumMap);
        org.apache.thrift.meta_data.b.a(a.class, a);
    }

    public a a(String str) {
        this.e = str;
        return this;
    }

    public a a(List<d> list) {
        this.f = list;
        return this;
    }

    public void a(f fVar) {
        fVar.g();
        while (true) {
            c i = fVar.i();
            if (i.b == (byte) 0) {
                fVar.h();
                c();
                return;
            }
            switch (i.c) {
                case (short) 1:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.e = fVar.w();
                        break;
                    }
                case (short) 2:
                    if (i.b != df.m) {
                        i.a(fVar, i.b);
                        break;
                    }
                    org.apache.thrift.protocol.d m = fVar.m();
                    this.f = new ArrayList(m.b);
                    for (int i2 = 0; i2 < m.b; i2++) {
                        d dVar = new d();
                        dVar.a(fVar);
                        this.f.add(dVar);
                    }
                    fVar.n();
                    break;
                default:
                    i.a(fVar, i.b);
                    break;
            }
            fVar.j();
        }
    }

    public boolean a() {
        return this.e != null;
    }

    public boolean a(a aVar) {
        if (aVar == null) {
            return false;
        }
        boolean a = a();
        boolean a2 = aVar.a();
        if ((a || a2) && (!a || !a2 || !this.e.equals(aVar.e))) {
            return false;
        }
        a = b();
        a2 = aVar.b();
        return !(a || a2) || (a && a2 && this.f.equals(aVar.f));
    }

    public int b(a aVar) {
        if (!getClass().equals(aVar.getClass())) {
            return getClass().getName().compareTo(aVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(aVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a()) {
            compareTo = org.apache.thrift.c.a(this.e, aVar.e);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(b()).compareTo(Boolean.valueOf(aVar.b()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (b()) {
            compareTo = org.apache.thrift.c.a(this.f, aVar.f);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }

    public void b(f fVar) {
        c();
        fVar.a(b);
        if (this.e != null) {
            fVar.a(c);
            fVar.a(this.e);
            fVar.b();
        }
        if (this.f != null) {
            fVar.a(d);
            fVar.a(new org.apache.thrift.protocol.d((byte) 12, this.f.size()));
            for (d b : this.f) {
                b.b(fVar);
            }
            fVar.e();
            fVar.b();
        }
        fVar.c();
        fVar.a();
    }

    public boolean b() {
        return this.f != null;
    }

    public void c() {
        if (this.e == null) {
            throw new org.apache.thrift.protocol.g("Required field 'host' was not present! " +
                    "Struct: " + toString());
        } else if (this.f == null) {
            throw new org.apache.thrift.protocol.g("Required field 'land_node_info' was not " +
                    "present! Struct: " + toString());
        }
    }

    public /* synthetic */ int compareTo(Object obj) {
        return b((a) obj);
    }

    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof a)) ? a((a) obj) : false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("HostInfo(");
        stringBuilder.append("host:");
        if (this.e == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.e);
        }
        stringBuilder.append(", ");
        stringBuilder.append("land_node_info:");
        if (this.f == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.f);
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }
}
