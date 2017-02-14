package com.zxinsight;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build.VERSION;

import com.zxinsight.common.util.m;

public class Session {
    public static void onResume(Context context) {
        TrackAgent.currentEvent().onResume(context);
    }

    public static void onResume(Context context, String str) {
        TrackAgent.currentEvent().onResume(context, str);
    }

    public static void onPause(Context context) {
        TrackAgent.currentEvent().onPause(context);
    }

    public static void onPause(Context context, String str) {
        TrackAgent.currentEvent().onPause(context, str);
    }

    public static void onPageStart(String str) {
        TrackAgent.currentEvent().onPageStart(str);
    }

    public static void onPageEnd(String str) {
        TrackAgent.currentEvent().onPageEnd(str);
    }

    @TargetApi(14)
    public static void setAutoSession(Application application) {
        MWConfiguration.initContext(application.getApplicationContext());
        if (VERSION.SDK_INT >= 14) {
            try {
                ActivityLifecycleCallbacks xVar = new x();
                application.unregisterActivityLifecycleCallbacks(xVar);
                application.registerActivityLifecycleCallbacks(xVar);
                m.a().h(true);
            } catch (NoSuchMethodError e) {
                m.a().h(false);
            } catch (NoClassDefFoundError e2) {
                m.a().h(false);
            }
        }
    }

    public static void onKillProcess() {
        TrackAgent.currentEvent().onKillProcess();
    }
}
