package u.aly;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: ControlPolicy */
public enum at$e implements cg {
    LATENT((short) 1, "latent");
    
    private static final Map<String, at$e> b = null;
    private final short c;
    private final String d;

    static {
        b = new HashMap();
        Iterator it = EnumSet.allOf(at$e.class).iterator();
        while (it.hasNext()) {
            at$e u_aly_at_e = (at$e) it.next();
            b.put(u_aly_at_e.b(), u_aly_at_e);
        }
    }

    public static at$e a(int i) {
        switch (i) {
            case 1:
                return LATENT;
            default:
                return null;
        }
    }

    public static at$e b(int i) {
        at$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static at$e a(String str) {
        return (at$e) b.get(str);
    }

    private at$e(short s, String str) {
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
