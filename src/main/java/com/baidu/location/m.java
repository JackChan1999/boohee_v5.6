package com.baidu.location;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.baidu.location.a.a;

class m extends SQLiteOpenHelper {
    private static m a = null;
    private static final String do = "bd_geofence.db";
    private static final int if = 2;

    public m(Context context) {
        super(context, do, null, 2);
    }

    public static synchronized m a(Context context) {
        m mVar;
        synchronized (m.class) {
            if (a == null) {
                a = new m(context);
            }
            mVar = a;
        }
        return mVar;
    }

    private String a(String str, String str2, String str3) {
        return "ALTER TABLE " + str + " ADD " + str2 + " " + str3;
    }

    private void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE  IF NOT EXISTS geofence (_id INTEGER PRIMARY KEY AUTOINCREMENT,geofence_id NTEXT,longitude NTEXT,latitude NTEXT,radius_type INTEGER,radius NTEXT,valid_date INTEGER,duration_millis INTEGER,coord_type NTEXT,next_active_time INTEGER,is_lac INTEGER,is_cell INTEGER,is_wifi INTEGER,next_exit_active_time INTEGER);");
        sQLiteDatabase.execSQL("CREATE TABLE  IF NOT EXISTS geofence_detail (_id INTEGER PRIMARY KEY AUTOINCREMENT,geofence_id NTEXT,ap NTEXT,ap_backup NTEXT);");
        sQLiteDatabase.execSQL("CREATE INDEX  IF NOT EXISTS ap_index ON geofence_detail (ap);");
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        a(sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i < 2) {
            a(a.goto, a.a, "INTEGER");
        }
        a(sQLiteDatabase);
    }
}
