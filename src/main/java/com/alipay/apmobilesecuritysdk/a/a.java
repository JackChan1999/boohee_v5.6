package com.alipay.apmobilesecuritysdk.a;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import com.alipay.apmobilesecuritysdk.e.b;
import com.alipay.apmobilesecuritysdk.e.e;
import com.alipay.apmobilesecuritysdk.e.f;
import com.alipay.security.mobile.module.a.b.c;
import com.alipay.security.mobile.module.a.d;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.commonutils.Dbg;
import com.alipay.security.mobile.module.commonutils.LOG;
import com.alipay.security.mobile.module.commonutils.crypto.SecurityUtils;
import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public final class a {
    private static boolean b = false;
    private final Context a;

    public a(Context context) {
        this.a = context;
    }

    public static String a(Context context) {
        String str = "";
        try {
            str = f.b();
            if (CommonUtils.isBlank(str)) {
                b a = com.alipay.apmobilesecuritysdk.e.a.a(context);
                if (!(a == null || CommonUtils.isBlank(a.c()))) {
                    str = a.c();
                    f.a(a);
                }
            }
        } catch (Throwable th) {
        }
        return str;
    }

    static /* synthetic */ void a(a aVar, String str, String str2, String str3) {
        try {
            com.alipay.security.mobile.module.a.b.a a = d.a(aVar.a).a(DeviceInfoConstant.OS_ANDROID, str, str2, str3);
            if (a.c) {
                e.a(aVar.a, a.b, a.a);
                a(false);
            }
        } catch (Throwable th) {
            LOG.logException(th);
        } finally {
            a(false);
        }
    }

    private static synchronized void a(boolean z) {
        synchronized (a.class) {
            b = z;
        }
    }

    private static synchronized boolean a() {
        boolean z;
        synchronized (a.class) {
            z = b;
        }
        return z;
    }

    private c b(Map<String, String> map) {
        c cVar = null;
        try {
            Context context = this.a;
            com.alipay.security.mobile.module.a.b.d dVar = new com.alipay.security.mobile.module.a.b.d();
            String str = "3";
            String str2 = "";
            String str3 = "";
            String str4 = "";
            String str5 = "";
            String valueFromMap = CommonUtils.getValueFromMap(map, "umid", "");
            b b = com.alipay.apmobilesecuritysdk.e.a.b(context);
            if (b != null) {
                str2 = b.c();
                str3 = b.a();
                str5 = str3;
                str3 = b.d();
            } else {
                String str6 = str5;
                str5 = str3;
                str3 = str6;
            }
            b = com.alipay.apmobilesecuritysdk.e.a.a();
            if (b != null) {
                str4 = b.a();
            }
            if (CommonUtils.isBlank(str5) && CommonUtils.isBlank(r1)) {
                str5 = com.alipay.apmobilesecuritysdk.a.a.a.b(context);
                str4 = com.alipay.apmobilesecuritysdk.a.a.a.a();
            }
            dVar.a(DeviceInfoConstant.OS_ANDROID);
            dVar.c(str5);
            dVar.b(str4);
            dVar.d(str2);
            dVar.e(valueFromMap);
            dVar.g(str3);
            dVar.f(str);
            dVar.a(com.alipay.apmobilesecuritysdk.c.e.a(context, map));
            cVar = d.a(this.a).a(dVar);
        } catch (Throwable th) {
            LOG.logException(th);
        }
        return cVar;
    }

    public static String b(Context context) {
        try {
            String a = f.a();
            if (!CommonUtils.isBlank(a)) {
                return a;
            }
            b a2 = com.alipay.apmobilesecuritysdk.e.a.a(context);
            if (a2 == null || CommonUtils.isBlank(a2.a())) {
                a = com.alipay.apmobilesecuritysdk.a.a.a.a(context);
                if (!CommonUtils.isBlank(a)) {
                    return a;
                }
                a = com.alipay.apmobilesecuritysdk.f.b.a(context);
                return !CommonUtils.isBlank(a) ? a : a;
            } else {
                f.a(a2);
                return a2.a();
            }
        } catch (Throwable th) {
            return "";
        }
    }

    public final String a(Map<String, String> map) {
        String str = "";
        if (map == null) {
            return "";
        }
        String b;
        String str2 = this.a.getFilesDir().getAbsolutePath() + "/log/ap";
        String str3 = Build.MODEL;
        String str4 = this.a.getApplicationContext().getApplicationInfo().packageName;
        String str5 = "security-sdk-token";
        String str6 = "3.0.2.20151027";
        String valueFromMap = CommonUtils.getValueFromMap(map, com.alipay.sdk.cons.b.c, "");
        String valueFromMap2 = CommonUtils.getValueFromMap(map, com.alipay.sdk.cons.b.g, "");
        String str7 = "";
        b a = com.alipay.apmobilesecuritysdk.e.a.a(this.a);
        if (a != null) {
            str7 = a.a();
        }
        LOG.init(str2, new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".log", new com.alipay.apmobilesecuritysdk.b.a(str3, str4, str5, str6, valueFromMap, valueFromMap2, str7).toString());
        String valueFromMap3 = CommonUtils.getValueFromMap(map, "userId", "");
        Date date = new Date();
        Object obj = (date.after(new Date(115, 10, 10, 0, 0, 0)) && date.before(new Date(115, 10, 11, 23, 59, 59))) ? 1 : null;
        if (obj != null) {
            obj = 1;
        } else {
            Date date2 = new Date(115, 11, 11, 0, 0, 0);
            Date date3 = new Date(115, 11, 12, 23, 59, 59);
            if (date.after(date2) && date.before(date3)) {
                int i = 1;
            } else {
                obj = null;
            }
        }
        if (obj != null) {
            a = com.alipay.apmobilesecuritysdk.e.a.a(this.a);
            if (a == null) {
                obj = 1;
            } else if (CommonUtils.isBlank(a.c())) {
                i = 1;
            } else {
                obj = null;
            }
        } else {
            com.alipay.apmobilesecuritysdk.c.e.a();
            b = com.alipay.apmobilesecuritysdk.c.e.b(this.a, map);
            Dbg.log("[*]currentDeviceInfoHash = " + b);
            b a2 = com.alipay.apmobilesecuritysdk.e.a.a(this.a);
            if (a2 == null) {
                Dbg.log("[*] LocalData = null");
                i = 1;
            } else {
                str4 = a2.b();
                Dbg.log("[*]storedDeviceInfoHash = " + str4);
                if ((!CommonUtils.equals(b, str4) ? 1 : null) != null) {
                    Dbg.log("[*]DeviceInfo Changed.");
                    i = 1;
                } else if (!f.a(this.a)) {
                    Dbg.log("[*]Token is out of date.");
                    i = 1;
                } else if (CommonUtils.isBlank(a2.c())) {
                    Dbg.log("[*]Token is empty.");
                    i = 1;
                } else {
                    obj = null;
                }
            }
        }
        if (obj != null) {
            com.alipay.security.mobile.module.a.b.b b2 = b((Map) map);
            if (b2 != null) {
                if (!CommonUtils.isBlank(b2.a) ? b2.c : false) {
                    Context context = this.a;
                    boolean equals = "1".equals(b2.h);
                    try {
                        Editor edit = context.getSharedPreferences("vkeyid_settings", 0).edit();
                        if (edit != null) {
                            edit.putString("log_switch", equals ? SecurityUtils.encrypt(SecurityUtils.getSeed(), "1") : SecurityUtils.encrypt(SecurityUtils.getSeed(), "0"));
                            edit.commit();
                        }
                    } catch (Throwable th) {
                    }
                    try {
                        b = e.c(this.a);
                        if (!(CommonUtils.isBlank(b2.i) || CommonUtils.equals(b, b2.i))) {
                            b = b2.b;
                            str4 = "";
                            if (!a()) {
                                a(true);
                                try {
                                    new b(this, valueFromMap3, b, str4).start();
                                } catch (Throwable th2) {
                                }
                            }
                        }
                        b = com.alipay.apmobilesecuritysdk.c.e.b(this.a, map);
                        if (b2 != null) {
                            b bVar = new b(b2, b);
                            com.alipay.apmobilesecuritysdk.e.a.a(this.a, bVar);
                            f.a(bVar);
                            context = this.a;
                            com.alipay.apmobilesecuritysdk.a.a.a.a(bVar);
                            context = this.a;
                            long currentTimeMillis = System.currentTimeMillis();
                            try {
                                Editor edit2 = context.getSharedPreferences("vkeyid_settings", 0).edit();
                                if (edit2 != null) {
                                    edit2.putString("vkey_valid", SecurityUtils.encrypt(SecurityUtils.getSeed(), String.valueOf(currentTimeMillis)));
                                    edit2.commit();
                                }
                            } catch (Throwable th3) {
                            }
                        }
                        b = b2.b;
                        new com.alipay.apmobilesecuritysdk.b.b(str2, d.a(this.a)).a(this.a);
                        return b;
                    } catch (Throwable th4) {
                        Throwable th5 = th4;
                        b = str;
                        LOG.logException(th5);
                        return b;
                    }
                }
            }
            if (b2 != null) {
                LOG.logMessage("Server error, result:" + b2.d);
            } else {
                LOG.logMessage("Server error, response is null");
            }
        }
        b = a(this.a);
        try {
            new com.alipay.apmobilesecuritysdk.b.b(str2, d.a(this.a)).a(this.a);
            return b;
        } catch (Throwable th6) {
            th5 = th6;
            LOG.logException(th5);
            return b;
        }
    }
}
