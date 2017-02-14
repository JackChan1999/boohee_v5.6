package com.alipay.euler.andfix;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import java.lang.reflect.Method;

public class Compat {
    public static boolean isChecked = false;
    public static boolean isSupport = false;

    public static synchronized boolean isSupport() {
        boolean z;
        synchronized (Compat.class) {
            if (isChecked) {
                z = isSupport;
            } else {
                isChecked = true;
                if (!isYunOS() && AndFix.setup() && isSupportSDKVersion()) {
                    isSupport = true;
                }
                if (inBlackList()) {
                    isSupport = false;
                }
                z = isSupport;
            }
        }
        return z;
    }

    @SuppressLint({"DefaultLocale"})
    private static boolean isYunOS() {
        String version = null;
        String vmName = null;
        try {
            Method m = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
            version = (String) m.invoke(null, new Object[]{"ro.yunos.version"});
            vmName = (String) m.invoke(null, new Object[]{"java.vm.name"});
        } catch (Exception e) {
        }
        if ((vmName == null || !vmName.toLowerCase().contains("lemur")) && (version == null || version.trim().length() <= 0)) {
            return false;
        }
        return true;
    }

    private static boolean isSupportSDKVersion() {
        if (VERSION.SDK_INT < 8 || VERSION.SDK_INT > 23) {
            return false;
        }
        return true;
    }

    private static boolean inBlackList() {
        return false;
    }
}
