package com.tencent.stat;

import android.content.Context;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.boohee.utils.DateFormatUtils;
import com.tencent.stat.common.StatConstants;
import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.k;
import com.tencent.stat.common.p;

import java.util.Iterator;

import org.apache.http.HttpHost;
import org.eclipse.mat.parser.index.IndexWriter;
import org.json.JSONException;
import org.json.JSONObject;

public class StatConfig {
    private static int                A                     = 4096;
    private static boolean            B                     = false;
    private static String             C                     = null;
    static         b                  a                     = new b(2);
    static         b                  b                     = new b(1);
    static         String             c                     = "__HIBERNATE__";
    static         String             d                     = "";
    private static StatLogger         e                     = k.b();
    private static StatReportStrategy f                     = StatReportStrategy.APP_LAUNCH;
    private static boolean            g                     = true;
    private static int                h                     = 30000;
    private static int                i                     = 1024;
    public static  boolean            isAutoExceptionCaught = true;
    private static int                j                     = 30;
    private static int                k                     = 3;
    private static int                l                     = 30;
    private static String             m                     = null;
    private static String n;
    private static String o;
    private static          int     p = 1440;
    private static          int     q = 1024;
    private static          boolean r = true;
    private static          long    s = 0;
    private static          long    t = DeviceInfoConstant.REQUEST_LOCATE_INTERVAL;
    private static          String  u = "http://pingma.qq.com:80/mstat/report";
    private static          int     v = 0;
    private static volatile int     w = 0;
    private static          int     x = 20;
    private static          int     y = 0;
    private static          boolean z = false;

    static int a() {
        return j;
    }

    static String a(Context context) {
        return k.d(p.a(context, "_mta_ky_tag_", null));
    }

    static String a(String str, String str2) {
        try {
            String string = b.b.getString(str);
            return string != null ? string : str2;
        } catch (Throwable th) {
            e.w(th);
            return str2;
        }
    }

    static synchronized void a(int i) {
        synchronized (StatConfig.class) {
            w = i;
        }
    }

    static void a(Context context, String str) {
        if (str != null) {
            p.b(context, "_mta_ky_tag_", k.c(str));
        }
    }

    static void a(b bVar) {
        if (bVar.a == b.a) {
            b = bVar;
            b(b.b);
        } else if (bVar.a == a.a) {
            a = bVar;
        }
    }

    static void a(b bVar, JSONObject jSONObject) {
        Object obj;
        Object obj2 = null;
        try {
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (str.equalsIgnoreCase("v")) {
                    int i = jSONObject.getInt(str);
                    obj = bVar.d != i ? 1 : obj2;
                    bVar.d = i;
                } else if (str.equalsIgnoreCase("c")) {
                    str = jSONObject.getString("c");
                    if (str.length() > 0) {
                        bVar.b = new JSONObject(str);
                    }
                    obj = obj2;
                } else {
                    if (str.equalsIgnoreCase("m")) {
                        bVar.c = jSONObject.getString("m");
                    }
                    obj = obj2;
                }
                obj2 = obj;
            }
            if (obj2 == 1) {
                n a = n.a(d.a());
                if (a != null) {
                    a.a(bVar);
                }
                if (bVar.a == b.a) {
                    b(bVar.b);
                    c(bVar.b);
                }
            }
        } catch (Exception e) {
            e.e(e);
        } catch (Object obj3) {
            e.e(obj3);
        }
    }

    static void a(JSONObject jSONObject) {
        try {
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (str.equalsIgnoreCase(Integer.toString(b.a))) {
                    a(b, jSONObject.getJSONObject(str));
                } else if (str.equalsIgnoreCase(Integer.toString(a.a))) {
                    a(a, jSONObject.getJSONObject(str));
                } else if (str.equalsIgnoreCase("rs")) {
                    StatReportStrategy statReportStrategy = StatReportStrategy
                            .getStatReportStrategy(jSONObject.getInt(str));
                    if (statReportStrategy != null) {
                        f = statReportStrategy;
                        e.d("Change to ReportStrategy:" + statReportStrategy.name());
                    }
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            e.e(e);
        }
    }

    static void a(boolean z) {
        StatNativeCrashReport.setNativeCrashDebugEnable(z);
    }

    static boolean a(int i, int i2, int i3) {
        return i >= i2 && i <= i3;
    }

    private static boolean a(String str) {
        if (str == null) {
            return false;
        }
        if (n == null) {
            n = str;
            return true;
        } else if (n.contains(str)) {
            return false;
        } else {
            n += "|" + str;
            return true;
        }
    }

    static HttpHost b() {
        if (m == null || m.length() <= 0) {
            return null;
        }
        String str = m;
        String[] split = str.split(":");
        int i = 80;
        if (split.length == 2) {
            str = split[0];
            i = Integer.parseInt(split[1]);
        }
        return new HttpHost(str, i);
    }

    static void b(int i) {
        if (i >= 0) {
            y = i;
        }
    }

    static void b(JSONObject jSONObject) {
        try {
            StatReportStrategy statReportStrategy = StatReportStrategy.getStatReportStrategy
                    (jSONObject.getInt("rs"));
            if (statReportStrategy != null) {
                setStatSendStrategy(statReportStrategy);
            }
        } catch (JSONException e) {
            e.d("rs not found.");
        }
    }

    static synchronized void c() {
        synchronized (StatConfig.class) {
            w++;
        }
    }

    static void c(JSONObject jSONObject) {
        try {
            String string = jSONObject.getString(c);
            e.d("hibernateVer:" + string + ", current version:" + StatConstants.VERSION);
            long b = k.b(string);
            if (k.b(StatConstants.VERSION) <= b) {
                p.b(d.a(), c, b);
                setEnableStatService(false);
                e.warn("MTA has disable for SDK version of " + string + " or lower.");
            }
        } catch (JSONException e) {
            e.d("__HIBERNATE__ not found.");
        }
    }

    static void d() {
        y++;
    }

    static int e() {
        return y;
    }

    public static synchronized String getAppKey(Context context) {
        String str;
        synchronized (StatConfig.class) {
            if (n != null) {
                str = n;
            } else {
                if (context != null) {
                    if (n == null) {
                        n = k.i(context);
                    }
                }
                if (n == null || n.trim().length() == 0) {
                    e.error((Object) "AppKey can not be null or empty, please read Developer's " +
                            "Guide first!");
                }
                str = n;
            }
        }
        return str;
    }

    public static int getCurSessionStatReportCount() {
        return w;
    }

    public static String getCustomProperty(String str) {
        try {
            return a.b.getString(str);
        } catch (Object th) {
            e.e(th);
            return null;
        }
    }

    public static String getCustomProperty(String str, String str2) {
        try {
            String string = a.b.getString(str);
            return string != null ? string : str2;
        } catch (Object th) {
            e.e(th);
            return str2;
        }
    }

    public static String getCustomUserId(Context context) {
        if (context == null) {
            e.error((Object) "Context for getCustomUid is null.");
            return null;
        }
        if (C == null) {
            C = p.a(context, "MTA_CUSTOM_UID", "");
        }
        return C;
    }

    public static DeviceInfo getDeviceInfo(Context context) {
        return StatMid.getDeviceInfo(context);
    }

    public static synchronized String getInstallChannel(Context context) {
        String str;
        synchronized (StatConfig.class) {
            if (o != null) {
                str = o;
            } else {
                o = k.j(context);
                if (o == null || o.trim().length() == 0) {
                    e.w("installChannel can not be null or empty, please read Developer's Guide " +
                            "first!");
                }
                str = o;
            }
        }
        return str;
    }

    public static int getMaxBatchReportCount() {
        return l;
    }

    public static int getMaxDaySessionNumbers() {
        return x;
    }

    public static int getMaxParallelTimmingEvents() {
        return q;
    }

    public static int getMaxReportEventLength() {
        return A;
    }

    public static int getMaxSendRetryCount() {
        return k;
    }

    public static int getMaxSessionStatReportCount() {
        return v;
    }

    public static int getMaxStoreEventCount() {
        return i;
    }

    public static String getMid(Context context) {
        return StatMid.getMid(context);
    }

    public static String getQQ() {
        return d;
    }

    public static int getSendPeriodMinutes() {
        return p;
    }

    public static int getSessionTimoutMillis() {
        return h;
    }

    public static String getStatReportUrl() {
        return u;
    }

    public static StatReportStrategy getStatSendStrategy() {
        return f;
    }

    public static void initNativeCrashReport(Context context, String str) {
        if (!isEnableStatService()) {
            return;
        }
        if (context == null) {
            e.error((Object) "The Context of StatConfig.initNativeCrashReport() can not be null!");
        } else {
            StatNativeCrashReport.initNativeCrash(context, str);
        }
    }

    public static boolean isAutoExceptionCaught() {
        return isAutoExceptionCaught;
    }

    public static boolean isDebugEnable() {
        return k.b().isDebugEnable();
    }

    public static boolean isEnableConcurrentProcess() {
        return B;
    }

    public static boolean isEnableSmartReporting() {
        return r;
    }

    public static boolean isEnableStatService() {
        return g;
    }

    public static void setAppKey(Context context, String str) {
        if (context == null) {
            e.error((Object) "ctx in StatConfig.setAppKey() is null");
        } else if (str == null || str.length() > 256) {
            e.error((Object) "appkey in StatConfig.setAppKey() is null or exceed 256 bytes");
        } else {
            if (n == null) {
                n = a(context);
            }
            if ((a(str) | a(k.i(context))) != 0) {
                a(context, n);
            }
        }
    }

    public static void setAppKey(String str) {
        if (str == null) {
            e.error((Object) "appkey in StatConfig.setAppKey() is null");
        } else if (str.length() > 256) {
            e.error((Object) "The length of appkey cann't exceed 256 bytes.");
        } else {
            n = str;
        }
    }

    public static void setAutoExceptionCaught(boolean z) {
        isAutoExceptionCaught = z;
    }

    public static void setCustomUserId(Context context, String str) {
        if (context == null) {
            e.error((Object) "Context for setCustomUid is null.");
            return;
        }
        p.b(context, "MTA_CUSTOM_UID", str);
        C = str;
    }

    public static void setDebugEnable(boolean z) {
        k.b().setDebugEnable(z);
        a(z);
    }

    public static void setEnableConcurrentProcess(boolean z) {
        B = z;
    }

    public static void setEnableSmartReporting(boolean z) {
        r = z;
    }

    public static void setEnableStatService(boolean z) {
        g = z;
        if (!z) {
            e.warn("!!!!!!MTA StatService has been disabled!!!!!!");
        }
    }

    public static void setInstallChannel(String str) {
        if (str.length() > 128) {
            e.error((Object) "the length of installChannel can not exceed the range of 128 bytes.");
        } else {
            o = str;
        }
    }

    public static void setMaxBatchReportCount(int i) {
        if (a(i, 2, 1000)) {
            l = i;
        } else {
            e.error((Object) "setMaxBatchReportCount can not exceed the range of [2,1000].");
        }
    }

    public static void setMaxDaySessionNumbers(int i) {
        if (i <= 0) {
            e.e((Object) "maxDaySessionNumbers must be greater than 0.");
        } else {
            x = i;
        }
    }

    public static void setMaxParallelTimmingEvents(int i) {
        if (a(i, 1, 4096)) {
            q = i;
        } else {
            e.error((Object) "setMaxParallelTimmingEvents can not exceed the range of [1, 4096].");
        }
    }

    public static void setMaxReportEventLength(int i) {
        if (i <= 0) {
            e.error((Object) "maxReportEventLength on setMaxReportEventLength() must greater than" +
                    " 0.");
        } else {
            A = i;
        }
    }

    public static void setMaxSendRetryCount(int i) {
        if (a(i, 1, 1000)) {
            k = i;
        } else {
            e.error((Object) "setMaxSendRetryCount can not exceed the range of [1,1000].");
        }
    }

    public static void setMaxSessionStatReportCount(int i) {
        if (i < 0) {
            e.error((Object) "maxSessionStatReportCount cannot be less than 0.");
        } else {
            v = i;
        }
    }

    public static void setMaxStoreEventCount(int i) {
        if (a(i, 0, IndexWriter.PAGE_SIZE_LONG)) {
            i = i;
        } else {
            e.error((Object) "setMaxStoreEventCount can not exceed the range of [0, 500000].");
        }
    }

    public static void setQQ(Context context, String str) {
        StatService.reportQQ(context, str);
    }

    public static void setSendPeriodMinutes(int i) {
        if (a(i, 1, 10080)) {
            p = i;
        } else {
            e.error((Object) "setSendPeriodMinutes can not exceed the range of [1, 7*24*60] " +
                    "minutes.");
        }
    }

    public static void setSessionTimoutMillis(int i) {
        if (a(i, 1000, DateFormatUtils.DAY)) {
            h = i;
        } else {
            e.error((Object) "setSessionTimoutMillis can not exceed the range of [1000, 24 * 60 * 60 * 1000].");
        }
    }

    public static void setStatReportUrl(String str) {
        if (str == null || str.length() == 0) {
            e.error((Object) "statReportUrl cannot be null or empty.");
        } else {
            u = str;
        }
    }

    public static void setStatSendStrategy(StatReportStrategy statReportStrategy) {
        f = statReportStrategy;
        e.d("Change to statSendStrategy: " + statReportStrategy);
    }
}
