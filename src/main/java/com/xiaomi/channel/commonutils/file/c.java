package com.xiaomi.channel.commonutils.file;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;

public class c {
    public static boolean a() {
        return Environment.getExternalStorageState().equals("removed");
    }

    public static boolean b() {
        return !Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean c() {
        return e() <= 102400;
    }

    public static boolean d() {
        return (b() || c() || a()) ? false : true;
    }

    public static long e() {
        if (b()) {
            return 0;
        }
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory == null || TextUtils.isEmpty(externalStorageDirectory
                .getPath())) {
            return 0;
        }
        StatFs statFs = new StatFs(externalStorageDirectory.getPath());
        return (((long) statFs.getAvailableBlocks()) - 4) * ((long) statFs.getBlockSize());
    }
}
