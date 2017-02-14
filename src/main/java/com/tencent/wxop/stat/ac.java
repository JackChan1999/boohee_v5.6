package com.tencent.wxop.stat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.boohee.utility.Const;
import com.boohee.utils.Utils;
import com.tencent.wxop.stat.b.r;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.ArrayList;
import java.util.List;

final class ac extends SQLiteOpenHelper {
    private String  a  = "";
    private Context co = null;

    public ac(Context context, String str) {
        super(context, str, null, 3);
        this.a = str;
        this.co = context.getApplicationContext();
        if (c.k()) {
            t.bZ.b("SQLiteOpenHelper " + this.a);
        }
    }

    private static void a(SQLiteDatabase sQLiteDatabase) {
        Cursor query;
        Throwable th;
        String str = null;
        try {
            query = sQLiteDatabase.query(Const.USER, null, null, null, null, null, null);
            try {
                ContentValues contentValues = new ContentValues();
                if (query.moveToNext()) {
                    str = query.getString(0);
                    query.getInt(1);
                    query.getString(2);
                    query.getLong(3);
                    contentValues.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, r.q(str));
                }
                if (str != null) {
                    sQLiteDatabase.update(Const.USER, contentValues, "uid=?", new String[]{str});
                }
                if (query != null) {
                    query.close();
                }
            } catch (Throwable th2) {
                th = th2;
                try {
                    t.bZ.b(th);
                    if (query != null) {
                        query.close();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
        } catch (Throwable th4) {
            th = th4;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    private static void b(SQLiteDatabase sQLiteDatabase) {
        Throwable th;
        Cursor cursor;
        Cursor query;
        try {
            query = sQLiteDatabase.query("events", null, null, null, null, null, null);
            try {
                List<ad> arrayList = new ArrayList();
                while (query.moveToNext()) {
                    arrayList.add(new ad(query.getLong(0), query.getString(1), query.getInt(2),
                            query.getInt(3)));
                }
                ContentValues contentValues = new ContentValues();
                for (ad adVar : arrayList) {
                    contentValues.put(Utils.RESPONSE_CONTENT, r.q(adVar.b));
                    sQLiteDatabase.update("events", contentValues, "event_id=?", new
                            String[]{Long.toString(adVar.K)});
                }
                if (query != null) {
                    query.close();
                }
            } catch (Throwable th2) {
                th = th2;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public final synchronized void close() {
        super.close();
    }

    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists events(event_id INTEGER PRIMARY KEY " +
                "AUTOINCREMENT NOT NULL, content TEXT, status INTEGER, send_count INTEGER, " +
                "timestamp LONG)");
        sQLiteDatabase.execSQL("create table if not exists user(uid TEXT PRIMARY KEY, user_type " +
                "INTEGER, app_ver TEXT, ts INTEGER)");
        sQLiteDatabase.execSQL("create table if not exists config(type INTEGER PRIMARY KEY NOT " +
                "NULL, content TEXT, md5sum TEXT, version INTEGER)");
        sQLiteDatabase.execSQL("create table if not exists keyvalues(key TEXT PRIMARY KEY NOT " +
                "NULL, value TEXT)");
        sQLiteDatabase.execSQL("CREATE INDEX if not exists status_idx ON events(status)");
    }

    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        t.bZ.debug("upgrade DB from oldVersion " + i + " to newVersion " + i2);
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
