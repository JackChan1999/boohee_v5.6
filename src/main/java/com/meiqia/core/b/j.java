package com.meiqia.core.b;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.meiqia.core.MQManager;
import com.meiqia.core.bean.MQClient;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class j {
    public static MQClient a(String str, h hVar) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String m = hVar.m();
        hVar.k(str);
        Object d = hVar.d();
        Object h = hVar.h();
        Object f = hVar.f();
        Object g = hVar.g();
        Object e = hVar.e();
        Object j = hVar.j();
        Object obj = (TextUtils.isEmpty(d) || TextUtils.isEmpty(h) || TextUtils.isEmpty(f) ||
                TextUtils.isEmpty(g) || TextUtils.isEmpty(e) || TextUtils.isEmpty(j)) ? null : 1;
        if (obj == null) {
            return null;
        }
        MQClient mQClient = new MQClient();
        mQClient.setVisitPageId(f);
        mQClient.setVisitId(e);
        mQClient.setTrackId(d);
        mQClient.setEnterpriseId(h);
        mQClient.setAESKey(j);
        mQClient.setBrowserId(g);
        mQClient.setBindUserId(str);
        hVar.k(m);
        return mQClient;
    }

    public static String a(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        if (!externalFilesDir.exists()) {
            externalFilesDir.mkdir();
        }
        File file = new File(externalFilesDir.getAbsolutePath() + "/mq");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static void a(Context context, Intent intent) {
        intent.setPackage(context.getPackageName());
        intent.putExtra("packageName", context.getPackageName());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void a(MQClient mQClient, h hVar) {
        String trackId = mQClient.getTrackId();
        String browserId = mQClient.getBrowserId();
        String enterpriseId = mQClient.getEnterpriseId();
        String visitId = mQClient.getVisitId();
        String visitPageId = mQClient.getVisitPageId();
        a(mQClient.getBindUserId(), hVar, mQClient.getAESKey(), trackId, enterpriseId, browserId,
                visitPageId, visitId);
    }

    public static void a(String str, h hVar, String str2, String str3, String str4, String str5,
                         String str6, String str7) {
        String m = hVar.m();
        hVar.k(str);
        hVar.h(str2);
        hVar.b(str3);
        hVar.f(str4);
        hVar.e(str5);
        hVar.d(str6);
        hVar.c(str7);
        hVar.k(m);
    }

    public static boolean a() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String b(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String c(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService
                ("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return "";
        }
        int type = activeNetworkInfo.getType();
        return type == 0 ? f.a(activeNetworkInfo.getSubtype()) : type == 1 ? "WIFI" :
                EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public static String d(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo
                    (context.getPackageName(), 0));
        } catch (Exception e) {
            return "";
        }
    }

    public static Map<String, Object> e(Context context) {
        Map<String, Object> hashMap = new HashMap();
        try {
            hashMap.put("device_brand", Build.BRAND);
            hashMap.put("device_model", Build.MODEL + " " + Build.DEVICE);
            hashMap.put("os_family", "Android");
            hashMap.put("os_version", VERSION.RELEASE);
            hashMap.put("resolution", g(context));
            hashMap.put("net_type", c(context));
            hashMap.put("app_version", b(context));
            hashMap.put("sdk_version", MQManager.getMeiqiaSDKVersion());
            hashMap.put("os_language", Locale.getDefault().getLanguage());
            hashMap.put("os_timezone", TimeZone.getDefault().getID());
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_NAME, d(context));
        } catch (Exception e) {
        }
        return hashMap;
    }

    public static boolean f(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                ("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }

    private static String g(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
    }
}
