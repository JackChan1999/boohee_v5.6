package com.tencent.wxop.stat.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public final class q {
    private static SharedPreferences db = null;

    private static synchronized SharedPreferences S(Context context) {
        SharedPreferences sharedPreferences;
        synchronized (q.class) {
            sharedPreferences = context.getSharedPreferences(".mta-wxop", 0);
            db = sharedPreferences;
            if (sharedPreferences == null) {
                db = PreferenceManager.getDefaultSharedPreferences(context);
            }
            sharedPreferences = db;
        }
        return sharedPreferences;
    }

    public static int a(Context context, String str, int i) {
        return S(context).getInt(l.e(context, "wxop_" + str), i);
    }

    public static void a(Context context, String str, long j) {
        String e = l.e(context, "wxop_" + str);
        Editor edit = S(context).edit();
        edit.putLong(e, j);
        edit.commit();
    }

    public static String b(Context context, String str, String str2) {
        return S(context).getString(l.e(context, "wxop_" + str), str2);
    }

    public static void b(Context context, String str, int i) {
        String e = l.e(context, "wxop_" + str);
        Editor edit = S(context).edit();
        edit.putInt(e, i);
        edit.commit();
    }

    public static void c(Context context, String str, String str2) {
        String e = l.e(context, "wxop_" + str);
        Editor edit = S(context).edit();
        edit.putString(e, str2);
        edit.commit();
    }

    public static long f(Context context, String str) {
        return S(context).getLong(l.e(context, "wxop_" + str), 0);
    }
}
