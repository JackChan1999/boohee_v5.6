package com.zxinsight;

import android.os.Handler;

public final class MagicWindowSDK {
    public static volatile boolean a = true;
    private static MLink b;

    public static synchronized void initSDK(MWConfiguration mWConfiguration) {
        synchronized (MagicWindowSDK.class) {
            if (a) {
                new Handler().post(new i());
                b = MLink.getInstance(MWConfiguration.getContext());
                a = false;
            }
        }
    }

    public static String getSDKVersion() {
        return "3.9.160727";
    }

    public static MLink getMLink() {
        return b;
    }
}
