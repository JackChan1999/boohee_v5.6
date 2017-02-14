package com.aspsine.multithreaddownload.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.boohee.one.sport.DownloadService;
import java.util.ArrayList;
import java.util.List;

public class ThreadInfoDao extends AbstractDao<ThreadInfo> {
    private static final String TABLE_NAME = ThreadInfo.class.getSimpleName();

    public ThreadInfoDao(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(_id integer primary key autoincrement, id integer, tag text, uri text, start long, end long, finished long)");
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
    }

    public void insert(ThreadInfo info) {
        getWritableDatabase().execSQL("insert into " + TABLE_NAME + "(id, tag, uri, start, end, finished) values(?, ?, ?, ?, ?, ?)", new Object[]{Integer.valueOf(info.getId()), info.getTag(), info.getUri(), Long.valueOf(info.getStart()), Long.valueOf(info.getEnd()), Long.valueOf(info.getFinished())});
    }

    public void delete(String tag) {
        getWritableDatabase().execSQL("delete from " + TABLE_NAME + " where tag = ?", new Object[]{tag});
    }

    public void update(String tag, int threadId, long finished) {
        getWritableDatabase().execSQL("update " + TABLE_NAME + " set finished = ?" + " where tag = ? and id = ? ", new Object[]{Long.valueOf(finished), tag, Integer.valueOf(threadId)});
    }

    public List<ThreadInfo> getThreadInfos(String tag) {
        List<ThreadInfo> list = new ArrayList();
        Cursor cursor = getReadableDatabase().rawQuery("select * from " + TABLE_NAME + " where tag = ?", new String[]{tag});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ThreadInfo info = new ThreadInfo();
                info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                info.setTag(cursor.getString(cursor.getColumnIndex(DownloadService.EXTRA_TAG)));
                info.setUri(cursor.getString(cursor.getColumnIndex("uri")));
                info.setEnd(cursor.getLong(cursor.getColumnIndex("end")));
                info.setStart(cursor.getLong(cursor.getColumnIndex("start")));
                info.setFinished(cursor.getLong(cursor.getColumnIndex("finished")));
                list.add(info);
            }
            cursor.close();
        }
        return list;
    }

    public boolean exists(String tag, int threadId) {
        Cursor cursor = getReadableDatabase().rawQuery("select * from " + TABLE_NAME + " where tag = ? and id = ?", new String[]{tag, threadId + ""});
        if (cursor == null) {
            return false;
        }
        boolean isExists = cursor.moveToNext();
        cursor.close();
        return isExists;
    }
}
