package u.aly;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: UALogEntry */
public enum bp$e implements cg {
    CLIENT_STATS((short) 1, "client_stats"),
    APP_INFO((short) 2, "app_info"),
    DEVICE_INFO((short) 3, "device_info"),
    MISC_INFO((short) 4, "misc_info"),
    ACTIVATE_MSG((short) 5, "activate_msg"),
    INSTANT_MSGS((short) 6, "instant_msgs"),
    SESSIONS((short) 7, "sessions"),
    IMPRINT((short) 8, "imprint"),
    ID_TRACKING((short) 9, "id_tracking"),
    ACTIVE_USER((short) 10, "active_user"),
    CONTROL_POLICY((short) 11, "control_policy"),
    GROUP_INFO((short) 12, "group_info");
    
    private static final Map<String, bp$e> m = null;
    private final short n;
    private final String o;

    static {
        m = new HashMap();
        Iterator it = EnumSet.allOf(bp$e.class).iterator();
        while (it.hasNext()) {
            bp$e u_aly_bp_e = (bp$e) it.next();
            m.put(u_aly_bp_e.b(), u_aly_bp_e);
        }
    }

    public static bp$e a(int i) {
        switch (i) {
            case 1:
                return CLIENT_STATS;
            case 2:
                return APP_INFO;
            case 3:
                return DEVICE_INFO;
            case 4:
                return MISC_INFO;
            case 5:
                return ACTIVATE_MSG;
            case 6:
                return INSTANT_MSGS;
            case 7:
                return SESSIONS;
            case 8:
                return IMPRINT;
            case 9:
                return ID_TRACKING;
            case 10:
                return ACTIVE_USER;
            case 11:
                return CONTROL_POLICY;
            case 12:
                return GROUP_INFO;
            default:
                return null;
        }
    }

    public static bp$e b(int i) {
        bp$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static bp$e a(String str) {
        return (bp$e) m.get(str);
    }

    private bp$e(short s, String str) {
        this.n = s;
        this.o = str;
    }

    public short a() {
        return this.n;
    }

    public String b() {
        return this.o;
    }
}
