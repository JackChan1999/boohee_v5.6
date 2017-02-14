package com.zxinsight.common.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zxinsight.common.util.c;

public class b {
    private static b b = null;
    private        a a = null;

    private b(Context context) {
        if (context == null) {
            c.a("create db fail, context is null!");
        } else {
            this.a = new a(context.getApplicationContext(), "mwsdk_analytics.db");
        }
    }

    public static synchronized b a(Context context) {
        b bVar;
        synchronized (b.class) {
            if (b == null) {
                b = new b(context.getApplicationContext());
            }
            bVar = b;
        }
        return bVar;
    }

    public void a(String str) {
        if (this.a == null) {
            c.a("create db fail, lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
            return;
        }
        try {
            SQLiteDatabase writableDatabase = this.a.getWritableDatabase();
            c.c("excSQL : " + str);
            writableDatabase.execSQL(str);
        } catch (Exception e) {
            c.a("when query database occur error :" + str + e.getMessage());
        }
    }

    public Cursor a(String str, String[] strArr, String str2, String[] strArr2, String str3,
                    String str4) {
        if (this.a == null) {
            c.a("create db fail, lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
            return null;
        }
        try {
            SQLiteDatabase writableDatabase = this.a.getWritableDatabase();
            c.c("Query table: " + str + "Query table");
            return writableDatabase.query(str, strArr, str2, strArr2, null, null, str3, str4);
        } catch (Exception e) {
            c.a("when query database occur error table:" + str + e.getMessage());
            return null;
        }
    }

    public long a(String str, ContentValues contentValues) {
        long j = -1;
        if (this.a == null) {
            c.a("create db fail, lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
        } else {
            try {
                SQLiteDatabase writableDatabase = this.a.getWritableDatabase();
                c.b("insert database, insert");
                j = writableDatabase.replace(str, null, contentValues);
            } catch (Exception e) {
                c.a("when insert database occur error table:" + str + e.getMessage());
            }
        }
        return j;
    }

    public int a(String str, String str2, String[] strArr) {
        int i = 0;
        if (this.a == null) {
            c.a("create db fail, lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
        } else {
            try {
                i = this.a.getWritableDatabase().delete(str, str2, strArr);
                c.c("Deleted " + str + " rows from table: " + i + "delete");
            } catch (Exception e) {
                c.a("when delete database occur error table:" + str + e.getMessage());
            }
        }
        return i;
    }

    public int b(String str) {
        Cursor cursor = null;
        int i = 0;
        if (this.a == null) {
            c.a("create db fail, lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
        } else {
            try {
                cursor = this.a.getWritableDatabase().rawQuery("select count(*) from " + str, null);
                if (cursor.moveToNext()) {
                    i = cursor.getInt(0);
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                c.a("getCount" + e.getMessage());
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return i;
    }
}
