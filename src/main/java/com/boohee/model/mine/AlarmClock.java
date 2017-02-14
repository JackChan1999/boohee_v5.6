package com.boohee.model.mine;

import android.content.Context;

import com.boohee.database.OnePreference;
import com.boohee.model.ModelBase;
import com.google.gson.Gson;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

public class AlarmClock extends ModelBase {
    public static String GET_UP_ALARM = "getUpClock";
    public static String SLEEP_ALARM  = "toSleepClock";
    public Boolean enable;
    public int     hour;
    public int     min;
    public String  time;
    public String  type;
    public int[]   weekdays;

    public static AlarmClock parseAlarm(JSONObject res) {
        AlarmClock alarmClock = null;
        try {
            return (AlarmClock) new Gson().fromJson(res.getJSONObject("alarm").toString(),
                    AlarmClock.class);
        } catch (Exception e) {
            e.printStackTrace();
            return alarmClock;
        }
    }

    public static AlarmClock getAlarm(Context ctx, OnePreference onePreference, String preference) {
        String alarm = OnePreference.getInstance(ctx).getString(preference);
        JSONObject alarmClockj = null;
        if (alarm != null) {
            try {
                alarmClockj = new JSONObject(alarm);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (alarmClockj == null) {
            return null;
        }
        return parseAlarm(alarmClockj);
    }

    public static float calculateScale(Context ctx) {
        float scale;
        OnePreference onePreference = OnePreference.getInstance(ctx);
        AlarmClock getUpClock = getAlarm(ctx, onePreference, GET_UP_ALARM);
        AlarmClock toSleepClock = getAlarm(ctx, onePreference, SLEEP_ALARM);
        int getUpHour = getUpClock.hour;
        int sleepHour = toSleepClock.hour;
        int getUpMin = getUpClock.min;
        int sleepMin = toSleepClock.min;
        if (sleepHour == 0) {
            sleepHour = 24;
        }
        if (getUpHour == 24) {
            getUpHour = 0;
        }
        float scaleHour = (float) ((sleepHour - getUpHour) * 60);
        float scaleMin = (float) (sleepMin - getUpMin);
        if (sleepHour < getUpHour) {
            scale = 24.0f;
        } else {
            scale = 24.0f - ((scaleHour + scaleMin) / 60.0f);
        }
        return new BigDecimal((double) scale).setScale(1, 4).floatValue();
    }
}
