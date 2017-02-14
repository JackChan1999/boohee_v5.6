package u.aly;

import android.content.Context;

/* compiled from: UMIdTracker */
public class j extends a {
    private static final String a = "idmd5";
    private Context b;

    public j(Context context) {
        super(a);
        this.b = context;
    }

    public String f() {
        return bt.g(this.b);
    }
}
