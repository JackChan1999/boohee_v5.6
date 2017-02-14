package com.boohee.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BHDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME    = "bhdb.sqlite";
    private static final int    DB_VERSION = 1;
    static final         String TAG        = BHDBHelper.class.getName();

    public BHDBHelper(Context context) {
        super(context, "bhdb.sqlite", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
