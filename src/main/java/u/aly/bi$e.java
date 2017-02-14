package u.aly;

import com.boohee.modeldao.SportRecordDao;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Page */
public enum bi$e implements cg {
    PAGE_NAME((short) 1, "page_name"),
    DURATION((short) 2, SportRecordDao.DURATION);
    
    private static final Map<String, bi$e> c = null;
    private final short d;
    private final String e;

    static {
        c = new HashMap();
        Iterator it = EnumSet.allOf(bi$e.class).iterator();
        while (it.hasNext()) {
            bi$e u_aly_bi_e = (bi$e) it.next();
            c.put(u_aly_bi_e.b(), u_aly_bi_e);
        }
    }

    public static bi$e a(int i) {
        switch (i) {
            case 1:
                return PAGE_NAME;
            case 2:
                return DURATION;
            default:
                return null;
        }
    }

    public static bi$e b(int i) {
        bi$e a = a(i);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Field " + i + " doesn't exist!");
    }

    public static bi$e a(String str) {
        return (bi$e) c.get(str);
    }

    private bi$e(short s, String str) {
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
