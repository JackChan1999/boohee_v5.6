package com.xiaomi.xmpush.thrift;

import com.umeng.socialize.common.SocializeConstants;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.thrift.b;
import org.apache.thrift.meta_data.e;
import org.apache.thrift.protocol.f;
import org.apache.thrift.protocol.g;
import org.apache.thrift.protocol.i;
import org.apache.thrift.protocol.k;

public class c implements Serializable, Cloneable, b<c, a> {
    public static final Map<a, org.apache.thrift.meta_data.b> m;
    private static final k                            n = new k("PushMetaInfo");
    private static final org.apache.thrift.protocol.c o = new org.apache.thrift.protocol.c("id",
            (byte) 11, (short) 1);
    private static final org.apache.thrift.protocol.c p = new org.apache.thrift.protocol.c
            ("messageTs", (byte) 10, (short) 2);
    private static final org.apache.thrift.protocol.c q = new org.apache.thrift.protocol.c
            ("topic", (byte) 11, (short) 3);
    private static final org.apache.thrift.protocol.c r = new org.apache.thrift.protocol.c
            ("title", (byte) 11, (short) 4);
    private static final org.apache.thrift.protocol.c s = new org.apache.thrift.protocol.c
            ("description", (byte) 11, (short) 5);
    private static final org.apache.thrift.protocol.c t = new org.apache.thrift.protocol.c
            ("notifyType", (byte) 8, (short) 6);
    private static final org.apache.thrift.protocol.c u = new org.apache.thrift.protocol.c("url",
            (byte) 11, (short) 7);
    private static final org.apache.thrift.protocol.c v = new org.apache.thrift.protocol.c
            ("passThrough", (byte) 8, (short) 8);
    private static final org.apache.thrift.protocol.c w = new org.apache.thrift.protocol.c
            ("notifyId", (byte) 8, (short) 9);
    private static final org.apache.thrift.protocol.c x = new org.apache.thrift.protocol.c
            ("extra", (byte) 13, (short) 10);
    private static final org.apache.thrift.protocol.c y = new org.apache.thrift.protocol.c
            ("internal", (byte) 13, (short) 11);
    private static final org.apache.thrift.protocol.c z = new org.apache.thrift.protocol.c
            ("ignoreRegInfo", (byte) 2, (short) 12);
    private BitSet              A;
    public  String              a;
    public  long                b;
    public  String              c;
    public  String              d;
    public  String              e;
    public  int                 f;
    public  String              g;
    public  int                 h;
    public  int                 i;
    public  Map<String, String> j;
    public  Map<String, String> k;
    public  boolean             l;

    static {
        Map enumMap = new EnumMap(a.class);
        enumMap.put(a.a, new org.apache.thrift.meta_data.b("id", (byte) 1, new org.apache.thrift
                .meta_data.c((byte) 11)));
        enumMap.put(a.b, new org.apache.thrift.meta_data.b("messageTs", (byte) 1, new org.apache
                .thrift.meta_data.c((byte) 10)));
        enumMap.put(a.c, new org.apache.thrift.meta_data.b("topic", (byte) 2, new org.apache
                .thrift.meta_data.c((byte) 11)));
        enumMap.put(a.d, new org.apache.thrift.meta_data.b("title", (byte) 2, new org.apache
                .thrift.meta_data.c((byte) 11)));
        enumMap.put(a.e, new org.apache.thrift.meta_data.b("description", (byte) 2, new org
                .apache.thrift.meta_data.c((byte) 11)));
        enumMap.put(a.f, new org.apache.thrift.meta_data.b("notifyType", (byte) 2, new org.apache
                .thrift.meta_data.c((byte) 8)));
        enumMap.put(a.g, new org.apache.thrift.meta_data.b("url", (byte) 2, new org.apache.thrift
                .meta_data.c((byte) 11)));
        enumMap.put(a.h, new org.apache.thrift.meta_data.b("passThrough", (byte) 2, new org
                .apache.thrift.meta_data.c((byte) 8)));
        enumMap.put(a.i, new org.apache.thrift.meta_data.b("notifyId", (byte) 2, new org.apache
                .thrift.meta_data.c((byte) 8)));
        enumMap.put(a.j, new org.apache.thrift.meta_data.b("extra", (byte) 2, new e((byte) 13,
                new org.apache.thrift.meta_data.c((byte) 11), new org.apache.thrift.meta_data.c(
                (byte) 11))));
        enumMap.put(a.k, new org.apache.thrift.meta_data.b("internal", (byte) 2, new e((byte) 13,
                new org.apache.thrift.meta_data.c((byte) 11), new org.apache.thrift.meta_data.c(
                (byte) 11))));
        enumMap.put(a.l, new org.apache.thrift.meta_data.b("ignoreRegInfo", (byte) 2, new org
                .apache.thrift.meta_data.c((byte) 2)));
        m = Collections.unmodifiableMap(enumMap);
        org.apache.thrift.meta_data.b.a(c.class, m);
    }

    public c() {
        this.A = new BitSet(5);
        this.l = false;
    }

    public c(c cVar) {
        Map hashMap;
        this.A = new BitSet(5);
        this.A.clear();
        this.A.or(cVar.A);
        if (cVar.c()) {
            this.a = cVar.a;
        }
        this.b = cVar.b;
        if (cVar.g()) {
            this.c = cVar.c;
        }
        if (cVar.i()) {
            this.d = cVar.d;
        }
        if (cVar.k()) {
            this.e = cVar.e;
        }
        this.f = cVar.f;
        if (cVar.n()) {
            this.g = cVar.g;
        }
        this.h = cVar.h;
        this.i = cVar.i;
        if (cVar.t()) {
            hashMap = new HashMap();
            for (Entry entry : cVar.j.entrySet()) {
                hashMap.put((String) entry.getKey(), (String) entry.getValue());
            }
            this.j = hashMap;
        }
        if (cVar.u()) {
            hashMap = new HashMap();
            for (Entry entry2 : cVar.k.entrySet()) {
                hashMap.put((String) entry2.getKey(), (String) entry2.getValue());
            }
            this.k = hashMap;
        }
        this.l = cVar.l;
    }

    public c a() {
        return new c(this);
    }

    public c a(int i) {
        this.f = i;
        b(true);
        return this;
    }

    public c a(String str) {
        this.a = str;
        return this;
    }

    public c a(Map<String, String> map) {
        this.j = map;
        return this;
    }

    public void a(String str, String str2) {
        if (this.j == null) {
            this.j = new HashMap();
        }
        this.j.put(str, str2);
    }

    public void a(f fVar) {
        fVar.g();
        while (true) {
            org.apache.thrift.protocol.c i = fVar.i();
            if (i.b == (byte) 0) {
                fVar.h();
                if (e()) {
                    x();
                    return;
                }
                throw new g("Required field 'messageTs' was not found in serialized data! Struct:" +
                        " " + toString());
            }
            org.apache.thrift.protocol.e k;
            int i2;
            switch (i.c) {
                case (short) 1:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.a = fVar.w();
                        break;
                    }
                case (short) 2:
                    if (i.b != (byte) 10) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.b = fVar.u();
                    a(true);
                    break;
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
                case (short) 6:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.f = fVar.t();
                    b(true);
                    break;
                case (short) 7:
                    if (i.b != (byte) 11) {
                        i.a(fVar, i.b);
                        break;
                    } else {
                        this.g = fVar.w();
                        break;
                    }
                case (short) 8:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.h = fVar.t();
                    c(true);
                    break;
                case (short) 9:
                    if (i.b != (byte) 8) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.i = fVar.t();
                    d(true);
                    break;
                case (short) 10:
                    if (i.b != (byte) 13) {
                        i.a(fVar, i.b);
                        break;
                    }
                    k = fVar.k();
                    this.j = new HashMap(k.c * 2);
                    for (i2 = 0; i2 < k.c; i2++) {
                        this.j.put(fVar.w(), fVar.w());
                    }
                    fVar.l();
                    break;
                case (short) 11:
                    if (i.b != (byte) 13) {
                        i.a(fVar, i.b);
                        break;
                    }
                    k = fVar.k();
                    this.k = new HashMap(k.c * 2);
                    for (i2 = 0; i2 < k.c; i2++) {
                        this.k.put(fVar.w(), fVar.w());
                    }
                    fVar.l();
                    break;
                case (short) 12:
                    if (i.b != (byte) 2) {
                        i.a(fVar, i.b);
                        break;
                    }
                    this.l = fVar.q();
                    e(true);
                    break;
                default:
                    i.a(fVar, i.b);
                    break;
            }
            fVar.j();
        }
    }

    public void a(boolean z) {
        this.A.set(0, z);
    }

    public boolean a(c cVar) {
        if (cVar == null) {
            return false;
        }
        boolean c = c();
        boolean c2 = cVar.c();
        if (((c || c2) && (!c || !c2 || !this.a.equals(cVar.a))) || this.b != cVar.b) {
            return false;
        }
        c = g();
        c2 = cVar.g();
        if ((c || c2) && (!c || !c2 || !this.c.equals(cVar.c))) {
            return false;
        }
        c = i();
        c2 = cVar.i();
        if ((c || c2) && (!c || !c2 || !this.d.equals(cVar.d))) {
            return false;
        }
        c = k();
        c2 = cVar.k();
        if ((c || c2) && (!c || !c2 || !this.e.equals(cVar.e))) {
            return false;
        }
        c = m();
        c2 = cVar.m();
        if ((c || c2) && (!c || !c2 || this.f != cVar.f)) {
            return false;
        }
        c = n();
        c2 = cVar.n();
        if ((c || c2) && (!c || !c2 || !this.g.equals(cVar.g))) {
            return false;
        }
        c = p();
        c2 = cVar.p();
        if ((c || c2) && (!c || !c2 || this.h != cVar.h)) {
            return false;
        }
        c = r();
        c2 = cVar.r();
        if ((c || c2) && (!c || !c2 || this.i != cVar.i)) {
            return false;
        }
        c = t();
        c2 = cVar.t();
        if ((c || c2) && (!c || !c2 || !this.j.equals(cVar.j))) {
            return false;
        }
        c = u();
        c2 = cVar.u();
        if ((c || c2) && (!c || !c2 || !this.k.equals(cVar.k))) {
            return false;
        }
        c = w();
        c2 = cVar.w();
        return !(c || c2) || (c && c2 && this.l == cVar.l);
    }

    public int b(c cVar) {
        if (!getClass().equals(cVar.getClass())) {
            return getClass().getName().compareTo(cVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(c()).compareTo(Boolean.valueOf(cVar.c()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (c()) {
            compareTo = org.apache.thrift.c.a(this.a, cVar.a);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(e()).compareTo(Boolean.valueOf(cVar.e()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (e()) {
            compareTo = org.apache.thrift.c.a(this.b, cVar.b);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(g()).compareTo(Boolean.valueOf(cVar.g()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (g()) {
            compareTo = org.apache.thrift.c.a(this.c, cVar.c);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(i()).compareTo(Boolean.valueOf(cVar.i()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (i()) {
            compareTo = org.apache.thrift.c.a(this.d, cVar.d);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(k()).compareTo(Boolean.valueOf(cVar.k()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (k()) {
            compareTo = org.apache.thrift.c.a(this.e, cVar.e);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(m()).compareTo(Boolean.valueOf(cVar.m()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m()) {
            compareTo = org.apache.thrift.c.a(this.f, cVar.f);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(n()).compareTo(Boolean.valueOf(cVar.n()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (n()) {
            compareTo = org.apache.thrift.c.a(this.g, cVar.g);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(p()).compareTo(Boolean.valueOf(cVar.p()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (p()) {
            compareTo = org.apache.thrift.c.a(this.h, cVar.h);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(r()).compareTo(Boolean.valueOf(cVar.r()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (r()) {
            compareTo = org.apache.thrift.c.a(this.i, cVar.i);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(t()).compareTo(Boolean.valueOf(cVar.t()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (t()) {
            compareTo = org.apache.thrift.c.a(this.j, cVar.j);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(u()).compareTo(Boolean.valueOf(cVar.u()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (u()) {
            compareTo = org.apache.thrift.c.a(this.k, cVar.k);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        compareTo = Boolean.valueOf(w()).compareTo(Boolean.valueOf(cVar.w()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (w()) {
            compareTo = org.apache.thrift.c.a(this.l, cVar.l);
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }

    public c b(int i) {
        this.h = i;
        c(true);
        return this;
    }

    public c b(String str) {
        this.c = str;
        return this;
    }

    public String b() {
        return this.a;
    }

    public void b(f fVar) {
        x();
        fVar.a(n);
        if (this.a != null) {
            fVar.a(o);
            fVar.a(this.a);
            fVar.b();
        }
        fVar.a(p);
        fVar.a(this.b);
        fVar.b();
        if (this.c != null && g()) {
            fVar.a(q);
            fVar.a(this.c);
            fVar.b();
        }
        if (this.d != null && i()) {
            fVar.a(r);
            fVar.a(this.d);
            fVar.b();
        }
        if (this.e != null && k()) {
            fVar.a(s);
            fVar.a(this.e);
            fVar.b();
        }
        if (m()) {
            fVar.a(t);
            fVar.a(this.f);
            fVar.b();
        }
        if (this.g != null && n()) {
            fVar.a(u);
            fVar.a(this.g);
            fVar.b();
        }
        if (p()) {
            fVar.a(v);
            fVar.a(this.h);
            fVar.b();
        }
        if (r()) {
            fVar.a(w);
            fVar.a(this.i);
            fVar.b();
        }
        if (this.j != null && t()) {
            fVar.a(x);
            fVar.a(new org.apache.thrift.protocol.e((byte) 11, (byte) 11, this.j.size()));
            for (Entry entry : this.j.entrySet()) {
                fVar.a((String) entry.getKey());
                fVar.a((String) entry.getValue());
            }
            fVar.d();
            fVar.b();
        }
        if (this.k != null && u()) {
            fVar.a(y);
            fVar.a(new org.apache.thrift.protocol.e((byte) 11, (byte) 11, this.k.size()));
            for (Entry entry2 : this.k.entrySet()) {
                fVar.a((String) entry2.getKey());
                fVar.a((String) entry2.getValue());
            }
            fVar.d();
            fVar.b();
        }
        if (w()) {
            fVar.a(z);
            fVar.a(this.l);
            fVar.b();
        }
        fVar.c();
        fVar.a();
    }

    public void b(boolean z) {
        this.A.set(1, z);
    }

    public c c(int i) {
        this.i = i;
        d(true);
        return this;
    }

    public c c(String str) {
        this.d = str;
        return this;
    }

    public void c(boolean z) {
        this.A.set(2, z);
    }

    public boolean c() {
        return this.a != null;
    }

    public /* synthetic */ int compareTo(Object obj) {
        return b((c) obj);
    }

    public long d() {
        return this.b;
    }

    public c d(String str) {
        this.e = str;
        return this;
    }

    public void d(boolean z) {
        this.A.set(3, z);
    }

    public void e(boolean z) {
        this.A.set(4, z);
    }

    public boolean e() {
        return this.A.get(0);
    }

    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof c)) ? a((c) obj) : false;
    }

    public String f() {
        return this.c;
    }

    public boolean g() {
        return this.c != null;
    }

    public String h() {
        return this.d;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.d != null;
    }

    public String j() {
        return this.e;
    }

    public boolean k() {
        return this.e != null;
    }

    public int l() {
        return this.f;
    }

    public boolean m() {
        return this.A.get(1);
    }

    public boolean n() {
        return this.g != null;
    }

    public int o() {
        return this.h;
    }

    public boolean p() {
        return this.A.get(2);
    }

    public int q() {
        return this.i;
    }

    public boolean r() {
        return this.A.get(3);
    }

    public Map<String, String> s() {
        return this.j;
    }

    public boolean t() {
        return this.j != null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("PushMetaInfo(");
        stringBuilder.append("id:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("messageTs:");
        stringBuilder.append(this.b);
        if (g()) {
            stringBuilder.append(", ");
            stringBuilder.append("topic:");
            if (this.c == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.c);
            }
        }
        if (i()) {
            stringBuilder.append(", ");
            stringBuilder.append("title:");
            if (this.d == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.d);
            }
        }
        if (k()) {
            stringBuilder.append(", ");
            stringBuilder.append("description:");
            if (this.e == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.e);
            }
        }
        if (m()) {
            stringBuilder.append(", ");
            stringBuilder.append("notifyType:");
            stringBuilder.append(this.f);
        }
        if (n()) {
            stringBuilder.append(", ");
            stringBuilder.append("url:");
            if (this.g == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.g);
            }
        }
        if (p()) {
            stringBuilder.append(", ");
            stringBuilder.append("passThrough:");
            stringBuilder.append(this.h);
        }
        if (r()) {
            stringBuilder.append(", ");
            stringBuilder.append("notifyId:");
            stringBuilder.append(this.i);
        }
        if (t()) {
            stringBuilder.append(", ");
            stringBuilder.append("extra:");
            if (this.j == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.j);
            }
        }
        if (u()) {
            stringBuilder.append(", ");
            stringBuilder.append("internal:");
            if (this.k == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.k);
            }
        }
        if (w()) {
            stringBuilder.append(", ");
            stringBuilder.append("ignoreRegInfo:");
            stringBuilder.append(this.l);
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public boolean u() {
        return this.k != null;
    }

    public boolean v() {
        return this.l;
    }

    public boolean w() {
        return this.A.get(4);
    }

    public void x() {
        if (this.a == null) {
            throw new g("Required field 'id' was not present! Struct: " + toString());
        }
    }
}
