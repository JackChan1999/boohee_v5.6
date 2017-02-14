package u.aly;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: ClientStats */
public enum as$e implements cg {
    SUCCESSFUL_REQUESTS((short) 1, "successful_requests"),
    FAILED_REQUESTS((short) 2, "failed_requests"),
    LAST_REQUEST_SPENT_MS((short) 3, "last_request_spent_ms");
    
    private static final Map<String, as$e> d = null;
    private final short e;
    private final String f;

    static {
        d = new HashMap();
        Iterator it = EnumSet.allOf(as$e.class).iterator();
        while (it.hasNext()) {
            as$e u_aly_as_e = (as$e) it.next();
            d.put(u_aly_as_e.b(), u_aly_as_e);
        }
    }

    public static as$e a(int i) {
        switch (i) {
            case 1:
                return SUCCESSFUL_REQUESTS;
            case 2:
                return FAILED_REQUESTS;
            case 3:
                return LAST_REQUEST_SPENT_MS;
            default:
                return null;
        }
    }

    public static as$e b(int i) {
        as$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static as$e a(String str) {
        return (as$e) d.get(str);
    }

    private as$e(short s, String str) {
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
