package com.alipay.security.mobile.module.deviceinfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.umeng.socialize.common.SocialSNSHelper;
import java.io.File;

public class EnvironmentInfo {
    private static EnvironmentInfo a = new EnvironmentInfo();

    private EnvironmentInfo() {
    }

    private static String a(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke(null, new Object[]{str, str2});
        } catch (Exception e) {
            return str2;
        }
    }

    public static EnvironmentInfo getInstance() {
        return a;
    }

    public String getBuildDisplayId() {
        return Build.DISPLAY;
    }

    public String getBuildTags() {
        return Build.TAGS;
    }

    public String getBuildVersionIncremental() {
        return VERSION.INCREMENTAL;
    }

    public String getBuildVersionRelease() {
        return VERSION.RELEASE;
    }

    public String getBuildVersionSDK() {
        return VERSION.SDK;
    }

    public String getGsmSimState() {
        return a("gsm.sim.state", "");
    }

    public String getGsmSimState2() {
        return a("gsm.sim.state.2", "");
    }

    public String getKernelQemu() {
        return a("ro.kernel.qemu", "0");
    }

    public String getNetworkConnectionType(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager.getActiveNetworkInfo().isConnected()) {
                return CommonUtils.equalsIgnoreCase(connectivityManager.getActiveNetworkInfo().getTypeName(), "WIFI") ? "WIFI" : connectivityManager.getActiveNetworkInfo().getExtraInfo();
            }
        } catch (Exception e) {
        }
        return "";
    }

    public String getOSName() {
        return DeviceInfoConstant.OS_ANDROID;
    }

    public String getProductBoard() {
        return Build.BOARD;
    }

    public String getProductBrand() {
        return Build.BRAND;
    }

    public String getProductDevice() {
        return Build.DEVICE;
    }

    public String getProductManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getProductModel() {
        return Build.MODEL;
    }

    public String getProductName() {
        return Build.PRODUCT;
    }

    public String getUsbState() {
        return a("sys.usb.state", "");
    }

    public String getWifiInterface() {
        return a("wifi.interface", "");
    }

    public boolean isEmulator(Context context) {
        try {
            if (Build.HARDWARE.contains("goldfish") || Build.PRODUCT.contains("sdk") || Build.FINGERPRINT.contains(SocialSNSHelper.SOCIALIZE_GENERIC_KEY)) {
                return true;
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return (telephonyManager == null || !CommonUtils.isZero(telephonyManager.getDeviceId())) ? CommonUtils.isBlank(Secure.getString(context.getContentResolver(), "android_id")) : true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRooted() {
        String[] strArr = new String[]{"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        int i = 0;
        while (i < 5) {
            try {
                if (new File(strArr[i] + "su").exists()) {
                    return true;
                }
                i++;
            } catch (Exception e) {
            }
        }
        return false;
    }
}
