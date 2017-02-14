package u.aly;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: IdTracking */
public enum bb$e implements cg {
    SNAPSHOTS((short) 1, "snapshots"),
    JOURNALS((short) 2, "journals"),
    CHECKSUM((short) 3, "checksum");
    
    private static final Map<String, bb$e> d = null;
    private final short e;
    private final String f;

    static {
        d = new HashMap();
        Iterator it = EnumSet.allOf(bb$e.class).iterator();
        while (it.hasNext()) {
            bb$e u_aly_bb_e = (bb$e) it.next();
            d.put(u_aly_bb_e.b(), u_aly_bb_e);
        }
    }

    public static bb$e a(int i) {
        switch (i) {
            case 1:
                return SNAPSHOTS;
            case 2:
                return JOURNALS;
            case 3:
                return CHECKSUM;
            default:
                return null;
        }
    }

    public static bb$e b(int i) {
        bb$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static bb$e a(String str) {
        return (bb$e) d.get(str);
    }

    private bb$e(short s, String str) {
        this.e = s;
        this.f = str;
    }

    public short a() {
        return this.e;
    }

    public String b() {
        return this.f;
    }
}
