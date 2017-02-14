package com.zxinsight.common.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.text.TextUtils;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.zxinsight.MWConfiguration;
import com.zxinsight.analytics.domain.UserProfile;
import com.zxinsight.analytics.domain.response.SendStrategy;
import com.zxinsight.analytics.domain.response.SharePlatform;
import com.zxinsight.analytics.domain.trackEvent.EventsProxy;

public class m {
    private static volatile m D;
    private static int     E = 0;
    private final  String  A = "share_sina";
    private final  String  B = "mw_channel";
    private final  String  C = "marketing_time";
    private        boolean F = false;
    private final  String  G = "page_with_fragment";
    private        String  H = "SPHelper";
    private final  String  a = "mw_mlink_yyb";
    private final  String  b = "persistent_data";
    private final  String  c = "mw_mLink_last_time";
    private final  String  d = "mw_mLink_current_time";
    private final  String  e = "mw_crash";
    private final  String  f = "mw_screen_orientation";
    private final  String  g = "mw_custom_web_title_bar";
    private final  String  h = "mw_web_broadcast";
    private final  String  i = "mw_city_code";
    private final  String  j = "mw_mLink_response";
    private final  String  k = "mw_mLink_appId";
    private final  String  l = "mw_mlink_ak";
    private final  String  m = "mw_mlink_k";
    private final  String  n = "mw_mlink_time";
    private final  String  o = "mw_mLink_tags";
    private final  String  p = "mw_mLink_p";
    private final  String  q = "mw_mlink_channel";
    private final  String  r = "policy";
    private final  String  s = "send_delay";
    private final  String  t = "send_batch";
    private final  String  u = "last_report_time";
    private final  String  v = "last_report_db_time";
    private final  String  w = "share_sdk";
    private final  String  x = "share_type";
    private final  String  y = "share_wx";
    private final  String  z = "share_qq";

    private m() {
        int i = 0;
        if (VERSION.SDK_INT > 11) {
            i = 4;
        }
        E = i;
    }

    public static m a() {
        if (D == null) {
            synchronized (m.class) {
                if (D == null) {
                    D = new m();
                }
            }
        }
        return D;
    }

    private SharedPreferences N() {
        if (MWConfiguration.getContext() == null) {
            return null;
        }
        return MWConfiguration.getContext().getSharedPreferences("persistent_data", E);
    }

    public void a(String str, boolean z) {
        if (N() != null) {
            Editor edit = N().edit();
            edit.putBoolean(str, z);
            edit.apply();
        }
    }

    public boolean b(String str, boolean z) {
        if (N() != null) {
            return N().getBoolean(str, z);
        }
        return z;
    }

    public boolean a(String str) {
        if (N() != null) {
            return N().getBoolean(str, false);
        }
        return false;
    }

    public void a(String str, int i) {
        if (N() != null) {
            Editor edit = N().edit();
            edit.putInt(str, i);
            edit.apply();
        }
    }

    public int b(String str, int i) {
        if (N() != null) {
            return N().getInt(str, i);
        }
        return i;
    }

    public void a(String str, String str2) {
        if (N() != null) {
            Editor edit = N().edit();
            edit.putString(str, str2);
            edit.apply();
        }
    }

    public void b(String str) {
        if (N() != null) {
            Editor edit = N().edit();
            edit.remove(str);
            edit.apply();
        }
    }

    public String b(String str, String str2) {
        if (N() != null) {
            return N().getString(str, str2);
        }
        return str2;
    }

    public String c(String str) {
        String str2 = "";
        if (N() != null) {
            return N().getString(str, "");
        }
        return str2;
    }

    public void c(String str, String str2) {
        if (N() != null) {
            Editor edit = N().edit();
            edit.putString(str, str2);
            edit.apply();
        }
    }

    public String d(String str, String str2) {
        if (N() != null) {
            return N().getString(str, str2);
        }
        return str2;
    }

    public String d(String str) {
        String str2 = "";
        if (N() != null) {
            return N().getString(str, "");
        }
        return str2;
    }

    public void a(String str, Long l) {
        if (N() != null) {
            Editor edit = N().edit();
            edit.putLong(str, l.longValue());
            edit.apply();
        }
    }

    public Long e(String str) {
        Long valueOf = Long.valueOf(0);
        if (N() != null) {
            return Long.valueOf(N().getLong(str, 0));
        }
        return valueOf;
    }

    public Long b(String str, Long l) {
        if (N() != null) {
            return Long.valueOf(N().getLong(str, l.longValue()));
        }
        return l;
    }

    public boolean b() {
        boolean b = b("sp_first_launch", true);
        c();
        return b;
    }

    public void c() {
        a("sp_first_launch", false);
    }

    public String d() {
        return d("sp_user_md5");
    }

    public String e() {
        return d("sp_profile");
    }

    public void a(UserProfile userProfile) {
        if (userProfile != null) {
            String a = n.a(o.c() + userProfile.profileId + DeviceInfoUtils.c(MWConfiguration
                    .getContext()));
            c("sp_user_md5", a);
            EventsProxy.create().addUserMd5(a);
            return;
        }
        b("sp_user_md5");
        EventsProxy.create().addUserMd5(null);
    }

    public void b(UserProfile userProfile) {
        if (userProfile != null) {
            c("sp_profile", userProfile.toString());
            c("sp_user_id", userProfile.profileId);
            c("sp_user_phone", userProfile.phone);
            String a = n.a(o.c() + userProfile.profileId + DeviceInfoUtils.c(MWConfiguration
                    .getContext()));
            c("sp_user_md5", a);
            EventsProxy.create().addUserMd5(a);
            return;
        }
        b("sp_profile");
        b("sp_user_id");
        b("sp_user_phone");
        b("sp_user_md5");
        EventsProxy.create().addUserMd5(null);
    }

    public void f(String str) {
        c("sp_session_id", str);
        EventsProxy.create().addSession(str);
    }

    public String f() {
        return d("sp_session_id");
    }

    public void g() {
        a("sp_session_time", Long.valueOf(System.currentTimeMillis()));
    }

    public long h() {
        return e("sp_session_time").longValue();
    }

    public void a(SendStrategy sendStrategy) {
        if (sendStrategy != null) {
            a("send_delay", sendStrategy.p);
            a("send_batch", sendStrategy.b);
        }
    }

    public long i() {
        return b("marketing_time", Long.valueOf(0)).longValue();
    }

    public void a(long j) {
        a("marketing_time", Long.valueOf(j));
    }

    public void a(SharePlatform sharePlatform) {
        if (sharePlatform != null) {
            int i = 0;
            if (l.b(sharePlatform.w)) {
                g(sharePlatform.w);
                i = 1;
            }
            if (l.b(sharePlatform.q)) {
                h(sharePlatform.q);
                i += 2;
            }
            if (l.b(sharePlatform.s)) {
                i(sharePlatform.s);
                i += 4;
            }
            c(i);
        }
    }

    public boolean a(int i) {
        return (j() & i) == i;
    }

    public int j() {
        return b("policy", 7);
    }

    public int k() {
        return b("send_batch", 30);
    }

    public int l() {
        return b("send_delay", 60) * 1000;
    }

    public long m() {
        return b("last_report_time", Long.valueOf(0)).longValue();
    }

    public void b(long j) {
        a("last_report_time", Long.valueOf(j));
    }

    public long n() {
        return b("last_report_db_time", Long.valueOf(0)).longValue();
    }

    public void c(long j) {
        a("last_report_db_time", Long.valueOf(j));
    }

    public boolean o() {
        return b("share_sdk", false);
    }

    public void a(boolean z) {
        a("share_sdk", z);
    }

    public boolean p() {
        return q() != 0;
    }

    public boolean b(int i) {
        return i != 0 && (q() & i) == i;
    }

    public int q() {
        return b("share_type", 0);
    }

    private void c(int i) {
        if (i < 0 || i > 15) {
            i = 15;
        }
        a("share_type", i);
    }

    public String r() {
        return d("share_wx", "wx1ab7f8ca7d39a40c");
    }

    public void g(String str) {
        c("share_wx", str);
    }

    public void h(String str) {
        c("share_qq", str);
    }

    public void i(String str) {
        c("share_sina", str);
    }

    public String s() {
        String a = o.a("mw_channel");
        if (TextUtils.isEmpty(a)) {
            return c("mw_channel");
        }
        return a;
    }

    public void j(String str) {
        if (!l.a(str)) {
            a("mw_channel", str);
        }
    }

    public boolean t() {
        return a("page_with_fragment");
    }

    public void b(boolean z) {
        a("page_with_fragment", z);
    }

    public boolean u() {
        return b("mw_crash", true);
    }

    public void c(boolean z) {
        a("mw_crash", z);
    }

    public boolean v() {
        return b("mw_screen_orientation", false);
    }

    public void d(boolean z) {
        a("mw_screen_orientation", z);
    }

    public Boolean w() {
        return Boolean.valueOf(this.F);
    }

    public void a(Boolean bool) {
        this.F = bool.booleanValue();
    }

    public boolean x() {
        return b("mw_custom_web_title_bar", false);
    }

    public void e(boolean z) {
        a("mw_custom_web_title_bar", z);
    }

    public boolean y() {
        return b("mw_web_broadcast", false);
    }

    public void f(boolean z) {
        a("mw_web_broadcast", z);
    }

    public String z() {
        return d("mw_city_code");
    }

    public void k(String str) {
        c("mw_city_code", str);
    }

    public String A() {
        return d("mw_mLink_response");
    }

    public void l(String str) {
        c("mw_mLink_response", str);
    }

    public long B() {
        return b("mw_mLink_last_time", Long.valueOf(0)).longValue();
    }

    public void d(long j) {
        a("mw_mLink_last_time", Long.valueOf(j));
    }

    public boolean C() {
        return b("mw_mlink_yyb", false);
    }

    public void g(boolean z) {
        a("mw_mlink_yyb", z);
    }

    public long D() {
        return b("mw_mLink_current_time", Long.valueOf(-1)).longValue();
    }

    public void e(long j) {
        a("mw_mLink_current_time", Long.valueOf(j));
    }

    public boolean E() {
        return System.currentTimeMillis() - h() > DeviceInfoConstant.REQUEST_LOCATE_INTERVAL;
    }

    public String F() {
        if (!f(e("mw_mlink_time").longValue())) {
            return String.valueOf(e("mw_mlink_time"));
        }
        b("mw_mlink_time");
        return "";
    }

    public String G() {
        if (!f(e("mw_mlink_time").longValue())) {
            return d("mw_mLink_appId");
        }
        b("mw_mLink_appId");
        return "";
    }

    public void m(String str) {
        if (l.b(str)) {
            c("mw_mLink_appId", str);
            a("mw_mlink_time", Long.valueOf(o.a()));
        }
    }

    public String H() {
        if (!f(e("mw_mlink_time").longValue())) {
            return d("mw_mlink_k");
        }
        b("mw_mlink_k");
        return "";
    }

    public void n(String str) {
        if (l.b(str)) {
            c("mw_mlink_k", str);
            a("mw_mlink_time", Long.valueOf(o.a()));
        }
    }

    public String I() {
        if (!f(e("mw_mlink_time").longValue())) {
            return d("mw_mlink_ak");
        }
        b("mw_mlink_ak");
        return "";
    }

    public void o(String str) {
        if (l.b(str)) {
            c("mw_mlink_ak", str);
            a("mw_mlink_time", Long.valueOf(o.a()));
        }
    }

    public String J() {
        if (!f(e("mw_mlink_time").longValue())) {
            return d("mw_mLink_tags");
        }
        b("mw_mLink_tags");
        return "";
    }

    public void p(String str) {
        if (l.b(str)) {
            c("mw_mLink_tags", str);
            a("mw_mlink_time", Long.valueOf(o.a()));
        }
    }

    public String K() {
        if (!f(e("mw_mlink_time").longValue())) {
            return d("mw_mlink_channel");
        }
        b("mw_mlink_channel");
        return "";
    }

    public void q(String str) {
        c("mw_mlink_channel", str);
        a("mw_mlink_time", Long.valueOf(o.a()));
    }

    public String L() {
        if (!f(e("mw_mlink_time").longValue())) {
            return d("mw_mLink_p");
        }
        b("mw_mLink_p");
        return "";
    }

    public void a(String str, String str2, String str3, String str4) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(str)) {
            stringBuilder.append(str);
        }
        stringBuilder.append(",");
        if (!TextUtils.isEmpty(str2)) {
            stringBuilder.append(str2);
        }
        stringBuilder.append(",");
        if (!TextUtils.isEmpty(str3)) {
            stringBuilder.append(str3);
        }
        stringBuilder.append(",");
        if (!TextUtils.isEmpty(str4)) {
            stringBuilder.append(str4);
        }
        stringBuilder.append(",");
        c("mw_mLink_p", stringBuilder.toString());
        a("mw_mlink_time", Long.valueOf(o.a()));
    }

    private boolean f(long j) {
        return o.a() - j > 2592000;
    }

    public void h(boolean z) {
        a("auto_session", z);
    }

    public boolean M() {
        return VERSION.SDK_INT >= 14 && a("auto_session");
    }
}
