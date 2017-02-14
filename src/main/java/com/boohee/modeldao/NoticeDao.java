package com.boohee.modeldao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.boohee.model.AlarmTip;
import com.boohee.model.Notice;

import java.util.ArrayList;

public class NoticeDao extends BHDaoBase {
    public static final  String ALARM_TIP_ID      = "alarm_tip_id";
    public static final  String ALARM_TIP_MESSAGE = "alarm_tip_message";
    public static final  String IS_OPENED         = "is_opened";
    private static final String TABLE_NAME        = "notices";

    public NoticeDao(Context ctx) {
        super(ctx);
    }

    public long add(AlarmTip alarmTip) {
        ContentValues cv = new ContentValues();
        cv.put(ALARM_TIP_ID, Integer.valueOf(alarmTip.id));
        cv.put(ALARM_TIP_MESSAGE, alarmTip.message);
        return this.db.insert(TABLE_NAME, null, cv);
    }

    public long addMessage(String message) {
        ContentValues cv = new ContentValues();
        cv.put(ALARM_TIP_ID, Integer.valueOf(0));
        cv.put(ALARM_TIP_MESSAGE, message);
        return this.db.insert(TABLE_NAME, null, cv);
    }

    public boolean delete(Notice notice) {
        return this.db.delete(TABLE_NAME, "id = ?", new String[]{Integer.toString(notice.id)}) > 0;
    }

    public int unReadCount() {
        int count = 0;
        Cursor cursor = this.db.query(TABLE_NAME, new String[]{"count(*)"}, "is_opened = 0",
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public ArrayList<Notice> getNotices() {
        ArrayList<Notice> alarms = new ArrayList();
        Cursor cursor = this.db.query(TABLE_NAME, null, null, null, null, null, "created_at DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                alarms.add(selectWithCursor(cursor));
            }
            cursor.close();
        }
        return alarms;
    }

    public Notice selectWithId(int id) {
        Notice notice = null;
        Cursor cursor = this.db.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(id)
        }, null, null, null);
        if (cursor.moveToFirst()) {
            notice = selectWithCursor(cursor);
        }
        cursor.close();
        return notice;
    }

    public Notice selectWithCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        return new Notice(cursor.getInt(cursor.getColumnIndex("id")), cursor.getInt(cursor
                .getColumnIndex(ALARM_TIP_ID)), cursor.getString(cursor.getColumnIndex
                (ALARM_TIP_MESSAGE)), cursor.getInt(cursor.getColumnIndex(IS_OPENED)));
    }

    public boolean updateIsOpened(Notice notice) {
        boolean z = true;
        if (notice == null) {
            return false;
        }
        ContentValues cv = new ContentValues();
        cv.put(IS_OPENED, Integer.valueOf(1));
        if (this.db.update(TABLE_NAME, cv, "id = ?", new String[]{String.valueOf(notice.id)}) <=
                -1) {
            z = false;
        }
        return z;
    }

    public boolean clear() {
        return this.db.delete(TABLE_NAME, null, null) > 0;
    }
}
