package com.baidu.location;

import com.baidu.location.b.b.b;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CommonEncrypt {
    public static boolean a;

    static {
        try {
            System.loadLibrary("locSDK4d");
            a = true;
        } catch (Exception e) {
            a = false;
        }
        a = true;
    }

    public static String a(String str) {
        if (!a) {
            return null;
        }
        try {
            return URLEncoder.encode(a(encrypt(str.getBytes())), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    public static String a(byte[] bArr) {
        try {
            return b.a(bArr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static native byte[] encrypt(byte[] bArr);
}
