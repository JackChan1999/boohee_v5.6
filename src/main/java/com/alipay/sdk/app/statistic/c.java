package com.alipay.sdk.app.statistic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.alipay.sdk.cons.a;
import com.alipay.sdk.tid.b;
import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.umeng.socialize.common.SocializeConstants;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class c {
    public static final String a = "net";
    public static final String b = "biz";
    public static final String c = "cp";
    public static final String d = "H5PayNetworkError";
    public static final String e = "H5AuthNetworkError";
    public static final String f = "SSLPayError";
    public static final String g = "SSLAUTHError";
    public static final String h = "H5PayDataAnalysisError";
    public static final String i = "H5AuthDataAnalysisError";
    public static final String j = "ClientSignError";
    public static final String k = "ClientBindFailed";
    public static final String l = "TriDesEncryptError";
    public static final String m = "TriDesDecryptError";
    public static final String n = "ClientBindException";
    public static final String o = "partner";
    public static final String p = "out_trade_no";
    public static final String q = "trade_no";
    String A;
    String r;
    String s;
    String t;
    String u;
    String v;
    String w;
    String x;
    String y;
    String z = "";

    public c(Context context) {
        String format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
        this.r = String.format("123456789,%s", new Object[]{format});
        this.t = a(context);
        format = a("15.0.4");
        String a = a(a.f);
        this.u = String.format("android,3,%s,%s,com.alipay.mcpay,5.0,-,-,-", new Object[]{format, a});
        format = a(b.a().a);
        a = a(com.alipay.sdk.sys.b.a().c());
        this.v = String.format("%s,%s,-,-,-", new Object[]{format, a});
        format = a(com.alipay.sdk.util.a.d(context));
        a = DeviceInfoConstant.OS_ANDROID;
        String a2 = a(VERSION.RELEASE);
        String a3 = a(Build.MODEL);
        String str = SocializeConstants.OP_DIVIDER_MINUS;
        String a4 = a(com.alipay.sdk.util.a.a(context).a());
        String a5 = a(com.alipay.sdk.util.a.b(context).a());
        String a6 = a(com.alipay.sdk.util.a.a(context).b());
        this.w = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,-", new Object[]{format, a, a2, a3, str, a4, a5, "gw", a6});
        this.x = SocializeConstants.OP_DIVIDER_MINUS;
        this.y = SocializeConstants.OP_DIVIDER_MINUS;
        this.A = SocializeConstants.OP_DIVIDER_MINUS;
    }

    private boolean a() {
        return TextUtils.isEmpty(this.z);
    }

    public final void a(String str, String str2, Throwable th) {
        a(str, str2, a(th));
    }

    public final void a(String str, String str2, String str3) {
        String str4 = "";
        if (!TextUtils.isEmpty(this.z)) {
            str4 = str4 + "^";
        }
        this.z += (str4 + String.format("%s,%s,%s,-", new Object[]{str, str2, a(str3)}));
    }

    static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.replace("[", "【").replace("]", "】").replace(SocializeConstants.OP_OPEN_PAREN, "（").replace(SocializeConstants.OP_CLOSE_PAREN, "）").replace(",", "，").replace(SocializeConstants.OP_DIVIDER_MINUS, "=").replace("^", "~");
    }

    private static String a(Throwable th) {
        if (th == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append(th.getClass().getName()).append(":");
            stringBuffer.append(th.getMessage());
            stringBuffer.append(" 》 ");
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null) {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    stringBuffer.append(stackTraceElement.toString() + " 》 ");
                }
            }
        } catch (Throwable th2) {
        }
        return stringBuffer.toString();
    }

    private String b(String str) {
        String str2 = null;
        if (TextUtils.isEmpty(this.z)) {
            return "";
        }
        String str3;
        String[] split = str.split(com.alipay.sdk.sys.a.b);
        if (split != null) {
            str3 = null;
            for (String split2 : split) {
                String[] split3 = split2.split("=");
                if (split3 != null && split3.length == 2) {
                    if (split3[0].equalsIgnoreCase(o)) {
                        split3[1].replace(com.alipay.sdk.sys.a.e, "");
                    } else if (split3[0].equalsIgnoreCase(p)) {
                        str3 = split3[1].replace(com.alipay.sdk.sys.a.e, "");
                    } else if (split3[0].equalsIgnoreCase(q)) {
                        str2 = split3[1].replace(com.alipay.sdk.sys.a.e, "");
                    }
                }
            }
        } else {
            str3 = null;
        }
        str2 = a(str2);
        String a = a(a(str3));
        this.s = String.format("%s,%s,-,%s,-,-,-", new Object[]{str2, str3, a});
        return String.format("[(%s),(%s),(%s),(%s),(%s),(%s),(%s),(%s),(%s),(%s)]", new Object[]{this.r, this.s, this.t, this.u, this.v, this.w, this.x, this.y, this.z, this.A});
    }

    @SuppressLint({"SimpleDateFormat"})
    private static String b() {
        String format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
        return String.format("123456789,%s", new Object[]{format});
    }

    private static String c(String str) {
        String str2;
        String str3 = null;
        String[] split = str.split(com.alipay.sdk.sys.a.b);
        if (split != null) {
            str2 = null;
            for (String split2 : split) {
                String[] split3 = split2.split("=");
                if (split3 != null && split3.length == 2) {
                    if (split3[0].equalsIgnoreCase(o)) {
                        split3[1].replace(com.alipay.sdk.sys.a.e, "");
                    } else if (split3[0].equalsIgnoreCase(p)) {
                        str2 = split3[1].replace(com.alipay.sdk.sys.a.e, "");
                    } else if (split3[0].equalsIgnoreCase(q)) {
                        str3 = split3[1].replace(com.alipay.sdk.sys.a.e, "");
                    }
                }
            }
        } else {
            str2 = null;
        }
        str3 = a(str3);
        String a = a(a(str2));
        return String.format("%s,%s,-,%s,-,-,-", new Object[]{str3, a(str2), a});
    }

    private static String a(Context context) {
        String str = SocializeConstants.OP_DIVIDER_MINUS;
        String str2 = SocializeConstants.OP_DIVIDER_MINUS;
        if (context != null) {
            try {
                Context applicationContext = context.getApplicationContext();
                str = applicationContext.getPackageName();
                str2 = applicationContext.getPackageManager().getPackageInfo(str, 0).versionName;
            } catch (Throwable th) {
            }
        }
        return String.format("%s,%s,-,-,-", new Object[]{str, str2});
    }

    private static String c() {
        String a = a("15.0.4");
        String a2 = a(a.f);
        return String.format("android,3,%s,%s,com.alipay.mcpay,5.0,-,-,-", new Object[]{a, a2});
    }

    private static String d() {
        String a = a(b.a().a);
        String a2 = a(com.alipay.sdk.sys.b.a().c());
        return String.format("%s,%s,-,-,-", new Object[]{a, a2});
    }

    private static String b(Context context) {
        String a = a(com.alipay.sdk.util.a.d(context));
        String str = DeviceInfoConstant.OS_ANDROID;
        String a2 = a(VERSION.RELEASE);
        String a3 = a(Build.MODEL);
        String str2 = SocializeConstants.OP_DIVIDER_MINUS;
        String a4 = a(com.alipay.sdk.util.a.a(context).a());
        String a5 = a(com.alipay.sdk.util.a.b(context).a());
        String a6 = a(com.alipay.sdk.util.a.a(context).b());
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,-", new Object[]{a, str, a2, a3, str2, a4, a5, "gw", a6});
    }
}
