package u.aly;

import com.boohee.utils.LeDongLiHelper;
import com.umeng.socialize.common.SocializeConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: AppInfo */
public class ar implements Serializable, Cloneable, bz<ar, e> {
    private static final int A = 1;
    public static final Map<e, cl> l;
    private static final dd m = new dd("AppInfo");
    private static final ct n = new ct("key", (byte) 11, (short) 1);
    private static final ct o = new ct("version", (byte) 11, (short) 2);
    private static final ct p = new ct("version_index", (byte) 8, (short) 3);
    private static final ct q = new ct(LeDongLiHelper.PACKAGE_NAME, (byte) 11, (short) 4);
    private static final ct r = new ct("sdk_type", (byte) 8, (short) 5);
    private static final ct s = new ct("sdk_version", (byte) 11, (short) 6);
    private static final ct t = new ct("channel", (byte) 11, (short) 7);
    private static final ct u = new ct("wrapper_type", (byte) 11, (short) 8);
    private static final ct v = new ct("wrapper_version", (byte) 11, (short) 9);
    private static final ct w = new ct("vertical_type", (byte) 8, (short) 10);
    private static final ct x = new ct("app_signature", (byte) 11, (short) 11);
    private static final Map<Class<? extends dg>, dh> y = new HashMap();
    private static final int z = 0;
    private byte B;
    private e[] C;
    public String a;
    public String b;
    public int c;
    public String d;
    public bm e;
    public String f;
    public String g;
    public String h;
    public String i;
    public int j;
    public String k;

    /* compiled from: AppInfo */
    private static class a extends di<ar> {
        private a() {
        }

        public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
            b(cyVar, (ar) bzVar);
        }

        public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
            a(cyVar, (ar) bzVar);
        }

        public void a(cy cyVar, ar arVar) throws cf {
            cyVar.j();
            while (true) {
                ct l = cyVar.l();
                if (l.b == (byte) 0) {
                    cyVar.k();
                    arVar.K();
                    return;
                }
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.a = cyVar.z();
                        arVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.b = cyVar.z();
                        arVar.b(true);
                        break;
                    case (short) 3:
                        if (l.b != (byte) 8) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.c = cyVar.w();
                        arVar.c(true);
                        break;
                    case (short) 4:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.d = cyVar.z();
                        arVar.d(true);
                        break;
                    case (short) 5:
                        if (l.b != (byte) 8) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.e = bm.a(cyVar.w());
                        arVar.e(true);
                        break;
                    case (short) 6:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.f = cyVar.z();
                        arVar.f(true);
                        break;
                    case (short) 7:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.g = cyVar.z();
                        arVar.g(true);
                        break;
                    case (short) 8:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.h = cyVar.z();
                        arVar.h(true);
                        break;
                    case (short) 9:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.i = cyVar.z();
                        arVar.i(true);
                        break;
                    case (short) 10:
                        if (l.b != (byte) 8) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.j = cyVar.w();
                        arVar.j(true);
                        break;
                    case (short) 11:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        arVar.k = cyVar.z();
                        arVar.k(true);
                        break;
                    default:
                        db.a(cyVar, l.b);
                        break;
                }
                cyVar.m();
            }
        }

        public void b(cy cyVar, ar arVar) throws cf {
            arVar.K();
            cyVar.a(ar.m);
            if (arVar.a != null) {
                cyVar.a(ar.n);
                cyVar.a(arVar.a);
                cyVar.c();
            }
            if (arVar.b != null && arVar.i()) {
                cyVar.a(ar.o);
                cyVar.a(arVar.b);
                cyVar.c();
            }
            if (arVar.l()) {
                cyVar.a(ar.p);
                cyVar.a(arVar.c);
                cyVar.c();
            }
            if (arVar.d != null && arVar.o()) {
                cyVar.a(ar.q);
                cyVar.a(arVar.d);
                cyVar.c();
            }
            if (arVar.e != null) {
                cyVar.a(ar.r);
                cyVar.a(arVar.e.a());
                cyVar.c();
            }
            if (arVar.f != null) {
                cyVar.a(ar.s);
                cyVar.a(arVar.f);
                cyVar.c();
            }
            if (arVar.g != null) {
                cyVar.a(ar.t);
                cyVar.a(arVar.g);
                cyVar.c();
            }
            if (arVar.h != null && arVar.A()) {
                cyVar.a(ar.u);
                cyVar.a(arVar.h);
                cyVar.c();
            }
            if (arVar.i != null && arVar.D()) {
                cyVar.a(ar.v);
                cyVar.a(arVar.i);
                cyVar.c();
            }
            if (arVar.G()) {
                cyVar.a(ar.w);
                cyVar.a(arVar.j);
                cyVar.c();
            }
            if (arVar.k != null && arVar.J()) {
                cyVar.a(ar.x);
                cyVar.a(arVar.k);
                cyVar.c();
            }
            cyVar.d();
            cyVar.b();
        }
    }

    /* compiled from: AppInfo */
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

    /* compiled from: AppInfo */
    private static class c extends dj<ar> {
        private c() {
        }

        public void a(cy cyVar, ar arVar) throws cf {
            de deVar = (de) cyVar;
            deVar.a(arVar.a);
            deVar.a(arVar.e.a());
            deVar.a(arVar.f);
            deVar.a(arVar.g);
            BitSet bitSet = new BitSet();
            if (arVar.i()) {
                bitSet.set(0);
            }
            if (arVar.l()) {
                bitSet.set(1);
            }
            if (arVar.o()) {
                bitSet.set(2);
            }
            if (arVar.A()) {
                bitSet.set(3);
            }
            if (arVar.D()) {
                bitSet.set(4);
            }
            if (arVar.G()) {
                bitSet.set(5);
            }
            if (arVar.J()) {
                bitSet.set(6);
            }
            deVar.a(bitSet, 7);
            if (arVar.i()) {
                deVar.a(arVar.b);
            }
            if (arVar.l()) {
                deVar.a(arVar.c);
            }
            if (arVar.o()) {
                deVar.a(arVar.d);
            }
            if (arVar.A()) {
                deVar.a(arVar.h);
            }
            if (arVar.D()) {
                deVar.a(arVar.i);
            }
            if (arVar.G()) {
                deVar.a(arVar.j);
            }
            if (arVar.J()) {
                deVar.a(arVar.k);
            }
        }

        public void b(cy cyVar, ar arVar) throws cf {
            de deVar = (de) cyVar;
            arVar.a = deVar.z();
            arVar.a(true);
            arVar.e = bm.a(deVar.w());
            arVar.e(true);
            arVar.f = deVar.z();
            arVar.f(true);
            arVar.g = deVar.z();
            arVar.g(true);
            BitSet b = deVar.b(7);
            if (b.get(0)) {
                arVar.b = deVar.z();
                arVar.b(true);
            }
            if (b.get(1)) {
                arVar.c = deVar.w();
                arVar.c(true);
            }
            if (b.get(2)) {
                arVar.d = deVar.z();
                arVar.d(true);
            }
            if (b.get(3)) {
                arVar.h = deVar.z();
                arVar.h(true);
            }
            if (b.get(4)) {
                arVar.i = deVar.z();
                arVar.i(true);
            }
            if (b.get(5)) {
                arVar.j = deVar.w();
                arVar.j(true);
            }
            if (b.get(6)) {
                arVar.k = deVar.z();
                arVar.k(true);
            }
        }
    }

    /* compiled from: AppInfo */
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

    /* compiled from: AppInfo */
    public enum e implements cg {
        KEY((short) 1, "key"),
        b((short) 2, "version"),
        VERSION_INDEX((short) 3, "version_index"),
        PACKAGE_NAME((short) 4, LeDongLiHelper.PACKAGE_NAME),
        SDK_TYPE((short) 5, "sdk_type"),
        SDK_VERSION((short) 6, "sdk_version"),
        CHANNEL((short) 7, "channel"),
        WRAPPER_TYPE((short) 8, "wrapper_type"),
        WRAPPER_VERSION((short) 9, "wrapper_version"),
        VERTICAL_TYPE((short) 10, "vertical_type"),
        APP_SIGNATURE((short) 11, "app_signature");
        
        private static final Map<String, e> l = null;
        private final short m;
        private final String n;

        static {
            l = new HashMap();
            Iterator it = EnumSet.allOf(e.class).iterator();
            while (it.hasNext()) {
                e eVar = (e) it.next();
                l.put(eVar.b(), eVar);
            }
        }

        public static e a(int i) {
            switch (i) {
                case 1:
                    return KEY;
                case 2:
                    return b;
                case 3:
                    return VERSION_INDEX;
                case 4:
                    return PACKAGE_NAME;
                case 5:
                    return SDK_TYPE;
                case 6:
                    return SDK_VERSION;
                case 7:
                    return CHANNEL;
                case 8:
                    return WRAPPER_TYPE;
                case 9:
                    return WRAPPER_VERSION;
                case 10:
                    return VERTICAL_TYPE;
                case 11:
                    return APP_SIGNATURE;
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
            return (e) l.get(str);
        }

        private e(short s, String str) {
            this.m = s;
            this.n = str;
        }

        public short a() {
            return this.m;
        }

        public String b() {
            return this.n;
        }
    }

    public /* synthetic */ cg b(int i) {
        return d(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        y.put(di.class, new b());
        y.put(dj.class, new d());
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.KEY, new cl("key", (byte) 1, new cm((byte) 11)));
        enumMap.put(e.b, new cl("version", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.VERSION_INDEX, new cl("version_index", (byte) 2, new cm((byte) 8)));
        enumMap.put(e.PACKAGE_NAME, new cl(LeDongLiHelper.PACKAGE_NAME, (byte) 2, new cm((byte) 11)));
        enumMap.put(e.SDK_TYPE, new cl("sdk_type", (byte) 1, new ck(df.n, bm.class)));
        enumMap.put(e.SDK_VERSION, new cl("sdk_version", (byte) 1, new cm((byte) 11)));
        enumMap.put(e.CHANNEL, new cl("channel", (byte) 1, new cm((byte) 11)));
        enumMap.put(e.WRAPPER_TYPE, new cl("wrapper_type", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.WRAPPER_VERSION, new cl("wrapper_version", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.VERTICAL_TYPE, new cl("vertical_type", (byte) 2, new cm((byte) 8)));
        enumMap.put(e.APP_SIGNATURE, new cl("app_signature", (byte) 2, new cm((byte) 11)));
        l = Collections.unmodifiableMap(enumMap);
        cl.a(ar.class, l);
    }

    public ar() {
        this.B = (byte) 0;
        this.C = new e[]{e.b, e.VERSION_INDEX, e.PACKAGE_NAME, e.WRAPPER_TYPE, e.WRAPPER_VERSION, e.VERTICAL_TYPE, e.APP_SIGNATURE};
    }

    public ar(String str, bm bmVar, String str2, String str3) {
        this();
        this.a = str;
        this.e = bmVar;
        this.f = str2;
        this.g = str3;
    }

    public ar(ar arVar) {
        this.B = (byte) 0;
        this.C = new e[]{e.b, e.VERSION_INDEX, e.PACKAGE_NAME, e.WRAPPER_TYPE, e.WRAPPER_VERSION, e.VERTICAL_TYPE, e.APP_SIGNATURE};
        this.B = arVar.B;
        if (arVar.e()) {
            this.a = arVar.a;
        }
        if (arVar.i()) {
            this.b = arVar.b;
        }
        this.c = arVar.c;
        if (arVar.o()) {
            this.d = arVar.d;
        }
        if (arVar.r()) {
            this.e = arVar.e;
        }
        if (arVar.u()) {
            this.f = arVar.f;
        }
        if (arVar.x()) {
            this.g = arVar.g;
        }
        if (arVar.A()) {
            this.h = arVar.h;
        }
        if (arVar.D()) {
            this.i = arVar.i;
        }
        this.j = arVar.j;
        if (arVar.J()) {
            this.k = arVar.k;
        }
    }

    public ar a() {
        return new ar(this);
    }

    public void b() {
        this.a = null;
        this.b = null;
        c(false);
        this.c = 0;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        j(false);
        this.j = 0;
        this.k = null;
    }

    public String c() {
        return this.a;
    }

    public ar a(String str) {
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

    public ar b(String str) {
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

    public int j() {
        return this.c;
    }

    public ar a(int i) {
        this.c = i;
        c(true);
        return this;
    }

    public void k() {
        this.B = bw.b(this.B, 0);
    }

    public boolean l() {
        return bw.a(this.B, 0);
    }

    public void c(boolean z) {
        this.B = bw.a(this.B, 0, z);
    }

    public String m() {
        return this.d;
    }

    public ar c(String str) {
        this.d = str;
        return this;
    }

    public void n() {
        this.d = null;
    }

    public boolean o() {
        return this.d != null;
    }

    public void d(boolean z) {
        if (!z) {
            this.d = null;
        }
    }

    public bm p() {
        return this.e;
    }

    public ar a(bm bmVar) {
        this.e = bmVar;
        return this;
    }

    public void q() {
        this.e = null;
    }

    public boolean r() {
        return this.e != null;
    }

    public void e(boolean z) {
        if (!z) {
            this.e = null;
        }
    }

    public String s() {
        return this.f;
    }

    public ar d(String str) {
        this.f = str;
        return this;
    }

    public void t() {
        this.f = null;
    }

    public boolean u() {
        return this.f != null;
    }

    public void f(boolean z) {
        if (!z) {
            this.f = null;
        }
    }

    public String v() {
        return this.g;
    }

    public ar e(String str) {
        this.g = str;
        return this;
    }

    public void w() {
        this.g = null;
    }

    public boolean x() {
        return this.g != null;
    }

    public void g(boolean z) {
        if (!z) {
            this.g = null;
        }
    }

    public String y() {
        return this.h;
    }

    public ar f(String str) {
        this.h = str;
        return this;
    }

    public void z() {
        this.h = null;
    }

    public boolean A() {
        return this.h != null;
    }

    public void h(boolean z) {
        if (!z) {
            this.h = null;
        }
    }

    public String B() {
        return this.i;
    }

    public ar g(String str) {
        this.i = str;
        return this;
    }

    public void C() {
        this.i = null;
    }

    public boolean D() {
        return this.i != null;
    }

    public void i(boolean z) {
        if (!z) {
            this.i = null;
        }
    }

    public int E() {
        return this.j;
    }

    public ar c(int i) {
        this.j = i;
        j(true);
        return this;
    }

    public void F() {
        this.B = bw.b(this.B, 1);
    }

    public boolean G() {
        return bw.a(this.B, 1);
    }

    public void j(boolean z) {
        this.B = bw.a(this.B, 1, z);
    }

    public String H() {
        return this.k;
    }

    public ar h(String str) {
        this.k = str;
        return this;
    }

    public void I() {
        this.k = null;
    }

    public boolean J() {
        return this.k != null;
    }

    public void k(boolean z) {
        if (!z) {
            this.k = null;
        }
    }

    public e d(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) y.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) y.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("AppInfo(");
        stringBuilder.append("key:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        if (i()) {
            stringBuilder.append(", ");
            stringBuilder.append("version:");
            if (this.b == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.b);
            }
        }
        if (l()) {
            stringBuilder.append(", ");
            stringBuilder.append("version_index:");
            stringBuilder.append(this.c);
        }
        if (o()) {
            stringBuilder.append(", ");
            stringBuilder.append("package_name:");
            if (this.d == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.d);
            }
        }
        stringBuilder.append(", ");
        stringBuilder.append("sdk_type:");
        if (this.e == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.e);
        }
        stringBuilder.append(", ");
        stringBuilder.append("sdk_version:");
        if (this.f == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.f);
        }
        stringBuilder.append(", ");
        stringBuilder.append("channel:");
        if (this.g == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.g);
        }
        if (A()) {
            stringBuilder.append(", ");
            stringBuilder.append("wrapper_type:");
            if (this.h == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.h);
            }
        }
        if (D()) {
            stringBuilder.append(", ");
            stringBuilder.append("wrapper_version:");
            if (this.i == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.i);
            }
        }
        if (G()) {
            stringBuilder.append(", ");
            stringBuilder.append("vertical_type:");
            stringBuilder.append(this.j);
        }
        if (J()) {
            stringBuilder.append(", ");
            stringBuilder.append("app_signature:");
            if (this.k == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.k);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void K() throws cf {
        if (this.a == null) {
            throw new cz("Required field 'key' was not present! Struct: " + toString());
        } else if (this.e == null) {
            throw new cz("Required field 'sdk_type' was not present! Struct: " + toString());
        } else if (this.f == null) {
            throw new cz("Required field 'sdk_version' was not present! Struct: " + toString());
        } else if (this.g == null) {
            throw new cz("Required field 'channel' was not present! Struct: " + toString());
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
            this.B = (byte) 0;
            a(new cs(new dk((InputStream) objectInputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }
}
