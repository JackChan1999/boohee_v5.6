package com.umeng.socialize.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;

public class DeviceConfig {
    public static final    int    DEFAULT_TIMEZONE = 8;
    protected static final String LOG_TAG          = DeviceConfig.class.getName();
    private static final   String MOBILE_NETWORK   = "2G/3G";
    protected static final String UNKNOW           = "Unknown";
    private static final   String WIFI             = "Wi-Fi";

    public static boolean isAppInstalled(String str, Context context) {
        boolean z = true;
        if (context == null) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(str, 1);
        } catch (NameNotFoundException e) {
            z = false;
        }
        return z;
    }

    public static String getAppVersion(String str, Context context) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionName;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isChinese(Context context) {
        return context.getResources().getConfiguration().locale.toString().equals(Locale.CHINA
                .toString());
    }

    public static boolean checkPermission(Context context, String str) {
        if (context.getPackageManager().checkPermission(str, context.getPackageName()) != 0) {
            return false;
        }
        return true;
    }

    public static String getDeviceId(Context context) {
        String deviceId;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager == null) {
            Log.w(LOG_TAG, "No IMEI.");
        }
        String str = "";
        try {
            if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                deviceId = telephonyManager.getDeviceId();
                if (TextUtils.isEmpty(deviceId)) {
                    return deviceId;
                }
                Log.w(LOG_TAG, "No IMEI.");
                deviceId = getMac(context);
                if (TextUtils.isEmpty(deviceId)) {
                    return deviceId;
                }
                Log.w(LOG_TAG, "Failed to take mac as IMEI. Try to use Secure.ANDROID_ID instead.");
                deviceId = Secure.getString(context.getContentResolver(), "android_id");
                Log.w(LOG_TAG, "getDeviceId: Secure.ANDROID_ID: " + deviceId);
                return deviceId;
            }
        } catch (Exception e) {
            Log.w(LOG_TAG, "No IMEI.", e);
        }
        deviceId = str;
        if (TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }
        Log.w(LOG_TAG, "No IMEI.");
        deviceId = getMac(context);
        if (TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }
        Log.w(LOG_TAG, "Failed to take mac as IMEI. Try to use Secure.ANDROID_ID instead.");
        deviceId = Secure.getString(context.getContentResolver(), "android_id");
        Log.w(LOG_TAG, "getDeviceId: Secure.ANDROID_ID: " + deviceId);
        return deviceId;
    }

    public static String[] getNetworkAccessMode(Context context) {
        String[] strArr = new String[]{UNKNOW, UNKNOW};
        if (context.getPackageManager().checkPermission("android.permission" +
                ".ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
            strArr[0] = UNKNOW;
            return strArr;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                ("connectivity");
        if (connectivityManager == null) {
            strArr[0] = UNKNOW;
            return strArr;
        } else if (connectivityManager.getNetworkInfo(1).getState() == State.CONNECTED) {
            strArr[0] = "Wi-Fi";
            return strArr;
        } else {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
            if (networkInfo.getState() != State.CONNECTED) {
                return strArr;
            }
            strArr[0] = "2G/3G";
            strArr[1] = networkInfo.getSubtypeName();
            return strArr;
        }
    }

    public static boolean isWiFiAvailable(Context context) {
        return "Wi-Fi".equals(getNetworkAccessMode(context)[0]);
    }

    public static boolean isOnline(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService
                    ("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isConnectedOrConnecting();
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        if (checkPermission(context, "android.permission.ACCESS_NETWORK_STATE") && isOnline
                (context)) {
            return true;
        }
        return false;
    }

    public static boolean isSdCardWrittenable() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public static String getMac(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (checkPermission(context, "android.permission.ACCESS_WIFI_STATE")) {
                return wifiManager.getConnectionInfo().getMacAddress();
            }
            Log.w(LOG_TAG, "Could not get mac address.[no permission android.permission" +
                    ".ACCESS_WIFI_STATE");
            return "";
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not get mac address." + e.toString());
        }
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }
}
