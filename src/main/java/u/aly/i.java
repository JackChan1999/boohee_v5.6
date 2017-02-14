package u.aly;

import android.os.Build;
import android.os.Build.VERSION;

/* compiled from: SerialTracker */
public class i extends a {
    private static final String a = "serial";

    public i() {
        super(a);
    }

    public String f() {
        if (VERSION.SDK_INT >= 9) {
            return Build.SERIAL;
        }
        return null;
    }
}
