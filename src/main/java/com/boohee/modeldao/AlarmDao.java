package com.boohee.modeldao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.boohee.model.Alarm;

import java.util.ArrayList;

public class AlarmDao extends BHDaoBase {
    public static final  String CODE       = "code";
    public static final  String DAYSOFWEEK = "daysofweek";
    public static final  String ENABLED    = "enabled";
    public static final  String HOUR       = "hour";
    public static final  String MESSAGE    = "message";
    public static final  String MINUTE     = "minute";
    private static final String TABLE_NAME = "alarms";
    public static final  String TITLE      = "title";
    public static final  String TYPE       = "type";

    public AlarmDao(Context ctx) {
        super(ctx);
    }

    public int update(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(HOUR, Integer.valueOf(alarm.hour));
        cv.put(MINUTE, Integer.valueOf(alarm.minute));
        cv.put(ENABLED, Integer.valueOf(alarm.enabled));
        return this.db.update(TABLE_NAME, cv, "code = ?", new String[]{alarm.code});
    }

    public void update(ArrayList<Alarm> alarms) {
        for (int i = 0; i < alarms.size(); i++) {
            update((Alarm) alarms.get(i));
        }
    }

    public Alarm query(String code) {
        Alarm alarm = null;
        Cursor c = this.db.rawQuery("select * from alarms where code = ?", new String[]{code});
        if (c.moveToFirst()) {
            alarm = selectWithCursor(c);
        }
        c.close();
        return alarm;
    }

    public Alarm selectWithCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        return new Alarm(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor
                .getColumnIndex("code")), cursor.getInt(cursor.getColumnIndex(HOUR)), cursor
                .getInt(cursor.getColumnIndex(MINUTE)), cursor.getInt(cursor.getColumnIndex
                (ENABLED)), cursor.getInt(cursor.getColumnIndex("type")));
    }

    public ArrayList<Alarm> getAlarms() {
        ArrayList<Alarm> alarms = new ArrayList();
        Cursor cursor = this.db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                alarms.add(selectWithCursor(cursor));
            }
            cursor.close();
        }
        return alarms;
    }

    public ArrayList<Alarm> getAlarmsByNoticeType(int type) {
        ArrayList<Alarm> alarms = new ArrayList();
        Cursor cursor = this.db.query(TABLE_NAME, null, "type = ?", new String[]{String.valueOf
                (type)}, null, null, "id");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                alarms.add(selectWithCursor(cursor));
            }
            cursor.close();
        }
        return alarms;
    }
}
