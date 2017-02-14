package com.alipay.apmobilesecuritysdk.e;

import android.content.Context;
import com.alipay.apmobilesecuritysdk.f.c;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.commonutils.LOG;
import org.json.JSONObject;

public final class a {
    public static synchronized b a() {
        b bVar = null;
        synchronized (a.class) {
            String a = c.a("wxcasxx_v3", "wxcasxx");
            if (!CommonUtils.isBlank(a)) {
                try {
                    JSONObject jSONObject = new JSONObject(a);
                    bVar = new b(jSONObject.optString("apdid"), jSONObject.optString("deviceInfoHash"), jSONObject.optString("token"), jSONObject.optString("timestamp"));
                } catch (Throwable e) {
                    LOG.logException(e);
                }
            }
        }
        return bVar;
    }

    public static synchronized b a(Context context) {
        b bVar = null;
        synchronized (a.class) {
            String c = c(context);
            if (CommonUtils.isBlank(c)) {
                c = c.a("wxcasxx_v3", "wxcasxx");
            }
            if (!CommonUtils.isBlank(c)) {
                try {
                    JSONObject jSONObject = new JSONObject(c);
                    bVar = new b(jSONObject.optString("apdid"), jSONObject.optString("deviceInfoHash"), jSONObject.optString("token"), jSONObject.optString("timestamp"));
                } catch (Throwable e) {
                    LOG.logException(e);
                }
            }
        }
        return bVar;
    }

    public static synchronized void a(Context context, b bVar) {
        synchronized (a.class) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("apdid", bVar.a());
                jSONObject.put("deviceInfoHash", bVar.b());
                jSONObject.put("token", bVar.c());
                jSONObject.put("timestamp", bVar.d());
                String jSONObject2 = jSONObject.toString();
                c.a(context, "vkeyid_profiles_v3", "deviceid", jSONObject2);
                c.a("wxcasxx_v3", "wxcasxx", jSONObject2);
            } catch (Throwable e) {
                LOG.logException(e);
            }
        }
    }

    public static synchronized b b(Context context) {
        b bVar = null;
        synchronized (a.class) {
            String c = c(context);
            if (!CommonUtils.isBlank(c)) {
                try {
                    JSONObject jSONObject = new JSONObject(c);
                    bVar = new b(jSONObject.optString("apdid"), jSONObject.optString("deviceInfoHash"), jSONObject.optString("token"), jSONObject.optString("timestamp"));
                } catch (Throwable e) {
                    LOG.logException(e);
                }
            }
        }
        return bVar;
    }

    private static String c(Context context) {
        return c.a(context, "vkeyid_profiles_v3", "deviceid");
    }
}
