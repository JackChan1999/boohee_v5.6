package com.meiqia.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.boohee.modeldao.UserDao;
import com.boohee.utils.Utils;
import com.meiqia.core.b.e;
import com.meiqia.core.b.g;
import com.meiqia.core.b.h;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class bc {
    private bb a;
    private h  b;
    private AtomicInteger c = new AtomicInteger();
    private SQLiteDatabase d;

    public bc(Context context) {
        this.a = new bb(context);
        this.b = new h(context);
    }

    private MQMessage a(Cursor cursor) {
        MQMessage mQMessage = new MQMessage();
        mQMessage.setId(cursor.getLong(cursor.getColumnIndex("id")));
        mQMessage.setAgent_id(cursor.getString(cursor.getColumnIndex("agent_id")));
        mQMessage.setContent(cursor.getString(cursor.getColumnIndex(Utils.RESPONSE_CONTENT)));
        mQMessage.setContent_type(cursor.getString(cursor.getColumnIndex("content_type")));
        mQMessage.setTrack_id(cursor.getString(cursor.getColumnIndex("track_id")));
        mQMessage.setAgent_nickname(cursor.getString(cursor.getColumnIndex("agent_nickname")));
        mQMessage.setConversation_id((long) cursor.getInt(cursor.getColumnIndex
                ("conversation_id")));
        mQMessage.setCreated_on(cursor.getLong(cursor.getColumnIndex("created_on")));
        mQMessage.setEnterprise_id((long) cursor.getInt(cursor.getColumnIndex("enterprise_id")));
        mQMessage.setFrom_type(cursor.getString(cursor.getColumnIndex("from_type")));
        mQMessage.setStatus(cursor.getString(cursor.getColumnIndex("status")));
        mQMessage.setType(cursor.getString(cursor.getColumnIndex("type")));
        mQMessage.setAvatar(cursor.getString(cursor.getColumnIndex(UserDao.AVATAR)));
        mQMessage.setMedia_url(cursor.getString(cursor.getColumnIndex("media_url")));
        mQMessage.setExtra(cursor.getString(cursor.getColumnIndex("extra")));
        mQMessage.setIs_read(cursor.getInt(cursor.getColumnIndex("isRead")) != 0);
        return mQMessage;
    }

    private void a(SQLiteDatabase sQLiteDatabase, MQMessage mQMessage) {
        ContentValues contentValues = new ContentValues();
        a(mQMessage, contentValues);
        sQLiteDatabase.insert("mq_message", null, contentValues);
    }

    private void a(MQMessage mQMessage, ContentValues contentValues) {
        contentValues.put("id", Long.valueOf(mQMessage.getId()));
        contentValues.put("agent_id", mQMessage.getAgent_id());
        contentValues.put(Utils.RESPONSE_CONTENT, mQMessage.getContent());
        contentValues.put("content_type", mQMessage.getContent_type());
        contentValues.put("conversation_id", Long.valueOf(mQMessage.getConversation_id()));
        contentValues.put("created_on", Long.valueOf(mQMessage.getCreated_on()));
        contentValues.put("enterprise_id", Long.valueOf(mQMessage.getEnterprise_id()));
        contentValues.put("from_type", mQMessage.getFrom_type());
        contentValues.put("track_id", mQMessage.getTrack_id());
        contentValues.put("type", mQMessage.getType());
        contentValues.put(UserDao.AVATAR, mQMessage.getAvatar());
        contentValues.put("isRead", Boolean.valueOf(mQMessage.is_read()));
        contentValues.put("status", mQMessage.getStatus());
        contentValues.put("agent_nickname", mQMessage.getAgent_nickname());
        contentValues.put("media_url", mQMessage.getMedia_url());
        contentValues.put("extra", mQMessage.getExtra());
    }

    private void b(SQLiteDatabase sQLiteDatabase, MQMessage mQMessage) {
        if (d(sQLiteDatabase, mQMessage)) {
            c(sQLiteDatabase, mQMessage);
        } else {
            a(sQLiteDatabase, mQMessage);
        }
    }

    private synchronized SQLiteDatabase c() {
        if (this.c.incrementAndGet() == 1) {
            this.d = this.a.getWritableDatabase();
        }
        return this.d;
    }

    private void c(SQLiteDatabase sQLiteDatabase, MQMessage mQMessage) {
        try {
            String[] strArr = new String[]{mQMessage.getId() + ""};
            ContentValues contentValues = new ContentValues();
            a(mQMessage, contentValues);
            sQLiteDatabase.update("mq_message", contentValues, "id=?", strArr);
        } catch (Exception e) {
            Log.d("meiqia", "updateMessage(SQLiteDatabase db, MQMessage message) error");
        }
    }

    private synchronized void d() {
        if (this.c.decrementAndGet() == 0 && this.d.isOpen()) {
            this.d.close();
        }
    }

    private boolean d(SQLiteDatabase sQLiteDatabase, MQMessage mQMessage) {
        boolean z = true;
        String str = "SELECT * FROM " + b() + " WHERE " + "id" + "=?";
        Cursor cursor = null;
        try {
            cursor = sQLiteDatabase.rawQuery(str, new String[]{mQMessage.getId() + ""});
            if (cursor == null || !cursor.moveToFirst()) {
                z = false;
            }
            if (cursor == null) {
                return z;
            }
            cursor.close();
            return z;
        } catch (Exception e) {
            e.a("findMessage() : " + e.toString());
            if (cursor == null) {
                return false;
            }
            cursor.close();
            return false;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void a() {
        try {
            c().execSQL("DELETE FROM mq_message");
        } catch (Exception e) {
            Log.d("meiqia", "deleteAllMessage error");
        } finally {
            d();
        }
    }

    public void a(long j) {
        try {
            String str = "mq_message";
            c().delete(str, "id=?", new String[]{j + ""});
        } catch (Exception e) {
            Log.d("meiqia", "deleteMessage error");
        } finally {
            d();
        }
    }

    public void a(long j, int i, OnGetMessageListCallback onGetMessageListCallback) {
        Cursor cursor = null;
        List arrayList = new ArrayList();
        try {
            cursor = c().rawQuery("select * from " + b() + " where " + "created_on" + " < " + j +
                    " and " + "track_id" + " = '" + b.a.getTrackId() + "' order by " +
                    "created_on" + " DESC" + " limit " + i, null);
            while (cursor.moveToNext()) {
                MQMessage a = a(cursor);
                if ("sending".equals(a.getStatus())) {
                    a.setStatus("failed");
                }
                arrayList.add(a);
            }
            Collections.sort(arrayList, new g());
            onGetMessageListCallback.onSuccess(arrayList);
        } catch (Exception e) {
            e.a("getMessageList(String id, int length) :" + e.toString());
            onGetMessageListCallback.onFailure(0, "");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            d();
        }
    }

    public void a(MQMessage mQMessage) {
        try {
            b(c(), mQMessage);
        } catch (Exception e) {
            Log.d("meiqia", "updateOrSaveMessage(MQMessage message) error");
        } finally {
            d();
        }
    }

    public void a(MQMessage mQMessage, long j) {
        SQLiteDatabase c = c();
        try {
            String[] strArr = new String[]{j + ""};
            ContentValues contentValues = new ContentValues();
            a(mQMessage, contentValues);
            c.update("mq_message", contentValues, "id=?", strArr);
        } catch (Exception e) {
            Log.d("meiqia", "updateMessageId error");
        } finally {
            d();
        }
    }

    public void a(List<MQMessage> list) {
        SQLiteDatabase c = c();
        c.beginTransaction();
        try {
            for (MQMessage mQMessage : list) {
                if (!d(c, mQMessage)) {
                    a(c, mQMessage);
                }
            }
            c.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("meiqia", "saveMessageList error");
        } finally {
            c.endTransaction();
            d();
        }
    }

    public MQMessage b(long j) {
        MQMessage mQMessage;
        Exception e;
        Throwable th;
        Cursor rawQuery;
        try {
            rawQuery = c().rawQuery("select * from " + b() + " where " + "id" + " = " + j, null);
            mQMessage = null;
            while (rawQuery.moveToNext()) {
                try {
                    mQMessage = a(rawQuery);
                } catch (Exception e2) {
                    e = e2;
                }
            }
            if (rawQuery != null) {
                rawQuery.close();
            }
            d();
        } catch (Exception e3) {
            rawQuery = null;
            Exception exception = e3;
            mQMessage = null;
            e = exception;
            try {
                e.a("getMessageList(String id, int length) : " + e.toString());
                if (rawQuery != null) {
                    rawQuery.close();
                }
                d();
                return mQMessage;
            } catch (Throwable th2) {
                th = th2;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                d();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            rawQuery = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            d();
            throw th;
        }
        return mQMessage;
    }

    public String b() {
        return "mq_message";
    }

    public void b(List<MQMessage> list) {
        SQLiteDatabase c = c();
        c.beginTransaction();
        try {
            for (MQMessage b : list) {
                b(c, b);
            }
            c.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("meiqia", "saveMessageList error");
        } finally {
            c.endTransaction();
            d();
        }
    }
}
