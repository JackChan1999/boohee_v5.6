package com.alipay.security.mobile.module.localstorage;

import com.alipay.security.mobile.module.localstorage.util.FileUtil;
import java.io.File;

public class PublicStorage {
    public static String readDataFromPublicArea(String str) {
        String str2 = "";
        try {
            str2 = SystemPropertyStorage.readDataFromSettings(str);
        } catch (Throwable th) {
        }
        return FileUtil.isBlank(str2) ? SDCardStorage.readDataFromSDCard(".SystemConfig" + File.separator + str) : str2;
    }

    public static void writeDataToPublicArea(String str, String str2) {
        try {
            SystemPropertyStorage.writeDataToSettings(str, str2);
        } catch (Throwable th) {
        }
        if (SDCardStorage.isSdCardAvailable()) {
            SDCardStorage.writeDataToSDCard(".SystemConfig" + File.separator + str, str2);
        }
    }
}
