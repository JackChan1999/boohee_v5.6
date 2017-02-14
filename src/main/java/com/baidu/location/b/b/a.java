package com.baidu.location.b.b;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class a {
    private static final String a = "AES";
    private static final String if = "AES/CBC/PKCS5Padding";

    private a() {
    }

    public static byte[] a(String str, String str2, byte[] bArr) throws Exception {
        Key secretKeySpec = new SecretKeySpec(str2.getBytes(), a);
        Cipher instance = Cipher.getInstance(if);
        instance.init(2, secretKeySpec, new IvParameterSpec(str.getBytes()));
        return instance.doFinal(bArr);
    }

    public static byte[] if(String str, String str2, byte[] bArr) throws Exception {
        Key secretKeySpec = new SecretKeySpec(str2.getBytes(), a);
        Cipher instance = Cipher.getInstance(if);
        instance.init(1, secretKeySpec, new IvParameterSpec(str.getBytes()));
        return instance.doFinal(bArr);
    }
}
