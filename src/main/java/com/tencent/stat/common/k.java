package com.tencent.stat.common;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.boohee.model.ModelName;
import com.boohee.utils.Coder;
import com.tencent.stat.StatConfig;
import com.umeng.analytics.a;
import com.xiaomi.account.openauth.utils.Network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpHost;
import org.eclipse.mat.parser.index.IndexWriter;
import org.json.JSONObject;

public class k {
    private static String     a = null;
    private static String     b = null;
    private static String     c = null;
    private static String     d = null;
    private static Random     e = null;
    private static StatLogger f = null;
    private static String     g = null;
    private static l          h = null;
    private static n          i = null;
    private static String     j = "__MTA_FIRST_ACTIVATE__";
    private static int        k = -1;

    public static String A(Context context) {
        try {
            SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
            if (sensorManager != null) {
                List sensorList = sensorManager.getSensorList(-1);
                if (sensorList != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < sensorList.size(); i++) {
                        stringBuilder.append(((Sensor) sensorList.get(i)).getType());
                        if (i != sensorList.size() - 1) {
                            stringBuilder.append(",");
                        }
                    }
                    return stringBuilder.toString();
                }
            }
        } catch (Object th) {
            f.e(th);
        }
        return "";
    }

    public static WifiInfo B(Context context) {
        if (a(context, "android.permission.ACCESS_WIFI_STATE")) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService("wifi");
            if (wifiManager != null) {
                return wifiManager.getConnectionInfo();
            }
        }
        return null;
    }

    public static String C(Context context) {
        try {
            WifiInfo B = B(context);
            if (B != null) {
                return B.getBSSID();
            }
        } catch (Object th) {
            f.e(th);
        }
        return null;
    }

    public static String D(Context context) {
        try {
            WifiInfo B = B(context);
            if (B != null) {
                return B.getSSID();
            }
        } catch (Object th) {
            f.e(th);
        }
        return null;
    }

    public static synchronized int E(Context context) {
        int i;
        synchronized (k.class) {
            if (k != -1) {
                i = k;
            } else {
                F(context);
                i = k;
            }
        }
        return i;
    }

    public static void F(Context context) {
        k = p.a(context, j, 1);
        f.e(Integer.valueOf(k));
        if (k == 1) {
            p.b(context, j, 0);
        }
    }

    private static long G(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ModelName
                .ACTIVITY);
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static int a() {
        return h().nextInt(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
    }

    public static Long a(String str, String str2, int i, int i2, Long l) {
        if (str == null || str2 == null) {
            return l;
        }
        if (str2.equalsIgnoreCase(".") || str2.equalsIgnoreCase("|")) {
            str2 = "\\" + str2;
        }
        String[] split = str.split(str2);
        if (split.length != i2) {
            return l;
        }
        try {
            Long valueOf = Long.valueOf(0);
            int i3 = 0;
            while (i3 < split.length) {
                Long valueOf2 = Long.valueOf(((long) i) * (valueOf.longValue() + Long.valueOf
                        (split[i3]).longValue()));
                i3++;
                valueOf = valueOf2;
            }
            return valueOf;
        } catch (NumberFormatException e) {
            return l;
        }
    }

    public static String a(long j) {
        return new SimpleDateFormat("yyyyMMdd").format(new Date(j));
    }

    public static String a(String str) {
        if (str == null) {
            return "0";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(Coder.KEY_MD5);
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                int i = b & 255;
                if (i < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(i));
            }
            return stringBuffer.toString();
        } catch (Throwable th) {
            return "0";
        }
    }

    public static HttpHost a(Context context) {
        if (context == null) {
            return null;
        }
        try {
            if (context.getPackageManager().checkPermission("android.permission" +
                    ".ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
                return null;
            }
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService
                    ("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return null;
            }
            if (activeNetworkInfo.getTypeName() != null && activeNetworkInfo.getTypeName()
                    .equalsIgnoreCase("WIFI")) {
                return null;
            }
            String extraInfo = activeNetworkInfo.getExtraInfo();
            if (extraInfo == null) {
                return null;
            }
            if (extraInfo.equals("cmwap") || extraInfo.equals("3gwap") || extraInfo.equals
                    ("uniwap")) {
                return new HttpHost(Network.CMWAP_GATEWAY, 80);
            }
            if (extraInfo.equals("ctwap")) {
                return new HttpHost("10.0.0.200", 80);
            }
            return null;
        } catch (Object th) {
            f.e(th);
        }
    }

    public static void a(JSONObject jSONObject, String str, String str2) {
        if (str2 != null) {
            try {
                if (str2.length() > 0) {
                    jSONObject.put(str, str2);
                }
            } catch (Object th) {
                f.e(th);
            }
        }
    }

    public static boolean a(Context context, String str) {
        try {
            return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
        } catch (Object th) {
            f.e(th);
            return false;
        }
    }

    public static byte[] a(byte[] bArr) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
        byte[] bArr2 = new byte[4096];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length * 2);
        while (true) {
            int read = gZIPInputStream.read(bArr2);
            if (read != -1) {
                byteArrayOutputStream.write(bArr2, 0, read);
            } else {
                bArr2 = byteArrayOutputStream.toByteArray();
                byteArrayInputStream.close();
                gZIPInputStream.close();
                byteArrayOutputStream.close();
                return bArr2;
            }
        }
    }

    public static long b(String str) {
        return a(str, ".", 100, 3, Long.valueOf(0)).longValue();
    }

    public static synchronized StatLogger b() {
        StatLogger statLogger;
        synchronized (k.class) {
            if (f == null) {
                f = new StatLogger("MtaSDK");
                f.setDebugEnable(false);
            }
            statLogger = f;
        }
        return statLogger;
    }

    public static synchronized String b(Context context) {
        String str;
        synchronized (k.class) {
            if (a == null || a.trim().length() == 0) {
                a = l(context);
                if (a == null || a.trim().length() == 0) {
                    a = Integer.toString(h().nextInt(ActivityChooserViewAdapter
                            .MAX_ACTIVITY_COUNT_UNLIMITED));
                }
                str = a;
            } else {
                str = a;
            }
        }
        return str;
    }

    public static String b(Context context, String str) {
        if (!StatConfig.isEnableConcurrentProcess()) {
            return str;
        }
        if (g == null) {
            g = u(context);
        }
        return g != null ? str + "_" + g : str;
    }

    public static long c() {
        try {
            Calendar instance = Calendar.getInstance();
            instance.set(11, 0);
            instance.set(12, 0);
            instance.set(13, 0);
            instance.set(14, 0);
            return instance.getTimeInMillis() + a.h;
        } catch (Object th) {
            f.e(th);
            return System.currentTimeMillis() + a.h;
        }
    }

    public static synchronized String c(Context context) {
        String str;
        synchronized (k.class) {
            if (c == null || "" == c) {
                c = f(context);
            }
            str = c;
        }
        return str;
    }

    public static String c(String str) {
        if (str == null) {
            return null;
        }
        if (VERSION.SDK_INT < 8) {
            return str;
        }
        try {
            return new String(g.b(e.a(str.getBytes("UTF-8")), 0), "UTF-8");
        } catch (Object th) {
            f.e(th);
            return str;
        }
    }

    public static int d() {
        return VERSION.SDK_INT;
    }

    public static DisplayMetrics d(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService("window"))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static String d(String str) {
        if (str == null) {
            return null;
        }
        if (VERSION.SDK_INT < 8) {
            return str;
        }
        try {
            return new String(e.b(g.a(str.getBytes("UTF-8"), 0)), "UTF-8");
        } catch (Object th) {
            f.e(th);
            return str;
        }
    }

    public static String e() {
        long f = f() / 1000000;
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return String.valueOf((((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize
                ())) / 1000000) + "/" + String.valueOf(f);
    }

    public static boolean e(Context context) {
        try {
            if (a(context, "android.permission.ACCESS_WIFI_STATE")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getApplicationContext().getSystemService("connectivity");
                if (connectivityManager != null) {
                    NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
                    if (allNetworkInfo != null) {
                        int i = 0;
                        while (i < allNetworkInfo.length) {
                            if (allNetworkInfo[i].getTypeName().equalsIgnoreCase("WIFI") &&
                                    allNetworkInfo[i].isConnected()) {
                                return true;
                            }
                            i++;
                        }
                    }
                }
                return false;
            }
            f.warn("can not get the permission of android.permission.ACCESS_WIFI_STATE");
            return false;
        } catch (Object th) {
            f.e(th);
        }
    }

    public static long f() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
    }

    public static String f(Context context) {
        if (a(context, "android.permission.ACCESS_WIFI_STATE")) {
            try {
                WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                return wifiManager == null ? "" : wifiManager.getConnectionInfo().getMacAddress();
            } catch (Exception e) {
                f.e(e);
                return "";
            }
        }
        f.e((Object) "Could not get permission of android.permission.ACCESS_WIFI_STATE");
        return "";
    }

    public static boolean g(Context context) {
        try {
            if (a(context, "android.permission.INTERNET") && a(context, "android.permission" +
                    ".ACCESS_NETWORK_STATE")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService("connectivity");
                if (connectivityManager != null) {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    return activeNetworkInfo != null && activeNetworkInfo.isAvailable() &&
                            activeNetworkInfo.getTypeName().equalsIgnoreCase("WIFI");
                }
                return false;
            }
            f.warn("can not get the permisson of android.permission.INTERNET");
            return false;
        } catch (Object th) {
            f.e(th);
        }
    }

    private static synchronized Random h() {
        Random random;
        synchronized (k.class) {
            if (e == null) {
                e = new Random();
            }
            random = e;
        }
        return random;
    }

    public static boolean h(Context context) {
        try {
            if (a(context, "android.permission.INTERNET") && a(context, "android.permission" +
                    ".ACCESS_NETWORK_STATE")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService("connectivity");
                if (connectivityManager != null) {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                        return true;
                    }
                    f.w("Network error");
                    return false;
                }
                return false;
            }
            f.warn("can not get the permisson of android.permission.INTERNET");
            return false;
        } catch (Throwable th) {
        }
    }

    private static long i() {
        long j = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"),
                    8192);
            j = (long) (Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]).intValue() *
                    1024);
            bufferedReader.close();
            return j;
        } catch (IOException e) {
            return j;
        }
    }

    public static String i(Context context) {
        if (b != null) {
            return b;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo
                    (context.getPackageName(), 128);
            if (applicationInfo != null) {
                String string = applicationInfo.metaData.getString("TA_APPKEY");
                if (string != null) {
                    b = string;
                    return string;
                }
                f.w("Could not read APPKEY meta-data from AndroidManifest.xml");
            }
        } catch (Throwable th) {
            f.w("Could not read APPKEY meta-data from AndroidManifest.xml");
        }
        return null;
    }

    public static String j(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo
                    (context.getPackageName(), 128);
            if (applicationInfo != null) {
                Object obj = applicationInfo.metaData.get("InstallChannel");
                if (obj != null) {
                    return obj.toString();
                }
                f.w("Could not read InstallChannel meta-data from AndroidManifest.xml");
            }
        } catch (Throwable th) {
            f.e((Object) "Could not read InstallChannel meta-data from AndroidManifest.xml");
        }
        return null;
    }

    public static String k(Context context) {
        return context == null ? null : context.getClass().getName();
    }

    public static String l(Context context) {
        try {
            if (a(context, "android.permission.READ_PHONE_STATE")) {
                String str = "";
                if (o(context)) {
                    str = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
                }
                if (str != null) {
                    return str;
                }
            }
            f.e((Object) "Could not get permission of android.permission.READ_PHONE_STATE");
        } catch (Object th) {
            f.e(th);
        }
        return null;
    }

    public static String m(Context context) {
        try {
            if (!a(context, "android.permission.READ_PHONE_STATE")) {
                f.e((Object) "Could not get permission of android.permission.READ_PHONE_STATE");
                return null;
            } else if (!o(context)) {
                return null;
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService
                        ("phone");
                return telephonyManager != null ? telephonyManager.getSimOperator() : null;
            }
        } catch (Object th) {
            f.e(th);
            return null;
        }
    }

    public static String n(Context context) {
        Object th;
        String str = "";
        String str2;
        try {
            str2 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            if (str2 != null) {
                return str2;
            }
            try {
                return "";
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            str2 = str;
            th = th4;
            f.e(th);
            return str2;
        }
    }

    public static boolean o(Context context) {
        return context.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE",
                context.getPackageName()) == 0;
    }

    public static String p(Context context) {
        try {
            if (a(context, "android.permission.INTERNET") && a(context, "android.permission" +
                    ".ACCESS_NETWORK_STATE")) {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService
                        ("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    String typeName = activeNetworkInfo.getTypeName();
                    String extraInfo = activeNetworkInfo.getExtraInfo();
                    if (typeName != null) {
                        return typeName.equalsIgnoreCase("WIFI") ? "WIFI" : typeName
                                .equalsIgnoreCase("MOBILE") ? extraInfo == null ? "MOBILE" :
                                extraInfo : extraInfo == null ? typeName : extraInfo;
                    }
                }
                return null;
            }
            f.e((Object) "can not get the permission of android.permission.ACCESS_WIFI_STATE");
            return null;
        } catch (Object th) {
            f.e(th);
        }
    }

    public static Integer q(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService
                    ("phone");
            if (telephonyManager != null) {
                return Integer.valueOf(telephonyManager.getNetworkType());
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public static String r(Context context) {
        String str;
        Object th;
        String str2 = "";
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            if (str != null) {
                try {
                    if (str.length() != 0) {
                        return str;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    f.e(th);
                    return str;
                }
            }
            return EnvironmentCompat.MEDIA_UNKNOWN;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            str = str2;
            th = th4;
            f.e(th);
            return str;
        }
    }

    public static int s(Context context) {
        try {
            if (o.a()) {
                return 1;
            }
        } catch (Object th) {
            f.e(th);
        }
        return 0;
    }

    public static String t(Context context) {
        try {
            if (a(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                String externalStorageState = Environment.getExternalStorageState();
                if (externalStorageState == null || !externalStorageState.equals("mounted")) {
                    return null;
                }
                externalStorageState = Environment.getExternalStorageDirectory().getPath();
                if (externalStorageState == null) {
                    return null;
                }
                StatFs statFs = new StatFs(externalStorageState);
                long blockCount = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize
                        ())) / 1000000;
                return String.valueOf((((long) statFs.getBlockSize()) * ((long) statFs
                        .getAvailableBlocks())) / 1000000) + "/" + String.valueOf(blockCount);
            }
            f.warn("can not get the permission of android.permission.WRITE_EXTERNAL_STORAGE");
            return null;
        } catch (Object th) {
            f.e(th);
            return null;
        }
    }

    static String u(Context context) {
        try {
            int myPid = Process.myPid();
            for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context
                    .getSystemService(ModelName.ACTIVITY)).getRunningAppProcesses()) {
                if (runningAppProcessInfo.pid == myPid) {
                    return runningAppProcessInfo.processName;
                }
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public static String v(Context context) {
        return b(context, StatConstants.a);
    }

    public static synchronized Integer w(Context context) {
        Integer valueOf;
        int i = 0;
        synchronized (k.class) {
            try {
                int a = p.a(context, "MTA_EVENT_INDEX", 0);
                if (a < 2147483646) {
                    i = a;
                }
                p.b(context, "MTA_EVENT_INDEX", i + 1);
            } catch (Object th) {
                f.e(th);
            }
            valueOf = Integer.valueOf(i + 1);
        }
        return valueOf;
    }

    public static String x(Context context) {
        return String.valueOf(G(context) / 1000000) + "/" + String.valueOf(i() / 1000000);
    }

    public static synchronized l y(Context context) {
        l lVar;
        synchronized (k.class) {
            if (h == null) {
                h = new l();
            }
            lVar = h;
        }
        return lVar;
    }

    public static JSONObject z(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            y(context);
            int b = l.b();
            if (b > 0) {
                jSONObject.put("fx", b / IndexWriter.PAGE_SIZE_INT);
            }
            y(context);
            b = l.c();
            if (b > 0) {
                jSONObject.put("fn", b / IndexWriter.PAGE_SIZE_INT);
            }
            y(context);
            b = l.a();
            if (b > 0) {
                jSONObject.put("n", b);
            }
            y(context);
            String d = l.d();
            if (d != null && d.length() == 0) {
                y(context);
                jSONObject.put("na", l.d());
            }
        } catch (Exception e) {
            f.e(e);
        }
        return jSONObject;
    }
}
