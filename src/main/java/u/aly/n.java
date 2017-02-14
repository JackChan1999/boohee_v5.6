package u.aly;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* compiled from: Caretaker */
public class n {
    private final String a = "umeng_event_snapshot";
    private boolean b = false;
    private SharedPreferences c;
    private Map<String, ArrayList<af>> d = new HashMap();

    public n(Context context) {
        this.c = y.a(context, "umeng_event_snapshot");
    }

    public void a(boolean z) {
        this.b = z;
    }

    public int a(String str) {
        if (this.d.containsKey(str)) {
            return ((ArrayList) this.d.get(str)).size();
        }
        return 0;
    }

    public void a(String str, af afVar) {
        if (this.b) {
            d(str);
        }
        if (this.d.containsKey(str)) {
            ((ArrayList) this.d.get(str)).add(afVar);
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.add(afVar);
            this.d.put(str, arrayList);
        }
        if (this.b) {
            c(str);
        }
    }

    public af b(String str) {
        af afVar;
        if (this.b) {
            d(str);
        }
        if (this.d.containsKey(str)) {
            ArrayList arrayList = (ArrayList) this.d.get(str);
            if (arrayList.size() > 0) {
                afVar = (af) arrayList.remove(arrayList.size() - 1);
                if (this.b) {
                    c(str);
                }
                return afVar;
            }
        }
        afVar = null;
        if (this.b) {
            c(str);
        }
        return afVar;
    }

    private void c(String str) {
        String str2 = null;
        if (this.d.containsKey(str)) {
            Serializable serializable = (ArrayList) this.d.get(str);
            while (serializable.size() > 4) {
                serializable.remove(0);
            }
            str2 = v.a(serializable);
        }
        this.c.edit().putString(str, str2).commit();
    }

    private boolean d(String str) {
        if (this.d.containsKey(str)) {
            return true;
        }
        String string = this.c.getString(str, null);
        if (string != null) {
            ArrayList arrayList = (ArrayList) v.a(string);
            if (arrayList != null) {
                this.d.put(str, arrayList);
                return true;
            }
        }
        return false;
    }
}
