package com.boohee.modeldao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.boohee.one.pedometer.StepModel;
import com.boohee.utils.DateHelper;

import java.util.ArrayList;
import java.util.List;

public class StepCounterDao extends ModelDaoBase {
    public static final  String DISTANCE   = "distance";
    public static final  String RECORD_ON  = "record_on";
    public static final  String STEP       = "step";
    private static final String TABLE_NAME = "steps";
    final                int    DAYS_COUNT = 6;

    public StepCounterDao(Context ctx) {
        super(ctx);
    }

    public StepModel queryStep(String date) {
        StepModel step = null;
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        Cursor cursor = this.db.rawQuery("select * from steps where record_on = ?", new
                String[]{date});
        if (cursor.moveToFirst()) {
            step = selectWithCursor(cursor);
        }
        cursor.close();
        return step;
    }

    public List<StepModel> getAllSteps() {
        List<StepModel> stepsList = new ArrayList();
        Cursor cursor = this.db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                stepsList.add(selectWithCursor(cursor));
            }
            cursor.close();
        }
        return stepsList;
    }

    public List<StepModel> getWeekSteps() {
        List<StepModel> steps = new ArrayList();
        String[] selectionArgs = new String[]{DateHelper.previousDay(DateHelper.yesterday(), -6),
                DateHelper.yesterday()};
        Cursor cursor = this.db.query(TABLE_NAME, null, "record_on between ? and ?",
                selectionArgs, null, null, "record_on");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                StepModel stepModel = selectWithCursor(cursor);
                if (stepModel != null && stepModel.step > 0) {
                    steps.add(stepModel);
                }
            }
            cursor.close();
        }
        return steps;
    }

    public void deleteWeekSteps() {
        String[] selectionArgs = new String[]{DateHelper.previousDay(DateHelper.yesterday(), -6),
                DateHelper.yesterday()};
        this.db.delete(TABLE_NAME, "record_on between ? and ?", selectionArgs);
    }

    public StepModel selectWithCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        String record_on = cursor.getString(cursor.getColumnIndex("record_on"));
        int step = cursor.getInt(cursor.getColumnIndex("step"));
        int distance = cursor.getInt(cursor.getColumnIndex(DISTANCE));
        StepModel stepModel = new StepModel();
        stepModel.record_on = record_on;
        stepModel.step = step;
        return stepModel;
    }

    public boolean update(StepModel step) {
        if (this.db.update(TABLE_NAME, getValues(step), "record_on = ?", new String[]{step
                .record_on}) > -1) {
            return true;
        }
        return false;
    }

    public boolean add(StepModel step) {
        if (TextUtils.isEmpty(step.record_on)) {
            return false;
        }
        if (queryStep(step.record_on) != null) {
            return update(step);
        }
        if (this.db.insert(TABLE_NAME, null, getValues(step)) > -1) {
            return true;
        }
        return false;
    }

    public ContentValues getValues(StepModel step) {
        ContentValues values = new ContentValues();
        values.put("record_on", step.record_on);
        values.put("step", Integer.valueOf(step.step));
        values.put(DISTANCE, Integer.valueOf(step.distance));
        return values;
    }
}
