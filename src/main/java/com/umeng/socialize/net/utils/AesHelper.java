package com.umeng.socialize.net.utils;

import android.text.TextUtils;

import com.boohee.utils.Coder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesHelper {
    private static final String UTF_8 = "UTF-8";
    private static       byte[] iv    = "nmeug.f9/Om+L823".getBytes();
    private static       byte[] pwd   = null;

    public static String encryptNoPadding(String str, String str2) throws Exception {
        Cipher instance = Cipher.getInstance("AES/CBC/NoPadding");
        int blockSize = instance.getBlockSize();
        Object bytes = str.getBytes(str2);
        int length = bytes.length;
        if (length % blockSize != 0) {
            length += blockSize - (length % blockSize);
        }
        Object obj = new byte[length];
        System.arraycopy(bytes, 0, obj, 0, bytes.length);
        instance.init(1, new SecretKeySpec(pwd, "AES"), new IvParameterSpec(iv));
        return Base64.encodeBase64String(instance.doFinal(obj));
    }

    public static String decryptNoPadding(String str, String str2) throws Exception {
        Cipher instance = Cipher.getInstance("AES/CBC/NoPadding");
        instance.init(2, new SecretKeySpec(pwd, "AES"), new IvParameterSpec(iv));
        return new String(instance.doFinal(Base64.decodeBase64(str)), str2);
    }

    public static void setPassword(String str) {
        if (!TextUtils.isEmpty(str)) {
            String md5 = md5(str);
            if (md5.length() >= 16) {
                md5 = md5.substring(0, 16);
            }
            pwd = md5.getBytes();
        }
    }

    public static byte[] getBytesUtf8(String str) {
        return getBytesUnchecked(str, "UTF-8");
    }

    public static byte[] getBytesUnchecked(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(str2);
        } catch (UnsupportedEncodingException e) {
            throw newIllegalStateException(str2, e);
        }
    }

    private static IllegalStateException newIllegalStateException(String str,
                                                                  UnsupportedEncodingException
                                                                          unsupportedEncodingException) {
        return new IllegalStateException(str + ": " + unsupportedEncodingException);
    }

    public static String newString(byte[] bArr, String str) {
        if (bArr == null) {
            return null;
        }
        try {
            return new String(bArr, str);
        } catch (UnsupportedEncodingException e) {
            throw newIllegalStateException(str, e);
        }
    }

    public static String newStringUtf8(byte[] bArr) {
        return newString(bArr, "UTF-8");
    }

    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes();
            MessageDigest instance = MessageDigest.getInstance(Coder.KEY_MD5);
            instance.reset();
            instance.update(bytes);
            bytes = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                stringBuffer.append(String.format("%02X", new Object[]{Byte.valueOf(bytes[i])}));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            return str.replaceAll("[^[a-z][A-Z][0-9][.][_]]", "");
        }
    }
}
