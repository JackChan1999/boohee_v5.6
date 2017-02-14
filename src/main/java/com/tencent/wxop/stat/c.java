package com.tencent.wxop.stat;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;

import com.alipay.sdk.sys.a;
import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.tencent.a.a.a.a.g;
import com.tencent.stat.DeviceInfo;
import com.tencent.stat.common.StatConstants;
import com.tencent.wxop.stat.b.b;
import com.tencent.wxop.stat.b.l;
import com.tencent.wxop.stat.b.q;
import com.tencent.wxop.stat.b.r;

import java.net.URI;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public final class c {
    static         String  M = "__MTA_KILL__";
    private static b       N = l.av();
    static         ah      O = new ah(2);
    static         ah      P = new ah(1);
    private static d       Q = d.APP_LAUNCH;
    private static boolean R = false;
    private static boolean S = true;
    private static int     T = 30000;
    private static int     U = 100000;
    private static int     V = 30;
    static         String  W = "__HIBERNATE__TIME";
    private static String  X = null;
    private static String Y;
    private static String Z;
    static                  int     aA = 512;
    private static          String  aa = "mta_channel";
    static                  String  ab = "";
    private static          int     ac = 180;
    static                  boolean ad = false;
    static                  int     ae = 100;
    static                  long    af = 10000;
    private static          int     ag = 1024;
    static                  boolean ah = true;
    private static          long    ai = 0;
    private static          long    aj = DeviceInfoConstant.REQUEST_LOCATE_INTERVAL;
    public static           boolean ak = true;
    static volatile         String  al = StatConstants.MTA_SERVER;
    private static volatile String  am = "http://pingma.qq.com:80/mstat/report";
    private static          int     an = 0;
    private static volatile int     ao = 0;
    private static          int     ap = 20;
    private static          int     aq = 0;
    private static          boolean ar = false;
    private static          int     as = 4096;
    private static          boolean at = false;
    private static          String  au = null;
    private static          boolean av = false;
    private static          ai      aw = null;
    static                  boolean ax = true;
    static                  int     ay = 0;
    static                  long    az = 10000;
    static                  String  c  = "__HIBERNATE__";
    private static          int     w  = 10;
    private static          int     x  = 100;
    private static          int     y  = 30;
    private static          int     z  = 1;

    public static int A() {
        return ap;
    }

    static void B() {
        aq++;
    }

    static void C() {
        aq = 0;
    }

    static int D() {
        return aq;
    }

    public static boolean E() {
        return at;
    }

    public static ai F() {
        return aw;
    }

    static void a(Context context, ah ahVar) {
        if (ahVar.aI == P.aI) {
            P = ahVar;
            a(ahVar.df);
            if (!P.df.isNull("iplist")) {
                g.r(context).b(P.df.getString("iplist"));
            }
        } else if (ahVar.aI == O.aI) {
            O = ahVar;
        }
    }

    private static void a(Context context, ah ahVar, JSONObject jSONObject) {
        try {
            String str;
            Object obj;
            Iterator keys = jSONObject.keys();
            Object obj2 = null;
            while (keys.hasNext()) {
                str = (String) keys.next();
                if (str.equalsIgnoreCase("v")) {
                    int i = jSONObject.getInt(str);
                    obj = ahVar.L != i ? 1 : obj2;
                    ahVar.L = i;
                    obj2 = obj;
                } else if (str.equalsIgnoreCase("c")) {
                    str = jSONObject.getString("c");
                    if (str.length() > 0) {
                        ahVar.df = new JSONObject(str);
                    }
                } else {
                    try {
                        if (str.equalsIgnoreCase("m")) {
                            ahVar.c = jSONObject.getString("m");
                        }
                    } catch (JSONException e) {
                        N.e("__HIBERNATE__ not found.");
                    } catch (Throwable th) {
                        N.b(th);
                    }
                }
            }
            if (obj2 == 1) {
                t s = t.s(ak.aB());
                if (s != null) {
                    s.b(ahVar);
                }
                if (ahVar.aI == P.aI) {
                    a(ahVar.df);
                    JSONObject jSONObject2 = ahVar.df;
                    if (!(jSONObject2 == null || jSONObject2.length() == 0)) {
                        Context aB = ak.aB();
                        try {
                            str = jSONObject2.optString(M);
                            if (l.e(str)) {
                                JSONObject jSONObject3 = new JSONObject(str);
                                if (jSONObject3.length() != 0) {
                                    if (!jSONObject3.isNull("sm")) {
                                        obj = jSONObject3.get("sm");
                                        int intValue = obj instanceof Integer ? ((Integer) obj)
                                                .intValue() : obj instanceof String ? Integer
                                                .valueOf((String) obj).intValue() : 0;
                                        if (intValue > 0) {
                                            if (R) {
                                                N.b("match sleepTime:" + intValue + " minutes");
                                            }
                                            q.a(aB, W, System.currentTimeMillis() + ((long) (
                                                    (intValue * 60) * 1000)));
                                            a(false);
                                            N.warn("MTA is disable for current SDK version");
                                        }
                                    }
                                    if (b(jSONObject3, a.h, "2.0.3")) {
                                        N.b((Object) "match sdk version:2.0.3");
                                        obj = 1;
                                    } else {
                                        obj = null;
                                    }
                                    if (b(jSONObject3, "md", Build.MODEL)) {
                                        N.b("match MODEL:" + Build.MODEL);
                                        obj = 1;
                                    }
                                    if (b(jSONObject3, a.j, l.D(aB))) {
                                        N.b("match app version:" + l.D(aB));
                                        obj = 1;
                                    }
                                    if (b(jSONObject3, "mf", Build.MANUFACTURER)) {
                                        N.b("match MANUFACTURER:" + Build.MANUFACTURER);
                                        obj = 1;
                                    }
                                    if (b(jSONObject3, "osv", VERSION.SDK_INT)) {
                                        N.b("match android SDK version:" + VERSION.SDK_INT);
                                        obj = 1;
                                    }
                                    if (b(jSONObject3, "ov", VERSION.SDK_INT)) {
                                        N.b("match android SDK version:" + VERSION.SDK_INT);
                                        obj = 1;
                                    }
                                    if (b(jSONObject3, DeviceInfo.TAG_IMEI, t.s(aB).t(aB).b())) {
                                        N.b("match imei:" + t.s(aB).t(aB).b());
                                        obj = 1;
                                    }
                                    if (b(jSONObject3, DeviceInfo.TAG_MID, h(aB))) {
                                        N.b("match mid:" + h(aB));
                                        obj = 1;
                                    }
                                    if (obj != null) {
                                        b(l.u("2.0.3"));
                                    }
                                }
                            }
                        } catch (Throwable th2) {
                            N.b(th2);
                        }
                        str = jSONObject2.getString(c);
                        if (R) {
                            N.e("hibernateVer:" + str + ", current version:2.0.3");
                        }
                        long u = l.u(str);
                        if (l.u("2.0.3") <= u) {
                            b(u);
                        }
                    }
                }
            }
            a(context, ahVar);
        } catch (Throwable th22) {
            N.b(th22);
        } catch (Throwable th222) {
            N.b(th222);
        }
    }

    static void a(Context context, JSONObject jSONObject) {
        try {
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (str.equalsIgnoreCase(Integer.toString(P.aI))) {
                    a(context, P, jSONObject.getJSONObject(str));
                } else if (str.equalsIgnoreCase(Integer.toString(O.aI))) {
                    a(context, O, jSONObject.getJSONObject(str));
                } else if (str.equalsIgnoreCase("rs")) {
                    d a = d.a(jSONObject.getInt(str));
                    if (a != null) {
                        Q = a;
                        if (R) {
                            N.e("Change to ReportStrategy:" + a.name());
                        }
                    }
                } else {
                    return;
                }
            }
        } catch (Throwable e) {
            N.b(e);
        }
    }

    public static void a(d dVar) {
        Q = dVar;
        if (dVar != d.PERIOD) {
            e.aZ = 0;
        }
        if (R) {
            N.e("Change to statSendStrategy: " + dVar);
        }
    }

    private static void a(JSONObject jSONObject) {
        try {
            d a = d.a(jSONObject.getInt("rs"));
            if (a != null) {
                a(a);
            }
        } catch (JSONException e) {
            if (R) {
                N.b((Object) "rs not found.");
            }
        }
    }

    public static void a(boolean z) {
        S = z;
        if (!z) {
            N.warn("!!!!!!MTA StatService has been disabled!!!!!!");
        }
    }

    private static void b(long j) {
        q.a(ak.aB(), c, j);
        a(false);
        N.warn("MTA is disable for current SDK version");
    }

    public static void b(Context context, String str) {
        if (context == null) {
            N.error("ctx in StatConfig.setAppKey() is null");
        } else if (str == null || str.length() > 256) {
            N.error("appkey in StatConfig.setAppKey() is null or exceed 256 bytes");
        } else {
            if (Y == null) {
                Y = r.t(q.b(context, "_mta_ky_tag_", null));
            }
            if ((m(str) | m(l.z(context))) != 0) {
                String str2 = Y;
                if (str2 != null) {
                    q.c(context, "_mta_ky_tag_", r.q(str2));
                }
            }
        }
    }

    private static boolean b(JSONObject jSONObject, String str, String str2) {
        if (!jSONObject.isNull(str)) {
            String optString = jSONObject.optString(str);
            if (l.e(str2) && l.e(optString) && str2.equalsIgnoreCase(optString)) {
                return true;
            }
        }
        return false;
    }

    public static void c(Context context, String str) {
        if (str.length() > 128) {
            N.error("the length of installChannel can not exceed the range of 128 bytes.");
            return;
        }
        Z = str;
        q.c(context, aa, str);
    }

    public static synchronized String d(Context context) {
        String str;
        synchronized (c.class) {
            if (Y != null) {
                str = Y;
            } else {
                if (context != null) {
                    if (Y == null) {
                        Y = l.z(context);
                    }
                }
                if (Y == null || Y.trim().length() == 0) {
                    N.error("AppKey can not be null or empty, please read Developer's Guide " +
                            "first!");
                }
                str = Y;
            }
        }
        return str;
    }

    public static synchronized String e(Context context) {
        String str;
        synchronized (c.class) {
            if (Z != null) {
                str = Z;
            } else {
                str = q.b(context, aa, "");
                Z = str;
                if (str == null || Z.trim().length() == 0) {
                    Z = l.A(context);
                }
                if (Z == null || Z.trim().length() == 0) {
                    N.c("installChannel can not be null or empty, please read Developer's Guide " +
                            "first!");
                }
                str = Z;
            }
        }
        return str;
    }

    public static String f(Context context) {
        return q.b(context, "mta.acc.qq", ab);
    }

    public static String g(Context context) {
        if (context == null) {
            N.error("Context for getCustomUid is null.");
            return null;
        }
        if (au == null) {
            au = q.b(context, "MTA_CUSTOM_UID", "");
        }
        return au;
    }

    public static String h(Context context) {
        return context != null ? g.a(context).f().c() : "0";
    }

    public static d j() {
        return Q;
    }

    public static boolean k() {
        return R;
    }

    static String l(String str) {
        try {
            String string = P.df.getString(str);
            if (string != null) {
                return string;
            }
        } catch (Throwable th) {
            N.c("can't find custom key:" + str);
        }
        return null;
    }

    public static boolean l() {
        return S;
    }

    public static int m() {
        return T;
    }

    private static boolean m(String str) {
        if (str == null) {
            return false;
        }
        if (Y == null) {
            Y = str;
            return true;
        } else if (Y.contains(str)) {
            return false;
        } else {
            Y += "|" + str;
            return true;
        }
    }

    public static int n() {
        return x;
    }

    public static void n(String str) {
        if (str.length() > 128) {
            N.error("the length of installChannel can not exceed the range of 128 bytes.");
        } else {
            Z = str;
        }
    }

    public static int o() {
        return y;
    }

    public static void o(String str) {
        if (str == null || str.length() == 0) {
            N.error("statReportUrl cannot be null or empty.");
            return;
        }
        am = str;
        try {
            al = new URI(am).getHost();
        } catch (Exception e) {
            N.c(e);
        }
        if (R) {
            N.b("url:" + am + ", domain:" + al);
        }
    }

    public static int p() {
        return w;
    }

    public static int q() {
        return z;
    }

    static int r() {
        return V;
    }

    public static int s() {
        return U;
    }

    public static void t() {
        ac = 60;
    }

    public static int u() {
        return ac;
    }

    public static int v() {
        return ag;
    }

    public static void w() {
        ah = true;
    }

    public static boolean x() {
        return ak;
    }

    public static String y() {
        return am;
    }

    static synchronized void z() {
        synchronized (c.class) {
            ao = 0;
        }
    }
}
