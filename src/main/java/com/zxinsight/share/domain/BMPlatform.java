package com.zxinsight.share.domain;

import com.tencent.connect.common.Constants;
import com.zxinsight.common.util.m;

import java.util.ArrayList;
import java.util.List;

public enum BMPlatform {
    PLATFORM_SINAWEIBO,
    PLATFORM_TENCENTWEIBO,
    PLATFORM_QZONE,
    PLATFORM_WXSESSION,
    PLATFORM_RENN,
    PLATFORM_QQ,
    PLATFORM_MESSAGE,
    PLATFORM_EMAIL,
    PLATFORM_WXTIMELINE,
    PLATFORM_MORE_SHARE,
    PLATFORM_COPYLINK;

    public static final String NAME_COPYLINK     = "CopyLink";
    public static final String NAME_EMAIL        = "Email";
    public static final String NAME_MESSAGE      = "ShortMessage";
    public static final String NAME_MORE_SHARE   = "WXTimeline";
    public static final String NAME_QQ           = "QQ";
    public static final String NAME_QZONE        = "QZone";
    public static final String NAME_RENN         = "Renren";
    public static final String NAME_SINAWEIBO    = "SinaWeibo";
    public static final String NAME_TENCENTWEIBO = "TencentWeibo";
    public static final String NAME_WXSESSION    = "WXSession";
    public static final String NAME_WXTIMELINE   = "More";

    public static List<BMPlatform> getOpenedShare() {
        List<BMPlatform> arrayList = new ArrayList();
        m a = m.a();
        if (a.b(1)) {
            arrayList.add(PLATFORM_WXSESSION);
            arrayList.add(PLATFORM_WXTIMELINE);
        }
        if (a.b(4)) {
            arrayList.add(PLATFORM_SINAWEIBO);
        }
        if (a.b(2)) {
            arrayList.add(PLATFORM_QQ);
            arrayList.add(PLATFORM_QZONE);
        }
        return arrayList;
    }

    public static String getIDByPlatform(BMPlatform bMPlatform) {
        switch (a.a[bMPlatform.ordinal()]) {
            case 1:
                return "0";
            case 2:
                return "1";
            case 3:
                return "2";
            case 4:
                return "3";
            case 5:
                return "4";
            case 6:
                return "5";
            case 7:
                return Constants.VIA_SHARE_TYPE_INFO;
            case 8:
                return "7";
            case 9:
                return "8";
            case 10:
                return "9";
            case 11:
                return Constants.VIA_REPORT_TYPE_SHARE_TO_QQ;
            default:
                return null;
        }
    }

    public static String getIDByPlatformName(String str) {
        if (NAME_WXSESSION.equals(str)) {
            return "0";
        }
        if (NAME_WXTIMELINE.equals(str)) {
            return "1";
        }
        if (NAME_SINAWEIBO.equals(str)) {
            return "2";
        }
        if ("QQ".equals(str)) {
            return "3";
        }
        if (NAME_QZONE.equals(str)) {
            return "4";
        }
        if (NAME_RENN.equals(str)) {
            return "5";
        }
        if (NAME_TENCENTWEIBO.equals(str)) {
            return Constants.VIA_SHARE_TYPE_INFO;
        }
        if (NAME_MESSAGE.equals(str)) {
            return "7";
        }
        if (NAME_EMAIL.equals(str)) {
            return "8";
        }
        if (NAME_COPYLINK.equals(str)) {
            return "9";
        }
        if (NAME_MORE_SHARE.equals(str)) {
            return Constants.VIA_REPORT_TYPE_SHARE_TO_QQ;
        }
        return "0";
    }

    public static String getPlatformName(BMPlatform bMPlatform) {
        switch (a.a[bMPlatform.ordinal()]) {
            case 1:
                return NAME_WXSESSION;
            case 2:
                return NAME_WXTIMELINE;
            case 3:
                return NAME_SINAWEIBO;
            case 4:
                return "QQ";
            case 5:
                return NAME_QZONE;
            case 6:
                return NAME_RENN;
            case 7:
                return NAME_TENCENTWEIBO;
            case 8:
                return NAME_MESSAGE;
            case 9:
                return NAME_EMAIL;
            case 10:
                return NAME_MORE_SHARE;
            case 11:
                return NAME_COPYLINK;
            default:
                return null;
        }
    }

    public static BMPlatform getBMPlatformByName(String str) {
        if (NAME_WXSESSION.equals(str)) {
            return PLATFORM_WXSESSION;
        }
        if (NAME_WXTIMELINE.equals(str)) {
            return PLATFORM_WXTIMELINE;
        }
        if ("QQ".equals(str)) {
            return PLATFORM_QQ;
        }
        if (NAME_QZONE.equals(str)) {
            return PLATFORM_QZONE;
        }
        if (NAME_TENCENTWEIBO.equals(str)) {
            return PLATFORM_TENCENTWEIBO;
        }
        if (NAME_SINAWEIBO.equals(str)) {
            return PLATFORM_SINAWEIBO;
        }
        if (NAME_RENN.equals(str)) {
            return PLATFORM_RENN;
        }
        if (NAME_MESSAGE.equals(str)) {
            return PLATFORM_MESSAGE;
        }
        if (NAME_EMAIL.equals(str)) {
            return PLATFORM_EMAIL;
        }
        if (NAME_COPYLINK.equals(str)) {
            return PLATFORM_COPYLINK;
        }
        if (NAME_MORE_SHARE.equals(str)) {
            return PLATFORM_MORE_SHARE;
        }
        return null;
    }
}
