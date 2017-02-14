package com.boohee.utility;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.boohee.one.BuildConfig;
import com.boohee.one.MyApplication;

public class Config {
    static final String TAG = Config.class.getSimpleName();

    public static String getChannel(Context context) {
        String channel = "UNKNOW";
        if (context == null) {
            return channel;
        }
        String channelKey = "UMENG_CHANNEL";
        Bundle metaData = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context
                    .getPackageName(), 128);
            if (ai != null) {
                metaData = ai.metaData;
            }
            if (metaData != null) {
                channel = metaData.getString(channelKey);
            }
        } catch (NameNotFoundException e) {
        }
        return channel;
    }

    public static String getVersionName() {
        try {
            return MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication
                    .getContext().getPackageName(), 0).versionName;
        } catch (Exception e) {
            String versionName = BuildConfig.VERSION_NAME;
            e.printStackTrace();
            return versionName;
        }
    }

    public static int getVersionCode() {
        try {
            return MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication
                    .getContext().getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 108;
        }
    }
}
