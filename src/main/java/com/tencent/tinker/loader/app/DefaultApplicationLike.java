package com.tencent.tinker.loader.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

public class DefaultApplicationLike extends ApplicationLike {
    private static final String TAG = "Tinker.DefaultAppLike";

    public DefaultApplicationLike(Application application, int tinkerFlags, boolean
            tinkerLoadVerifyFlag, long applicationStartElapsedTime, long
            applicationStartMillisTime, Intent tinkerResultIntent, Resources[] resources,
                                  ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent, resources, classLoader,
                assetManager);
    }

    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
    }

    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory level:" + level);
    }

    public void onTerminate() {
        Log.d(TAG, "onTerminate");
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged:" + newConfig.toString());
    }

    public void onBaseContextAttached(Context base) {
        Log.d(TAG, "onBaseContextAttached:");
    }
}
