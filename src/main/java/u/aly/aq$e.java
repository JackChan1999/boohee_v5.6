package u.aly;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: ActiveUser */
public enum aq$e implements cg {
    PROVIDER((short) 1, "provider"),
    PUID((short) 2, "puid");
    
    private static final Map<String, aq$e> c = null;
    private final short d;
    private final String e;

    static {
        c = new HashMap();
        Iterator it = EnumSet.allOf(aq$e.class).iterator();
        while (it.hasNext()) {
            aq$e u_aly_aq_e = (aq$e) it.next();
            c.put(u_aly_aq_e.b(), u_aly_aq_e);
        }
    }

    public static aq$e a(int i) {
        switch (i) {
            case 1:
                return PROVIDER;
            case 2:
                return PUID;
            default:
                return null;
        }
    }

    public static aq$e b(int i) {
        aq$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static aq$e a(String str) {
        return (aq$e) c.get(str);
    }

    private aq$e(short s, String str) {
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
