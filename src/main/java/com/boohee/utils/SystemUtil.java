package com.boohee.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;

import com.boohee.model.ModelName;
import com.boohee.one.MyApplication;

public class SystemUtil {
    static final String TAG = SystemUtil.class.getName();

    public static String getMacAddress(Context context) {
        String address = "";
        try {
            address = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo()
                    .getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    public static int getVersion() {
        return VERSION.SDK_INT;
    }

    public static String getVersionCode() {
        return VERSION.RELEASE;
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static boolean isHasSdCard() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public static String getAppVersionName() {
        Context context = MyApplication.getContext();
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return versionName;
        }
    }

    public static int getAppVersionCode() {
        Context context = MyApplication.getContext();
        int versionCode = 0;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return versionCode;
        }
    }

    public static int getAppVersionCode(String packageName) {
        int versionCode = 0;
        try {
            return MyApplication.getContext().getPackageManager().getPackageInfo(packageName, 0)
                    .versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return versionCode;
        }
    }

    public static String getProcessName(Context context) {
        int pid = Process.myPid();
        for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService
                (ModelName.ACTIVITY)).getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }
}
