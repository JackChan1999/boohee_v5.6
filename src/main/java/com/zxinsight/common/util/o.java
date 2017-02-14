package com.zxinsight.common.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.zxinsight.MWConfiguration;

import java.util.Locale;
import java.util.regex.Pattern;

public class o {
    private static ProgressDialog a;
    private static String         b;
    private static String         c;

    public static long a() {
        return System.currentTimeMillis() / 1000;
    }

    public static String b() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static String c() {
        if (l.a(b)) {
            b = b("MW_APPID");
        }
        return b;
    }

    public static String a(String str) {
        if (l.a(c)) {
            c = b(str);
        }
        return c;
    }

    public static String b(String str) {
        String str2 = "";
        if (l.a(str)) {
            return "";
        }
        try {
            ApplicationInfo applicationInfo = MWConfiguration.getContext().getPackageManager()
                    .getApplicationInfo(MWConfiguration.getContext().getPackageName(), 128);
            if (applicationInfo.metaData == null) {
                return str2;
            }
            str2 = applicationInfo.metaData.getString(str);
            if (l.b(str2)) {
                return str2.trim();
            }
            return str2;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            c.e("please make sure the " + str + " in AndroidManifest.xml is right! " + str + " = " +
                    "" + str2);
            return str2;
        }
    }

    public static void a(Activity activity, String str, boolean z) {
        d();
        a = new ProgressDialog(activity);
        a.setProgressStyle(0);
        a.setMessage(str);
        a.setIndeterminate(false);
        a.setCancelable(true);
        a.setOnCancelListener(new p(z, activity));
        a.show();
    }

    public static void d() {
        if (a != null) {
            a.dismiss();
            a = null;
        }
    }

    public static boolean a(Context context, String str) {
        if (context == null) {
            context = MWConfiguration.getContext();
        }
        return context.getApplicationContext().getPackageManager().checkPermission(str, context
                .getApplicationContext().getPackageName()) == 0;
    }

    public static int a(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }

    public static boolean c(String str) {
        if (l.a(str)) {
            return false;
        }
        return Pattern.compile("([0-9]{10}|[0-9]{13})", 2).matcher(str).matches();
    }

    public static String a(String str, String str2) {
        return "zh".equalsIgnoreCase(Locale.getDefault().getLanguage()) ? str : str2;
    }
}
