package com.qiniu.android.http;

import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.qiniu.android.common.Constants;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.utils.StringUtils;
import com.umeng.socialize.common.SocializeConstants;

import java.util.Locale;
import java.util.Random;

public final class UserAgent {
    private static UserAgent _instance = new UserAgent();
    public final   String    id        = genId();
    public final   String    ua        = getUserAgent(this.id);

    private UserAgent() {
    }

    public static UserAgent instance() {
        return _instance;
    }

    private static String genId() {
        return System.currentTimeMillis() + "" + new Random().nextInt(NetworkInfo.ISP_OTHER);
    }

    private static String getUserAgent(String id) {
        return String.format("QiniuAndroid/%s (%s; %s; %s)", new Object[]{Constants.VERSION,
                osVersion(), device(), id});
    }

    private static String osVersion() {
        String v = VERSION.RELEASE;
        if (v == null) {
            return "";
        }
        return StringUtils.strip(v.trim());
    }

    private static String device() {
        String model = Build.MODEL.trim();
        String device = deviceName(Build.MANUFACTURER.trim(), model);
        if (TextUtils.isEmpty(device)) {
            device = deviceName(Build.BRAND.trim(), model);
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (device == null) {
            device = "";
        }
        return StringUtils.strip(stringBuilder.append(device).append(SocializeConstants
                .OP_DIVIDER_MINUS).append(model).toString());
    }

    private static String deviceName(String manufacturer, String model) {
        String str = manufacturer.toLowerCase(Locale.getDefault());
        if (str.startsWith(EnvironmentCompat.MEDIA_UNKNOWN) || str.startsWith("alps") || str
                .startsWith(DeviceInfoConstant.OS_ANDROID) || str.startsWith("sprd") || str
                .startsWith("spreadtrum") || str.startsWith("rockchip") || str.startsWith
                ("wondermedia") || str.startsWith("mtk") || str.startsWith("mt65") || str
                .startsWith("nvidia") || str.startsWith("brcm") || str.startsWith("marvell") ||
                model.toLowerCase(Locale.getDefault()).contains(str)) {
            return null;
        }
        return manufacturer;
    }

    public String toString() {
        return this.ua;
    }
}
