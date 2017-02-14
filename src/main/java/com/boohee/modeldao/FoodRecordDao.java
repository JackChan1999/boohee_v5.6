package com.boohee.modeldao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.boohee.database.UserPreference;
import com.boohee.model.RecordFood;
import com.boohee.one.sync.SyncHelper;

import java.util.ArrayList;
import java.util.List;

public class FoodRecordDao extends ModelDaoBase {
    public static final String AMOUNT       = "amount";
    public static final String CALORY       = "calory";
    public static final String CODE         = "code";
    public static final String FOOD_NAME    = "food_name";
    public static final String FOOD_UNIT_ID = "food_unit_id";
    public static final String RECORD_ON    = "record_on";
    public static final String TABLE_NAME   = "food_records";
    public static final String TIME_TYPE    = "time_type";
    public static final String UNIT_NAME    = "unit_name";
    public static final String USER_KEY     = "user_key";

    public FoodRecordDao(Context ctx) {
        super(ctx);
    }

    public RecordFood select(String food_name, String record_on, int time_type, String unit_name) {
        String[] selectionArgs = new String[]{food_name, record_on, Integer.toString(time_type),
                unit_name, UserPreference.getUserKey(this.ctx)};
        Cursor cursor = this.db.query(TABLE_NAME, null, "food_name = ? and record_on = ? and " +
                "time_type = ? and unit_name = ? and user_key = ?", selectionArgs, null, null,
                null);
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        RecordFood record = selectWithCursor(cursor);
        cursor.close();
        return record;
    }

    public RecordFood add(String food_name, int time_type, String code, float amount, float
            calory, int food_unit_id, String unit_name, String record_on) {
        RecordFood record = select(food_name, record_on, time_type, unit_name);
        ContentValues initialValues = new ContentValues();
        if (record != null) {
            initialValues.put(AMOUNT, Float.valueOf(record.amount + amount));
            initialValues.put("calory", Float.valueOf(record.calory + calory));
            ContentValues contentValues = initialValues;
            this.db.update(TABLE_NAME, contentValues, "_id = ?", new String[]{Integer.toString
                    (record.id)});
            record.amount += amount;
            record.calory += calory;
            SyncHelper.syncAllEatings();
            return record;
        }
        initialValues.put("time_type", Integer.valueOf(time_type));
        initialValues.put("food_name", food_name);
        initialValues.put("code", code);
        initialValues.put(AMOUNT, Float.valueOf(amount));
        initialValues.put("calory", Float.valueOf(calory));
        initialValues.put(FOOD_UNIT_ID, Integer.valueOf(food_unit_id));
        initialValues.put("unit_name", unit_name);
        initialValues.put("record_on", record_on);
        initialValues.put("user_key", UserPreference.getUserKey(this.ctx));
        int id = (int) this.db.insert(TABLE_NAME, null, initialValues);
        SyncHelper.syncAllEatings();
        return new RecordFood(UserPreference.getUserKey(this.ctx), food_name, time_type,
                record_on, code, amount, food_unit_id, unit_name, calory);
    }

    public RecordFood add(RecordFood record) {
        return add(record.food_name, record.time_type, record.code, record.amount, record.calory,
                record.food_unit_id, record.unit_name, record.record_on);
    }

    public List<RecordFood> getList(String record_on) {
        String[] selectionArgs = new String[]{record_on, UserPreference.getUserKey(this.ctx)};
        List<RecordFood> list = new ArrayList();
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

    public List<RecordFood> getList() {
        String[] selectionArgs = new String[]{UserPreference.getUserKey(this.ctx)};
        List<RecordFood> list = new ArrayList();
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

    public boolean update(RecordFood record) {
        boolean is_update = true;
        RecordFood r = select(record.food_name, record.record_on, record.time_type, record
                .unit_name);
        ContentValues initialValues = new ContentValues();
        if (r != null) {
            initialValues.put("time_type", Integer.valueOf(record.time_type));
            initialValues.put(AMOUNT, Float.valueOf(record.amount));
            initialValues.put("calory", Float.valueOf(record.calory));
            if (this.db.update(TABLE_NAME, initialValues, "_id = ?", new String[]{Integer
                    .toString(record.id)}) <= -1) {
                is_update = false;
            }
            SyncHelper.syncAllEatings();
            return is_update;
        }
        initialValues.put("time_type", Integer.valueOf(record.time_type));
        initialValues.put("food_name", record.food_name);
        initialValues.put("code", record.code);
        initialValues.put(AMOUNT, Float.valueOf(record.amount));
        initialValues.put("calory", Float.valueOf(record.calory));
        initialValues.put(FOOD_UNIT_ID, Integer.valueOf(record.food_unit_id));
        initialValues.put("unit_name", record.unit_name);
        initialValues.put("record_on", record.record_on);
        initialValues.put("user_key", UserPreference.getUserKey(this.ctx));
        int id = (int) this.db.insert(TABLE_NAME, null, initialValues);
        SyncHelper.syncAllEatings();
        if (id <= -1) {
            return false;
        }
        return true;
    }

    public boolean delete(RecordFood record) {
        if (this.db.delete(TABLE_NAME, "_id = ?", new String[]{Integer.toString(record.id)}) > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteAll() {
        return this.db.delete(TABLE_NAME, null, null) > 0;
    }

    public RecordFood selectWithCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        String user_key = cursor.getString(cursor.getColumnIndex("user_key"));
        String unit_name = cursor.getString(cursor.getColumnIndex("unit_name"));
        String record_on = cursor.getString(cursor.getColumnIndex("record_on"));
        float amount = cursor.getFloat(cursor.getColumnIndex(AMOUNT));
        return new RecordFood(id, user_key, cursor.getString(cursor.getColumnIndex("food_name")),
                cursor.getInt(cursor.getColumnIndex("time_type")), record_on, cursor.getString
                (cursor.getColumnIndex("code")), amount, cursor.getInt(cursor.getColumnIndex
                (FOOD_UNIT_ID)), unit_name, cursor.getFloat(cursor.getColumnIndex("calory")));
    }
}
