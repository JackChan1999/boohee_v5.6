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

/* compiled from: IdTracking */
public class bb implements Serializable, Cloneable, bz<bb, e> {
    public static final Map<e, cl> d;
    private static final dd e = new dd("IdTracking");
    private static final ct f = new ct("snapshots", (byte) 13, (short) 1);
    private static final ct g = new ct("journals", df.m, (short) 2);
    private static final ct h = new ct("checksum", (byte) 11, (short) 3);
    private static final Map<Class<? extends dg>, dh> i = new HashMap();
    public Map<String, ba> a;
    public List<az> b;
    public String c;
    private e[] j;

    public /* synthetic */ cg b(int i) {
        return a(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        i.put(di.class, new b(null));
        i.put(dj.class, new d(null));
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.a, new cl("snapshots", (byte) 1, new co((byte) 13, new cm((byte) 11), new cq((byte) 12, ba.class))));
        enumMap.put(e.b, new cl("journals", (byte) 2, new cn(df.m, new cq((byte) 12, az.class))));
        enumMap.put(e.c, new cl("checksum", (byte) 2, new cm((byte) 11)));
        d = Collections.unmodifiableMap(enumMap);
        cl.a(bb.class, d);
    }

    public bb() {
        this.j = new e[]{e.b, e.c};
    }

    public bb(Map<String, ba> map) {
        this();
        this.a = map;
    }

    public bb(bb bbVar) {
        this.j = new e[]{e.b, e.c};
        if (bbVar.f()) {
            Map hashMap = new HashMap();
            for (Entry entry : bbVar.a.entrySet()) {
                hashMap.put((String) entry.getKey(), new ba((ba) entry.getValue()));
            }
            this.a = hashMap;
        }
        if (bbVar.l()) {
            List arrayList = new ArrayList();
            for (az azVar : bbVar.b) {
                arrayList.add(new az(azVar));
            }
            this.b = arrayList;
        }
        if (bbVar.o()) {
            this.c = bbVar.c;
        }
    }

    public bb a() {
        return new bb(this);
    }

    public void b() {
        this.a = null;
        this.b = null;
        this.c = null;
    }

    public int c() {
        return this.a == null ? 0 : this.a.size();
    }

    public void a(String str, ba baVar) {
        if (this.a == null) {
            this.a = new HashMap();
        }
        this.a.put(str, baVar);
    }

    public Map<String, ba> d() {
        return this.a;
    }

    public bb a(Map<String, ba> map) {
        this.a = map;
        return this;
    }

    public void e() {
        this.a = null;
    }

    public boolean f() {
        return this.a != null;
    }

    public void a(boolean z) {
        if (!z) {
            this.a = null;
        }
    }

    public int h() {
        return this.b == null ? 0 : this.b.size();
    }

    public Iterator<az> i() {
        return this.b == null ? null : this.b.iterator();
    }

    public void a(az azVar) {
        if (this.b == null) {
            this.b = new ArrayList();
        }
        this.b.add(azVar);
    }

    public List<az> j() {
        return this.b;
    }

    public bb a(List<az> list) {
        this.b = list;
        return this;
    }

    public void k() {
        this.b = null;
    }

    public boolean l() {
        return this.b != null;
    }

    public void b(boolean z) {
        if (!z) {
            this.b = null;
        }
    }

    public String m() {
        return this.c;
    }

    public bb a(String str) {
        this.c = str;
        return this;
    }

    public void n() {
        this.c = null;
    }

    public boolean o() {
        return this.c != null;
    }

    public void c(boolean z) {
        if (!z) {
            this.c = null;
        }
    }

    public e a(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) i.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) i.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("IdTracking(");
        stringBuilder.append("snapshots:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        if (l()) {
            stringBuilder.append(", ");
            stringBuilder.append("journals:");
            if (this.b == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.b);
            }
        }
        if (o()) {
            stringBuilder.append(", ");
            stringBuilder.append("checksum:");
            if (this.c == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.c);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void p() throws cf {
        if (this.a == null) {
            throw new cz("Required field 'snapshots' was not present! Struct: " + toString());
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
