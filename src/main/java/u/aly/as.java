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

/* compiled from: ClientStats */
public class as implements Serializable, Cloneable, bz<as, e> {
    public static final Map<e, cl> d;
    private static final dd e = new dd("ClientStats");
    private static final ct f = new ct("successful_requests", (byte) 8, (short) 1);
    private static final ct g = new ct("failed_requests", (byte) 8, (short) 2);
    private static final ct h = new ct("last_request_spent_ms", (byte) 8, (short) 3);
    private static final Map<Class<? extends dg>, dh> i = new HashMap();
    private static final int j = 0;
    private static final int k = 1;
    private static final int l = 2;
    public int a;
    public int b;
    public int c;
    private byte m;
    private e[] n;

    public /* synthetic */ cg b(int i) {
        return e(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        i.put(di.class, new b(null));
        i.put(dj.class, new d(null));
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.a, new cl("successful_requests", (byte) 1, new cm((byte) 8)));
        enumMap.put(e.b, new cl("failed_requests", (byte) 1, new cm((byte) 8)));
        enumMap.put(e.c, new cl("last_request_spent_ms", (byte) 2, new cm((byte) 8)));
        d = Collections.unmodifiableMap(enumMap);
        cl.a(as.class, d);
    }

    public as() {
        this.m = (byte) 0;
        this.n = new e[]{e.c};
        this.a = 0;
        this.b = 0;
    }

    public as(int i, int i2) {
        this();
        this.a = i;
        a(true);
        this.b = i2;
        b(true);
    }

    public as(as asVar) {
        this.m = (byte) 0;
        this.n = new e[]{e.c};
        this.m = asVar.m;
        this.a = asVar.a;
        this.b = asVar.b;
        this.c = asVar.c;
    }

    public as a() {
        return new as(this);
    }

    public void b() {
        this.a = 0;
        this.b = 0;
        c(false);
        this.c = 0;
    }

    public int c() {
        return this.a;
    }

    public as a(int i) {
        this.a = i;
        a(true);
        return this;
    }

    public void d() {
        this.m = bw.b(this.m, 0);
    }

    public boolean e() {
        return bw.a(this.m, 0);
    }

    public void a(boolean z) {
        this.m = bw.a(this.m, 0, z);
    }

    public int f() {
        return this.b;
    }

    public as c(int i) {
        this.b = i;
        b(true);
        return this;
    }

    public void h() {
        this.m = bw.b(this.m, 1);
    }

    public boolean i() {
        return bw.a(this.m, 1);
    }

    public void b(boolean z) {
        this.m = bw.a(this.m, 1, z);
    }

    public int j() {
        return this.c;
    }

    public as d(int i) {
        this.c = i;
        c(true);
        return this;
    }

    public void k() {
        this.m = bw.b(this.m, 2);
    }

    public boolean l() {
        return bw.a(this.m, 2);
    }

    public void c(boolean z) {
        this.m = bw.a(this.m, 2, z);
    }

    public e e(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) i.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) i.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("ClientStats(");
        stringBuilder.append("successful_requests:");
        stringBuilder.append(this.a);
        stringBuilder.append(", ");
        stringBuilder.append("failed_requests:");
        stringBuilder.append(this.b);
        if (l()) {
            stringBuilder.append(", ");
            stringBuilder.append("last_request_spent_ms:");
            stringBuilder.append(this.c);
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void m() throws cf {
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
            this.m = (byte) 0;
            a(new cs(new dk((InputStream) objectInputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }
}
