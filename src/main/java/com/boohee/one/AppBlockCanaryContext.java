package com.boohee.one;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.boohee.utils.Helper;
import com.github.moduth.blockcanary.BlockCanaryContext;

public class AppBlockCanaryContext extends BlockCanaryContext {
    private static final String TAG = "AppBlockCanaryContext";

    public String getQualifier() {
        String qualifier = "";
        try {
            PackageInfo info = MyApplication.getContext().getPackageManager().getPackageInfo
                    (MyApplication.getContext().getPackageName(), 0);
            qualifier = qualifier + info.versionCode + "_" + info.versionName + "_YYB";
        } catch (NameNotFoundException e) {
            Helper.showLog(TAG, "getQualifier exception" + e);
        }
        return qualifier;
    }

    public String getUid() {
        return "87224330";
    }

    public String getNetworkType() {
        return "WIFI";
    }

    public int getConfigDuration() {
        return 800;
    }

    public int getConfigBlockThreshold() {
        return 800;
    }

    public boolean isNeedDisplay() {
        return false;
    }
}
