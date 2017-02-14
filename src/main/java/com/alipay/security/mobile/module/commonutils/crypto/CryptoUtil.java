package com.alipay.security.mobile.module.commonutils.crypto;

import com.alipay.security.mobile.module.commonutils.CommonUtils;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class CryptoUtil {
    public static final byte[] DEFAULT_KEY = Hex.decode("7B726A5DDD72CBF8D1700FB6EB278AFD7559C40A3761E5A71614D0AC9461ED8EE9F6AAEB443CD648");
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String HMAC_SHA_1 = "HMAC-SHA-1";
    public static final String RAW = "RAW";

    private CryptoUtil() {
    }

    public static String digestWithHmacSha1(String str) {
        String str2 = null;
        if (!CommonUtils.isBlank(str)) {
            Mac instance;
            try {
                instance = Mac.getInstance("HmacSHA1");
            } catch (NoSuchAlgorithmException e) {
                try {
                    instance = Mac.getInstance(HMAC_SHA_1);
                } catch (NoSuchAlgorithmException e2) {
                }
            }
            try {
                instance.init(new SecretKeySpec(DEFAULT_KEY, RAW));
                byte[] doFinal = instance.doFinal(str.getBytes("UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < 16; i++) {
                    stringBuilder.append(String.format("%02x", new Object[]{Byte.valueOf(doFinal[i])}));
                }
                str2 = stringBuilder.toString();
            } catch (Exception e3) {
            }
        }
        return str2;
    }

    public static byte[] digestWithHmacSha1(byte[] bArr, byte[] bArr2) {
        Mac instance;
        try {
            instance = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            instance = Mac.getInstance(HMAC_SHA_1);
        }
        instance.init(new SecretKeySpec(bArr2, RAW));
        return instance.doFinal(bArr);
    }
}
