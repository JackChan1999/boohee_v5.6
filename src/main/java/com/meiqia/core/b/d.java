package com.meiqia.core.b;

import com.boohee.utils.Coder;
import com.tencent.connect.common.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class d {
    private static final String[] a = new String[]{"0", "1", "2", "3", "4", "5", Constants
            .VIA_SHARE_TYPE_INFO, "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    private static String a(byte b) {
        int i;
        if (b < (byte) 0) {
            i = b + 256;
        }
        return a[i / 16] + a[i % 16];
    }

    public static String a(String str) {
        try {
            str = a(MessageDigest.getInstance(Coder.KEY_MD5).digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }

    private static String a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte a : bArr) {
            stringBuffer.append(a(a));
        }
        return stringBuffer.toString();
    }
}
