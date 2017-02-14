package com.boohee.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.boohee.model.User;
import com.boohee.one.MyApplication;
import com.boohee.utility.Const;

public class UserPreference {
    static final String PREFS_NAME = UserPreference.class.getSimpleName();
    private static UserPreference    userPrefs;
    private        SharedPreferences setting;

    private UserPreference(Context context) {
        this.setting = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public static UserPreference getInstance(Context context) {
        if (userPrefs == null) {
            userPrefs = new UserPreference(context);
        }
        return userPrefs;
    }

    public static String getToken(Context context) {
        return getInstance(context).getString("token");
    }

    public static String getUserKey(Context context) {
        return getInstance(context).getString("user_key");
    }

    public static String getQQOpenID() {
        return getInstance(MyApplication.getContext()).getString(Const.QQ_OPEN_ID);
    }

    public static String getQQAccessToken() {
        return getInstance(MyApplication.getContext()).getString(Const.QQ_ACCESS_TOKEN);
    }

    public static long getQQExpiresIn() {
        return getInstance(MyApplication.getContext()).getLong(Const.QQ_EXPIRES_IN);
    }

    public Editor getEditor() {
        return this.setting.edit();
    }

    public void putString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return this.setting.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.setting.getBoolean(key, defaultValue);
    }

    public void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public void putFloat(String key, float value) {
        getEditor().putFloat(key, value).commit();
    }

    public void remove(String key) {
        getEditor().remove(key).commit();
    }

    public String getString(String key) {
        return this.setting.getString(key, null);
    }

    public long getLong(String key) {
        return this.setting.getLong(key, System.currentTimeMillis());
    }

    public int getInt(String key) {
        return this.setting.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return this.setting.getInt(key, defaultValue);
    }

    public Float getFloat(String key) {
        return Float.valueOf(this.setting.getFloat(key, 0.0f));
    }

    public Float getFloat(String key, float defaultValue) {
        return Float.valueOf(this.setting.getFloat(key, defaultValue));
    }

    public String getString(String key, String default_value) {
        return this.setting.getString(key, default_value);
    }

    public User getUser() {
        if (getFloat("target_weight").floatValue() > 0.0f) {
            return new User(Const.DEFAULT_USER_NAME, getString("sex_type"), getString("birthday")
                    , getFloat("height").floatValue(), getFloat("weight").floatValue(), getFloat
                    ("target_weight").floatValue(), getString("target_date"), 0);
        }
        return null;
    }
}
