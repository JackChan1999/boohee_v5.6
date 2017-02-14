package com.alipay.apmobilesecuritysdk.face;

import android.content.Context;
import com.alipay.apmobilesecuritysdk.a.a;
import java.util.Map;

public class SecureSdk {
    public static synchronized String getApdidToken(Context context, Map<String, String> map) {
        String a;
        synchronized (SecureSdk.class) {
            a = new a(context).a((Map) map);
        }
        return a;
    }
}
