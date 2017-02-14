package u.aly;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: InstantMsg */
public enum be$e implements cg {
    ID((short) 1, "id"),
    ERRORS((short) 2, "errors"),
    EVENTS((short) 3, "events"),
    GAME_EVENTS((short) 4, "game_events");
    
    private static final Map<String, be$e> e = null;
    private final short f;
    private final String g;

    static {
        e = new HashMap();
        Iterator it = EnumSet.allOf(be$e.class).iterator();
        while (it.hasNext()) {
            be$e u_aly_be_e = (be$e) it.next();
            e.put(u_aly_be_e.b(), u_aly_be_e);
        }
    }

    public static be$e a(int i) {
        switch (i) {
            case 1:
                return ID;
            case 2:
                return ERRORS;
            case 3:
                return EVENTS;
            case 4:
                return GAME_EVENTS;
            default:
                return null;
        }
    }

    public static be$e b(int i) {
        be$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static be$e a(String str) {
        return (be$e) e.get(str);
    }

    private be$e(short s, String str) {
        this.f = s;
        this.g = str;
    }

    public short a() {
        return this.f;
    }

    public String b() {
        return this.g;
    }
}
