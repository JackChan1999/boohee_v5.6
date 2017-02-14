package com.boohee.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.boohee.utility.Config;

public class Utils {
    protected static final String ACTION_LOGIN        = "com.baidu.pushdemo.action.LOGIN";
    public static final    String ACTION_MESSAGE      = "com.baiud.pushdemo.action.MESSAGE";
    public static final    String ACTION_RESPONSE     = "bccsclient.action.RESPONSE";
    public static final    String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN  = "access_token";
    public static final    String EXTRA_MESSAGE       = "message";
    public static final    String RESPONSE_CONTENT    = "content";
    public static final    String RESPONSE_ERRCODE    = "errcode";
    public static final    String RESPONSE_METHOD     = "method";
    public static final    String TAG                 = "PushDemoActivity";

    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context
                    .getPackageName(), 128);
            if (ai != null) {
                metaData = ai.metaData;
            }
            if (metaData != null) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
        }
        return apiKey;
    }

    public static boolean hasBind(Context context) {
        if ("ok".equalsIgnoreCase(PreferenceManager.getDefaultSharedPreferences(context)
                .getString("bind_flag", ""))) {
            return true;
        }
        return false;
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    public static boolean hasVersionTag(Context context) {
        if (Config.getVersionName().equalsIgnoreCase(PreferenceManager
                .getDefaultSharedPreferences(context).getString("version_tag", ""))) {
            return true;
        }
        return false;
    }

    public static void setVersionTag(Context context) {
        String currentVersion = Config.getVersionName();
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("version_tag", currentVersion);
        editor.commit();
    }

    public static float calBmi(float height, float weight) {
        float h = height / 100.0f;
        return (float) (((double) Math.round(10.0f * (weight / (h * h)))) / 10.0d);
    }

    public static float calWeightWithBmiAndHeigt(float bmi, float height) {
        return ArithmeticUtil.round(((height / 100.0f) * bmi) * (height / 100.0f), 1);
    }
}
