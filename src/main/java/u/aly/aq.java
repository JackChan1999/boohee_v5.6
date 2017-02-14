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

/* compiled from: ActiveUser */
public class aq implements Serializable, Cloneable, bz<aq, e> {
    public static final Map<e, cl> c;
    private static final dd d = new dd("ActiveUser");
    private static final ct e = new ct("provider", (byte) 11, (short) 1);
    private static final ct f = new ct("puid", (byte) 11, (short) 2);
    private static final Map<Class<? extends dg>, dh> g = new HashMap();
    public String a;
    public String b;

    public /* synthetic */ cg b(int i) {
        return a(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        g.put(di.class, new b(null));
        g.put(dj.class, new d(null));
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.a, new cl("provider", (byte) 1, new cm((byte) 11)));
        enumMap.put(e.b, new cl("puid", (byte) 1, new cm((byte) 11)));
        c = Collections.unmodifiableMap(enumMap);
        cl.a(aq.class, c);
    }

    public aq(String str, String str2) {
        this();
        this.a = str;
        this.b = str2;
    }

    public aq(aq aqVar) {
        if (aqVar.e()) {
            this.a = aqVar.a;
        }
        if (aqVar.i()) {
            this.b = aqVar.b;
        }
    }

    public aq a() {
        return new aq(this);
    }

    public void b() {
        this.a = null;
        this.b = null;
    }

    public String c() {
        return this.a;
    }

    public aq a(String str) {
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

    public String f() {
        return this.b;
    }

    public aq b(String str) {
        this.b = str;
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

    public e a(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) g.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) g.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("ActiveUser(");
        stringBuilder.append("provider:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("puid:");
        if (this.b == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.b);
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void j() throws cf {
        if (this.a == null) {
            throw new cz("Required field 'provider' was not present! Struct: " + toString());
        } else if (this.b == null) {
            throw new cz("Required field 'puid' was not present! Struct: " + toString());
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
