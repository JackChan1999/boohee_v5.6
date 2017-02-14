package com.alipay.security.mobile.module.localstorage;

import com.alipay.security.mobile.module.localstorage.util.FileUtil;

public class SystemPropertyStorage {
    public static String readDataFromSettings(String str) {
        return System.getProperty(str);
    }

    public static void writeDataToSettings(String str, String str2) {
        if (!FileUtil.isBlank(str2)) {
            System.setProperty(str, str2);
        }
    }
}
