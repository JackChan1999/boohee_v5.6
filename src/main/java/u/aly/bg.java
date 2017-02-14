package u.aly;

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

/* compiled from: Location */
public class bg implements Serializable, Cloneable, bz<bg, e> {
    public static final Map<e, cl> d;
    private static final dd e = new dd("Location");
    private static final ct f = new ct("lat", (byte) 4, (short) 1);
    private static final ct g = new ct("lng", (byte) 4, (short) 2);
    private static final ct h = new ct(DeviceInfo.TAG_TIMESTAMPS, (byte) 10, (short) 3);
    private static final Map<Class<? extends dg>, dh> i = new HashMap();
    private static final int j = 0;
    private static final int k = 1;
    private static final int l = 2;
    public double a;
    public double b;
    public long c;
    private byte m;

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
        enumMap.put(e.a, new cl("lat", (byte) 1, new cm((byte) 4)));
        enumMap.put(e.b, new cl("lng", (byte) 1, new cm((byte) 4)));
        enumMap.put(e.c, new cl(DeviceInfo.TAG_TIMESTAMPS, (byte) 1, new cm((byte) 10)));
        d = Collections.unmodifiableMap(enumMap);
        cl.a(bg.class, d);
    }

    public bg() {
        this.m = (byte) 0;
    }

    public bg(double d, double d2, long j) {
        this();
        this.a = d;
        a(true);
        this.b = d2;
        b(true);
        this.c = j;
        c(true);
    }

    public bg(bg bgVar) {
        this.m = (byte) 0;
        this.m = bgVar.m;
        this.a = bgVar.a;
        this.b = bgVar.b;
        this.c = bgVar.c;
    }

    public bg a() {
        return new bg(this);
    }

    public void b() {
        a(false);
        this.a = 0.0d;
        b(false);
        this.b = 0.0d;
        c(false);
        this.c = 0;
    }

    public double c() {
        return this.a;
    }

    public bg a(double d) {
        this.a = d;
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

    public double f() {
        return this.b;
    }

    public bg b(double d) {
        this.b = d;
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

    public long j() {
        return this.c;
    }

    public bg a(long j) {
        this.c = j;
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
        StringBuilder stringBuilder = new StringBuilder("Location(");
        stringBuilder.append("lat:");
        stringBuilder.append(this.a);
        stringBuilder.append(", ");
        stringBuilder.append("lng:");
        stringBuilder.append(this.b);
        stringBuilder.append(", ");
        stringBuilder.append("ts:");
        stringBuilder.append(this.c);
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
