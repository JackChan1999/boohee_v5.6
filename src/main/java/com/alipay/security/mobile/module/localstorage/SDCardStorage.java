package com.alipay.security.mobile.module.localstorage;

import android.os.Environment;
import com.alipay.security.mobile.module.localstorage.util.FileUtil;
import java.io.File;

public class SDCardStorage {
    public static boolean isSdCardAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return externalStorageState != null && externalStorageState.length() > 0 && ((externalStorageState.equals("mounted") || externalStorageState.equals("mounted_ro")) && Environment.getExternalStorageDirectory() != null);
    }

    public static String readDataFromSDCard(String str) {
        try {
            if (isSdCardAvailable()) {
                File file = new File(Environment.getExternalStorageDirectory(), str);
                if (file.exists()) {
                    return FileUtil.readFile(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void writeDataToSDCard(String str, String str2) {
        try {
            if (isSdCardAvailable()) {
                File file = new File(Environment.getExternalStorageDirectory(), str);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }
                FileUtil.writeFile(file.getAbsolutePath(), str2);
            }
        } catch (Exception e) {
        }
    }
}
