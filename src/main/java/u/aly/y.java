package u.aly;

import android.content.Context;
import android.content.SharedPreferences;

/* compiled from: PreferenceWrapper */
public class y {
    private static final String a = "umeng_general_config";

    private y() {
    }

    public static SharedPreferences a(Context context, String str) {
        return context.getSharedPreferences(str, 0);
    }

    public static SharedPreferences a(Context context) {
        return context.getSharedPreferences(a, 0);
    }
}
