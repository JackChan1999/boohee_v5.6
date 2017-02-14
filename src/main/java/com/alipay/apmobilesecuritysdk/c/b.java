package com.alipay.apmobilesecuritysdk.c;

import android.content.Context;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import java.util.HashMap;
import java.util.Map;

public final class b {
    public static synchronized Map<String, String> a(Context context, Map<String, String> map) {
        Map<String, String> hashMap;
        synchronized (b.class) {
            hashMap = new HashMap();
            String valueFromMap = CommonUtils.getValueFromMap(map, com.alipay.sdk.cons.b.c, "");
            String valueFromMap2 = CommonUtils.getValueFromMap(map, com.alipay.sdk.cons.b.g, "");
            String a = com.alipay.apmobilesecuritysdk.f.b.a(context);
            String valueFromMap3 = CommonUtils.getValueFromMap(map, "userId", "");
            hashMap.put("AC1", valueFromMap);
            hashMap.put("AC2", valueFromMap2);
            hashMap.put("AC3", "");
            hashMap.put("AC4", a);
            hashMap.put("AC5", valueFromMap3);
        }
        return hashMap;
    }
}
