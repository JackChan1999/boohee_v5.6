package cn.sharesdk.framework;

import android.content.Context;
import android.graphics.Bitmap;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Ln;
import java.util.HashMap;

public class ShareSDK {
    private static k a;
    private static boolean b = true;

    static String a(int i, String str) {
        c();
        return a.a(i, str);
    }

    static String a(Bitmap bitmap) {
        c();
        return a.a(bitmap);
    }

    static String a(String str) {
        c();
        return a.d(str);
    }

    static String a(String str, boolean z, int i, String str2) {
        c();
        return a.a(str, z, i, str2);
    }

    static void a(int i, int i2) {
        c();
        a.a(i, i2);
    }

    static void a(String str, String str2) {
        c();
        a.a(str, str2);
    }

    static boolean a() {
        c();
        return a.e();
    }

    static boolean a(HashMap<String, Object> hashMap) {
        c();
        return a.a((HashMap) hashMap);
    }

    static String b(String str, String str2) {
        c();
        return a.b(str, str2);
    }

    static boolean b() {
        c();
        return a.f();
    }

    static boolean b(HashMap<String, Object> hashMap) {
        c();
        return a.b((HashMap) hashMap);
    }

    private static void c() {
        if (a == null) {
            throw new NullPointerException("Please call ShareSDK.initSDK(Context) before any action.");
        }
    }

    public static void closeDebug() {
        b = false;
    }

    public static void deleteCache() {
        c();
        a.g();
    }

    @Deprecated
    public static Platform getPlatform(Context context, String str) {
        c();
        return a.b(str);
    }

    public static Platform getPlatform(String str) {
        c();
        return a.b(str);
    }

    public static synchronized Platform[] getPlatformList() {
        Platform[] a;
        synchronized (ShareSDK.class) {
            c();
            a = a.a();
        }
        return a;
    }

    @Deprecated
    public static synchronized Platform[] getPlatformList(Context context) {
        Platform[] platformList;
        synchronized (ShareSDK.class) {
            platformList = getPlatformList();
        }
        return platformList;
    }

    public static int getSDKVersionCode() {
        c();
        return a.c();
    }

    public static String getSDKVersionName() {
        c();
        return a.b();
    }

    public static <T extends Service> T getService(Class<T> cls) {
        c();
        return a.c((Class) cls);
    }

    public static void initSDK(Context context) {
        initSDK(context, null, true);
    }

    public static void initSDK(Context context, String str) {
        initSDK(context, str, true);
    }

    public static void initSDK(Context context, String str, boolean z) {
        Ln.close();
        DeviceHelper instance = DeviceHelper.getInstance(context);
        if (instance == null) {
            throw new NullPointerException("Please call ShareSDK.initSDK(Context) in the main process!");
        } else if (instance.isMainProcess(context) && a == null) {
            k kVar = new k(context);
            kVar.a(str);
            kVar.a(z);
            kVar.startThread();
            a = kVar;
        }
    }

    public static void initSDK(Context context, boolean z) {
        initSDK(context, null, z);
    }

    public static boolean isDebug() {
        return b;
    }

    public static boolean isRemoveCookieOnAuthorize() {
        c();
        return a.d();
    }

    public static void logApiEvent(String str, int i) {
        c();
        a.a(str, i);
    }

    public static void logDemoEvent(int i, Platform platform) {
        c();
        a.a(i, platform);
    }

    public static String platformIdToName(int i) {
        c();
        return a.c(i);
    }

    public static int platformNameToId(String str) {
        c();
        return a.c(str);
    }

    public static void registerPlatform(Class<? extends CustomPlatform> cls) {
        c();
        a.d((Class) cls);
    }

    public static void registerService(Class<? extends Service> cls) {
        c();
        a.a((Class) cls);
    }

    public static void removeCookieOnAuthorize(boolean z) {
        c();
        a.b(z);
    }

    public static void setConnTimeout(int i) {
        c();
        a.a(i);
    }

    public static void setPlatformDevInfo(String str, HashMap<String, Object> hashMap) {
        c();
        a.a(str, (HashMap) hashMap);
    }

    public static void setReadTimeout(int i) {
        c();
        a.b(i);
    }

    public static void stopSDK() {
    }

    public static void stopSDK(Context context) {
        stopSDK();
    }

    public static void unregisterPlatform(Class<? extends CustomPlatform> cls) {
        c();
        a.e(cls);
    }

    public static void unregisterService(Class<? extends Service> cls) {
        c();
        a.b((Class) cls);
    }
}
