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

/* compiled from: InstantMsg */
public class be implements Serializable, Cloneable, bz<be, e> {
    public static final Map<e, cl> e;
    private static final dd f = new dd("InstantMsg");
    private static final ct g = new ct("id", (byte) 11, (short) 1);
    private static final ct h = new ct("errors", df.m, (short) 2);
    private static final ct i = new ct("events", df.m, (short) 3);
    private static final ct j = new ct("game_events", df.m, (short) 4);
    private static final Map<Class<? extends dg>, dh> k = new HashMap();
    public String a;
    public List<av> b;
    public List<ax> c;
    public List<ax> d;
    private e[] l;

    public /* synthetic */ cg b(int i) {
        return a(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        k.put(di.class, new b(null));
        k.put(dj.class, new d(null));
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.a, new cl("id", (byte) 1, new cm((byte) 11)));
        enumMap.put(e.b, new cl("errors", (byte) 2, new cn(df.m, new cq((byte) 12, av.class))));
        enumMap.put(e.c, new cl("events", (byte) 2, new cn(df.m, new cq((byte) 12, ax.class))));
        enumMap.put(e.d, new cl("game_events", (byte) 2, new cn(df.m, new cq((byte) 12, ax.class))));
        e = Collections.unmodifiableMap(enumMap);
        cl.a(be.class, e);
    }

    public be() {
        this.l = new e[]{e.b, e.c, e.d};
    }

    public be(String str) {
        this();
        this.a = str;
    }

    public be(be beVar) {
        List arrayList;
        this.l = new e[]{e.b, e.c, e.d};
        if (beVar.e()) {
            this.a = beVar.a;
        }
        if (beVar.k()) {
            arrayList = new ArrayList();
            for (av avVar : beVar.b) {
                arrayList.add(new av(avVar));
            }
            this.b = arrayList;
        }
        if (beVar.p()) {
            arrayList = new ArrayList();
            for (ax axVar : beVar.c) {
                arrayList.add(new ax(axVar));
            }
            this.c = arrayList;
        }
        if (beVar.u()) {
            arrayList = new ArrayList();
            for (ax axVar2 : beVar.d) {
                arrayList.add(new ax(axVar2));
            }
            this.d = arrayList;
        }
    }

    public be a() {
        return new be(this);
    }

    public void b() {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
    }

    public String c() {
        return this.a;
    }

    public be a(String str) {
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

    public int f() {
        return this.b == null ? 0 : this.b.size();
    }

    public Iterator<av> h() {
        return this.b == null ? null : this.b.iterator();
    }

    public void a(av avVar) {
        if (this.b == null) {
            this.b = new ArrayList();
        }
        this.b.add(avVar);
    }

    public List<av> i() {
        return this.b;
    }

    public be a(List<av> list) {
        this.b = list;
        return this;
    }

    public void j() {
        this.b = null;
    }

    public boolean k() {
        return this.b != null;
    }

    public void b(boolean z) {
        if (!z) {
            this.b = null;
        }
    }

    public int l() {
        return this.c == null ? 0 : this.c.size();
    }

    public Iterator<ax> m() {
        return this.c == null ? null : this.c.iterator();
    }

    public void a(ax axVar) {
        if (this.c == null) {
            this.c = new ArrayList();
        }
        this.c.add(axVar);
    }

    public List<ax> n() {
        return this.c;
    }

    public be b(List<ax> list) {
        this.c = list;
        return this;
    }

    public void o() {
        this.c = null;
    }

    public boolean p() {
        return this.c != null;
    }

    public void c(boolean z) {
        if (!z) {
            this.c = null;
        }
    }

    public int q() {
        return this.d == null ? 0 : this.d.size();
    }

    public Iterator<ax> r() {
        return this.d == null ? null : this.d.iterator();
    }

    public void b(ax axVar) {
        if (this.d == null) {
            this.d = new ArrayList();
        }
        this.d.add(axVar);
    }

    public List<ax> s() {
        return this.d;
    }

    public be c(List<ax> list) {
        this.d = list;
        return this;
    }

    public void t() {
        this.d = null;
    }

    public boolean u() {
        return this.d != null;
    }

    public void d(boolean z) {
        if (!z) {
            this.d = null;
        }
    }

    public e a(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) k.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) k.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("InstantMsg(");
        stringBuilder.append("id:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        if (k()) {
            stringBuilder.append(", ");
            stringBuilder.append("errors:");
            if (this.b == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.b);
            }
        }
        if (p()) {
            stringBuilder.append(", ");
            stringBuilder.append("events:");
            if (this.c == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.c);
            }
        }
        if (u()) {
            stringBuilder.append(", ");
            stringBuilder.append("game_events:");
            if (this.d == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.d);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void v() throws cf {
        if (this.a == null) {
            throw new cz("Required field 'id' was not present! Struct: " + toString());
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
