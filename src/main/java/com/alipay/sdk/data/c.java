package com.alipay.sdk.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.TextView;
import com.alipay.mobilesecuritysdk.face.SecurityClientMobile;
import com.alipay.sdk.cons.a;
import com.alipay.sdk.sys.b;
import com.alipay.sdk.util.i;
import com.boohee.one.http.DnspodFree;
import com.umeng.socialize.common.SocializeConstants;
import java.util.HashMap;
import java.util.Random;

public final class c {
    private static final String d = "virtualImeiAndImsi";
    private static final String e = "virtual_imei";
    private static final String f = "virtual_imsi";
    private static c g;
    public String a;
    public String b = "sdk-and-lite";
    public String c;

    private String c() {
        return this.c;
    }

    private c() {
    }

    public static synchronized c a() {
        c cVar;
        synchronized (c.class) {
            if (g == null) {
                g = new c();
            }
            cVar = g;
        }
        return cVar;
    }

    public final synchronized void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            PreferenceManager.getDefaultSharedPreferences(b.a().a).edit().putString(com.alipay.sdk.cons.b.i, str).commit();
            a.b = str;
        }
    }

    private static String d() {
        return "1";
    }

    private static String a(Context context) {
        return Float.toString(new TextView(context).getTextSize());
    }

    private static String e() {
        return "-1;-1";
    }

    private String a(com.alipay.sdk.tid.b bVar) {
        String d;
        String str;
        Context context = b.a().a;
        com.alipay.sdk.util.a a = com.alipay.sdk.util.a.a(context);
        if (TextUtils.isEmpty(this.a)) {
            String a2 = i.a();
            String b = i.b();
            d = i.d(context);
            str = a.a;
            this.a = "Msp/15.0.4" + " (" + a2 + DnspodFree.IP_SPLIT + b + DnspodFree.IP_SPLIT + d + DnspodFree.IP_SPLIT + str.substring(0, str.indexOf("://")) + DnspodFree.IP_SPLIT + i.e(context) + DnspodFree.IP_SPLIT + Float.toString(new TextView(context).getTextSize());
        }
        d = com.alipay.sdk.util.a.b(context).a();
        str = "-1;-1";
        String str2 = "1";
        String a3 = a.a();
        String b2 = a.b();
        Context context2 = b.a().a;
        SharedPreferences sharedPreferences = context2.getSharedPreferences(d, 0);
        CharSequence string = sharedPreferences.getString(f, null);
        if (TextUtils.isEmpty(string)) {
            if (TextUtils.isEmpty(com.alipay.sdk.tid.b.a().a)) {
                Object c = b.a().c();
                string = TextUtils.isEmpty(c) ? b() : c.substring(3, 18);
            } else {
                string = com.alipay.sdk.util.a.a(context2).a();
            }
            sharedPreferences.edit().putString(f, string).commit();
        }
        CharSequence charSequence = string;
        Context context3 = b.a().a;
        SharedPreferences sharedPreferences2 = context3.getSharedPreferences(d, 0);
        string = sharedPreferences2.getString(e, null);
        if (TextUtils.isEmpty(string)) {
            if (TextUtils.isEmpty(com.alipay.sdk.tid.b.a().a)) {
                string = b();
            } else {
                string = com.alipay.sdk.util.a.a(context3).b();
            }
            sharedPreferences2.edit().putString(e, string).commit();
        }
        CharSequence charSequence2 = string;
        if (bVar != null) {
            this.c = bVar.b;
        }
        String replace = Build.MANUFACTURER.replace(DnspodFree.IP_SPLIT, " ");
        String replace2 = Build.MODEL.replace(DnspodFree.IP_SPLIT, " ");
        boolean b3 = b.b();
        String c2 = a.c();
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        String ssid = connectionInfo != null ? connectionInfo.getSSID() : "-1";
        connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        String bssid = connectionInfo != null ? connectionInfo.getBSSID() : "00";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.a).append(DnspodFree.IP_SPLIT).append(d).append(DnspodFree.IP_SPLIT).append(str).append(DnspodFree.IP_SPLIT).append(str2).append(DnspodFree.IP_SPLIT).append(a3).append(DnspodFree.IP_SPLIT).append(b2).append(DnspodFree.IP_SPLIT).append(this.c).append(DnspodFree.IP_SPLIT).append(replace).append(DnspodFree.IP_SPLIT).append(replace2).append(DnspodFree.IP_SPLIT).append(b3).append(DnspodFree.IP_SPLIT).append(c2).append(";-1;-1;").append(this.b).append(DnspodFree.IP_SPLIT).append(charSequence).append(DnspodFree.IP_SPLIT).append(charSequence2).append(DnspodFree.IP_SPLIT).append(ssid).append(DnspodFree.IP_SPLIT).append(bssid);
        if (bVar != null) {
            HashMap hashMap = new HashMap();
            hashMap.put(com.alipay.sdk.cons.b.c, bVar.a);
            hashMap.put(com.alipay.sdk.cons.b.g, b.a().c());
            c = a(context, hashMap);
            if (!TextUtils.isEmpty(c)) {
                stringBuilder.append(DnspodFree.IP_SPLIT).append(c);
            }
        }
        stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    private static String f() {
        Context context = b.a().a;
        SharedPreferences sharedPreferences = context.getSharedPreferences(d, 0);
        String string = sharedPreferences.getString(e, null);
        if (TextUtils.isEmpty(string)) {
            if (TextUtils.isEmpty(com.alipay.sdk.tid.b.a().a)) {
                string = b();
            } else {
                string = com.alipay.sdk.util.a.a(context).b();
            }
            sharedPreferences.edit().putString(e, string).commit();
        }
        return string;
    }

    private static String g() {
        Context context = b.a().a;
        SharedPreferences sharedPreferences = context.getSharedPreferences(d, 0);
        String string = sharedPreferences.getString(f, null);
        if (TextUtils.isEmpty(string)) {
            if (TextUtils.isEmpty(com.alipay.sdk.tid.b.a().a)) {
                Object c = b.a().c();
                if (TextUtils.isEmpty(c)) {
                    string = b();
                } else {
                    string = c.substring(3, 18);
                }
            } else {
                string = com.alipay.sdk.util.a.a(context).a();
            }
            sharedPreferences.edit().putString(f, string).commit();
        }
        return string;
    }

    public static String b() {
        return Long.toHexString(System.currentTimeMillis()) + (new Random().nextInt(9000) + 1000);
    }

    private static String b(Context context) {
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        if (connectionInfo != null) {
            return connectionInfo.getSSID();
        }
        return "-1";
    }

    private static String c(Context context) {
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        if (connectionInfo != null) {
            return connectionInfo.getBSSID();
        }
        return "00";
    }

    public static String a(Context context, HashMap<String, String> hashMap) {
        String str = "";
        try {
            str = SecurityClientMobile.GetApdid(context, hashMap);
        } catch (Throwable th) {
        }
        "apdid:" + str;
        return str;
    }
}
