package com.boohee.one.video.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class VideoPreference {
    public static final String KEY_BGM           = "KEY_BGM";
    public static final String KEY_BGM_IS_OPEN   = "bgm_is_open";
    public static final String PREF_NAME         = "SPORT_VIDEO";
    public static final String TODAY_IS_COMPLETE = "today_is_complete";
    private static SharedPreferences preference;
    private        Context           context;

    public VideoPreference(Context context) {
        preference = context.getSharedPreferences(PREF_NAME, 0);
    }

    public void putDownload(int id) {
        preference.edit().putBoolean(String.valueOf(id), true).commit();
    }

    public void putNotDownload(int id) {
        preference.edit().putBoolean(String.valueOf(id), false).commit();
    }

    public boolean isDownloaded(int id) {
        return preference.getBoolean(String.valueOf(id), false);
    }

    public void putComplete(String date) {
        preference.edit().putBoolean(date, true).commit();
    }

    public boolean todayIsComplete(String date) {
        return preference.getBoolean(date, false);
    }

    public String getBgm() {
        return preference.getString(KEY_BGM, "");
    }

    public void putBgm(String bgm) {
        if (!TextUtils.isEmpty(bgm)) {
            preference.edit().putString(KEY_BGM, bgm).commit();
        }
    }

    public boolean isBgmOpen() {
        return preference.getBoolean(KEY_BGM_IS_OPEN, true);
    }

    public void putBgmState(boolean state) {
        preference.edit().putBoolean(KEY_BGM_IS_OPEN, state);
    }
}
