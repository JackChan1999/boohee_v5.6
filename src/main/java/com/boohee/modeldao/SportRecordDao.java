package com.boohee.modeldao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.boohee.database.UserPreference;
import com.boohee.model.RecordSport;
import com.boohee.one.sync.SyncHelper;

import java.util.ArrayList;
import java.util.List;

public class SportRecordDao extends ModelDaoBase {
    public static final String ACTIVITY_ID   = "activity_id";
    public static final String ACTIVITY_NAME = "activity_name";
    public static final String CALORY        = "calory";
    public static final String DURATION      = "duration";
    public static final String METS          = "mets";
    public static final String RECORD_ON     = "record_on";
    public static final String TABLE_NAME    = "sport_records";
    public static final String UNIT_NAME     = "unit_name";
    public static final String USER_KEY      = "user_key";

    public SportRecordDao(Context ctx) {
        super(ctx);
    }

    public RecordSport add(float duration, String activity_name, int activity_id, float calory,
                           float mets, String unit_name, String record_on) {
        RecordSport record = select(activity_id, record_on);
        ContentValues initialValues = new ContentValues();
        if (record != null) {
            initialValues.put(DURATION, Float.valueOf(record.duration + duration));
            initialValues.put("calory", Float.valueOf(record.calory + calory));
            this.db.update(TABLE_NAME, initialValues, "_id = ?", new String[]{Integer.toString
                    (record.id)});
            record.duration += duration;
            record.calory += calory;
            SyncHelper.syncAllSports();
            return record;
        }
        initialValues.put(DURATION, Float.valueOf(duration));
        initialValues.put(ACTIVITY_ID, Integer.valueOf(activity_id));
        initialValues.put(ACTIVITY_NAME, activity_name);
        initialValues.put("calory", Float.valueOf(calory));
        initialValues.put(METS, Float.valueOf(mets));
        initialValues.put("unit_name", unit_name);
        initialValues.put("record_on", record_on);
        initialValues.put("user_key", UserPreference.getUserKey(this.ctx));
        int id = (int) this.db.insert(TABLE_NAME, null, initialValues);
        SyncHelper.syncAllSports();
        return new RecordSport(id, record_on, duration, activity_id, activity_name, calory,
                unit_name, mets, UserPreference.getUserKey(this.ctx));
    }

    public RecordSport add(RecordSport record) {
        return add(record.duration, record.activity_name, record.activity_id, record.calory,
                record.mets, record.unit_name, record.record_on);
    }

    public boolean update(RecordSport record) {
        boolean is_update = true;
        RecordSport r = select(record.activity_id, record.record_on);
        ContentValues initialValues = new ContentValues();
        if (r != null) {
            initialValues.put(DURATION, Float.valueOf(record.duration));
            initialValues.put("calory", Float.valueOf(record.calory));
            if (this.db.update(TABLE_NAME, initialValues, "_id = ?", new String[]{Integer
                    .toString(record.id)}) <= -1) {
                is_update = false;
            }
            SyncHelper.syncAllSports();
            return is_update;
        }
        initialValues.put(DURATION, Float.valueOf(record.duration));
        initialValues.put(ACTIVITY_ID, Integer.valueOf(record.activity_id));
        initialValues.put(ACTIVITY_NAME, record.activity_name);
        initialValues.put("calory", Float.valueOf(record.calory));
        initialValues.put(METS, Float.valueOf(record.mets));
        initialValues.put("unit_name", record.unit_name);
        initialValues.put("record_on", record.record_on);
        initialValues.put("user_key", UserPreference.getUserKey(this.ctx));
        int id = (int) this.db.insert(TABLE_NAME, null, initialValues);
        SyncHelper.syncAllSports();
        if (id <= -1) {
            return false;
        }
        return true;
    }

    public boolean delete(RecordSport record) {
        if (this.db.delete(TABLE_NAME, "_id = ?", new String[]{Integer.toString(record.id)}) > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteAll() {
        return this.db.delete(TABLE_NAME, null, null) > 0;
    }

    public RecordSport select(int activity_id, String record_on) {
        String[] selectionArgs = new String[]{Integer.toString(activity_id), record_on,
                UserPreference.getUserKey(this.ctx)};
        Cursor cursor = this.db.query(TABLE_NAME, null, "activity_id = ? and record_on = ? and " +
                "user_key = ?", selectionArgs, null, null, null);
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        RecordSport record = selectWithCursor(cursor);
        cursor.close();
        return record;
    }

    public List<RecordSport> getList(String record_on) {
        String[] selectionArgs = new String[]{record_on, UserPreference.getUserKey(this.ctx)};
        List<RecordSport> list = new ArrayList();
        Cursor cursor = this.db.query(TABLE_NAME, null, "record_on = ? and user_key = ?",
                selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(selectWithCursor(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<RecordSport> getList() {
        String[] selectionArgs = new String[]{UserPreference.getUserKey(this.ctx)};
        List<RecordSport> list = new ArrayList();
        Cursor cursor = this.db.query(TABLE_NAME, null, "user_key = ?", selectionArgs, null,
                null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(selectWithCursor(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public RecordSport selectWithCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        return new RecordSport(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString
                (cursor.getColumnIndex("record_on")), cursor.getFloat(cursor.getColumnIndex
                (DURATION)), cursor.getInt(cursor.getColumnIndex(ACTIVITY_ID)), cursor.getString
                (cursor.getColumnIndex(ACTIVITY_NAME)), cursor.getFloat(cursor.getColumnIndex
                ("calory")), cursor.getString(cursor.getColumnIndex("unit_name")), cursor
                .getFloat(cursor.getColumnIndex(METS)), cursor.getString(cursor.getColumnIndex("user_key")));
    }
}
