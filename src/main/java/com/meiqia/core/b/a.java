package com.meiqia.core.b;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class a {
    public static        boolean a = false;
    public static final  char[]  b = new char[]{'\u0000', '\u0001', '\u0002', '\u0003', '\u0004',
            '\u0005', '\u0006', '\u0007', '\b', '\t', '\u0010', '\u0011', '\u0012', '\u0013',
            '\u0014', '\u0015'};
    private static final byte[]  c = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
            (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
            (byte) 0, (byte) 0, (byte) 0};

    public static String a(String str, String str2) {
        try {
            SecretKeySpec a = a(str);
            c("message", str2);
            String trim = Base64.encodeToString(a(a, c, str2.getBytes("UTF-8")), 0).trim();
            c("Base64.DEFAULT", trim);
            return trim;
        } catch (Throwable e) {
            if (a) {
                Log.e("AESCrypt", "UnsupportedEncodingException ", e);
            }
            throw new GeneralSecurityException(e);
        }
    }

    private static String a(byte[] bArr) {
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
                'D', 'E', 'F'};
        char[] cArr2 = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            cArr2[i * 2] = cArr[i2 >>> 4];
            cArr2[(i * 2) + 1] = cArr[i2 & 15];
        }
        return new String(cArr2);
    }

    private static SecretKeySpec a(String str) {
        if (!TextUtils.isEmpty(str)) {
            return new SecretKeySpec(str.getBytes("UTF-8"), "AES");
        }
        throw new UnsupportedEncodingException("password is null");
    }

    private static void a(String str, byte[] bArr) {
        if (a) {
            Log.d("AESCrypt", str + "[" + bArr.length + "] [" + a(bArr) + "]");
        }
    }

    public static byte[] a(SecretKeySpec secretKeySpec, byte[] bArr, byte[] bArr2) {
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS7Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
        instance.init(1, secretKeySpec);
        byte[] iv = instance.getIV();
        byte[] doFinal = instance.doFinal(bArr2);
        a("cipherText", doFinal);
        return a(iv, doFinal);
    }

    static byte[] a(byte[] bArr, byte[] bArr2) {
        Object obj = null;
        int length = bArr != null ? bArr.length : 0;
        int length2 = bArr2 != null ? bArr2.length : 0;
        if (length + length2 > 0) {
            obj = new byte[(length + length2)];
        }
        if (length > 0) {
            System.arraycopy(bArr, 0, obj, 0, length);
        }
        if (length2 > 0) {
            System.arraycopy(bArr2, 0, obj, length, length2);
        }
        return obj;
    }

    public static String b(String str, String str2) {
        try {
            SecretKeySpec a = a(str);
            c("base64EncodedCipherText", str2);
            byte[] decode = Base64.decode(str2, 2);
            a("decodedCipherText", decode);
            byte[] b = b(a, Arrays.copyOfRange(decode, 0, 16), Arrays.copyOfRange(decode, 16,
                    decode.length));
            a("decryptedBytes", b);
            String str3 = new String(b, "UTF-8");
            c("message", str3);
            return str3;
        } catch (Throwable e) {
            if (a) {
                Log.e("AESCrypt", "UnsupportedEncodingException ", e);
            }
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] b(SecretKeySpec secretKeySpec, byte[] bArr, byte[] bArr2) {
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS7Padding");
        instance.init(2, secretKeySpec, new IvParameterSpec(bArr));
        byte[] doFinal = instance.doFinal(bArr2);
        a("decryptedBytes", doFinal);
        return doFinal;
    }

    private static void c(String str, String str2) {
        if (a) {
            Log.d("AESCrypt", str + "[" + str2.length() + "] [" + str2 + "]");
        }
    }
}
