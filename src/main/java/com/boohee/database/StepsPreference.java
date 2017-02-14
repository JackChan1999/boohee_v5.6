package com.boohee.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.boohee.one.MyApplication;

public class StepsPreference {
    public static final String PREFS_NAME              = "StepsPreference";
    public static final String PREF_STEPS_BEFORE_TODAY = "pref_steps_before_today";
    public static final String PREF_STEPS_CURRENT_DAY  = "pref_steps_current_day";
    public static final String PREF_STEPS_IS_FIRST     = "pref_step_is_first";
    public static final String PREF_STEPS_LAST_SENSOR  = "pref_steps_last_sensor";
    public static final String PREF_STEPS_NOTIFICATION = "pref_step_notification";
    public static final String PREF_STEPS_TARGET       = "pref_steps_target";
    public static final String PREF_STEPS_TOTAL        = "pref_steps_total";
    public static final String PREF_STEP_OPEN          = "pref_step_open";
    public static final String PREF_SYNC_DATE          = "pref_sync_date";
    private static StepsPreference   preference;
    private        SharedPreferences setting;

    public StepsPreference(Context ctx) {
        this.setting = ctx.getSharedPreferences(PREFS_NAME, 0);
    }

    public static StepsPreference getInstance(Context context) {
        if (preference == null) {
            preference = new StepsPreference(context);
        }
        return preference;
    }

    public static StepsPreference getInstance() {
        return getInstance(MyApplication.getContext());
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

    public boolean getBoolean(String key, boolean value) {
        return this.setting.getBoolean(key, value);
    }

    public void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public long getLong(String key) {
        return this.setting.getLong(key, 0);
    }

    public void putLong(String key, long value) {
        getEditor().putLong(key, value).commit();
    }

    public float getFloat(String key) {
        return this.setting.getFloat(key, 0.0f);
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

    public int getInt(String key) {
        return this.setting.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return this.setting.getInt(key, defaultValue);
    }

    public String getString(String key, String default_value) {
        return this.setting.getString(key, default_value);
    }

    public static int getReceivePush(Context context) {
        return getInstance(context).getInt("isReceivePush", 1);
    }

    public static void putReceivePush(Context context, int value) {
        getInstance(context).putInt("isReceivePush", value);
    }

    public boolean contains(String key) {
        return this.setting.contains(key);
    }

    public void clearAll() {
        this.setting.edit().clear().commit();
    }

    public static String getStepsCurrentDay() {
        return getInstance(MyApplication.getContext()).getString(PREF_STEPS_CURRENT_DAY, "");
    }

    public static void putStepsCurrentDay(String currentDay) {
        getInstance(MyApplication.getContext()).putString(PREF_STEPS_CURRENT_DAY, currentDay);
    }

    public static int getStepsTotal() {
        return getInstance(MyApplication.getContext()).getInt(PREF_STEPS_TOTAL, 0);
    }

    public static void putStepsTotal(int stepsTotal) {
        getInstance(MyApplication.getContext()).putInt(PREF_STEPS_TOTAL, stepsTotal);
    }

    public static int getStepsBeforeToday() {
        return getInstance(MyApplication.getContext()).getInt(PREF_STEPS_BEFORE_TODAY, 0);
    }

    public static void putStepsBeforeToday(int stepsBeforeToday) {
        getInstance(MyApplication.getContext()).putInt(PREF_STEPS_BEFORE_TODAY, stepsBeforeToday);
    }

    public static int getLastSensorSteps() {
        return getInstance(MyApplication.getContext()).getInt(PREF_STEPS_LAST_SENSOR, 0);
    }

    public static void putLasSensorSteps(int lastSensorSteps) {
        getInstance(MyApplication.getContext()).putInt(PREF_STEPS_LAST_SENSOR, lastSensorSteps);
    }

    public static int getStepsTarget() {
        return getInstance(MyApplication.getContext()).getInt(PREF_STEPS_TARGET, 10000);
    }

    public static void putStepsTarget(int target) {
        getInstance(MyApplication.getContext()).putInt(PREF_STEPS_TARGET, target);
    }

    public static void putStepOpen(boolean isOpen) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_STEP_OPEN, isOpen);
    }

    public static boolean isStepOpen() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_STEP_OPEN, true);
    }

    public static void putStepNotification(boolean isOpen) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_STEPS_NOTIFICATION, isOpen);
    }

    public static boolean isStepNotificationOpen() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_STEPS_NOTIFICATION, true);
    }

    public static boolean isFirst() {
        return getInstance().getBoolean(PREF_STEPS_IS_FIRST, true);
    }

    public static void putIsFirst(boolean isFirst) {
        getInstance().putBoolean(PREF_STEPS_IS_FIRST, isFirst);
    }

    public static String getPrefSyncDate() {
        return getInstance().getString(PREF_SYNC_DATE, "");
    }

    public static void putPrefSyncDate(String date) {
        getInstance().putString(PREF_SYNC_DATE, date);
    }
}
