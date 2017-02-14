package com.meizu.flyme.reflect;

import android.app.WallpaperManager;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class WallpaperManagerProxy extends Proxy {
    private static final String   TAG    = "WallpaperManagerProxy";
    private static       Class<?> sClass = WallpaperManager.class;
    private static Method sSetLockWallpaper;

    public static boolean setLockWallpaper(Context context, String path) {
        boolean result = false;
        WallpaperManager wm = WallpaperManager.getInstance(context);
        try {
            InputStream is = new FileInputStream(path);
            sSetLockWallpaper = Proxy.getMethod(sSetLockWallpaper, sClass,
                    "setStreamToLockWallpaper", InputStream.class);
            if (wm != null) {
                result = Proxy.invoke(sSetLockWallpaper, wm, is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean setHomeWallpaper(Context context, String path) {
        try {
            WallpaperManager.getInstance(context).setStream(new FileInputStream(path));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
