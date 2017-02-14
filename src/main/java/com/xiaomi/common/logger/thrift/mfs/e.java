package com.xiaomi.common.logger.thrift.mfs;

import com.umeng.socialize.common.SocializeConstants;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.thrift.b;
import org.apache.thrift.protocol.c;
import org.apache.thrift.protocol.f;
import org.apache.thrift.protocol.i;
import org.apache.thrift.protocol.k;

public class e implements Serializable, Cloneable, b<e, a> {
    public static final Map<a, org.apache.thrift.meta_data.b> a;
    private static final k b = new k("Location");
    private static final c c = new c("contry", (byte) 11, (short) 1);
    private static final c d = new c("province", (byte) 11, (short) 2);
    private static final c e = new c("city", (byte) 11, (short) 3);
    private static final c f = new c("isp", (byte) 11, (short) 4);
    private String g;
    private String h;
    private String i;
    private String j;

    public enum a {
        CONTRY((short) 1, "contry"),
        PROVINCE((short) 2, "province"),
        CITY((short) 3, "city"),
        ISP((short) 4, "isp");

        private static final Map<String, a> e = null;
        private final short  f;
        private final String g;

        static {
            e = new HashMap();
            Iterator it = EnumSet.allOf(a.class).iterator();
            while (it.hasNext()) {
                a aVar = (a) it.next();
                e.put(aVar.a(), aVar);
            }
        }

        private a(short s, String str) {
            this.f = s;
            this.g = str;
        }

        public String a() {
            return this.g;
        }
    }

    static {
        Map enumMap = new EnumMap(a.class);
        enumMap.put(a.CONTRY, new org.apache.thrift.meta_data.b("contry", (byte) 2, new org
                .apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.PROVINCE, new org.apache.thrift.meta_data.b("province", (byte) 2, new org
                .apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.CITY, new org.apache.thrift.meta_data.b("city", (byte) 2, new org.apache
                .thrift.meta_data.c((byte) 11)));
        enumMap.put(a.ISP, new org.apache.thrift.meta_data.b("isp", (byte) 2, new org.apache
                .thrift.meta_data.c((byte) 11)));
        a = Collections.unmodifiableMap(enumMap);
        org.apache.thrift.meta_data.b.a(e.class, a);
    }

    public e a(String str) {
        this.g = str;
        return this;
    }

    public void a(f fVar) {
        fVar.g();
        while (true) {
            c i = fVar.i();
            if (i.b == (byte) 0) {
                fVar.h();
                e();
                return;
            }
            switch (i.c) {
                case (short) 1:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.g = fVar.w();
                        break;
                    }
                case (short) 2:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.h = fVar.w();
                        break;
                    }
                case (short) 3:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.i = fVar.w();
                        break;
                    }
                case (short) 4:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.j = fVar.w();
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
        return this.g != null;
    }

    public boolean a(e eVar) {
        if (eVar == null) {
            return false;
        }
        boolean a = a();
        boolean a2 = eVar.a();
        if ((a || a2) && (!a || !a2 || !this.g.equals(eVar.g))) {
            return false;
        }
        a = b();
        a2 = eVar.b();
        if ((a || a2) && (!a || !a2 || !this.h.equals(eVar.h))) {
            return false;
        }
        a = c();
        a2 = eVar.c();
        if ((a || a2) && (!a || !a2 || !this.i.equals(eVar.i))) {
            return false;
        }
        a = d();
        a2 = eVar.d();
        return !(a || a2) || (a && a2 && this.j.equals(eVar.j));
    }

    public int b(e eVar) {
        if (!getClass().equals(eVar.getClass())) {
            return getClass().getName().compareTo(eVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(eVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a()) {
            compareTo = org.apache.thrift.c.a(this.g, eVar.g);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(b()).compareTo(Boolean.valueOf(eVar.b()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (b()) {
            compareTo = org.apache.thrift.c.a(this.h, eVar.h);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(c()).compareTo(Boolean.valueOf(eVar.c()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (c()) {
            compareTo = org.apache.thrift.c.a(this.i, eVar.i);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(d()).compareTo(Boolean.valueOf(eVar.d()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (d()) {
            compareTo = org.apache.thrift.c.a(this.j, eVar.j);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }

    public e b(String str) {
        this.h = str;
        return this;
    }

    public void b(f fVar) {
        e();
        fVar.a(b);
        if (this.g != null && a()) {
            fVar.a(c);
            fVar.a(this.g);
            fVar.b();
        }
        if (this.h != null && b()) {
            fVar.a(d);
            fVar.a(this.h);
            fVar.b();
        }
        if (this.i != null && c()) {
            fVar.a(e);
            fVar.a(this.i);
            fVar.b();
        }
        if (this.j != null && d()) {
            fVar.a(f);
            fVar.a(this.j);
            fVar.b();
        }
        fVar.c();
        fVar.a();
    }

    public boolean b() {
        return this.h != null;
    }

    public e c(String str) {
        this.i = str;
        return this;
    }

    public boolean c() {
        return this.i != null;
    }

    public /* synthetic */ int compareTo(Object obj) {
        return b((e) obj);
    }

    public e d(String str) {
        this.j = str;
        return this;
    }

    public boolean d() {
        return this.j != null;
    }

    public void e() {
    }

    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof e)) ? a((e) obj) : false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        Object obj = null;
        StringBuilder stringBuilder = new StringBuilder("Location(");
        Object obj2 = 1;
        if (a()) {
            stringBuilder.append("contry:");
            if (this.g == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.g);
            }
            obj2 = null;
        }
        if (b()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("province:");
            if (this.h == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.h);
            }
            obj2 = null;
        }
        if (c()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("city:");
            if (this.i == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.i);
            }
        } else {
            obj = obj2;
        }
        if (d()) {
            if (obj == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("isp:");
            if (this.j == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.j);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }
}
