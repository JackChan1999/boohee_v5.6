package u.aly;

import android.content.Context;

/* compiled from: IDFATracker */
public class d extends a {
    private static final String a = "idfa";
    private Context b;

    public d(Context context) {
        super(a);
        this.b = context;
    }

    public String f() {
        String a = br.a(this.b);
        return a == null ? "" : a;
    }
}
