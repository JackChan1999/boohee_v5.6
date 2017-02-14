package com.boohee.modeldao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boohee.database.BHDBHelper;

public class BHDaoBase {
    public static final String  ID  = "id";
    protected           Context ctx = null;
    protected SQLiteDatabase db;

    public BHDaoBase(Context ctx) {
        this.ctx = ctx;
        if (this.db == null) {
            this.db = new BHDBHelper(ctx).getReadableDatabase();
        }
    }

    protected Object selectWithCursor(Cursor cursor) {
        return null;
    }

    public void closeDB() {
        if (this.db != null && this.db.isOpen()) {
            this.db.close();
        }
    }

    protected String makePlaceholders(int len) {
        if (len < 1) {
            throw new RuntimeException("No placeholders");
        }
        StringBuilder sb = new StringBuilder((len * 2) - 1);
        sb.append("?");
        for (int i = 1; i < len; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }
}
