package com.umeng.socialize.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocializeConstants;

public class LoginInfoHelp {
    private static final String SOCIALIZE_IDENTITY_INFO_KEY = "socialize_identity_info";
    private static final String SOCIALIZE_IDENTITY_UNSHOW   = "socialize_identity_unshow";

    public static synchronized void saveLoginInfo(Context context, String str) {
        synchronized (LoginInfoHelp.class) {
            Editor edit = context.getSharedPreferences(SocializeConstants.SOCIAL_PREFERENCE_NAME,
                    0).edit();
            edit.putString(SOCIALIZE_IDENTITY_INFO_KEY, str);
            edit.commit();
            if (isGuestLogin(context) && !TextUtils.isEmpty(str)) {
                setGuest(context, false);
            }
        }
    }

    public static synchronized void rmLoginInfo(Context context) {
        synchronized (LoginInfoHelp.class) {
            Editor edit = context.getSharedPreferences(SocializeConstants.SOCIAL_PREFERENCE_NAME,
                    0).edit();
            edit.remove(SOCIALIZE_IDENTITY_INFO_KEY);
            edit.commit();
        }
    }

    public static SHARE_MEDIA getLoginInfo(Context context) {
        return SHARE_MEDIA.convertToEmun(context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_NAME, 0).getString(SOCIALIZE_IDENTITY_INFO_KEY, ""));
    }

    public static void setGuest(Context context, boolean z) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_NAME, 0);
        synchronized (sharedPreferences) {
            Editor edit = sharedPreferences.edit();
            edit.putBoolean(SOCIALIZE_IDENTITY_UNSHOW, z);
            edit.commit();
        }
    }

    public static boolean isPlatformLogin(Context context) {
        if (getLoginInfo(context) != null) {
            return true;
        }
        return false;
    }

    public static boolean isCustomLogin(Context context) {
        if ("custom".equals(context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_NAME, 0).getString(SOCIALIZE_IDENTITY_INFO_KEY, ""))) {
            return true;
        }
        return false;
    }

    public static boolean isGuestLogin(Context context) {
        boolean isPlatformLogin = isPlatformLogin(context);
        if (isPlatformLogin) {
            return isPlatformLogin;
        }
        return context.getSharedPreferences(SocializeConstants.SOCIAL_PREFERENCE_NAME, 0)
                .getBoolean(SOCIALIZE_IDENTITY_UNSHOW, false);
    }

    public static boolean isLogin(Context context) {
        boolean isPlatformLogin = isPlatformLogin(context);
        if (!isPlatformLogin) {
            isPlatformLogin = isGuestLogin(context);
        }
        if (isPlatformLogin) {
            return isPlatformLogin;
        }
        return isCustomLogin(context);
    }
}
