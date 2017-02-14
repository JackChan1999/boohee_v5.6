package com.alipay.security.mobile.module.commonutils;

import android.util.Base64;
import com.boohee.utils.Coder;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class CommonUtils {
    public static boolean equals(String str, String str2) {
        return str == null ? str2 == null : str.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str, String str2) {
        return str == null ? str2 == null : str.equalsIgnoreCase(str2);
    }

    public static String getNonNullString(String str) {
        return isBlank(str) ? "" : str;
    }

    public static String getValueFromMap(Map<String, String> map, String str, String str2) {
        if (map == null) {
            return str2;
        }
        String str3 = (String) map.get(str);
        return str3 != null ? str3 : str2;
    }

    public static boolean isBlank(String str) {
        if (str != null) {
            int length = str.length();
            if (length != 0) {
                for (int i = 0; i < length; i++) {
                    if (!Character.isWhitespace(str.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isZero(String str) {
        if (str != null) {
            int length = str.length();
            if (length != 0) {
                int i = 0;
                while (i < length) {
                    if (!Character.isWhitespace(str.charAt(i)) && str.charAt(i) != '0') {
                        return false;
                    }
                    i++;
                }
                return true;
            }
        }
        return true;
    }

    public static String sha1ByString(String str) {
        String str2 = null;
        try {
            if (!isBlank(str)) {
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

    public static String textCompress(String str) {
        try {
            Object array = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(str.length()).array();
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream(str.length());
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(str.getBytes("UTF-8"));
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            Object obj = new byte[(byteArrayOutputStream.toByteArray().length + 4)];
            System.arraycopy(array, 0, obj, 0, 4);
            System.arraycopy(byteArrayOutputStream.toByteArray(), 0, obj, 4, byteArrayOutputStream.toByteArray().length);
            return Base64.encodeToString(obj, 8);
        } catch (Exception e) {
            return "";
        }
    }
}
