package com.alipay.security.mobile.module.commonutils.crypto;

public final class HotpExUtil {
    public static final int HOTPEX_HASH_LEN = 20;
    public static final int MAX_CODE_LEN = 8;
    public static final byte[] salt1 = Hex.decode("7B726A5DDD72CBF8D1700FB6EB278AFD7559C40A3761E5A71614D0AC9461ED8EE9F6AAEB443CD648");
    public static final byte[] salt2 = Hex.decode("C9582A82777392CAA65AD7F5228150E3F966C09D6A00288B5C6E0CFB441E111B713B4E0822A8C830");

    private HotpExUtil() {
    }

    public static byte[] process(byte[] bArr) {
        Object obj = new byte[20];
        if (ByteUtil.initWithByte(obj, (byte) 0, 0, 20)) {
            Object obj2 = new byte[20];
            if (ByteUtil.initWithByte(obj2, (byte) 0, 0, 20)) {
                Object digestWithHmacSha1 = CryptoUtil.digestWithHmacSha1(bArr, salt1);
                System.arraycopy(digestWithHmacSha1, 0, obj, 0, digestWithHmacSha1.length);
                digestWithHmacSha1 = CryptoUtil.digestWithHmacSha1(bArr, salt2);
                System.arraycopy(digestWithHmacSha1, 0, obj2, 0, digestWithHmacSha1.length);
                r2 = new byte[8];
                int i = obj[19] & 15;
                r2[3] = (byte) (obj[i] & 127);
                r2[2] = (byte) (obj[i + 1] & 255);
                r2[1] = (byte) (obj[i + 2] & 255);
                r2[0] = (byte) (obj[i + 3] & 255);
                int i2 = obj2[19] & 15;
                r2[4] = (byte) (obj2[i2] & 127);
                r2[5] = (byte) (obj2[i2 + 1] & 255);
                r2[6] = (byte) (obj2[i2 + 2] & 255);
                r2[7] = (byte) (obj2[i2 + 3] & 255);
                return r2;
            }
            throw new IllegalStateException("failed to init hash2.");
        }
        throw new IllegalStateException("failed to init hash1.");
    }

    public static byte[] process(byte[] bArr, int i) {
        byte[] process = process(bArr);
        if (process == null || i <= 0) {
            return null;
        }
        if (i >= 8) {
            return process;
        }
        byte[] bArr2 = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            bArr2[i2] = process[i2];
        }
        return bArr2;
    }
}
