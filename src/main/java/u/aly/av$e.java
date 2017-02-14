package u.aly;

import com.tencent.open.SocialConstants;
import com.tencent.stat.DeviceInfo;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Error */
public enum av$e implements cg {
    TS((short) 1, DeviceInfo.TAG_TIMESTAMPS),
    CONTEXT((short) 2, "context"),
    SOURCE((short) 3, SocialConstants.PARAM_SOURCE);
    
    private static final Map<String, av$e> d = null;
    private final short e;
    private final String f;

    static {
        d = new HashMap();
        Iterator it = EnumSet.allOf(av$e.class).iterator();
        while (it.hasNext()) {
            av$e u_aly_av_e = (av$e) it.next();
            d.put(u_aly_av_e.b(), u_aly_av_e);
        }
    }

    public static av$e a(int i) {
        switch (i) {
            case 1:
                return TS;
            case 2:
                return CONTEXT;
            case 3:
                return SOURCE;
            default:
                return null;
        }
    }

    public static av$e b(int i) {
        av$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static av$e a(String str) {
        return (av$e) d.get(str);
    }

    private av$e(short s, String str) {
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
