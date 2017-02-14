package u.aly;

import android.content.Context;
import android.telephony.TelephonyManager;

/* compiled from: ImeiTracker */
public class f extends a {
    private static final String a = "imei";
    private Context b;

    public f(Context context) {
        super("imei");
        this.b = context;
    }

    public String f() {
        TelephonyManager telephonyManager = (TelephonyManager) this.b.getSystemService("phone");
        if (telephonyManager == null) {
        }
        try {
            if (bt.a(this.b, "android.permission.READ_PHONE_STATE")) {
                return telephonyManager.getDeviceId();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
