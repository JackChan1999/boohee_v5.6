package com.android.volley;

import com.boohee.utils.Coder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class InternalUtils {
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    InternalUtils() {
    }

    private static String convertToHex(byte[] bytes) {
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[(j * 2) + 1] = HEX_CHARS[v & 15];
        }
        return new String(hexChars);
    }

    public static String sha1Hash(String text) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(Coder.KEY_SHA);
            byte[] bytes = text.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            hash = convertToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        return hash;
    }
}
