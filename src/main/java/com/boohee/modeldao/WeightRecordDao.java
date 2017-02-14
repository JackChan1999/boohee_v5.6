package com.boohee.modeldao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.boohee.database.OnePreference;
import com.boohee.model.mine.WeightRecord;
import com.boohee.utils.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

public class WeightRecordDao extends ModelDaoBase {
    public static final String CREATED_AT = "created_at";
    public static final String PHOTOS     = "photos";
    public static final String RECORD_ON  = "record_on";
    public static final String TABLE_NAME = "weight_records";
    static final        String TAG        = WeightRecordDao.class.getSimpleName();
    public static final String WEIGHT     = "weight";

    public WeightRecordDao(Context ctx) {
        super(ctx);
    }

    public float getLastestWeight() {
        if (OnePreference.getLatestWeight() > 0.0f) {
            return OnePreference.getLatestWeight();
        }
        return 55.0f;
    }

    public boolean add(String weight, String record_on, JSONArray photos) {
        ContentValues cv = new ContentValues();
        cv.put("weight", weight);
        cv.put("record_on", record_on);
        if (photos != null) {
            cv.put(PHOTOS, photos.toString());
        }
        return this.db.replace(TABLE_NAME, null, cv) > -1;
    }

    public WeightRecord select(String record_on) {
        WeightRecord record = null;
        Cursor c = this.db.rawQuery("select * from weight_records where record_on = ?", new
                String[]{record_on});
        if (c != null && c.moveToFirst()) {
            record = selectWithCursor(c);
        }
        c.close();
        return record;
    }

    public List<WeightRecord> getList() {
        List<WeightRecord> list = new ArrayList();
        Cursor cursor = this.db.query(TABLE_NAME, null, null, null, null, null, "record_on DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(selectWithCursor(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<WeightRecord> getMonthLists(Date record_on) {
        List<WeightRecord> records = new ArrayList();
        Date beginMonth = DateHelper.getFirstDayOfMonth(record_on);
        Date endMonth = DateHelper.getLastDayOfMonth(record_on);
        String[] selectionArgs = new String[]{DateHelper.format(beginMonth), DateHelper.format
                (endMonth)};
        Cursor cursor = this.db.query(TABLE_NAME, null, "record_on between ? and ?",
                selectionArgs, null, null, "record_on");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                records.add(selectWithCursor(cursor));
            }
            cursor.close();
        }
        return records;
    }

    public boolean delete(String record_on) {
        return this.db.delete(TABLE_NAME, "record_on = ?", new String[]{record_on}) > 0;
    }

    public WeightRecord selectWithCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        return new WeightRecord(cursor.getString(cursor.getColumnIndex("weight")), cursor
                .getString(cursor.getColumnIndex("record_on")), cursor.getString(cursor
                .getColumnIndex("created_at")), cursor.getString(cursor.getColumnIndex(PHOTOS)));
    }
}
