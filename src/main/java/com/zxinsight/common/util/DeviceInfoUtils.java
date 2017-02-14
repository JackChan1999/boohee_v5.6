package com.zxinsight.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class DeviceInfoUtils {

    class WebAppInterface {
        private WebAppInterface() {
        }

        @JavascriptInterface
        public void runOnAndroidJavaScript(String str) {
            m.a().a("fp", str);
        }
    }

    public static void a(Context context) {
        if (TextUtils.isEmpty(m.a().d("fp"))) {
            i(context);
        }
        d();
    }

    public static String a() {
        return m.a().c("fp");
    }

    public static String b() {
        return "0";
    }

    public static String c() {
        return m.a().b("fa", o.b());
    }

    public static void d() {
        if (TextUtils.isEmpty(m.a().c("fa"))) {
            m.a().a("fa", o.b());
        }
    }

    public static String e() {
        return l.a(m.a().c("first_time_tag")) ? "0" : "1";
    }

    public static void f() {
        if (TextUtils.isEmpty(m.a().c("first_time_tag"))) {
            m.a().a("first_time_tag", "1");
        }
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private static void i(Context context) {
        WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/mw_fingerprint.html");
        webView.addJavascriptInterface(new WebAppInterface(), "getFingerPrint");
    }

    public static String b(Context context) {
        if (!o.a(context, "android.permission.ACCESS_NETWORK_STATE")) {
            return "";
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                ("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager.getNetworkInfo(1).getState() == State.CONNECTED) {
            return "0";
        }
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return "-1";
        }
        switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
            case 0:
                return "3";
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return "1";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return "2";
            case 13:
                return "3";
            default:
                return "1";
        }
    }

    public static String c(Context context) {
        if (context == null) {
            return "";
        }
        if (!o.a(context, "android.permission.READ_PHONE_STATE")) {
            return Secure.getString(context.getContentResolver(), "android_id");
        }
        String str;
        CharSequence deviceId = ((TelephonyManager) context.getSystemService("phone"))
                .getDeviceId();
        CharSequence charSequence = "";
        if (deviceId != null) {
            charSequence = deviceId.replace("0", "");
        }
        if ((TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(r0)) && VERSION.SDK_INT >= 9) {
            try {
                Class cls = Class.forName("android.os.SystemProperties");
                str = (String) cls.getMethod("get", new Class[]{String.class, String.class})
                        .invoke(cls, new Object[]{"ro.serialno", EnvironmentCompat.MEDIA_UNKNOWN});
            } catch (Exception e) {
                str = null;
            }
        } else {
            charSequence = deviceId;
        }
        if (l.b(str)) {
            return str;
        }
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String g() {
        return VERSION.RELEASE;
    }

    public static String h() {
        return Build.BRAND;
    }

    public static String i() {
        return Build.MANUFACTURER;
    }

    public static String j() {
        return Build.MODEL;
    }

    public static String d(Context context) {
        Display defaultDisplay;
        Point point;
        int i;
        int i2;
        if (VERSION.SDK_INT >= 13 && VERSION.SDK_INT < 17) {
            defaultDisplay = ((WindowManager) context.getSystemService("window"))
                    .getDefaultDisplay();
            point = new Point();
            defaultDisplay.getSize(point);
            i = point.x;
            i2 = point.y;
            if (context.getResources().getConfiguration().orientation == 1) {
                return i + "x" + i2;
            }
            return i2 + "x" + i;
        } else if (VERSION.SDK_INT >= 17) {
            defaultDisplay = ((WindowManager) context.getSystemService("window"))
                    .getDefaultDisplay();
            point = new Point();
            defaultDisplay.getRealSize(point);
            i = point.x;
            i2 = point.y;
            if (context.getResources().getConfiguration().orientation == 1) {
                return i + "x" + i2;
            }
            return i2 + "x" + i;
        } else {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            if (context.getResources().getConfiguration().orientation == 1) {
                return displayMetrics.widthPixels + "x" + displayMetrics.heightPixels;
            }
            return displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
        }
    }

    public static String e(Context context) {
        return context.getPackageName();
    }

    public static String f(Context context) {
        String str = "1.0";
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (NameNotFoundException e) {
            return str;
        }
    }

    public static String g(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (!o.a(context, "android.permission.READ_PHONE_STATE")) {
            return a(telephonyManager);
        }
        String subscriberId = telephonyManager.getSubscriberId();
        if (TextUtils.isEmpty(subscriberId)) {
            return a(telephonyManager);
        }
        if (subscriberId.startsWith("46000") || subscriberId.startsWith("46002") || subscriberId
                .startsWith("46007")) {
            return "1";
        }
        if (subscriberId.startsWith("46001") || subscriberId.startsWith("46006")) {
            return "2";
        }
        if (subscriberId.startsWith("46003") || subscriberId.startsWith("46005")) {
            return "3";
        }
        if (subscriberId.startsWith("46020")) {
            return "4";
        }
        return a(telephonyManager);
    }

    private static String a(TelephonyManager telephonyManager) {
        String simOperator = telephonyManager.getSimOperator();
        if ("46000".equals(simOperator) || "46002".equals(simOperator) || "46007".equals
                (simOperator)) {
            return "1";
        }
        if ("46001".equals(simOperator) || "46006".equals(simOperator)) {
            return "2";
        }
        if ("46003".equals(simOperator) || "46005".equals(simOperator)) {
            return "3";
        }
        if ("46020".equals(simOperator)) {
            return "4";
        }
        return "0";
    }

    public static String h(Context context) {
        if (!o.a(context, "android.permission.ACCESS_WIFI_STATE")) {
            return "";
        }
        String str = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager != null) {
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                return connectionInfo.getMacAddress();
            }
        }
        return str;
    }
}
