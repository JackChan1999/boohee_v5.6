package u.aly;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Traffic */
public enum bo$e implements cg {
    UPLOAD_TRAFFIC((short) 1, "upload_traffic"),
    DOWNLOAD_TRAFFIC((short) 2, "download_traffic");
    
    private static final Map<String, bo$e> c = null;
    private final short d;
    private final String e;

    static {
        c = new HashMap();
        Iterator it = EnumSet.allOf(bo$e.class).iterator();
        while (it.hasNext()) {
            bo$e u_aly_bo_e = (bo$e) it.next();
            c.put(u_aly_bo_e.b(), u_aly_bo_e);
        }
    }

    public static bo$e a(int i) {
        switch (i) {
            case 1:
                return UPLOAD_TRAFFIC;
            case 2:
                return DOWNLOAD_TRAFFIC;
            default:
                return null;
        }
    }

    public static bo$e b(int i) {
        bo$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static bo$e a(String str) {
        return (bo$e) c.get(str);
    }

    private bo$e(short s, String str) {
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
