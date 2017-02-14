package com.alipay.apmobilesecuritysdk.f;

import com.alipay.security.mobile.module.commonutils.crypto.Base64Util;
import java.util.Map;
import java.util.Set;

public final class a {
    public static String a(Map<String, Integer> map, String str, String str2) {
        if (str2 == null || str2.length() <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str2 + ":");
        if (str == null || str.length() <= 0) {
            return stringBuilder.toString();
        }
        String[] split = str.split(",");
        if (split == null || split.length <= 0) {
            return stringBuilder.toString();
        }
        if (map == null || map.size() <= 0) {
            return stringBuilder.toString();
        }
        Set keySet = map.keySet();
        if (keySet == null || keySet.size() <= 0) {
            return stringBuilder.toString();
        }
        try {
            int i;
            byte[] bArr = new byte[((split.length / 8) + 1)];
            for (i = 0; i < bArr.length; i++) {
                bArr[i] = (byte) 0;
            }
            int i2 = 0;
            for (Object obj : split) {
                int i3 = bArr[i2 / 8];
                if (keySet.contains(obj)) {
                    i3 |= 128 >> (i2 % 8);
                }
                bArr[i2 / 8] = (byte) (i3 & 255);
                i2++;
            }
            stringBuilder.append(Base64Util.encode(bArr));
        } catch (Throwable th) {
        }
        return stringBuilder.toString();
    }
}
