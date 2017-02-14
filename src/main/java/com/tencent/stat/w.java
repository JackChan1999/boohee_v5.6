package com.tencent.stat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.boohee.utility.Const;
import com.boohee.utils.Utils;
import com.tencent.stat.common.k;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.ArrayList;
import java.util.List;

class w extends SQLiteOpenHelper {
    public w(Context context) {
        super(context, k.v(context), null, 3);
    }

    private void a(SQLiteDatabase sQLiteDatabase) {
        Object th;
        Throwable th2;
        String str = null;
        Cursor query;
        try {
            query = sQLiteDatabase.query(Const.USER, null, null, null, null, null, null);
            try {
                ContentValues contentValues = new ContentValues();
                if (query.moveToNext()) {
                    str = query.getString(0);
                    query.getInt(1);
                    query.getString(2);
                    query.getLong(3);
                    contentValues.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, k.c(str));
                }
                if (str != null) {
                    sQLiteDatabase.update(Const.USER, contentValues, "uid=?", new String[]{str});
                }
                if (query != null) {
                    query.close();
                }
            } catch (Throwable th3) {
                th = th3;
                try {
                    n.e.e(th);
                    if (query != null) {
                        query.close();
                    }
                } catch (Throwable th4) {
                    th2 = th4;
                    if (query != null) {
                        query.close();
                    }
                    throw th2;
                }
            }
        } catch (Throwable th5) {
            th2 = th5;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th2;
        }
    }

    private void b(SQLiteDatabase sQLiteDatabase) {
        Cursor query;
        Object th;
        Cursor cursor;
        Throwable th2;
        try {
            query = sQLiteDatabase.query("events", null, null, null, null, null, null);
            try {
                List<x> arrayList = new ArrayList();
                while (query.moveToNext()) {
                    arrayList.add(new x(query.getLong(0), query.getString(1), query.getInt(2),
                            query.getInt(3)));
                }
                ContentValues contentValues = new ContentValues();
                for (x xVar : arrayList) {
                    contentValues.put(Utils.RESPONSE_CONTENT, k.c(xVar.b));
                    sQLiteDatabase.update("events", contentValues, "event_id=?", new
                            String[]{Long.toString(xVar.a)});
                }
                if (query != null) {
                    query.close();
                }
            } catch (Throwable th3) {
                th2 = th3;
                if (query != null) {
                    query.close();
                }
                throw th2;
            }
        } catch (Throwable th4) {
            th2 = th4;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th2;
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists events(event_id INTEGER PRIMARY KEY " +
                "AUTOINCREMENT NOT NULL, content TEXT, status INTEGER, send_count INTEGER, " +
                "timestamp LONG)");
        sQLiteDatabase.execSQL("create table if not exists user(uid TEXT PRIMARY KEY, user_type " +
                "INTEGER, app_ver TEXT, ts INTEGER)");
        sQLiteDatabase.execSQL("create table if not exists config(type INTEGER PRIMARY KEY NOT " +
                "NULL, content TEXT, md5sum TEXT, version INTEGER)");
        sQLiteDatabase.execSQL("create table if not exists keyvalues(key TEXT PRIMARY KEY NOT " +
                "NULL, value TEXT)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        n.e.debug("upgrade DB from oldVersion " + i + " to newVersion " + i2);
        if (i == 1) {
            sQLiteDatabase.execSQL("create table if not exists keyvalues(key TEXT PRIMARY KEY NOT" +
                    " NULL, value TEXT)");
            a(sQLiteDatabase);
            b(sQLiteDatabase);
        }
        if (i == 2) {
            a(sQLiteDatabase);
            b(sQLiteDatabase);
        }
    }
}
