package com.alipay.mobilesecuritysdk.face;

import android.content.Context;
import com.alipay.apmobilesecuritysdk.e.a;
import com.alipay.apmobilesecuritysdk.e.f;
import com.alipay.apmobilesecuritysdk.face.APSecuritySdk;
import com.alipay.sdk.cons.b;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import java.util.HashMap;
import java.util.Map;

public class SecurityClientMobile {
    public static synchronized String GetApdid(Context context, Map<String, String> map) {
        String a;
        synchronized (SecurityClientMobile.class) {
            Map hashMap = new HashMap();
            hashMap.put(b.g, CommonUtils.getValueFromMap(map, b.g, ""));
            hashMap.put(b.c, CommonUtils.getValueFromMap(map, b.c, ""));
            hashMap.put("userId", CommonUtils.getValueFromMap(map, "userId", ""));
            APSecuritySdk.getInstance(context).initToken(0, hashMap, null);
            a = f.a();
            if (CommonUtils.isBlank(a)) {
                com.alipay.apmobilesecuritysdk.e.b a2 = a.a(context);
                if (a2 == null || CommonUtils.isBlank(a2.a())) {
                    a = com.alipay.apmobilesecuritysdk.a.a.a.a(context);
                    if (CommonUtils.isBlank(a)) {
                        a = com.alipay.apmobilesecuritysdk.f.b.a(context);
                    }
                } else {
                    a = a2.a();
                }
            }
        }
        return a;
    }
}
