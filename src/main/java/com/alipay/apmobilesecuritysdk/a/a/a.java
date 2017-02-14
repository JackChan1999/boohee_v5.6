package com.alipay.apmobilesecuritysdk.a.a;

import android.content.Context;
import com.alipay.apmobilesecuritysdk.e.b;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.commonutils.crypto.SecurityUtils;
import com.alipay.security.mobile.module.localstorage.PublicStorage;
import com.alipay.security.mobile.module.localstorage.SharePreferenceStorage;
import java.util.Map;
import org.json.JSONObject;

public final class a {
    public static synchronized String a() {
        String str = null;
        synchronized (a.class) {
            String b = b();
            if (!CommonUtils.isBlank(b)) {
                String[] split = b.split("`");
                if (split != null && split.length >= 2) {
                    str = split[0];
                }
            }
        }
        return str;
    }

    public static synchronized String a(Context context) {
        String a;
        synchronized (a.class) {
            a = a();
            if (CommonUtils.isBlank(a)) {
                a = b(context);
            }
        }
        return a;
    }

    public static synchronized void a(b bVar) {
        synchronized (a.class) {
            if (!CommonUtils.isBlank(bVar.a())) {
                if (!bVar.a().equals(a())) {
                    String str = bVar.a() + "`" + bVar.d();
                    if (str != null) {
                        try {
                            str = SecurityUtils.encrypt(SecurityUtils.getSeed(), str);
                            JSONObject jSONObject = new JSONObject();
                            jSONObject.put("device", str);
                            PublicStorage.writeDataToPublicArea("deviceid_v2", jSONObject.toString());
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    private static String b() {
        try {
            return SecurityUtils.decrypt(SecurityUtils.getSeed(), new JSONObject(PublicStorage.readDataFromPublicArea("deviceid_v2")).getString("device"));
        } catch (Exception e) {
            return null;
        }
    }

    public static synchronized String b(Context context) {
        String str = null;
        synchronized (a.class) {
            String str2 = "";
            try {
                String dataFromSharePreference = SharePreferenceStorage.getDataFromSharePreference(context, "profiles", "deviceid", "");
                dataFromSharePreference = CommonUtils.isBlank(dataFromSharePreference) ? null : SecurityUtils.decrypt(SecurityUtils.getSeed(), dataFromSharePreference);
                if (!CommonUtils.isBlank(dataFromSharePreference)) {
                    b bVar = new b();
                    Map a = b.a(dataFromSharePreference);
                    if (a != null) {
                        str = (String) a.get("deviceId");
                    }
                    str = str2;
                }
            } catch (Throwable th) {
            }
        }
        return str;
    }
}
