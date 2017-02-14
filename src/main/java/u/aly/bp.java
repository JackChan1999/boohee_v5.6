package u.aly;

import com.umeng.socialize.common.SocializeConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/* compiled from: UALogEntry */
public class bp implements Serializable, Cloneable, bz<bp, e> {
    private static final Map<Class<? extends dg>, dh> A = new HashMap();
    public static final Map<e, cl> m;
    private static final dd n = new dd("UALogEntry");
    private static final ct o = new ct("client_stats", (byte) 12, (short) 1);
    private static final ct p = new ct("app_info", (byte) 12, (short) 2);
    private static final ct q = new ct("device_info", (byte) 12, (short) 3);
    private static final ct r = new ct("misc_info", (byte) 12, (short) 4);
    private static final ct s = new ct("activate_msg", (byte) 12, (short) 5);
    private static final ct t = new ct("instant_msgs", df.m, (short) 6);
    private static final ct u = new ct("sessions", df.m, (short) 7);
    private static final ct v = new ct("imprint", (byte) 12, (short) 8);
    private static final ct w = new ct("id_tracking", (byte) 12, (short) 9);
    private static final ct x = new ct("active_user", (byte) 12, (short) 10);
    private static final ct y = new ct("control_policy", (byte) 12, (short) 11);
    private static final ct z = new ct("group_info", (byte) 13, (short) 12);
    private e[] B;
    public as a;
    public ar b;
    public au c;
    public bh d;
    public ap e;
    public List<be> f;
    public List<bn> g;
    public bc h;
    public bb i;
    public aq j;
    public at k;
    public Map<String, Integer> l;

    public /* synthetic */ cg b(int i) {
        return a(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        A.put(di.class, new b(null));
        A.put(dj.class, new d(null));
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.a, new cl("client_stats", (byte) 1, new cq((byte) 12, as.class)));
        enumMap.put(e.b, new cl("app_info", (byte) 1, new cq((byte) 12, ar.class)));
        enumMap.put(e.c, new cl("device_info", (byte) 1, new cq((byte) 12, au.class)));
        enumMap.put(e.d, new cl("misc_info", (byte) 1, new cq((byte) 12, bh.class)));
        enumMap.put(e.e, new cl("activate_msg", (byte) 2, new cq((byte) 12, ap.class)));
        enumMap.put(e.f, new cl("instant_msgs", (byte) 2, new cn(df.m, new cq((byte) 12, be.class))));
        enumMap.put(e.g, new cl("sessions", (byte) 2, new cn(df.m, new cq((byte) 12, bn.class))));
        enumMap.put(e.h, new cl("imprint", (byte) 2, new cq((byte) 12, bc.class)));
        enumMap.put(e.i, new cl("id_tracking", (byte) 2, new cq((byte) 12, bb.class)));
        enumMap.put(e.j, new cl("active_user", (byte) 2, new cq((byte) 12, aq.class)));
        enumMap.put(e.k, new cl("control_policy", (byte) 2, new cq((byte) 12, at.class)));
        enumMap.put(e.l, new cl("group_info", (byte) 2, new co((byte) 13, new cm((byte) 11), new cm((byte) 8))));
        m = Collections.unmodifiableMap(enumMap);
        cl.a(bp.class, m);
    }

    public bp() {
        this.B = new e[]{e.e, e.f, e.g, e.h, e.i, e.j, e.k, e.l};
    }

    public bp(as asVar, ar arVar, au auVar, bh bhVar) {
        this();
        this.a = asVar;
        this.b = arVar;
        this.c = auVar;
        this.d = bhVar;
    }

    public bp(bp bpVar) {
        List arrayList;
        this.B = new e[]{e.e, e.f, e.g, e.h, e.i, e.j, e.k, e.l};
        if (bpVar.e()) {
            this.a = new as(bpVar.a);
        }
        if (bpVar.i()) {
            this.b = new ar(bpVar.b);
        }
        if (bpVar.l()) {
            this.c = new au(bpVar.c);
        }
        if (bpVar.o()) {
            this.d = new bh(bpVar.d);
        }
        if (bpVar.r()) {
            this.e = new ap(bpVar.e);
        }
        if (bpVar.w()) {
            arrayList = new ArrayList();
            for (be beVar : bpVar.f) {
                arrayList.add(new be(beVar));
            }
            this.f = arrayList;
        }
        if (bpVar.B()) {
            arrayList = new ArrayList();
            for (bn bnVar : bpVar.g) {
                arrayList.add(new bn(bnVar));
            }
            this.g = arrayList;
        }
        if (bpVar.E()) {
            this.h = new bc(bpVar.h);
        }
        if (bpVar.H()) {
            this.i = new bb(bpVar.i);
        }
        if (bpVar.K()) {
            this.j = new aq(bpVar.j);
        }
        if (bpVar.N()) {
            this.k = new at(bpVar.k);
        }
        if (bpVar.R()) {
            Map hashMap = new HashMap();
            for (Entry entry : bpVar.l.entrySet()) {
                hashMap.put((String) entry.getKey(), (Integer) entry.getValue());
            }
            this.l = hashMap;
        }
    }

    public bp a() {
        return new bp(this);
    }

    public void b() {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = null;
        this.l = null;
    }

    public as c() {
        return this.a;
    }

    public bp a(as asVar) {
        this.a = asVar;
        return this;
    }

    public void d() {
        this.a = null;
    }

    public boolean e() {
        return this.a != null;
    }

    public void a(boolean z) {
        if (!z) {
            this.a = null;
        }
    }

    public ar f() {
        return this.b;
    }

    public bp a(ar arVar) {
        this.b = arVar;
        return this;
    }

    public void h() {
        this.b = null;
    }

    public boolean i() {
        return this.b != null;
    }

    public void b(boolean z) {
        if (!z) {
            this.b = null;
        }
    }

    public au j() {
        return this.c;
    }

    public bp a(au auVar) {
        this.c = auVar;
        return this;
    }

    public void k() {
        this.c = null;
    }

    public boolean l() {
        return this.c != null;
    }

    public void c(boolean z) {
        if (!z) {
            this.c = null;
        }
    }

    public bh m() {
        return this.d;
    }

    public bp a(bh bhVar) {
        this.d = bhVar;
        return this;
    }

    public void n() {
        this.d = null;
    }

    public boolean o() {
        return this.d != null;
    }

    public void d(boolean z) {
        if (!z) {
            this.d = null;
        }
    }

    public ap p() {
        return this.e;
    }

    public bp a(ap apVar) {
        this.e = apVar;
        return this;
    }

    public void q() {
        this.e = null;
    }

    public boolean r() {
        return this.e != null;
    }

    public void e(boolean z) {
        if (!z) {
            this.e = null;
        }
    }

    public int s() {
        return this.f == null ? 0 : this.f.size();
    }

    public Iterator<be> t() {
        return this.f == null ? null : this.f.iterator();
    }

    public void a(be beVar) {
        if (this.f == null) {
            this.f = new ArrayList();
        }
        this.f.add(beVar);
    }

    public List<be> u() {
        return this.f;
    }

    public bp a(List<be> list) {
        this.f = list;
        return this;
    }

    public void v() {
        this.f = null;
    }

    public boolean w() {
        return this.f != null;
    }

    public void f(boolean z) {
        if (!z) {
            this.f = null;
        }
    }

    public int x() {
        return this.g == null ? 0 : this.g.size();
    }

    public Iterator<bn> y() {
        return this.g == null ? null : this.g.iterator();
    }

    public void a(bn bnVar) {
        if (this.g == null) {
            this.g = new ArrayList();
        }
        this.g.add(bnVar);
    }

    public List<bn> z() {
        return this.g;
    }

    public bp b(List<bn> list) {
        this.g = list;
        return this;
    }

    public void A() {
        this.g = null;
    }

    public boolean B() {
        return this.g != null;
    }

    public void g(boolean z) {
        if (!z) {
            this.g = null;
        }
    }

    public bc C() {
        return this.h;
    }

    public bp a(bc bcVar) {
        this.h = bcVar;
        return this;
    }

    public void D() {
        this.h = null;
    }

    public boolean E() {
        return this.h != null;
    }

    public void h(boolean z) {
        if (!z) {
            this.h = null;
        }
    }

    public bb F() {
        return this.i;
    }

    public bp a(bb bbVar) {
        this.i = bbVar;
        return this;
    }

    public void G() {
        this.i = null;
    }

    public boolean H() {
        return this.i != null;
    }

    public void i(boolean z) {
        if (!z) {
            this.i = null;
        }
    }

    public aq I() {
        return this.j;
    }

    public bp a(aq aqVar) {
        this.j = aqVar;
        return this;
    }

    public void J() {
        this.j = null;
    }

    public boolean K() {
        return this.j != null;
    }

    public void j(boolean z) {
        if (!z) {
            this.j = null;
        }
    }

    public at L() {
        return this.k;
    }

    public bp a(at atVar) {
        this.k = atVar;
        return this;
    }

    public void M() {
        this.k = null;
    }

    public boolean N() {
        return this.k != null;
    }

    public void k(boolean z) {
        if (!z) {
            this.k = null;
        }
    }

    public int O() {
        return this.l == null ? 0 : this.l.size();
    }

    public void a(String str, int i) {
        if (this.l == null) {
            this.l = new HashMap();
        }
        this.l.put(str, Integer.valueOf(i));
    }

    public Map<String, Integer> P() {
        return this.l;
    }

    public bp a(Map<String, Integer> map) {
        this.l = map;
        return this;
    }

    public void Q() {
        this.l = null;
    }

    public boolean R() {
        return this.l != null;
    }

    public void l(boolean z) {
        if (!z) {
            this.l = null;
        }
    }

    public e a(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) A.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) A.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("UALogEntry(");
        stringBuilder.append("client_stats:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("app_info:");
        if (this.b == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.b);
        }
        stringBuilder.append(", ");
        stringBuilder.append("device_info:");
        if (this.c == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.c);
        }
        stringBuilder.append(", ");
        stringBuilder.append("misc_info:");
        if (this.d == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.d);
        }
        if (r()) {
            stringBuilder.append(", ");
            stringBuilder.append("activate_msg:");
            if (this.e == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.e);
            }
        }
        if (w()) {
            stringBuilder.append(", ");
            stringBuilder.append("instant_msgs:");
            if (this.f == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.f);
            }
        }
        if (B()) {
            stringBuilder.append(", ");
            stringBuilder.append("sessions:");
            if (this.g == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.g);
            }
        }
        if (E()) {
            stringBuilder.append(", ");
            stringBuilder.append("imprint:");
            if (this.h == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.h);
            }
        }
        if (H()) {
            stringBuilder.append(", ");
            stringBuilder.append("id_tracking:");
            if (this.i == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.i);
            }
        }
        if (K()) {
            stringBuilder.append(", ");
            stringBuilder.append("active_user:");
            if (this.j == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.j);
            }
        }
        if (N()) {
            stringBuilder.append(", ");
            stringBuilder.append("control_policy:");
            if (this.k == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.k);
            }
        }
        if (R()) {
            stringBuilder.append(", ");
            stringBuilder.append("group_info:");
            if (this.l == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.l);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void S() throws cf {
        if (this.a == null) {
            throw new cz("Required field 'client_stats' was not present! Struct: " + toString());
        } else if (this.b == null) {
            throw new cz("Required field 'app_info' was not present! Struct: " + toString());
        } else if (this.c == null) {
            throw new cz("Required field 'device_info' was not present! Struct: " + toString());
        } else if (this.d == null) {
            throw new cz("Required field 'misc_info' was not present! Struct: " + toString());
        } else {
            if (this.a != null) {
                this.a.m();
            }
            if (this.b != null) {
                this.b.K();
            }
            if (this.c != null) {
                this.c.af();
            }
            if (this.d != null) {
                this.d.H();
            }
            if (this.e != null) {
                this.e.f();
            }
            if (this.h != null) {
                this.h.n();
            }
            if (this.i != null) {
                this.i.p();
            }
            if (this.j != null) {
                this.j.j();
            }
            if (this.k != null) {
                this.k.f();
            }
        }
    }

    private void a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            b(new cs(new dk((OutputStream) objectOutputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }

    private void a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            a(new cs(new dk((InputStream) objectInputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }
}
