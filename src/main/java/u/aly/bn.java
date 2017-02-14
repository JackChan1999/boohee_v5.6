package u.aly;

import com.boohee.modeldao.SportRecordDao;
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

/* compiled from: Session */
public class bn implements Serializable, Cloneable, bz<bn, e> {
    public static final Map<e, cl> h;
    private static final dd i = new dd("Session");
    private static final ct j = new ct("id", (byte) 11, (short) 1);
    private static final ct k = new ct("start_time", (byte) 10, (short) 2);
    private static final ct l = new ct("end_time", (byte) 10, (short) 3);
    private static final ct m = new ct(SportRecordDao.DURATION, (byte) 10, (short) 4);
    private static final ct n = new ct("pages", df.m, (short) 5);
    private static final ct o = new ct("locations", df.m, (short) 6);
    private static final ct p = new ct("traffic", (byte) 12, (short) 7);
    private static final Map<Class<? extends dg>, dh> q = new HashMap();
    private static final int r = 0;
    private static final int s = 1;
    private static final int t = 2;
    public String a;
    public long b;
    public long c;
    public long d;
    public List<bi> e;
    public List<bg> f;
    public bo g;
    private byte u;
    private e[] v;

    public /* synthetic */ cg b(int i) {
        return a(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        q.put(di.class, new b(null));
        q.put(dj.class, new d(null));
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.a, new cl("id", (byte) 1, new cm((byte) 11)));
        enumMap.put(e.b, new cl("start_time", (byte) 1, new cm((byte) 10)));
        enumMap.put(e.c, new cl("end_time", (byte) 1, new cm((byte) 10)));
        enumMap.put(e.d, new cl(SportRecordDao.DURATION, (byte) 1, new cm((byte) 10)));
        enumMap.put(e.e, new cl("pages", (byte) 2, new cn(df.m, new cq((byte) 12, bi.class))));
        enumMap.put(e.f, new cl("locations", (byte) 2, new cn(df.m, new cq((byte) 12, bg.class))));
        enumMap.put(e.g, new cl("traffic", (byte) 2, new cq((byte) 12, bo.class)));
        h = Collections.unmodifiableMap(enumMap);
        cl.a(bn.class, h);
    }

    public bn() {
        this.u = (byte) 0;
        this.v = new e[]{e.e, e.f, e.g};
    }

    public bn(String str, long j, long j2, long j3) {
        this();
        this.a = str;
        this.b = j;
        b(true);
        this.c = j2;
        c(true);
        this.d = j3;
        d(true);
    }

    public bn(bn bnVar) {
        List arrayList;
        this.u = (byte) 0;
        this.v = new e[]{e.e, e.f, e.g};
        this.u = bnVar.u;
        if (bnVar.e()) {
            this.a = bnVar.a;
        }
        this.b = bnVar.b;
        this.c = bnVar.c;
        this.d = bnVar.d;
        if (bnVar.t()) {
            arrayList = new ArrayList();
            for (bi biVar : bnVar.e) {
                arrayList.add(new bi(biVar));
            }
            this.e = arrayList;
        }
        if (bnVar.y()) {
            arrayList = new ArrayList();
            for (bg bgVar : bnVar.f) {
                arrayList.add(new bg(bgVar));
            }
            this.f = arrayList;
        }
        if (bnVar.B()) {
            this.g = new bo(bnVar.g);
        }
    }

    public bn a() {
        return new bn(this);
    }

    public void b() {
        this.a = null;
        b(false);
        this.b = 0;
        c(false);
        this.c = 0;
        d(false);
        this.d = 0;
        this.e = null;
        this.f = null;
        this.g = null;
    }

    public String c() {
        return this.a;
    }

    public bn a(String str) {
        this.a = str;
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

    public long f() {
        return this.b;
    }

    public bn a(long j) {
        this.b = j;
        b(true);
        return this;
    }

    public void h() {
        this.u = bw.b(this.u, 0);
    }

    public boolean i() {
        return bw.a(this.u, 0);
    }

    public void b(boolean z) {
        this.u = bw.a(this.u, 0, z);
    }

    public long j() {
        return this.c;
    }

    public bn b(long j) {
        this.c = j;
        c(true);
        return this;
    }

    public void k() {
        this.u = bw.b(this.u, 1);
    }

    public boolean l() {
        return bw.a(this.u, 1);
    }

    public void c(boolean z) {
        this.u = bw.a(this.u, 1, z);
    }

    public long m() {
        return this.d;
    }

    public bn c(long j) {
        this.d = j;
        d(true);
        return this;
    }

    public void n() {
        this.u = bw.b(this.u, 2);
    }

    public boolean o() {
        return bw.a(this.u, 2);
    }

    public void d(boolean z) {
        this.u = bw.a(this.u, 2, z);
    }

    public int p() {
        return this.e == null ? 0 : this.e.size();
    }

    public Iterator<bi> q() {
        return this.e == null ? null : this.e.iterator();
    }

    public void a(bi biVar) {
        if (this.e == null) {
            this.e = new ArrayList();
        }
        this.e.add(biVar);
    }

    public List<bi> r() {
        return this.e;
    }

    public bn a(List<bi> list) {
        this.e = list;
        return this;
    }

    public void s() {
        this.e = null;
    }

    public boolean t() {
        return this.e != null;
    }

    public void e(boolean z) {
        if (!z) {
            this.e = null;
        }
    }

    public int u() {
        return this.f == null ? 0 : this.f.size();
    }

    public Iterator<bg> v() {
        return this.f == null ? null : this.f.iterator();
    }

    public void a(bg bgVar) {
        if (this.f == null) {
            this.f = new ArrayList();
        }
        this.f.add(bgVar);
    }

    public List<bg> w() {
        return this.f;
    }

    public bn b(List<bg> list) {
        this.f = list;
        return this;
    }

    public void x() {
        this.f = null;
    }

    public boolean y() {
        return this.f != null;
    }

    public void f(boolean z) {
        if (!z) {
            this.f = null;
        }
    }

    public bo z() {
        return this.g;
    }

    public bn a(bo boVar) {
        this.g = boVar;
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

    public e a(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) q.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) q.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Session(");
        stringBuilder.append("id:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("start_time:");
        stringBuilder.append(this.b);
        stringBuilder.append(", ");
        stringBuilder.append("end_time:");
        stringBuilder.append(this.c);
        stringBuilder.append(", ");
        stringBuilder.append("duration:");
        stringBuilder.append(this.d);
        if (t()) {
            stringBuilder.append(", ");
            stringBuilder.append("pages:");
            if (this.e == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.e);
            }
        }
        if (y()) {
            stringBuilder.append(", ");
            stringBuilder.append("locations:");
            if (this.f == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.f);
            }
        }
        if (B()) {
            stringBuilder.append(", ");
            stringBuilder.append("traffic:");
            if (this.g == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.g);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void C() throws cf {
        if (this.a == null) {
            throw new cz("Required field 'id' was not present! Struct: " + toString());
        } else if (this.g != null) {
            this.g.j();
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
            this.u = (byte) 0;
            a(new cs(new dk((InputStream) objectInputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }
}
