package com.xiaomi.push.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;

import com.boohee.utils.LeDongLiHelper;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.channel.commonutils.misc.a;
import com.xiaomi.channel.commonutils.string.d;

import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;

public class h {
    private static g a;

    public static synchronized g a(Context context) {
        g gVar = null;
        synchronized (h.class) {
            if (a != null) {
                gVar = a;
            } else {
                SharedPreferences sharedPreferences = context.getSharedPreferences
                        ("mipush_account", 0);
                Object string = sharedPreferences.getString("uuid", null);
                Object string2 = sharedPreferences.getString("token", null);
                Object string3 = sharedPreferences.getString("security", null);
                String string4 = sharedPreferences.getString(SocializeProtocolConstants
                        .PROTOCOL_KEY_APP_ID, null);
                String string5 = sharedPreferences.getString("app_token", null);
                String string6 = sharedPreferences.getString(LeDongLiHelper.PACKAGE_NAME, null);
                Object string7 = sharedPreferences.getString("device_id", null);
                int i = sharedPreferences.getInt("env_type", 1);
                if (!TextUtils.isEmpty(string7) && string7.startsWith("a-")) {
                    string7 = f.c(context);
                    sharedPreferences.edit().putString("device_id", string7).commit();
                }
                if (!(TextUtils.isEmpty(string) || TextUtils.isEmpty(string2) || TextUtils
                        .isEmpty(string3))) {
                    CharSequence c = f.c(context);
                    if ("com.xiaomi.xmsf".equals(context.getPackageName()) || TextUtils.isEmpty
                            (c) || TextUtils.isEmpty(r8) || r8.equals(c)) {
                        a = new g(string, string2, string3, string4, string5, string6, i);
                        gVar = a;
                    } else {
                        b.d("erase the old account.");
                        b(context);
                    }
                }
            }
        }
        return gVar;
    }

    public static synchronized g a(Context context, String str, String str2, String str3) {
        g gVar = null;
        synchronized (h.class) {
            PackageInfo packageInfo;
            Map treeMap = new TreeMap();
            treeMap.put("devid", f.a(context));
            String str4 = c(context) ? "1000271" : str2;
            String str5 = c(context) ? "420100086271" : str3;
            String str6 = c(context) ? "com.xiaomi.xmsf" : str;
            treeMap.put("appid", str4);
            treeMap.put("apptoken", str5);
            try {
                packageInfo = context.getPackageManager().getPackageInfo(str6, 16384);
            } catch (Throwable e) {
                b.a(e);
                packageInfo = null;
            }
            treeMap.put("appversion", packageInfo != null ? String.valueOf(packageInfo
                    .versionCode) : "0");
            treeMap.put("'sdkversion", Integer.toString(8));
            treeMap.put("packagename", str6);
            treeMap.put("model", Build.MODEL);
            treeMap.put("imei_md5", d.a(f.b(context)));
            treeMap.put(SocializeProtocolConstants.PROTOCOL_KEY_OS, VERSION.RELEASE +
                    SocializeConstants.OP_DIVIDER_MINUS + VERSION.INCREMENTAL);
            com.xiaomi.channel.commonutils.network.b a = com.xiaomi.channel.commonutils.network.d
                    .a(context, a(), treeMap);
            String str7 = "";
            if (a != null) {
                str7 = a.a();
            }
            if (!TextUtils.isEmpty(str7)) {
                JSONObject jSONObject = new JSONObject(str7);
                if (jSONObject.getInt("code") == 0) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("data");
                    gVar = new g(jSONObject2.getString("userId") + "@xiaomi.com/an" + d.a(6),
                            jSONObject2.getString("token"), jSONObject2.getString("ssecurity"),
                            str4, str5, str6, a.c());
                    a(context, gVar);
                    a = gVar;
                } else {
                    k.a(context, jSONObject.getInt("code"), jSONObject.optString("description"));
                    b.a(str7);
                }
            }
        }
        return gVar;
    }

    public static String a() {
        if (a.b()) {
            return "http://10.237.12.17:9085/pass/register";
        }
        return "https://" + (a.a() ? "sandbox.xmpush.xiaomi.com" : "register.xmpush.xiaomi.com")
                + "/pass/register";
    }

    private static void a(Context context, g gVar) {
        Editor edit = context.getSharedPreferences("mipush_account", 0).edit();
        edit.putString("uuid", gVar.a);
        edit.putString("security", gVar.c);
        edit.putString("token", gVar.b);
        edit.putString(SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, gVar.d);
        edit.putString(LeDongLiHelper.PACKAGE_NAME, gVar.f);
        edit.putString("app_token", gVar.e);
        edit.putString("device_id", f.c(context));
        edit.putInt("env_type", gVar.g);
        edit.commit();
    }

    public static void b(Context context) {
        context.getSharedPreferences("mipush_account", 0).edit().clear().commit();
        a = null;
    }

    private static boolean c(Context context) {
        return context.getPackageName().equals("com.xiaomi.xmsf");
    }
}
