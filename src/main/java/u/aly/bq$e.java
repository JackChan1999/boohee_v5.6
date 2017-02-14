package u.aly;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: UMEnvelope */
public enum bq$e implements cg {
    VERSION((short) 1, "version"),
    ADDRESS((short) 2, "address"),
    SIGNATURE((short) 3, "signature"),
    SERIAL_NUM((short) 4, "serial_num"),
    TS_SECS((short) 5, "ts_secs"),
    LENGTH((short) 6, "length"),
    ENTITY((short) 7, "entity"),
    GUID((short) 8, "guid"),
    CHECKSUM((short) 9, "checksum"),
    CODEX((short) 10, "codex");
    
    private static final Map<String, bq$e> k = null;
    private final short l;
    private final String m;

    static {
        k = new HashMap();
        Iterator it = EnumSet.allOf(bq$e.class).iterator();
        while (it.hasNext()) {
            bq$e u_aly_bq_e = (bq$e) it.next();
            k.put(u_aly_bq_e.b(), u_aly_bq_e);
        }
    }

    public static bq$e a(int i) {
        switch (i) {
            case 1:
                return VERSION;
            case 2:
                return ADDRESS;
            case 3:
                return SIGNATURE;
            case 4:
                return SERIAL_NUM;
            case 5:
                return TS_SECS;
            case 6:
                return LENGTH;
            case 7:
                return ENTITY;
            case 8:
                return GUID;
            case 9:
                return CHECKSUM;
            case 10:
                return CODEX;
            default:
                return null;
        }
    }

    public static bq$e b(int i) {
        bq$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static bq$e a(String str) {
        return (bq$e) k.get(str);
    }

    private bq$e(short s, String str) {
        this.l = s;
        this.m = str;
    }

    public short a() {
        return this.l;
    }

    public String b() {
        return this.m;
    }
}
