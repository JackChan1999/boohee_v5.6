package com.zxinsight;

import android.content.res.ColorStateList;
import android.graphics.Color;

import com.zxinsight.analytics.domain.response.Style;
import com.zxinsight.common.util.m;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomStyle {
    static int    a = -16777216;
    static int    b = -16777216;
    static int    c = -16777216;
    static int    d = -7829368;
    static int    e = -7829368;
    static int    f = -1;
    static int    g = -1;
    static int    h = -1;
    static int    i = -1;
    static int    j = -1;
    static int    k = 0;
    static String l = "WebViewTopBar";
    static String m = "webview_bottom";
    static String n = "WebViewLeftMenuNormal";
    static String o = "WebViewLeftMenuPressed";
    static String p = "WebViewRightMenuNormal";
    static String q = "WebViewRightMenuPressed";
    static String r = "SocialPopUpBg";
    static String s = "SocialPopUpText";
    static String t = "SocialShareKit";

    private static String filterColor(String str) {
        Matcher matcher = Pattern.compile("([0-9A-Fa-f]{8})|([0-9A-Fa-f]{6})").matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private static int parseColor(String str) {
        String filterColor = filterColor(str);
        if (!"#".equalsIgnoreCase(String.valueOf(filterColor.charAt(0)))) {
            filterColor = "#" + filterColor;
        }
        return Color.parseColor(filterColor);
    }

    private static boolean isColor(String str) {
        if (Pattern.compile("([0-9A-Fa-f]{8})|([0-9A-Fa-f]{6})").matcher(str).find()) {
            return true;
        }
        return false;
    }

    private static boolean isZeroOne(String str) {
        if (Pattern.compile("(0|1)").matcher(str).find()) {
            return true;
        }
        return false;
    }

    public static int getWebViewBottom(String str) {
        String str2 = "#FF000000";
        if (isColor(str2)) {
            return parseColor(str2);
        }
        return g;
    }

    public static int getWebViewBottomTextNormal(String str) {
        String str2 = "#FFFF00FF";
        if (isColor(str2)) {
            return parseColor(str2);
        }
        return h;
    }

    public static int getWebViewBottomTextPressed(String str) {
        String str2 = "#FFFF00FF";
        if (isColor(str2)) {
            return parseColor(str2);
        }
        return i;
    }

    public static int getWebViewTopBar(String str) {
        String trim = m.a().c(l + str).replaceAll("\r", "").replaceAll("\n", "").trim();
        if (isColor(trim)) {
            return parseColor(trim);
        }
        return f;
    }

    public static int getWebViewLeftMenuNormal(String str) {
        String trim = m.a().c(n + str).replaceAll("\r", "").replaceAll("\n", "").trim();
        if (isColor(trim)) {
            return parseColor(trim);
        }
        return a;
    }

    public static int getWebViewLeftMenuPressed(String str) {
        String trim = m.a().c(o + str).replaceAll("\r", "").replaceAll("\n", "").trim();
        if (isColor(trim)) {
            return parseColor(trim);
        }
        return d;
    }

    public static int getWebViewRightMenuNormal(String str) {
        String trim = m.a().c(p + str).replaceAll("\r", "").replaceAll("\n", "").trim();
        if (isColor(trim)) {
            return parseColor(trim);
        }
        return b;
    }

    public static int getWebViewRightMenuPressed(String str) {
        String trim = m.a().c(q + str).replaceAll("\r", "").replaceAll("\n", "").trim();
        if (isColor(trim)) {
            return parseColor(trim);
        }
        return e;
    }

    public static int getSocialPopUpBg(String str) {
        String trim = m.a().c(r + str).replaceAll("\r", "").replaceAll("\n", "").trim();
        if (isColor(trim)) {
            return parseColor(trim);
        }
        return j;
    }

    public static int getSocialShareKit(String str) {
        String trim = m.a().c(t + str).replaceAll("\r", "").replaceAll("\n", "").trim();
        if (isZeroOne(trim)) {
            return Integer.parseInt(trim);
        }
        return k;
    }

    static void setStyle(String str, Style style) {
        if (style != null) {
            setWebViewLeftMenuNormal(str, style.fc);
            setWebViewLeftMenuPressed(str, style.fcp);
            setWebViewRightMenuNormal(str, style.fc);
            setWebViewRightMenuPressed(str, style.fcp);
            setWebViewTopBar(str, style.nv);
            setSocialPopUpBg(str, style.bg);
            setSocialShareKit(str, style.sh);
        }
    }

    private static void setWebViewTopBar(String str, String str2) {
        try {
            m.a().a(l + str, String.valueOf(str2));
        } catch (Exception e) {
            m.a().a(l + str, "");
        }
    }

    private static void setWebViewLeftMenuNormal(String str, String str2) {
        try {
            m.a().a(n + str, String.valueOf(str2));
        } catch (Exception e) {
            m.a().a(n + str, "");
        }
    }

    private static void setWebViewLeftMenuPressed(String str, String str2) {
        try {
            m.a().a(o + str, str2);
        } catch (Exception e) {
            m.a().a(o + str, "");
        }
    }

    private static void setWebViewRightMenuNormal(String str, String str2) {
        try {
            m.a().a(p + str, str2);
        } catch (Exception e) {
            m.a().a(p + str, "");
        }
    }

    private static void setWebViewRightMenuPressed(String str, String str2) {
        try {
            m.a().a(q + str, str2);
        } catch (Exception e) {
            m.a().a(q + str, "");
        }
    }

    private static void setSocialPopUpBg(String str, String str2) {
        try {
            m.a().a(r + str, str2);
        } catch (Exception e) {
            m.a().a(r + str, "");
        }
    }

    private static void setSocialShareKit(String str, String str2) {
        try {
            m.a().a(t + str, str2);
        } catch (Exception e) {
            m.a().a(t + str, "");
        }
    }

    public static ColorStateList textColor(int i, int i2) {
        return createColorStateList(i, i2, i2, i);
    }

    private static ColorStateList createColorStateList(int i, int i2, int i3, int i4) {
        int[] iArr = new int[]{i2, i3, i, i3, i4, i};
        int[][] iArr2 = new int[6][];
        iArr2[0] = new int[]{16842919, 16842910};
        iArr2[1] = new int[]{16842910, 16842908};
        iArr2[2] = new int[]{16842910};
        iArr2[3] = new int[]{16842908};
        iArr2[4] = new int[]{16842909};
        iArr2[5] = new int[0];
        return new ColorStateList(iArr2, iArr);
    }
}
