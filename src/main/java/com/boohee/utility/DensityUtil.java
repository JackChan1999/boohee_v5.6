package com.boohee.utility;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.view.ViewConfiguration;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;

import java.lang.reflect.Method;

public class DensityUtil {
    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static float getScreenWidth(Context context) {
        return (float) context.getResources().getDisplayMetrics().widthPixels;
    }

    public static float getScreenHeight(Context context) {
        return (float) context.getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean hasNavBar(Context context) {
        boolean z = true;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", DeviceInfoConstant
                .OS_ANDROID);
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            String sNavBarOverride = null;
            if (VERSION.SDK_INT >= 19) {
                try {
                    Method m = Class.forName("android.os.SystemProperties").getDeclaredMethod
                            ("get", new Class[]{String.class});
                    m.setAccessible(true);
                    sNavBarOverride = (String) m.invoke(null, new Object[]{"qemu.hw.mainkeys"});
                } catch (Throwable th) {
                    sNavBarOverride = null;
                }
            }
            if ("1".equals(sNavBarOverride)) {
                return false;
            }
            if ("0".equals(sNavBarOverride)) {
                return true;
            }
            return hasNav;
        }
        if (ViewConfiguration.get(context).hasPermanentMenuKey()) {
            z = false;
        }
        return z;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", DeviceInfoConstant
                .OS_ANDROID);
        if (resourceId > 0) {
            return res.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
