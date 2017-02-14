package u.aly;

import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
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

/* compiled from: DeviceInfo */
public class au implements Serializable, Cloneable, bz<au, e> {
    private static final ct A = new ct(SocializeProtocolConstants.PROTOCOL_KEY_OS, (byte) 11, (short) 7);
    private static final ct B = new ct("os_version", (byte) 11, (short) 8);
    private static final ct C = new ct("resolution", (byte) 12, (short) 9);
    private static final ct D = new ct("is_jailbroken", (byte) 2, (short) 10);
    private static final ct E = new ct("is_pirated", (byte) 2, (short) 11);
    private static final ct F = new ct("device_board", (byte) 11, (short) 12);
    private static final ct G = new ct("device_brand", (byte) 11, (short) 13);
    private static final ct H = new ct("device_manutime", (byte) 10, (short) 14);
    private static final ct I = new ct("device_manufacturer", (byte) 11, (short) 15);
    private static final ct J = new ct("device_manuid", (byte) 11, (short) 16);
    private static final ct K = new ct("device_name", (byte) 11, (short) 17);
    private static final ct L = new ct("wp_device", (byte) 11, (short) 18);
    private static final Map<Class<? extends dg>, dh> M = new HashMap();
    private static final int N = 0;
    private static final int O = 1;
    private static final int P = 2;
    public static final Map<e, cl> s;
    private static final dd t = new dd("DeviceInfo");
    private static final ct u = new ct("device_id", (byte) 11, (short) 1);
    private static final ct v = new ct("idmd5", (byte) 11, (short) 2);
    private static final ct w = new ct("mac_address", (byte) 11, (short) 3);
    private static final ct x = new ct("open_udid", (byte) 11, (short) 4);
    private static final ct y = new ct("model", (byte) 11, (short) 5);
    private static final ct z = new ct("cpu", (byte) 11, (short) 6);
    private byte Q;
    private e[] R;
    public String a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f;
    public String g;
    public String h;
    public bk i;
    public boolean j;
    public boolean k;
    public String l;
    public String m;
    public long n;
    public String o;
    public String p;
    public String q;
    public String r;

    /* compiled from: DeviceInfo */
    private static class a extends di<au> {
        private a() {
        }

        public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
            b(cyVar, (au) bzVar);
        }

        public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
            a(cyVar, (au) bzVar);
        }

        public void a(cy cyVar, au auVar) throws cf {
            cyVar.j();
            while (true) {
                ct l = cyVar.l();
                if (l.b == (byte) 0) {
                    cyVar.k();
                    auVar.af();
                    return;
                }
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.a = cyVar.z();
                        auVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.b = cyVar.z();
                        auVar.b(true);
                        break;
                    case (short) 3:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.c = cyVar.z();
                        auVar.c(true);
                        break;
                    case (short) 4:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.d = cyVar.z();
                        auVar.d(true);
                        break;
                    case (short) 5:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.e = cyVar.z();
                        auVar.e(true);
                        break;
                    case (short) 6:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.f = cyVar.z();
                        auVar.f(true);
                        break;
                    case (short) 7:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.g = cyVar.z();
                        auVar.g(true);
                        break;
                    case (short) 8:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.h = cyVar.z();
                        auVar.h(true);
                        break;
                    case (short) 9:
                        if (l.b != (byte) 12) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.i = new bk();
                        auVar.i.a(cyVar);
                        auVar.i(true);
                        break;
                    case (short) 10:
                        if (l.b != (byte) 2) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.j = cyVar.t();
                        auVar.k(true);
                        break;
                    case (short) 11:
                        if (l.b != (byte) 2) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.k = cyVar.t();
                        auVar.m(true);
                        break;
                    case (short) 12:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.l = cyVar.z();
                        auVar.n(true);
                        break;
                    case (short) 13:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.m = cyVar.z();
                        auVar.o(true);
                        break;
                    case (short) 14:
                        if (l.b != (byte) 10) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.n = cyVar.x();
                        auVar.p(true);
                        break;
                    case (short) 15:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.o = cyVar.z();
                        auVar.q(true);
                        break;
                    case (short) 16:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.p = cyVar.z();
                        auVar.r(true);
                        break;
                    case (short) 17:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.q = cyVar.z();
                        auVar.s(true);
                        break;
                    case (short) 18:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        auVar.r = cyVar.z();
                        auVar.t(true);
                        break;
                    default:
                        db.a(cyVar, l.b);
                        break;
                }
                cyVar.m();
            }
        }

        public void b(cy cyVar, au auVar) throws cf {
            auVar.af();
            cyVar.a(au.t);
            if (auVar.a != null && auVar.e()) {
                cyVar.a(au.u);
                cyVar.a(auVar.a);
                cyVar.c();
            }
            if (auVar.b != null && auVar.i()) {
                cyVar.a(au.v);
                cyVar.a(auVar.b);
                cyVar.c();
            }
            if (auVar.c != null && auVar.l()) {
                cyVar.a(au.w);
                cyVar.a(auVar.c);
                cyVar.c();
            }
            if (auVar.d != null && auVar.o()) {
                cyVar.a(au.x);
                cyVar.a(auVar.d);
                cyVar.c();
            }
            if (auVar.e != null && auVar.r()) {
                cyVar.a(au.y);
                cyVar.a(auVar.e);
                cyVar.c();
            }
            if (auVar.f != null && auVar.u()) {
                cyVar.a(au.z);
                cyVar.a(auVar.f);
                cyVar.c();
            }
            if (auVar.g != null && auVar.x()) {
                cyVar.a(au.A);
                cyVar.a(auVar.g);
                cyVar.c();
            }
            if (auVar.h != null && auVar.A()) {
                cyVar.a(au.B);
                cyVar.a(auVar.h);
                cyVar.c();
            }
            if (auVar.i != null && auVar.D()) {
                cyVar.a(au.C);
                auVar.i.b(cyVar);
                cyVar.c();
            }
            if (auVar.G()) {
                cyVar.a(au.D);
                cyVar.a(auVar.j);
                cyVar.c();
            }
            if (auVar.J()) {
                cyVar.a(au.E);
                cyVar.a(auVar.k);
                cyVar.c();
            }
            if (auVar.l != null && auVar.M()) {
                cyVar.a(au.F);
                cyVar.a(auVar.l);
                cyVar.c();
            }
            if (auVar.m != null && auVar.P()) {
                cyVar.a(au.G);
                cyVar.a(auVar.m);
                cyVar.c();
            }
            if (auVar.S()) {
                cyVar.a(au.H);
                cyVar.a(auVar.n);
                cyVar.c();
            }
            if (auVar.o != null && auVar.V()) {
                cyVar.a(au.I);
                cyVar.a(auVar.o);
                cyVar.c();
            }
            if (auVar.p != null && auVar.Y()) {
                cyVar.a(au.J);
                cyVar.a(auVar.p);
                cyVar.c();
            }
            if (auVar.q != null && auVar.ab()) {
                cyVar.a(au.K);
                cyVar.a(auVar.q);
                cyVar.c();
            }
            if (auVar.r != null && auVar.ae()) {
                cyVar.a(au.L);
                cyVar.a(auVar.r);
                cyVar.c();
            }
            cyVar.d();
            cyVar.b();
        }
    }

    /* compiled from: DeviceInfo */
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

    /* compiled from: DeviceInfo */
    private static class c extends dj<au> {
        private c() {
        }

        public void a(cy cyVar, au auVar) throws cf {
            cyVar = (de) cyVar;
            BitSet bitSet = new BitSet();
            if (auVar.e()) {
                bitSet.set(0);
            }
            if (auVar.i()) {
                bitSet.set(1);
            }
            if (auVar.l()) {
                bitSet.set(2);
            }
            if (auVar.o()) {
                bitSet.set(3);
            }
            if (auVar.r()) {
                bitSet.set(4);
            }
            if (auVar.u()) {
                bitSet.set(5);
            }
            if (auVar.x()) {
                bitSet.set(6);
            }
            if (auVar.A()) {
                bitSet.set(7);
            }
            if (auVar.D()) {
                bitSet.set(8);
            }
            if (auVar.G()) {
                bitSet.set(9);
            }
            if (auVar.J()) {
                bitSet.set(10);
            }
            if (auVar.M()) {
                bitSet.set(11);
            }
            if (auVar.P()) {
                bitSet.set(12);
            }
            if (auVar.S()) {
                bitSet.set(13);
            }
            if (auVar.V()) {
                bitSet.set(14);
            }
            if (auVar.Y()) {
                bitSet.set(15);
            }
            if (auVar.ab()) {
                bitSet.set(16);
            }
            if (auVar.ae()) {
                bitSet.set(17);
            }
            cyVar.a(bitSet, 18);
            if (auVar.e()) {
                cyVar.a(auVar.a);
            }
            if (auVar.i()) {
                cyVar.a(auVar.b);
            }
            if (auVar.l()) {
                cyVar.a(auVar.c);
            }
            if (auVar.o()) {
                cyVar.a(auVar.d);
            }
            if (auVar.r()) {
                cyVar.a(auVar.e);
            }
            if (auVar.u()) {
                cyVar.a(auVar.f);
            }
            if (auVar.x()) {
                cyVar.a(auVar.g);
            }
            if (auVar.A()) {
                cyVar.a(auVar.h);
            }
            if (auVar.D()) {
                auVar.i.b(cyVar);
            }
            if (auVar.G()) {
                cyVar.a(auVar.j);
            }
            if (auVar.J()) {
                cyVar.a(auVar.k);
            }
            if (auVar.M()) {
                cyVar.a(auVar.l);
            }
            if (auVar.P()) {
                cyVar.a(auVar.m);
            }
            if (auVar.S()) {
                cyVar.a(auVar.n);
            }
            if (auVar.V()) {
                cyVar.a(auVar.o);
            }
            if (auVar.Y()) {
                cyVar.a(auVar.p);
            }
            if (auVar.ab()) {
                cyVar.a(auVar.q);
            }
            if (auVar.ae()) {
                cyVar.a(auVar.r);
            }
        }

        public void b(cy cyVar, au auVar) throws cf {
            cyVar = (de) cyVar;
            BitSet b = cyVar.b(18);
            if (b.get(0)) {
                auVar.a = cyVar.z();
                auVar.a(true);
            }
            if (b.get(1)) {
                auVar.b = cyVar.z();
                auVar.b(true);
            }
            if (b.get(2)) {
                auVar.c = cyVar.z();
                auVar.c(true);
            }
            if (b.get(3)) {
                auVar.d = cyVar.z();
                auVar.d(true);
            }
            if (b.get(4)) {
                auVar.e = cyVar.z();
                auVar.e(true);
            }
            if (b.get(5)) {
                auVar.f = cyVar.z();
                auVar.f(true);
            }
            if (b.get(6)) {
                auVar.g = cyVar.z();
                auVar.g(true);
            }
            if (b.get(7)) {
                auVar.h = cyVar.z();
                auVar.h(true);
            }
            if (b.get(8)) {
                auVar.i = new bk();
                auVar.i.a(cyVar);
                auVar.i(true);
            }
            if (b.get(9)) {
                auVar.j = cyVar.t();
                auVar.k(true);
            }
            if (b.get(10)) {
                auVar.k = cyVar.t();
                auVar.m(true);
            }
            if (b.get(11)) {
                auVar.l = cyVar.z();
                auVar.n(true);
            }
            if (b.get(12)) {
                auVar.m = cyVar.z();
                auVar.o(true);
            }
            if (b.get(13)) {
                auVar.n = cyVar.x();
                auVar.p(true);
            }
            if (b.get(14)) {
                auVar.o = cyVar.z();
                auVar.q(true);
            }
            if (b.get(15)) {
                auVar.p = cyVar.z();
                auVar.r(true);
            }
            if (b.get(16)) {
                auVar.q = cyVar.z();
                auVar.s(true);
            }
            if (b.get(17)) {
                auVar.r = cyVar.z();
                auVar.t(true);
            }
        }
    }

    /* compiled from: DeviceInfo */
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

    /* compiled from: DeviceInfo */
    public enum e implements cg {
        DEVICE_ID((short) 1, "device_id"),
        IDMD5((short) 2, "idmd5"),
        MAC_ADDRESS((short) 3, "mac_address"),
        OPEN_UDID((short) 4, "open_udid"),
        MODEL((short) 5, "model"),
        CPU((short) 6, "cpu"),
        OS((short) 7, SocializeProtocolConstants.PROTOCOL_KEY_OS),
        OS_VERSION((short) 8, "os_version"),
        RESOLUTION((short) 9, "resolution"),
        IS_JAILBROKEN((short) 10, "is_jailbroken"),
        IS_PIRATED((short) 11, "is_pirated"),
        DEVICE_BOARD((short) 12, "device_board"),
        DEVICE_BRAND((short) 13, "device_brand"),
        DEVICE_MANUTIME((short) 14, "device_manutime"),
        DEVICE_MANUFACTURER((short) 15, "device_manufacturer"),
        DEVICE_MANUID((short) 16, "device_manuid"),
        DEVICE_NAME((short) 17, "device_name"),
        WP_DEVICE((short) 18, "wp_device");
        
        private static final Map<String, e> s = null;
        private final short t;
        private final String u;

        static {
            s = new HashMap();
            Iterator it = EnumSet.allOf(e.class).iterator();
            while (it.hasNext()) {
                e eVar = (e) it.next();
                s.put(eVar.b(), eVar);
            }
        }

        public static e a(int i) {
            switch (i) {
                case 1:
                    return DEVICE_ID;
                case 2:
                    return IDMD5;
                case 3:
                    return MAC_ADDRESS;
                case 4:
                    return OPEN_UDID;
                case 5:
                    return MODEL;
                case 6:
                    return CPU;
                case 7:
                    return OS;
                case 8:
                    return OS_VERSION;
                case 9:
                    return RESOLUTION;
                case 10:
                    return IS_JAILBROKEN;
                case 11:
                    return IS_PIRATED;
                case 12:
                    return DEVICE_BOARD;
                case 13:
                    return DEVICE_BRAND;
                case 14:
                    return DEVICE_MANUTIME;
                case 15:
                    return DEVICE_MANUFACTURER;
                case 16:
                    return DEVICE_MANUID;
                case 17:
                    return DEVICE_NAME;
                case 18:
                    return WP_DEVICE;
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
            return (e) s.get(str);
        }

        private e(short s, String str) {
            this.t = s;
            this.u = str;
        }

        public short a() {
            return this.t;
        }

        public String b() {
            return this.u;
        }
    }

    public /* synthetic */ cg b(int i) {
        return a(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        M.put(di.class, new b());
        M.put(dj.class, new d());
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.DEVICE_ID, new cl("device_id", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.IDMD5, new cl("idmd5", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.MAC_ADDRESS, new cl("mac_address", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.OPEN_UDID, new cl("open_udid", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.MODEL, new cl("model", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.CPU, new cl("cpu", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.OS, new cl(SocializeProtocolConstants.PROTOCOL_KEY_OS, (byte) 2, new cm((byte) 11)));
        enumMap.put(e.OS_VERSION, new cl("os_version", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.RESOLUTION, new cl("resolution", (byte) 2, new cq((byte) 12, bk.class)));
        enumMap.put(e.IS_JAILBROKEN, new cl("is_jailbroken", (byte) 2, new cm((byte) 2)));
        enumMap.put(e.IS_PIRATED, new cl("is_pirated", (byte) 2, new cm((byte) 2)));
        enumMap.put(e.DEVICE_BOARD, new cl("device_board", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.DEVICE_BRAND, new cl("device_brand", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.DEVICE_MANUTIME, new cl("device_manutime", (byte) 2, new cm((byte) 10)));
        enumMap.put(e.DEVICE_MANUFACTURER, new cl("device_manufacturer", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.DEVICE_MANUID, new cl("device_manuid", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.DEVICE_NAME, new cl("device_name", (byte) 2, new cm((byte) 11)));
        enumMap.put(e.WP_DEVICE, new cl("wp_device", (byte) 2, new cm((byte) 11)));
        s = Collections.unmodifiableMap(enumMap);
        cl.a(au.class, s);
    }

    public au() {
        this.Q = (byte) 0;
        this.R = new e[]{e.DEVICE_ID, e.IDMD5, e.MAC_ADDRESS, e.OPEN_UDID, e.MODEL, e.CPU, e.OS, e.OS_VERSION, e.RESOLUTION, e.IS_JAILBROKEN, e.IS_PIRATED, e.DEVICE_BOARD, e.DEVICE_BRAND, e.DEVICE_MANUTIME, e.DEVICE_MANUFACTURER, e.DEVICE_MANUID, e.DEVICE_NAME, e.WP_DEVICE};
    }

    public au(au auVar) {
        this.Q = (byte) 0;
        this.R = new e[]{e.DEVICE_ID, e.IDMD5, e.MAC_ADDRESS, e.OPEN_UDID, e.MODEL, e.CPU, e.OS, e.OS_VERSION, e.RESOLUTION, e.IS_JAILBROKEN, e.IS_PIRATED, e.DEVICE_BOARD, e.DEVICE_BRAND, e.DEVICE_MANUTIME, e.DEVICE_MANUFACTURER, e.DEVICE_MANUID, e.DEVICE_NAME, e.WP_DEVICE};
        this.Q = auVar.Q;
        if (auVar.e()) {
            this.a = auVar.a;
        }
        if (auVar.i()) {
            this.b = auVar.b;
        }
        if (auVar.l()) {
            this.c = auVar.c;
        }
        if (auVar.o()) {
            this.d = auVar.d;
        }
        if (auVar.r()) {
            this.e = auVar.e;
        }
        if (auVar.u()) {
            this.f = auVar.f;
        }
        if (auVar.x()) {
            this.g = auVar.g;
        }
        if (auVar.A()) {
            this.h = auVar.h;
        }
        if (auVar.D()) {
            this.i = new bk(auVar.i);
        }
        this.j = auVar.j;
        this.k = auVar.k;
        if (auVar.M()) {
            this.l = auVar.l;
        }
        if (auVar.P()) {
            this.m = auVar.m;
        }
        this.n = auVar.n;
        if (auVar.V()) {
            this.o = auVar.o;
        }
        if (auVar.Y()) {
            this.p = auVar.p;
        }
        if (auVar.ab()) {
            this.q = auVar.q;
        }
        if (auVar.ae()) {
            this.r = auVar.r;
        }
    }

    public au a() {
        return new au(this);
    }

    public void b() {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        k(false);
        this.j = false;
        m(false);
        this.k = false;
        this.l = null;
        this.m = null;
        p(false);
        this.n = 0;
        this.o = null;
        this.p = null;
        this.q = null;
        this.r = null;
    }

    public String c() {
        return this.a;
    }

    public au a(String str) {
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

    public au b(String str) {
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

    public String j() {
        return this.c;
    }

    public au c(String str) {
        this.c = str;
        return this;
    }

    public void k() {
        this.c = null;
    }

    public boolean l() {
        return this.c != null;
    }

    public void c(boolean z) {
        if (!z) {
            this.c = null;
        }
    }

    public String m() {
        return this.d;
    }

    public au d(String str) {
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

    public String p() {
        return this.e;
    }

    public au e(String str) {
        this.e = str;
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

    public au f(String str) {
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

    public au g(String str) {
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

    public au h(String str) {
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

    public bk B() {
        return this.i;
    }

    public au a(bk bkVar) {
        this.i = bkVar;
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

    public boolean E() {
        return this.j;
    }

    public au j(boolean z) {
        this.j = z;
        k(true);
        return this;
    }

    public void F() {
        this.Q = bw.b(this.Q, 0);
    }

    public boolean G() {
        return bw.a(this.Q, 0);
    }

    public void k(boolean z) {
        this.Q = bw.a(this.Q, 0, z);
    }

    public boolean H() {
        return this.k;
    }

    public au l(boolean z) {
        this.k = z;
        m(true);
        return this;
    }

    public void I() {
        this.Q = bw.b(this.Q, 1);
    }

    public boolean J() {
        return bw.a(this.Q, 1);
    }

    public void m(boolean z) {
        this.Q = bw.a(this.Q, 1, z);
    }

    public String K() {
        return this.l;
    }

    public au i(String str) {
        this.l = str;
        return this;
    }

    public void L() {
        this.l = null;
    }

    public boolean M() {
        return this.l != null;
    }

    public void n(boolean z) {
        if (!z) {
            this.l = null;
        }
    }

    public String N() {
        return this.m;
    }

    public au j(String str) {
        this.m = str;
        return this;
    }

    public void O() {
        this.m = null;
    }

    public boolean P() {
        return this.m != null;
    }

    public void o(boolean z) {
        if (!z) {
            this.m = null;
        }
    }

    public long Q() {
        return this.n;
    }

    public au a(long j) {
        this.n = j;
        p(true);
        return this;
    }

    public void R() {
        this.Q = bw.b(this.Q, 2);
    }

    public boolean S() {
        return bw.a(this.Q, 2);
    }

    public void p(boolean z) {
        this.Q = bw.a(this.Q, 2, z);
    }

    public String T() {
        return this.o;
    }

    public au k(String str) {
        this.o = str;
        return this;
    }

    public void U() {
        this.o = null;
    }

    public boolean V() {
        return this.o != null;
    }

    public void q(boolean z) {
        if (!z) {
            this.o = null;
        }
    }

    public String W() {
        return this.p;
    }

    public au l(String str) {
        this.p = str;
        return this;
    }

    public void X() {
        this.p = null;
    }

    public boolean Y() {
        return this.p != null;
    }

    public void r(boolean z) {
        if (!z) {
            this.p = null;
        }
    }

    public String Z() {
        return this.q;
    }

    public au m(String str) {
        this.q = str;
        return this;
    }

    public void aa() {
        this.q = null;
    }

    public boolean ab() {
        return this.q != null;
    }

    public void s(boolean z) {
        if (!z) {
            this.q = null;
        }
    }

    public String ac() {
        return this.r;
    }

    public au n(String str) {
        this.r = str;
        return this;
    }

    public void ad() {
        this.r = null;
    }

    public boolean ae() {
        return this.r != null;
    }

    public void t(boolean z) {
        if (!z) {
            this.r = null;
        }
    }

    public e a(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) M.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) M.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        Object obj = null;
        StringBuilder stringBuilder = new StringBuilder("DeviceInfo(");
        Object obj2 = 1;
        if (e()) {
            stringBuilder.append("device_id:");
            if (this.a == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.a);
            }
            obj2 = null;
        }
        if (i()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("idmd5:");
            if (this.b == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.b);
            }
            obj2 = null;
        }
        if (l()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("mac_address:");
            if (this.c == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.c);
            }
            obj2 = null;
        }
        if (o()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("open_udid:");
            if (this.d == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.d);
            }
            obj2 = null;
        }
        if (r()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("model:");
            if (this.e == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.e);
            }
            obj2 = null;
        }
        if (u()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("cpu:");
            if (this.f == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.f);
            }
            obj2 = null;
        }
        if (x()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("os:");
            if (this.g == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.g);
            }
            obj2 = null;
        }
        if (A()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("os_version:");
            if (this.h == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.h);
            }
            obj2 = null;
        }
        if (D()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("resolution:");
            if (this.i == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.i);
            }
            obj2 = null;
        }
        if (G()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("is_jailbroken:");
            stringBuilder.append(this.j);
            obj2 = null;
        }
        if (J()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("is_pirated:");
            stringBuilder.append(this.k);
            obj2 = null;
        }
        if (M()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("device_board:");
            if (this.l == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.l);
            }
            obj2 = null;
        }
        if (P()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("device_brand:");
            if (this.m == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.m);
            }
            obj2 = null;
        }
        if (S()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("device_manutime:");
            stringBuilder.append(this.n);
            obj2 = null;
        }
        if (V()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("device_manufacturer:");
            if (this.o == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.o);
            }
            obj2 = null;
        }
        if (Y()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("device_manuid:");
            if (this.p == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.p);
            }
            obj2 = null;
        }
        if (ab()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("device_name:");
            if (this.q == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.q);
            }
        } else {
            obj = obj2;
        }
        if (ae()) {
            if (obj == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("wp_device:");
            if (this.r == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.r);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void af() throws cf {
        if (this.i != null) {
            this.i.j();
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
            this.Q = (byte) 0;
            a(new cs(new dk((InputStream) objectInputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }
}
