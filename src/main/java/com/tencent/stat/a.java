package com.tencent.stat;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings.System;

import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.d;
import com.tencent.stat.common.k;
import com.tencent.stat.common.p;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class a {
    private static a          b = null;
    private        StatLogger a = k.b();
    private        boolean    c = false;
    private        boolean    d = false;
    private        boolean    e = false;
    private        Context    f = null;

    private a(Context context) {
        this.f = context.getApplicationContext();
        this.c = b(context);
        this.d = d(context);
        this.e = c(context);
    }

    public static synchronized a a(Context context) {
        a aVar;
        synchronized (a.class) {
            if (b == null) {
                b = new a(context);
            }
            aVar = b;
        }
        return aVar;
    }

    private boolean b(Context context) {
        if (k.a(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            return true;
        }
        this.a.e((Object) "Check permission failed: android.permission.WRITE_EXTERNAL_STORAGE");
        return false;
    }

    private boolean c(Context context) {
        if (k.a(context, "android.permission.WRITE_SETTINGS")) {
            return true;
        }
        this.a.e((Object) "Check permission failed: android.permission.WRITE_SETTINGS");
        return false;
    }

    private boolean d(Context context) {
        return k.d() < 14 ? b(context) : true;
    }

    public boolean a(String str, String str2) {
        p.b(this.f, str, str2);
        return true;
    }

    public String b(String str, String str2) {
        return p.a(this.f, str, str2);
    }

    public boolean c(String str, String str2) {
        if (!this.c) {
            return false;
        }
        try {
            d.a(Environment.getExternalStorageDirectory() + "/" + "Tencent/mta");
            File file = new File(Environment.getExternalStorageDirectory(), "Tencent/mta/.mid.txt");
            if (file != null) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write(str + "," + str2);
                bufferedWriter.write("\n");
                bufferedWriter.close();
            }
            return true;
        } catch (Throwable th) {
            this.a.w(th);
            return false;
        }
    }

    public String d(String str, String str2) {
        if (!this.c) {
            return null;
        }
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Tencent/mta/.mid.txt");
            if (file != null) {
                for (String split : d.a(file)) {
                    String[] split2 = split.split(",");
                    if (split2.length == 2 && split2[0].equals(str)) {
                        return split2[1];
                    }
                }
            }
        } catch (FileNotFoundException e) {
            this.a.w("Tencent/mta/.mid.txt not found.");
        } catch (Throwable th) {
            this.a.w(th);
        }
        return null;
    }

    public boolean e(String str, String str2) {
        if (!this.e) {
            return false;
        }
        System.putString(this.f.getContentResolver(), str, str2);
        return true;
    }

    public String f(String str, String str2) {
        return !this.e ? str2 : System.getString(this.f.getContentResolver(), str);
    }
}
