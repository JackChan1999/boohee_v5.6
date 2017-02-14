package u.aly;

import com.umeng.socialize.common.SocializeConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/* compiled from: ControlPolicy */
public class at implements Serializable, Cloneable, bz<at, e> {
    public static final Map<e, cl> b;
    private static final dd c = new dd("ControlPolicy");
    private static final ct d = new ct("latent", (byte) 12, (short) 1);
    private static final Map<Class<? extends dg>, dh> e = new HashMap();
    public bf a;
    private e[] f;

    public /* synthetic */ cg b(int i) {
        return a(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        e.put(di.class, new b(null));
        e.put(dj.class, new d(null));
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.a, new cl("latent", (byte) 2, new cq((byte) 12, bf.class)));
        b = Collections.unmodifiableMap(enumMap);
        cl.a(at.class, b);
    }

    public at() {
        this.f = new e[]{e.a};
    }

    public at(at atVar) {
        this.f = new e[]{e.a};
        if (atVar.e()) {
            this.a = new bf(atVar.a);
        }
    }

    public at a() {
        return new at(this);
    }

    public void b() {
        this.a = null;
    }

    public bf c() {
        return this.a;
    }

    public at a(bf bfVar) {
        this.a = bfVar;
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

    public e a(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) e.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) e.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("ControlPolicy(");
        if (e()) {
            stringBuilder.append("latent:");
            if (this.a == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.a);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void f() throws cf {
        if (this.a != null) {
            this.a.j();
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
