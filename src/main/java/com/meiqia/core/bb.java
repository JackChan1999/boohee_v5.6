package com.meiqia.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class bb extends SQLiteOpenHelper {
    public bb(Context context) {
        super(context, "meiqia.db", null, 5);
    }

    public void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS mq_message(id INTEGER PRIMARY KEY," +
                "agent_id INTEGER,content TEXT,content_type TEXT,conversation_id INTEGER," +
                "created_on INTEGER,enterprise_id INTEGER,from_type TEXT,track_id TEXT,avatar " +
                "TEXT,status TEXT,agent_nickname TEXT,media_url TEXT,isRead INTEGER,type TEXT," +
                "extra TEXT)");
    }

    public void b(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS mq_conversation(id INTEGER PRIMARY " +
                "KEY,agent_id INTEGER,created_on TEXT,ended_by TEXT,ended_on TEXT,enterprise_id " +
                "INTEGER,last_msg_content TEXT,last_updated TEXT,track_id TEXT,visit_id TEXT," +
                "last_msg_created_on TEXT)");
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        a(sQLiteDatabase);
        b(sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        switch (i) {
            case 4:
                sQLiteDatabase.execSQL("ALTER TABLE mq_message ADD COLUMN extra");
                return;
            default:
                return;
        }
    }
}
