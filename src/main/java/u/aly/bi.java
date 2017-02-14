package u.aly;

import com.boohee.modeldao.SportRecordDao;
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

/* compiled from: Page */
public class bi implements Serializable, Cloneable, bz<bi, e> {
    public static final Map<e, cl> c;
    private static final dd d = new dd("Page");
    private static final ct e = new ct("page_name", (byte) 11, (short) 1);
    private static final ct f = new ct(SportRecordDao.DURATION, (byte) 10, (short) 2);
    private static final Map<Class<? extends dg>, dh> g = new HashMap();
    private static final int h = 0;
    public String a;
    public long b;
    private byte i;

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
        enumMap.put(e.a, new cl("page_name", (byte) 1, new cm((byte) 11)));
        enumMap.put(e.b, new cl(SportRecordDao.DURATION, (byte) 1, new cm((byte) 10)));
        c = Collections.unmodifiableMap(enumMap);
        cl.a(bi.class, c);
    }

    public bi() {
        this.i = (byte) 0;
    }

    public bi(String str, long j) {
        this();
        this.a = str;
        this.b = j;
        b(true);
    }

    public bi(bi biVar) {
        this.i = (byte) 0;
        this.i = biVar.i;
        if (biVar.e()) {
            this.a = biVar.a;
        }
        this.b = biVar.b;
    }

    public bi a() {
        return new bi(this);
    }

    public void b() {
        this.a = null;
        b(false);
        this.b = 0;
    }

    public String c() {
        return this.a;
    }

    public bi a(String str) {
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

    public bi a(long j) {
        this.b = j;
        b(true);
        return this;
    }

    public void h() {
        this.i = bw.b(this.i, 0);
    }

    public boolean i() {
        return bw.a(this.i, 0);
    }

    public void b(boolean z) {
        this.i = bw.a(this.i, 0, z);
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
        StringBuilder stringBuilder = new StringBuilder("Page(");
        stringBuilder.append("page_name:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("duration:");
        stringBuilder.append(this.b);
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void j() throws cf {
        if (this.a == null) {
            throw new cz("Required field 'page_name' was not present! Struct: " + toString());
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
            this.i = (byte) 0;
            a(new cs(new dk((InputStream) objectInputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }
}
