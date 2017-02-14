package com.alipay.apmobilesecuritysdk.c;

import android.content.Context;
import com.alipay.apmobilesecuritysdk.e.e;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.deviceinfo.DeviceInfo;
import java.util.HashMap;
import java.util.Map;

public final class a {
    public static synchronized Map<String, String> a(Context context) {
        Map<String, String> hashMap;
        synchronized (a.class) {
            hashMap = new HashMap();
            hashMap.put("AA1", context.getPackageName());
            hashMap.put("AA2", b(context));
            hashMap.put("AA3", "security-sdk-token");
            hashMap.put("AA4", "3.0.2.20151027");
            String str = "AA5";
            com.alipay.security.mobile.module.a.b.a d = e.d(context);
            String str2 = d.b;
            String str3 = d.a;
            Object a = (CommonUtils.isBlank(str2) || CommonUtils.isBlank(str3)) ? "" : com.alipay.apmobilesecuritysdk.f.a.a(DeviceInfo.getInstance().getAllAppName(context), str3, str2);
            hashMap.put(str, a);
        }
        return hashMap;
    }

    private static String b(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16).versionName;
        } catch (Exception e) {
            return "0.0.0";
        }
    }
}
