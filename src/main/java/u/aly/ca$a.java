package u.aly;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: TBaseHelper */
class ca$a implements Comparator {
    private ca$a() {
    }

    public int compare(Object obj, Object obj2) {
        if (obj == null && obj2 == null) {
            return 0;
        }
        if (obj == null) {
            return -1;
        }
        if (obj2 == null) {
            return 1;
        }
        if (obj instanceof List) {
            return ca.a((List) obj, (List) obj2);
        }
        if (obj instanceof Set) {
            return ca.a((Set) obj, (Set) obj2);
        }
        if (obj instanceof Map) {
            return ca.a((Map) obj, (Map) obj2);
        }
        if (obj instanceof byte[]) {
            return ca.a((byte[]) obj, (byte[]) obj2);
        }
        return ca.a((Comparable) obj, (Comparable) obj2);
    }
}
