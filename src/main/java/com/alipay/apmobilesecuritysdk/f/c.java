package com.alipay.apmobilesecuritysdk.f;

import android.content.Context;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.commonutils.crypto.SecurityUtils;
import com.alipay.security.mobile.module.localstorage.PublicStorage;
import com.alipay.security.mobile.module.localstorage.SharePreferenceStorage;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public final class c {
    public static String a(Context context, String str, String str2) {
        String str3 = null;
        if (!(context == null || CommonUtils.isBlank(str) || CommonUtils.isBlank(str2))) {
            try {
                String dataFromSharePreference = SharePreferenceStorage.getDataFromSharePreference(context, str, str2, "");
                if (!CommonUtils.isBlank(dataFromSharePreference)) {
                    str3 = SecurityUtils.decrypt(SecurityUtils.getSeed(), dataFromSharePreference);
                }
            } catch (Exception e) {
            }
        }
        return str3;
    }

    public static String a(String str, String str2) {
        String str3 = null;
        if (!(CommonUtils.isBlank(str) || CommonUtils.isBlank(str2))) {
            try {
                String readDataFromPublicArea = PublicStorage.readDataFromPublicArea(str);
                if (!CommonUtils.isBlank(readDataFromPublicArea)) {
                    readDataFromPublicArea = new JSONObject(readDataFromPublicArea).getString(str2);
                    if (!CommonUtils.isBlank(readDataFromPublicArea)) {
                        str3 = SecurityUtils.decrypt(SecurityUtils.getSeed(), readDataFromPublicArea);
                    }
                }
            } catch (Exception e) {
            }
        }
        return str3;
    }

    public static void a(Context context, String str, String str2, String str3) {
        if (!CommonUtils.isBlank(str) && !CommonUtils.isBlank(str2) && context != null && !CommonUtils.isBlank(str3)) {
            try {
                String encrypt = SecurityUtils.encrypt(SecurityUtils.getSeed(), str3);
                Map hashMap = new HashMap();
                hashMap.put(str2, encrypt);
                SharePreferenceStorage.writeDataToSharePreference(context, str, hashMap);
            } catch (Exception e) {
            }
        }
    }

    public static void a(String str, String str2, String str3) {
        if (!CommonUtils.isBlank(str) && !CommonUtils.isBlank(str2) && !CommonUtils.isBlank(str3)) {
            try {
                String encrypt = SecurityUtils.encrypt(SecurityUtils.getSeed(), str3);
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(str2, encrypt);
                PublicStorage.writeDataToPublicArea(str, jSONObject.toString());
            } catch (Exception e) {
            }
        }
    }
}
