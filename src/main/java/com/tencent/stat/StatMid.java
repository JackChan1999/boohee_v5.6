package com.tencent.stat;

import android.content.Context;

import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.k;

public class StatMid {
    private static StatLogger a = k.b();
    private static DeviceInfo b = null;

    static synchronized DeviceInfo a(Context context) {
        DeviceInfo a;
        synchronized (StatMid.class) {
            try {
                a a2 = a.a(context);
                DeviceInfo a3 = a(a2.d(DeviceInfo.TAG_FLAG, null));
                a.d("get device info from internal storage:" + a3);
                DeviceInfo a4 = a(a2.f(DeviceInfo.TAG_FLAG, null));
                a.d("get device info from setting.system:" + a4);
                a = a(a2.b(DeviceInfo.TAG_FLAG, null));
                a.d("get device info from SharedPreference:" + a);
                b = a(a, a4, a3);
                if (b == null) {
                    b = new DeviceInfo();
                }
                a = n.a(context).b(context);
                if (a != null) {
                    b.d(a.getImei());
                    b.e(a.getMac());
                    b.b(a.getUserType());
                }
            } catch (Object th) {
                a.e(th);
            }
            a = b;
        }
        return a;
    }

    static DeviceInfo a(DeviceInfo deviceInfo, DeviceInfo deviceInfo2) {
        return (deviceInfo == null || deviceInfo2 == null) ? deviceInfo == null ? deviceInfo2 !=
                null ? deviceInfo2 : null : deviceInfo : deviceInfo.a(deviceInfo2) >= 0 ?
                deviceInfo : deviceInfo2;
    }

    static DeviceInfo a(DeviceInfo deviceInfo, DeviceInfo deviceInfo2, DeviceInfo deviceInfo3) {
        return a(a(deviceInfo, deviceInfo2), a(deviceInfo2, deviceInfo3));
    }

    private static DeviceInfo a(String str) {
        return str != null ? DeviceInfo.a(k.d(str)) : null;
    }

    public static DeviceInfo getDeviceInfo(Context context) {
        if (context == null) {
            a.error((Object) "Context for StatConfig.getDeviceInfo is null.");
            return null;
        }
        if (b == null) {
            a(context);
        }
        return b;
    }

    public static String getMid(Context context) {
        if (b == null) {
            getDeviceInfo(context);
        }
        return b.getMid();
    }

    public static void updateDeviceInfo(Context context, String str) {
        try {
            getDeviceInfo(context);
            b.c(str);
            b.a(b.a() + 1);
            b.a(System.currentTimeMillis());
            String jSONObject = b.c().toString();
            a.d("save DeviceInfo:" + jSONObject);
            jSONObject = k.c(jSONObject).replace("\n", "");
            a a = a.a(context);
            a.c(DeviceInfo.TAG_FLAG, jSONObject);
            a.e(DeviceInfo.TAG_FLAG, jSONObject);
            a.a(DeviceInfo.TAG_FLAG, jSONObject);
        } catch (Object th) {
            a.e(th);
        }
    }
}
