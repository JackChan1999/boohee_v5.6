package u.aly;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.boohee.one.http.DnspodFree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/* compiled from: ViewPageTracker */
public class ae {
    private static final String a = "activities";
    private final Map<String, Long> b = new HashMap();
    private final ArrayList<ac> c = new ArrayList();

    public void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.b) {
                this.b.put(str, Long.valueOf(System.currentTimeMillis()));
            }
        }
    }

    public void b(String str) {
        if (!TextUtils.isEmpty(str)) {
            Long l;
            synchronized (this.b) {
                l = (Long) this.b.remove(str);
            }
            if (l == null) {
                bv.e("please call 'onPageStart(%s)' before onPageEnd", str);
                return;
            }
            long currentTimeMillis = System.currentTimeMillis() - l.longValue();
            synchronized (this.c) {
                this.c.add(new ac(str, currentTimeMillis));
            }
        }
    }

    public void a() {
        String str = null;
        long j = 0;
        synchronized (this.b) {
            for (Entry entry : this.b.entrySet()) {
                String str2;
                long j2;
                if (((Long) entry.getValue()).longValue() > j) {
                    long longValue = ((Long) entry.getValue()).longValue();
                    str2 = (String) entry.getKey();
                    j2 = longValue;
                } else {
                    j2 = j;
                    str2 = str;
                }
                str = str2;
                j = j2;
            }
        }
        if (str != null) {
            b(str);
        }
    }

    public void a(Context context) {
        SharedPreferences a = y.a(context);
        Editor edit = a.edit();
        if (this.c.size() > 0) {
            Object string = a.getString(a, "");
            StringBuilder stringBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(string)) {
                stringBuilder.append(string);
                stringBuilder.append(DnspodFree.IP_SPLIT);
            }
            synchronized (this.c) {
                Iterator it = this.c.iterator();
                while (it.hasNext()) {
                    ac acVar = (ac) it.next();
                    stringBuilder.append(String.format("[\"%s\",%d]", new Object[]{acVar.a, Long.valueOf(acVar.b)}));
                    stringBuilder.append(DnspodFree.IP_SPLIT);
                }
                this.c.clear();
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            edit.remove(a);
            edit.putString(a, stringBuilder.toString());
        }
        edit.commit();
    }

    public static List<bi> a(SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString(a, "");
        if ("".equals(string)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        try {
            String[] split = string.split(DnspodFree.IP_SPLIT);
            for (String str : split) {
                if (!TextUtils.isEmpty(str)) {
                    arrayList.add(new aj(str));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (arrayList.size() > 0) {
            return arrayList;
        }
        return null;
    }
}
