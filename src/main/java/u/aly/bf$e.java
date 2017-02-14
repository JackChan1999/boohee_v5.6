package u.aly;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Latent */
public enum bf$e implements cg {
    LATENCY((short) 1, "latency"),
    INTERVAL((short) 2, "interval");
    
    private static final Map<String, bf$e> c = null;
    private final short d;
    private final String e;

    static {
        c = new HashMap();
        Iterator it = EnumSet.allOf(bf$e.class).iterator();
        while (it.hasNext()) {
            bf$e u_aly_bf_e = (bf$e) it.next();
            c.put(u_aly_bf_e.b(), u_aly_bf_e);
        }
    }

    public static bf$e a(int i) {
        switch (i) {
            case 1:
                return LATENCY;
            case 2:
                return INTERVAL;
            default:
                return null;
        }
    }

    public static bf$e b(int i) {
        bf$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static bf$e a(String str) {
        return (bf$e) c.get(str);
    }

    private bf$e(short s, String str) {
        this.d = s;
        this.e = str;
    }

    public short a() {
        return this.d;
    }

    public String b() {
        return this.e;
    }
}
