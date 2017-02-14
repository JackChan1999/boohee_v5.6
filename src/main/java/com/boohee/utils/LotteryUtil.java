package com.boohee.utils;

import android.content.Context;

import com.boohee.database.UserPreference;
import com.boohee.one.http.client.BooheeClient;

import java.util.Date;

public class LotteryUtil {
    public static final String LOTTOS_URL = "/api/v1/xiaomi_gift.html?token=%1$s";

    public static boolean isVisible() {
        Date from = DateHelper.parseString("2014-12-28");
        Date end = DateHelper.parseString("2015-01-02");
        Date today = new Date();
        if (today.after(from) && today.before(end)) {
            return true;
        }
        return false;
    }

    public static boolean isVisible(String startStr, String endStr) {
        try {
            Date from = DateHelper.parseString(startStr);
            Date end = DateHelper.parseString(endStr);
            Date today = new Date();
            if (today.after(from) && today.before(end)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getLottosUrl(Context context) {
        return String.format(BooheeClient.build("status").getDefaultURL(LOTTOS_URL), new
                Object[]{UserPreference.getToken(context)});
    }
}
