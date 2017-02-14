package com.alipay.apmobilesecuritysdk.c;

import android.content.Context;
import com.alipay.security.mobile.module.deviceinfo.EnvironmentInfo;
import java.util.HashMap;
import java.util.Map;

public final class d {
    public static synchronized Map<String, String> a(Context context) {
        Map<String, String> hashMap;
        synchronized (d.class) {
            EnvironmentInfo instance = EnvironmentInfo.getInstance();
            hashMap = new HashMap();
            hashMap.put("AE1", instance.getOSName());
            hashMap.put("AE2", (instance.isRooted() ? "1" : "0"));
            hashMap.put("AE3", (instance.isEmulator(context) ? "1" : "0"));
            hashMap.put("AE4", instance.getProductBoard());
            hashMap.put("AE5", instance.getProductBrand());
            hashMap.put("AE6", instance.getProductDevice());
            hashMap.put("AE7", instance.getBuildDisplayId());
            hashMap.put("AE8", instance.getBuildVersionIncremental());
            hashMap.put("AE9", instance.getProductManufacturer());
            hashMap.put("AE10", instance.getProductModel());
            hashMap.put("AE11", instance.getProductName());
            hashMap.put("AE12", instance.getBuildVersionRelease());
            hashMap.put("AE13", instance.getBuildVersionSDK());
            hashMap.put("AE14", instance.getBuildTags());
            hashMap.put("AE15", instance.getKernelQemu());
        }
        return hashMap;
    }
}
