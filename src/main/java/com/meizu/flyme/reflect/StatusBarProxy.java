package com.meizu.flyme.reflect;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import java.lang.reflect.Field;

public class StatusBarProxy {
    private static final String TAG = "StatusBar";

    public static boolean setStatusBarDarkIcon(Window window, boolean dark) {
        if (window == null) {
            return false;
        }
        try {
            LayoutParams lp = window.getAttributes();
            Field darkFlag = LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= bit ^ -1;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "setStatusBarDarkIcon: failed");
            return false;
        }
    }

    @TargetApi(19)
    public static boolean setImmersedWindow(Window window, boolean immersive) {
        if (window == null) {
            return false;
        }
        LayoutParams lp = window.getAttributes();
        if (VERSION.SDK_INT < 19) {
            try {
                Field flags = lp.getClass().getDeclaredField("meizuFlags");
                flags.setAccessible(true);
                int value = flags.getInt(lp);
                if (immersive) {
                    value |= 64;
                } else {
                    value &= -65;
                }
                flags.setInt(lp, value);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "setImmersedWindow: failed");
                return false;
            }
        }
        lp.flags |= 67108864;
        window.setAttributes(lp);
        return true;
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(c.getField
                    ("status_bar_height").get(c.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 75;
        }
    }
}
