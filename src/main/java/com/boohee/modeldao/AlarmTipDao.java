package com.boohee.modeldao;

import android.content.Context;
import android.database.Cursor;

import com.boohee.model.AlarmTip;
import com.boohee.utils.DateHelper;

import java.util.ArrayList;
import java.util.Date;

public class AlarmTipDao extends BHDaoBase {
    public static final  Date   BASE_DATE  = DateHelper.parseString("2013-05-01");
    public static final  String CODE       = "code";
    public static final  String MESSAGE    = "message";
    public static final  String NAME       = "name";
    private static final String TABLE_NAME = "alarm_tips";

    public AlarmTipDao(Context ctx) {
        super(ctx);
    }

    public AlarmTip selectWithCode(String code) {
        return (AlarmTip) getList(code).get(DateHelper.between(BASE_DATE) % getCount(code));
    }

    public ArrayList<AlarmTip> getList(String code) {
        ArrayList<AlarmTip> list = new ArrayList();
        Cursor cursor = this.db.query(TABLE_NAME, null, "code = ?", new String[]{code}, null,
                null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(selectWithCursor(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public int getCount(String code) {
        int count = 0;
        Cursor cursor = this.db.rawQuery("select count(*) from alarm_tips where code = ?", new
                String[]{code});
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public AlarmTip selectWithCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        return new AlarmTip(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor
                .getColumnIndex("message")), cursor.getString(cursor.getColumnIndex("code")),
                cursor.getString(cursor.getColumnIndex("name")));
    }

    public AlarmTip getRandomTip() {
        Cursor cursor = this.db.rawQuery("SELECT * FROM alarm_tips ORDER BY RANDOM() LIMIT 1;",
                null);
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        return selectWithCursor(cursor);
    }
}
