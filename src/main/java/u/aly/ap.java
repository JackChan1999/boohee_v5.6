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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: ActivateMsg */
public class ap implements Serializable, Cloneable, bz<ap, e> {
    public static final Map<e, cl> b;
    private static final dd c = new dd("ActivateMsg");
    private static final ct d = new ct(DeviceInfo.TAG_TIMESTAMPS, (byte) 10, (short) 1);
    private static final Map<Class<? extends dg>, dh> e = new HashMap();
    private static final int f = 0;
    public long a;
    private byte g;

    /* compiled from: ActivateMsg */
    private static class a extends di<ap> {
        private a() {
        }

        public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
            b(cyVar, (ap) bzVar);
        }

        public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
            a(cyVar, (ap) bzVar);
        }

        public void a(cy cyVar, ap apVar) throws cf {
            cyVar.j();
            while (true) {
                ct l = cyVar.l();
                if (l.b == (byte) 0) {
                    cyVar.k();
                    if (apVar.e()) {
                        apVar.f();
                        return;
                    }
                    throw new cz("Required field 'ts' was not found in serialized data! Struct: " + toString());
                }
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 10) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        apVar.a = cyVar.x();
                        apVar.a(true);
                        break;
                    default:
                        db.a(cyVar, l.b);
                        break;
                }
                cyVar.m();
            }
        }

        public void b(cy cyVar, ap apVar) throws cf {
            apVar.f();
            cyVar.a(ap.c);
            cyVar.a(ap.d);
            cyVar.a(apVar.a);
            cyVar.c();
            cyVar.d();
            cyVar.b();
        }
    }

    /* compiled from: ActivateMsg */
    private static class b implements dh {
        private b() {
        }

        public /* synthetic */ dg b() {
            return a();
        }

        public a a() {
            return new a();
        }
    }

    /* compiled from: ActivateMsg */
    private static class c extends dj<ap> {
        private c() {
        }

        public void a(cy cyVar, ap apVar) throws cf {
            ((de) cyVar).a(apVar.a);
        }

        public void b(cy cyVar, ap apVar) throws cf {
            apVar.a = ((de) cyVar).x();
            apVar.a(true);
        }
    }

    /* compiled from: ActivateMsg */
    private static class d implements dh {
        private d() {
        }

        public /* synthetic */ dg b() {
            return a();
        }

        public c a() {
            return new c();
        }
    }

    /* compiled from: ActivateMsg */
    public enum e implements cg {
        TS((short) 1, DeviceInfo.TAG_TIMESTAMPS);
        
        private static final Map<String, e> b = null;
        private final short c;
        private final String d;

        static {
            b = new HashMap();
            Iterator it = EnumSet.allOf(e.class).iterator();
            while (it.hasNext()) {
                e eVar = (e) it.next();
                b.put(eVar.b(), eVar);
            }
        }

        public static e a(int i) {
            switch (i) {
                case 1:
                    return TS;
                default:
                    return null;
            }
        }

        public static e b(int i) {
            e a = a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        public static e a(String str) {
            return (e) b.get(str);
        }

        private e(short s, String str) {
            this.c = s;
            this.d = str;
        }

        public short a() {
            return this.c;
        }

        public String b() {
            return this.d;
        }
    }

    public /* synthetic */ cg b(int i) {
        return a(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        e.put(di.class, new b());
        e.put(dj.class, new d());
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.TS, new cl(DeviceInfo.TAG_TIMESTAMPS, (byte) 1, new cm((byte) 10)));
        b = Collections.unmodifiableMap(enumMap);
        cl.a(ap.class, b);
    }

    public ap() {
        this.g = (byte) 0;
    }

    public ap(long j) {
        this();
        this.a = j;
        a(true);
    }

    public ap(ap apVar) {
        this.g = (byte) 0;
        this.g = apVar.g;
        this.a = apVar.a;
    }

    public ap a() {
        return new ap(this);
    }

    public void b() {
        a(false);
        this.a = 0;
    }

    public long c() {
        return this.a;
    }

    public ap a(long j) {
        this.a = j;
        a(true);
        return this;
    }

    public void d() {
        this.g = bw.b(this.g, 0);
    }

    public boolean e() {
        return bw.a(this.g, 0);
    }

    public void a(boolean z) {
        this.g = bw.a(this.g, 0, z);
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
        StringBuilder stringBuilder = new StringBuilder("ActivateMsg(");
        stringBuilder.append("ts:");
        stringBuilder.append(this.a);
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void f() throws cf {
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
            this.g = (byte) 0;
            a(new cs(new dk((InputStream) objectInputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }
}
