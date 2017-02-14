package u.aly;

import com.tencent.open.SocialConstants;
import com.tencent.stat.DeviceInfo;
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

/* compiled from: Error */
public class av implements Serializable, Cloneable, bz<av, e> {
    public static final Map<e, cl> d;
    private static final dd e = new dd("Error");
    private static final ct f = new ct(DeviceInfo.TAG_TIMESTAMPS, (byte) 10, (short) 1);
    private static final ct g = new ct("context", (byte) 11, (short) 2);
    private static final ct h = new ct(SocialConstants.PARAM_SOURCE, (byte) 8, (short) 3);
    private static final Map<Class<? extends dg>, dh> i = new HashMap();
    private static final int j = 0;
    public long a;
    public String b;
    public aw c;
    private byte k;
    private e[] l;

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
        enumMap.put(e.a, new cl(DeviceInfo.TAG_TIMESTAMPS, (byte) 1, new cm((byte) 10)));
        enumMap.put(e.b, new cl("context", (byte) 1, new cm((byte) 11)));
        enumMap.put(e.c, new cl(SocialConstants.PARAM_SOURCE, (byte) 2, new ck(df.n, aw.class)));
        d = Collections.unmodifiableMap(enumMap);
        cl.a(av.class, d);
    }

    public av() {
        this.k = (byte) 0;
        this.l = new e[]{e.c};
    }

    public av(long j, String str) {
        this();
        this.a = j;
        b(true);
        this.b = str;
    }

    public av(av avVar) {
        this.k = (byte) 0;
        this.l = new e[]{e.c};
        this.k = avVar.k;
        this.a = avVar.a;
        if (avVar.i()) {
            this.b = avVar.b;
        }
        if (avVar.l()) {
            this.c = avVar.c;
        }
    }

    public av a() {
        return new av(this);
    }

    public void b() {
        b(false);
        this.a = 0;
        this.b = null;
        this.c = null;
    }

    public long c() {
        return this.a;
    }

    public av a(long j) {
        this.a = j;
        b(true);
        return this;
    }

    public void d() {
        this.k = bw.b(this.k, 0);
    }

    public boolean e() {
        return bw.a(this.k, 0);
    }

    public void b(boolean z) {
        this.k = bw.a(this.k, 0, z);
    }

    public String f() {
        return this.b;
    }

    public av a(String str) {
        this.b = str;
        return this;
    }

    public void h() {
        this.b = null;
    }

    public boolean i() {
        return this.b != null;
    }

    public void c(boolean z) {
        if (!z) {
            this.b = null;
        }
    }

    public aw j() {
        return this.c;
    }

    public av a(aw awVar) {
        this.c = awVar;
        return this;
    }

    public void k() {
        this.c = null;
    }

    public boolean l() {
        return this.c != null;
    }

    public void d(boolean z) {
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
        StringBuilder stringBuilder = new StringBuilder("Error(");
        stringBuilder.append("ts:");
        stringBuilder.append(this.a);
        stringBuilder.append(", ");
        stringBuilder.append("context:");
        if (this.b == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.b);
        }
        if (l()) {
            stringBuilder.append(", ");
            stringBuilder.append("source:");
            if (this.c == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.c);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void m() throws cf {
        if (this.b == null) {
            throw new cz("Required field 'context' was not present! Struct: " + toString());
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
            this.k = (byte) 0;
            a(new cs(new dk((InputStream) objectInputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }
}
