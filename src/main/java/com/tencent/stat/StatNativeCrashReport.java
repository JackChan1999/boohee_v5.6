package com.tencent.stat;

import android.content.Context;

import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.k;
import com.tencent.stat.common.p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashSet;

public class StatNativeCrashReport {
    public static final String                PRE_TAG_TOMBSTONE_FNAME = "tombstone_";
    static              StatNativeCrashReport a                       = new StatNativeCrashReport();
    private static      StatLogger            b                       = k.b();
    private static boolean d;
    private static   boolean e = false;
    private static   String  f = null;
    private volatile boolean c = false;

    static {
        d = false;
        try {
            System.loadLibrary("MtaNativeCrash");
        } catch (Throwable th) {
            d = false;
            b.w(th);
        }
    }

    static String a(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(readLine);
                stringBuilder.append('\n');
            }
            bufferedReader.close();
        } catch (Exception e) {
            b.e(e);
        }
        return stringBuilder.toString();
    }

    static LinkedHashSet<File> a(Context context) {
        LinkedHashSet<File> linkedHashSet = new LinkedHashSet();
        String tombstonesDir = getTombstonesDir(context);
        if (tombstonesDir != null) {
            File file = new File(tombstonesDir);
            if (file != null && file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File file2 : listFiles) {
                        if (file2.getName().startsWith(PRE_TAG_TOMBSTONE_FNAME) && file2.isFile()) {
                            b.d("get tombstone file:" + file2.getAbsolutePath().toString());
                            linkedHashSet.add(file2.getAbsoluteFile());
                        }
                    }
                }
            }
        }
        return linkedHashSet;
    }

    static long b(File file) {
        long j = 0;
        try {
            j = Long.valueOf(file.getName().replace(PRE_TAG_TOMBSTONE_FNAME, "")).longValue();
        } catch (Exception e) {
            b.e(e);
        }
        return j;
    }

    public static void doNativeCrashTest() {
        a.makeJniCrash();
    }

    public static String getTombstonesDir(Context context) {
        if (f == null) {
            f = p.a(context, "__mta_tombstone__", "");
        }
        return f;
    }

    public static void initNativeCrash(Context context, String str) {
        if (!a.c) {
            if (str == null) {
                try {
                    str = context.getDir("tombstones", 0).getAbsolutePath();
                } catch (Throwable th) {
                    b.w(th);
                    return;
                }
            }
            if (str.length() > 128) {
                b.e("The length of tombstones dir: " + str + " can't exceeds 200 bytes.");
                return;
            }
            f = str;
            p.b(context, "__mta_tombstone__", str);
            setNativeCrashEnable(true);
            a.initJNICrash(str);
            a.c = true;
            b.d("initNativeCrash success.");
        }
    }

    public static boolean isNativeCrashDebugEnable() {
        return e;
    }

    public static boolean isNativeCrashEnable() {
        return d;
    }

    public static String onNativeCrashHappened() {
        String str = "";
        try {
            new RuntimeException("MTA has caught a native crash, java stack:\n").printStackTrace();
            return str;
        } catch (RuntimeException e) {
            return e.toString();
        }
    }

    public static void setNativeCrashDebugEnable(boolean z) {
        try {
            a.enableNativeCrashDebug(z);
            e = z;
        } catch (Throwable th) {
            b.w(th);
        }
    }

    public static void setNativeCrashEnable(boolean z) {
        try {
            a.enableNativeCrash(z);
            d = z;
        } catch (Throwable th) {
            b.w(th);
        }
    }

    public native void enableNativeCrash(boolean z);

    public native void enableNativeCrashDebug(boolean z);

    public native boolean initJNICrash(String str);

    public native String makeJniCrash();

    public native String stringFromJNI();
}
