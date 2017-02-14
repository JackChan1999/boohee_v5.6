package u.aly;

import com.tencent.stat.DeviceInfo;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Location */
public enum bg$e implements cg {
    LAT((short) 1, "lat"),
    LNG((short) 2, "lng"),
    TS((short) 3, DeviceInfo.TAG_TIMESTAMPS);
    
    private static final Map<String, bg$e> d = null;
    private final short e;
    private final String f;

    static {
        d = new HashMap();
        Iterator it = EnumSet.allOf(bg$e.class).iterator();
        while (it.hasNext()) {
            bg$e u_aly_bg_e = (bg$e) it.next();
            d.put(u_aly_bg_e.b(), u_aly_bg_e);
        }
    }

    public static bg$e a(int i) {
        switch (i) {
            case 1:
                return LAT;
            case 2:
                return LNG;
            case 3:
                return TS;
            default:
                return null;
        }
    }

    public static bg$e b(int i) {
        bg$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static bg$e a(String str) {
        return (bg$e) d.get(str);
    }

    private bg$e(short s, String str) {
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
