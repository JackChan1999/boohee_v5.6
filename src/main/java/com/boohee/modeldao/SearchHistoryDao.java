package com.boohee.modeldao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.boohee.model.SearchHistory;
import com.boohee.utils.DateHelper;

import java.util.Date;

public class SearchHistoryDao extends ModelDaoBase {
    public static final  String CREATE_AT  = "created_at";
    public static final  String NAME       = "name";
    private static final String TABLE_NAME = "search_histories";
    static final         String TAG        = SearchHistoryDao.class.getName();

    public SearchHistoryDao(Context ctx) {
        super(ctx);
    }

    public boolean add(String name) {
        ContentValues cv;
        if (select(name) != null) {
            cv = new ContentValues();
            cv.put("created_at", DateHelper.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            if (this.db.update(TABLE_NAME, cv, "name = ?", new String[]{name}) > -1) {
                return true;
            }
            return false;
        }
        boolean is_insert;
        cv = new ContentValues();
        cv.put("name", name);
        cv.put("created_at", DateHelper.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        if (this.db.insert(TABLE_NAME, null, cv) > -1) {
            is_insert = true;
        } else {
            is_insert = false;
        }
        return is_insert;
    }

    public Cursor selectHistory() {
        return this.db.rawQuery("select * from search_histories order by created_at desc", null);
    }

    public SearchHistory selectWithCursor(Cursor cursor) {
        return new SearchHistory(cursor.getString(cursor.getColumnIndex("name")), DateHelper
                .parseFromString(cursor.getString(cursor.getColumnIndex("created_at")),
                        "yyyy-MM-dd HH:mm:ss"));
    }

    public SearchHistory select(String name) {
        SearchHistory history = null;
        Cursor c = this.db.rawQuery("select * from search_histories where name = ? order by " +
                "created_at desc", new String[]{name});
        if (c != null && c.moveToFirst()) {
            history = selectWithCursor(c);
        }
        c.close();
        return history;
    }

    public boolean clear() {
        return this.db.delete(TABLE_NAME, null, null) > 0;
    }
}
