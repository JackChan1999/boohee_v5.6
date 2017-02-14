package u.aly;

import com.boohee.modeldao.SportRecordDao;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Session */
public enum bn$e implements cg {
    ID((short) 1, "id"),
    START_TIME((short) 2, "start_time"),
    END_TIME((short) 3, "end_time"),
    DURATION((short) 4, SportRecordDao.DURATION),
    PAGES((short) 5, "pages"),
    LOCATIONS((short) 6, "locations"),
    TRAFFIC((short) 7, "traffic");
    
    private static final Map<String, bn$e> h = null;
    private final short i;
    private final String j;

    static {
        h = new HashMap();
        Iterator it = EnumSet.allOf(bn$e.class).iterator();
        while (it.hasNext()) {
            bn$e u_aly_bn_e = (bn$e) it.next();
            h.put(u_aly_bn_e.b(), u_aly_bn_e);
        }
    }

    public static bn$e a(int i) {
        switch (i) {
            case 1:
                return ID;
            case 2:
                return START_TIME;
            case 3:
                return END_TIME;
            case 4:
                return DURATION;
            case 5:
                return PAGES;
            case 6:
                return LOCATIONS;
            case 7:
                return TRAFFIC;
            default:
                return null;
        }
    }

    public static bn$e b(int i) {
        bn$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static bn$e a(String str) {
        return (bn$e) h.get(str);
    }

    private bn$e(short s, String str) {
        this.i = s;
        this.j = str;
    }

    public short a() {
        return this.i;
    }

    public String b() {
        return this.j;
    }
}
