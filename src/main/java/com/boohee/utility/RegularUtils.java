package com.boohee.utility;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class RegularUtils {
    public static final String CELLPHONE_CHECK = "^1\\d{10}$";
    public static final String CHECK_IP        = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\." +
            "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    public static final String EMAIL_CHECK     = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@" +
            "([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final String USER_NAME_CHECK = "[\\u4e00-\\u9fa5\\w]+";

    public static boolean checkCellPhone(String cellphone) {
        return Pattern.compile(CELLPHONE_CHECK).matcher(cellphone).matches();
    }

    public static boolean checkEmail(String email) {
        if (Pattern.compile(EMAIL_CHECK).matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public static boolean checkUserName(String userName) {
        if (Pattern.compile(USER_NAME_CHECK).matcher(userName).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isIP(String addr) {
        if (TextUtils.isEmpty(addr) || addr.length() < 7 || addr.length() > 15 || !Pattern
                .compile(CHECK_IP).matcher(addr).matches()) {
            return false;
        }
        return true;
    }
}
