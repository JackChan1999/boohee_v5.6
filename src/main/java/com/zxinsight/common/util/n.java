package com.zxinsight.common.util;

import android.text.TextUtils;

import com.boohee.utils.Coder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class n {
    public static String a(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(Coder.KEY_MD5);
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digest) {
                int i = b & 255;
                if (i < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(i));
            }
            return stringBuilder.toString().toLowerCase().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String b(String str) {
        if (!Pattern.compile("^((\\+?86)?\\s?\\-?)1[0-9]{10}").matcher(str).matches()) {
            return "";
        }
        Matcher matcher = Pattern.compile("^((\\+?86)?)\\s?\\-?").matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, "");
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static boolean a(String str, String str2) {
        return (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) || (l.b(str) && str.equals
                (str2));
    }

    public static boolean c(String str) {
        return Pattern.compile("^((\\+?86)?\\s?\\-?)1[0-9]{10}").matcher(str).matches();
    }

    public static boolean d(String str) {
        return Pattern.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]+$",
                2).matcher(str).matches();
    }
}
