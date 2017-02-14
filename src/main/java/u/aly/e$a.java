package u.aly;

import android.content.Context;
import android.text.TextUtils;
import java.util.HashSet;
import java.util.Set;

/* compiled from: IdTracker */
public class e$a {
    private Context a;
    private Set<String> b = new HashSet();

    public e$a(Context context) {
        this.a = context;
    }

    public boolean a(String str) {
        return !this.b.contains(str);
    }

    public void b(String str) {
        this.b.add(str);
    }

    public void c(String str) {
        this.b.remove(str);
    }

    public void a() {
        if (!this.b.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String append : this.b) {
                stringBuilder.append(append);
                stringBuilder.append(',');
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            y.a(this.a).edit().putString("invld_id", stringBuilder.toString()).commit();
        }
    }

    public void b() {
        Object string = y.a(this.a).getString("invld_id", null);
        if (!TextUtils.isEmpty(string)) {
            String[] split = string.split(",");
            if (split != null) {
                for (CharSequence charSequence : split) {
                    if (!TextUtils.isEmpty(charSequence)) {
                        this.b.add(charSequence);
                    }
                }
            }
        }
    }
}
