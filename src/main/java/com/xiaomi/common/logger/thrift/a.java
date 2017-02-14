package com.xiaomi.common.logger.thrift;

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
import org.apache.thrift.protocol.i;
import org.apache.thrift.protocol.k;

public class a implements Serializable, Cloneable, b<a, a> {
    public static final Map<a, org.apache.thrift.meta_data.b> f;
    private static final k      g = new k("Common");
    private static final c      h = new c("uuid", (byte) 10, (short) 1);
    private static final c      i = new c("time", (byte) 11, (short) 2);
    private static final c      j = new c("clientIp", (byte) 11, (short) 3);
    private static final c      k = new c("serverIp", (byte) 11, (short) 4);
    private static final c      l = new c("serverHost", (byte) 11, (short) 5);
    public               long   a = 0;
    public               String b = "";
    public               String c = "";
    public               String d = "";
    public               String e = "";
    private              BitSet m = new BitSet(1);

    public enum a {
        UUID((short) 1, "uuid"),
        TIME((short) 2, "time"),
        CLIENT_IP((short) 3, "clientIp"),
        SERVER_IP((short) 4, "serverIp"),
        SERVER_HOST((short) 5, "serverHost");

        private static final Map<String, a> f = null;
        private final short  g;
        private final String h;

        static {
            f = new HashMap();
            Iterator it = EnumSet.allOf(a.class).iterator();
            while (it.hasNext()) {
                a aVar = (a) it.next();
                f.put(aVar.a(), aVar);
            }
        }

        private a(short s, String str) {
            this.g = s;
            this.h = str;
        }

        public String a() {
            return this.h;
        }
    }

    static {
        Map enumMap = new EnumMap(a.class);
        enumMap.put(a.UUID, new org.apache.thrift.meta_data.b("uuid", (byte) 2, new org.apache
                .thrift.meta_data.c((byte) 10)));
        enumMap.put(a.TIME, new org.apache.thrift.meta_data.b("time", (byte) 2, new org.apache
                .thrift.meta_data.c((byte) 11)));
        enumMap.put(a.CLIENT_IP, new org.apache.thrift.meta_data.b("clientIp", (byte) 2, new org
                .apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.SERVER_IP, new org.apache.thrift.meta_data.b("serverIp", (byte) 2, new org
                .apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.SERVER_HOST, new org.apache.thrift.meta_data.b("serverHost", (byte) 2, new
                org.apache.thrift.meta_data.c((byte) 11)));
        f = Collections.unmodifiableMap(enumMap);
        org.apache.thrift.meta_data.b.a(a.class, f);
    }

    public void a(f fVar) {
        fVar.g();
        while (true) {
            c i = fVar.i();
            if (i.b == (byte) 0) {
                fVar.h();
                f();
                return;
            }
            switch (i.c) {
                case (short) 1:
                    if (i.b != (byte) 10) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.a = fVar.u();
                    a(true);
                    break;
                case (short) 2:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.b = fVar.w();
                        break;
                    }
                case (short) 3:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.c = fVar.w();
                        break;
                    }
                case (short) 4:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.d = fVar.w();
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
                default:
                    i.a(fVar, i.b);
                    break;
            }
            fVar.j();
        }
    }

    public void a(boolean z) {
        this.m.set(0, z);
    }

    public boolean a() {
        return this.m.get(0);
    }

    public boolean a(a aVar) {
        if (aVar == null) {
            return false;
        }
        boolean a = a();
        boolean a2 = aVar.a();
        if ((a || a2) && (!a || !a2 || this.a != aVar.a)) {
            return false;
        }
        a = b();
        a2 = aVar.b();
        if ((a || a2) && (!a || !a2 || !this.b.equals(aVar.b))) {
            return false;
        }
        a = c();
        a2 = aVar.c();
        if ((a || a2) && (!a || !a2 || !this.c.equals(aVar.c))) {
            return false;
        }
        a = d();
        a2 = aVar.d();
        if ((a || a2) && (!a || !a2 || !this.d.equals(aVar.d))) {
            return false;
        }
        a = e();
        a2 = aVar.e();
        return !(a || a2) || (a && a2 && this.e.equals(aVar.e));
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
            compareTo = org.apache.thrift.c.a(this.a, aVar.a);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(b()).compareTo(Boolean.valueOf(aVar.b()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (b()) {
            compareTo = org.apache.thrift.c.a(this.b, aVar.b);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(c()).compareTo(Boolean.valueOf(aVar.c()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (c()) {
            compareTo = org.apache.thrift.c.a(this.c, aVar.c);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(d()).compareTo(Boolean.valueOf(aVar.d()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (d()) {
            compareTo = org.apache.thrift.c.a(this.d, aVar.d);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(e()).compareTo(Boolean.valueOf(aVar.e()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (e()) {
            compareTo = org.apache.thrift.c.a(this.e, aVar.e);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }

    public void b(f fVar) {
        f();
        fVar.a(g);
        if (a()) {
            fVar.a(h);
            fVar.a(this.a);
            fVar.b();
        }
        if (this.b != null && b()) {
            fVar.a(i);
            fVar.a(this.b);
            fVar.b();
        }
        if (this.c != null && c()) {
            fVar.a(j);
            fVar.a(this.c);
            fVar.b();
        }
        if (this.d != null && d()) {
            fVar.a(k);
            fVar.a(this.d);
            fVar.b();
        }
        if (this.e != null && e()) {
            fVar.a(l);
            fVar.a(this.e);
            fVar.b();
        }
        fVar.c();
        fVar.a();
    }

    public boolean b() {
        return this.b != null;
    }

    public boolean c() {
        return this.c != null;
    }

    public /* synthetic */ int compareTo(Object obj) {
        return b((a) obj);
    }

    public boolean d() {
        return this.d != null;
    }

    public boolean e() {
        return this.e != null;
    }

    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof a)) ? a((a) obj) : false;
    }

    public void f() {
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        Object obj = null;
        StringBuilder stringBuilder = new StringBuilder("Common(");
        Object obj2 = 1;
        if (a()) {
            stringBuilder.append("uuid:");
            stringBuilder.append(this.a);
            obj2 = null;
        }
        if (b()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("time:");
            if (this.b == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.b);
            }
            obj2 = null;
        }
        if (c()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("clientIp:");
            if (this.c == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.c);
            }
            obj2 = null;
        }
        if (d()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("serverIp:");
            if (this.d == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.d);
            }
        } else {
            obj = obj2;
        }
        if (e()) {
            if (obj == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("serverHost:");
            if (this.e == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.e);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }
}
