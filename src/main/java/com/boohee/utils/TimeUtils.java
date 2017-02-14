package com.boohee.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static CharSequence getListTime(String created_at) {
        Date date = null;
        SimpleDateFormat srcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US);
        SimpleDateFormat dstDateFormat = new SimpleDateFormat("MMMM dd yyyy", Locale.US);
        try {
            date = srcDateFormat.parse(created_at);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dstDateFormat.format(date);
    }

    public static Date parseDateTime(String created_at) {
        Date date = null;
        SimpleDateFormat srcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US);
        try {
            if (TextUtils.isEmpty(created_at)) {
                return null;
            }
            date = srcDateFormat.parse(created_at);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNow() {
        String date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US).format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
