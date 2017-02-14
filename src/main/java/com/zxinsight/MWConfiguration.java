package com.zxinsight;

import android.content.Context;
import android.os.Build.VERSION;

import com.zxinsight.common.reflect.b;
import com.zxinsight.common.util.m;

public class MWConfiguration {
    public static final int ORIGINAL  = 0;
    public static final int SHARE_SDK = 1;
    public static final int UMENG     = 2;
    private static volatile Context context;

    public MWConfiguration(Context context) {
        initDefaultValue(context);
    }

    private static synchronized void initDefaultValue(Context context) {
        synchronized (MWConfiguration.class) {
            if (context != null) {
                context = context.getApplicationContext();
            } else if (VERSION.SDK_INT >= 14) {
                context = (Context) b.a("android.app.ActivityThread").d("currentApplication").a();
            }
            m a = m.a();
            a.a(Boolean.valueOf(true));
            a.c(true);
            a.e(false);
            a.f(false);
            a.b(false);
            a.a(false);
        }
    }

    public static Context getContext() {
        if (context == null && VERSION.SDK_INT >= 14) {
            context = (Context) b.a("android.app.ActivityThread").d("currentApplication").a();
        }
        return context;
    }

    public static void initContext(Context context) {
        if (context != null && context == null) {
            context = context.getApplicationContext();
        }
    }

    public MWConfiguration setDebugModel(boolean z) {
        m.a().a(Boolean.valueOf(z));
        return this;
    }

    public MWConfiguration setChannel(String str) {
        m.a().j(str);
        return this;
    }

    public MWConfiguration setScreenOrientationPortrait() {
        m.a().d(true);
        return this;
    }

    public MWConfiguration setCrashTrackOff() {
        m.a().c(false);
        return this;
    }

    public MWConfiguration setCustomWebViewTitleBarOn() {
        m.a().e(true);
        return this;
    }

    public MWConfiguration setCustomWebViewTitleBarOn(boolean z) {
        m.a().e(z);
        return this;
    }

    public MWConfiguration setWebViewBroadcastOpen() {
        m.a().f(true);
        return this;
    }

    public MWConfiguration setWebViewBroadcastOpen(boolean z) {
        m.a().f(z);
        return this;
    }

    public MWConfiguration setCityCode(String str) {
        m.a().k(str);
        return this;
    }

    public MWConfiguration setPageTrackWithFragment(boolean z) {
        m.a().b(z);
        return this;
    }

    public MWConfiguration setSharePlatform(int i) {
        if (i < 0 || i > 2) {
            i = 0;
        }
        if (i == 1) {
            m.a().a(true);
        } else {
            m.a().a(false);
        }
        return this;
    }

    @Deprecated
    public MWConfiguration setMLinkOpen() {
        return this;
    }
}
