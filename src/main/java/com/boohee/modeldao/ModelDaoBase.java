package com.boohee.modeldao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boohee.database.DBHelper;
import com.boohee.database.UserPreference;
import com.boohee.utils.Helper;

public class ModelDaoBase {
    public static final String  ID                = "_id";
    public static final String  REMOTE_CREATED_AT = "remote_created_at";
    public static final String  REMOTE_ID         = "remote_id";
    public static final String  REMOTE_UPDATED_AT = "remote_updated_at";
    static final        String  TAG               = ModelDaoBase.class.getName();
    public static final String  UPDATED_AT        = "updated_at";
    public static final String  USER_KEY          = "user_key";
    protected           Context ctx               = null;
    protected SQLiteDatabase db;
    protected String         user_key;

    public ModelDaoBase(Context ctx) {
        this.ctx = ctx;
        if (this.db == null || !this.db.isOpen()) {
            this.db = DBHelper.getInstance(ctx).getWritableDatabase();
        }
        this.user_key = UserPreference.getUserKey(ctx);
    }

    protected Object selectWithCursor(Cursor cursor) {
        return null;
    }

    public void closeDB() {
        if (this.db != null && this.db.isOpen()) {
            Helper.showLog(TAG, "close db");
            this.db.close();
        }
    }

    public int getUnregisterRecordCount() {
        if (this.db == null) {
            return 0;
        }
        int count = 0;
        Cursor weightCursor = this.db.query(WeightRecordDao.TABLE_NAME, new String[]{"count(*)"},
                "user_key is null", null, null, null, null);
        if (weightCursor != null && weightCursor.moveToFirst()) {
            count = 0 + weightCursor.getInt(0);
            weightCursor.close();
        }
        Helper.showLog(TAG, "getUnregisterRecordCount:" + count);
        return count;
    }

    public void importData(String user_key) {
        if (this.db != null) {
            ContentValues cValues = new ContentValues();
            cValues.put("user_key", user_key);
            this.db.beginTransaction();
            try {
                this.db.execSQL("insert into record_logs(user_key, local_id, model_name, " +
                        "exec_type) select ?, _id, 'eating', 'update' from food_records where " +
                        "user_key is null;", new String[]{user_key});
                this.db.execSQL("insert into record_logs(user_key, local_id, model_name, " +
                        "exec_type) select ?, _id, 'activity', 'update' from sport_records where " +
                        "user_key is null;", new String[]{user_key});
                this.db.execSQL("insert into record_logs(user_key, local_id, model_name, " +
                        "exec_type) select ?, _id, 'weight', 'update' from weight_records where " +
                        "user_key is null;", new String[]{user_key});
                this.db.update(WeightRecordDao.TABLE_NAME, cValues, "user_key is null", null);
                this.db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.db.endTransaction();
            }
        }
    }
}
