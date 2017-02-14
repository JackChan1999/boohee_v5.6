package com.alipay.security.mobile.module.commonutils.crypto;

import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.boohee.utils.Coder;
import java.security.MessageDigest;

public class DigestUtil {
    public static String MD5(String str) {
        String str2 = null;
        try {
            if (!CommonUtils.isBlank(str)) {
                MessageDigest instance = MessageDigest.getInstance(Coder.KEY_MD5);
                instance.update(str.getBytes("UTF-8"));
                byte[] digest = instance.digest();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < 16; i++) {
                    stringBuilder.append(String.format("%02x", new Object[]{Byte.valueOf(digest[i])}));
                }
                str2 = stringBuilder.toString();
            }
        } catch (Exception e) {
        }
        return str2;
    }

    public static String digestWithSha1(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA1");
            instance.update(bArr);
            byte[] digest = instance.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                stringBuilder.append(String.format("%02x", new Object[]{Byte.valueOf(digest[i])}));
            }
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] sha1ByByte(String str) {
        byte[] bArr = null;
        try {
            if (!CommonUtils.isBlank(str)) {
                MessageDigest instance = MessageDigest.getInstance(Coder.KEY_SHA);
                instance.update(str.getBytes("UTF-8"));
                bArr = instance.digest();
            }
        } catch (Exception e) {
        }
        return bArr;
    }

    public static String sha1ByString(String str) {
        String str2 = null;
        try {
            if (!CommonUtils.isBlank(str)) {
                MessageDigest instance = MessageDigest.getInstance(Coder.KEY_SHA);
                instance.update(str.getBytes("UTF-8"));
                byte[] digest = instance.digest();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < digest.length; i++) {
                    stringBuilder.append(String.format("%02x", new Object[]{Byte.valueOf(digest[i])}));
                }
                str2 = stringBuilder.toString();
            }
        } catch (Exception e) {
        }
        return str2;
    }
}
