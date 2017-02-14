package com.boohee.utils;

import android.text.TextUtils;

import com.boohee.utility.RegularUtils;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class TextUtil {
    public static boolean isEmpty(CharSequence... strs) {
        for (CharSequence str : strs) {
            if (TextUtils.isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNull(String str) {
        if (str == null || TextUtils.isEmpty(str.trim()) || "null".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isEmail(String strEmail) {
        if (TextUtils.isEmpty(strEmail)) {
            return false;
        }
        return Pattern.compile(RegularUtils.EMAIL_CHECK).matcher(strEmail).matches();
    }

    public static boolean isPhoneNumber(String srtPhone) {
        if (TextUtils.isEmpty(srtPhone)) {
            return false;
        }
        return RegularUtils.checkCellPhone(srtPhone);
    }

    public static String m2(float f) {
        if (f == 0.0f) {
            return "0.00";
        }
        if (f < 1.0f) {
            return f + "";
        }
        return new DecimalFormat("#.00").format((double) f);
    }

    public static String addComma(float f) {
        return new DecimalFormat("#,###").format(Float.valueOf(f));
    }

    public static String checkId(String url) {
        String id = "id=";
        if (!url.contains(id)) {
            return url;
        }
        StringBuilder result = new StringBuilder();
        for (int i = url.indexOf(id) + id.length(); i < url.length(); i++) {
            char ch = url.charAt(i);
            if ('0' > ch || ch > '9') {
                break;
            }
            result.append(ch);
        }
        return result.toString();
    }
}
