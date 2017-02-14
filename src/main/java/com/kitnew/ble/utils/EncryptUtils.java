package com.kitnew.ble.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtils {
    private static final String        HEX             = "0123456789ABCDEF";
    private static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(getEncryptKey()
            .getBytes(), "AES");

    private static native String getEncryptKey();

    static {
        System.loadLibrary("yolanda_calc");
    }

    private EncryptUtils() {
    }

    private static byte[] encrypt(byte[] clear) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, SECRET_KEY_SPEC);
        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, SECRET_KEY_SPEC);
        return cipher.doFinal(encrypted);
    }

    private static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    private static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(i * 2, (i * 2) + 2), 16).byteValue();
        }
        return result;
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 15)).append(HEX.charAt(b & 15));
    }

    private static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(buf.length * 2);
        for (byte aBuf : buf) {
            appendHex(result, aBuf);
        }
        return result.toString();
    }

    public static String encrypt(String string) {
        try {
            return toHex(encrypt(string.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String string) {
        if (string == null) {
            return null;
        }
        try {
            return new String(decrypt(toByte(string)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
