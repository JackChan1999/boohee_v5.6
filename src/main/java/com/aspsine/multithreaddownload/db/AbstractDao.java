package com.aspsine.multithreaddownload.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractDao<T> {
    private DBOpenHelper mHelper;

    public AbstractDao(Context context) {
        this.mHelper = new DBOpenHelper(context);
    }

    protected SQLiteDatabase getWritableDatabase() {
        return this.mHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return this.mHelper.getReadableDatabase();
    }

    public void close() {
        this.mHelper.close();
    }
}
