package com.qiniu.android.utils;

import com.qiniu.android.common.Constants;

import java.io.UnsupportedEncodingException;

public final class StringUtils {
    public static String join(String[] array, String sep) {
        int bufSize = 0;
        if (array == null) {
            return null;
        }
        int arraySize = array.length;
        int sepSize = 0;
        if (!(sep == null || sep.equals(""))) {
            sepSize = sep.length();
        }
        if (arraySize != 0) {
            bufSize = ((array[0] == null ? 16 : array[0].length()) + sepSize) * arraySize;
        }
        StringBuilder buf = new StringBuilder(bufSize);
        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(sep);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String jsonJoin(String[] array) {
        int arraySize = array.length;
        StringBuilder buf = new StringBuilder(arraySize * (array[0].length() + 3));
        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(',');
            }
            buf.append('\"');
            buf.append(array[i]);
            buf.append('\"');
        }
        return buf.toString();
    }

    public static byte[] utf8Bytes(String data) {
        try {
            return data.getBytes(Constants.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static String strip(String s) {
        StringBuilder b = new StringBuilder();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            if (c > '\u001f' && c < '') {
                b.append(c);
            }
        }
        return b.toString();
    }
}
