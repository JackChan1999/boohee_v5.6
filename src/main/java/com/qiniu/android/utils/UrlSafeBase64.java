package com.qiniu.android.utils;

import android.util.Base64;

import com.qiniu.android.common.Constants;

import java.io.UnsupportedEncodingException;

public final class UrlSafeBase64 {
    public static String encodeToString(String data) {
        try {
            return encodeToString(data.getBytes(Constants.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeToString(byte[] data) {
        return Base64.encodeToString(data, 10);
    }

    public static byte[] decode(String data) {
        return Base64.decode(data, 10);
    }
}
