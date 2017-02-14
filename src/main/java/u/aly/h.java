package u.aly;

import android.content.Context;

/* compiled from: MacTracker */
public class h extends a {
    private static final String a = "mac";
    private Context b;

    public h(Context context) {
        super("mac");
        this.b = context;
    }

    public String f() {
        String str = null;
        try {
            str = bt.q(this.b);
        } catch (Exception e) {
        }
        return str;
    }
}
