package com.boohee.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.boohee.one.MyApplication;
import com.boohee.push.PushManager;
import com.boohee.utils.Helper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME    = "food.db";
    private static final int    DB_VERSION = 21;
    static final         String TAG        = DBHelper.class.getName();
    private static DBHelper helper;
    private final String ALERT_USER_AVATAR_COLUMN              = "ALTER TABLE users ADD COLUMN " +
            "avatar varchar(255);";
    private final String ALERT_USER_BADGE_COLUMN               = "ALTER TABLE users ADD COLUMN " +
            "badges_count int(11);";
    private final String ALERT_USER_BEGIN_DATE                 = "ALTER TABLE users ADD COLUMN " +
            "begin_date varchar(255);";
    private final String ALERT_USER_DESCRIPTION_COLUMN         = "ALTER TABLE users ADD COLUMN " +
            "description varchar(255);";
    private final String ALERT_USER_DISEASES_COLUMN            = "ALTER TABLE users ADD COLUMN " +
            "diseases varchar(255);";
    private final String ALERT_USER_ENVIOUS_COUNT_COLUMN       = "ALTER TABLE users ADD COLUMN " +
            "envious_count int(11);";
    private final String ALERT_USER_FOLLOWER_COLUMN            = "ALTER TABLE users ADD COLUMN " +
            "follower_count int(11);";
    private final String ALERT_USER_FOLLOWING_COLUMN           = "ALTER TABLE users ADD COLUMN " +
            "following_count int(11);";
    private final String ALERT_USER_LATEST_WAIST_AT_COLUMN     = "ALTER TABLE users ADD COLUMN " +
            "latest_waist_at varchar(255);";
    private final String ALERT_USER_LATEST_WAIST_COLUMN        = "ALTER TABLE users ADD COLUMN " +
            "latest_waist decimal(5,2);";
    private final String ALERT_USER_LATEST_WEIGHT_COLUMN       = "ALTER TABLE users ADD COLUMN " +
            "latest_weight decimal(5,2);";
    private final String ALERT_USER_LATEST_WEIGT_AT_COLUMN     = "ALTER TABLE users ADD COLUMN " +
            "latest_weight_at varchar(255);";
    private final String ALERT_USER_NEED_TEST_COLUMN           = "ALTER TABLE users ADD COLUMN " +
            "need_test int(11);";
    private final String ALERT_USER_POST_COUNT_COLUMN          = "ALTER TABLE users ADD COLUMN " +
            "post_count int(11);";
    private final String ALERT_USER_SPORT_CONDITION_COLUMN     = "ALTER TABLE users ADD COLUMN " +
            "sport_condition varchar(255);";
    private final String CREATE_FOOD_RECORDS                   = "CREATE TABLE if not exists " +
            "food_records (_id integer PRIMARY KEY autoincrement, food_name varchar(255), " +
            "record_on varchar(255), unit_name varchar(255), code varchar(255), amount int(11), " +
            "food_unit_id int(11), time_type int(11), calory decimal(5,2), user_key varchar(255));";
    private final String CREATE_FOOD_RECORDS_INDEX             = "CREATE UNIQUE INDEX if not " +
            "exists index_on_record_on_and_food_name_and_time_type_and_unit_name_and_user_key on " +
            "food_records (record_on, food_name, unit_name, time_type, user_key);";
    private final String CREATE_SEARCH_HISTORIES               = "CREATE TABLE if not exists " +
            "search_histories (_id integer PRIMARY KEY autoincrement, name varchar(255), " +
            "created_at datetime default CURRENT_TIMESTAMP);";
    private final String CREATE_SPORT_RECORDS                  = "CREATE TABLE if not exists " +
            "sport_records (_id integer PRIMARY KEY autoincrement, duration int(11), record_on " +
            "varchar(255), activity_id int(11), activity_name varchar(255), mets int(11), " +
            "unit_name varchar(255), calory decimal(5,2), user_key varchar(255));";
    private final String CREATE_SPORT_RECORDS_INDEX            = "CREATE UNIQUE INDEX if not " +
            "exists index_on_record_on_and_activity_id_and_user_key on sport_records (record_on, " +
            "activity_id, user_key);";
    private final String CREATE_STEPS                          = "CREATE TABLE if not exists " +
            "steps (_id integer PRIMARY KEY autoincrement, record_on varchar(255), step int(11), " +
            "distance int (11));";
    private final String CREATE_USERS                          = "CREATE TABLE if not exists " +
            "users (_id integer PRIMARY KEY autoincrement, token varchar(255), user_key varchar" +
            "(255), cellphone varchar(255), user_name varchar(255), sex_type varchar(255), " +
            "birthday varchar(255), height decimal(5,2), begin_weight decimal(5,2), target_weight" +
            " decimal(5,2), target_date datetime, target_calory int(11), avatar varchar(255), " +
            "updated_at datetime default CURRENT_TIMESTAMP, description vachar(255), " +
            "latest_weight decimal(5,2), latest_weight_at varchar(255), latest_waist decimal(5,2)" +
            ", latest_waist_at varchar(255), post_count int(11), envious_count int(11), " +
            "following_count int(11), follower_count int(11), sport_condition varchar (255), " +
            "diseases varchar(255), need_test int(11), badges_count int(11), begin_date varchar" +
            "(255));";
    private final String CREATE_WEIGHT_RECORDS                 = "CREATE TABLE if not exists " +
            "weight_records (_id integer PRIMARY KEY autoincrement, weight varchar(255), " +
            "record_on varchar(255), photos varchar(255), created_at datetime default " +
            "CURRENT_TIMESTAMP);";
    private final String CREATE_WEIGHT_RECORDS_INDEX           = "CREATE UNIQUE INDEX if not " +
            "exists record_on_index on weight_records (record_on);";
    private final String DROP_TABLE_CART_GOODS                 = "DROP TABLE if exists cart_goods;";
    private final String DROP_TABLE_COMMON_FOODS               = "DROP TABLE if exists " +
            "common_foods;";
    private final String DROP_TABLE_COMMON_SOPRTS              = "DROP TABLE if exists " +
            "common_sports;";
    private final String DROP_TABLE_FAVOUR_FOODS               = "DROP TABLE if exists " +
            "favour_foods;";
    private final String DROP_TABLE_FOOD_RECORDS               = "DROP TABLE if exists " +
            "food_records;";
    private final String DROP_TABLE_RECORD_LOGS                = "DROP TABLE if exists " +
            "record_logs;";
    private final String DROP_TABLE_SPORT_RECORDS              = "DROP TABLE if exists " +
            "sport_records;";
    private final String DROP_TABLE_SYNC_STATUS                = "DROP TABLE if exists " +
            "sync_status;";
    private final String DROP_TABLE_USER_READ_MIN_RECORD_AT    = "DROP TABLE if exists " +
            "user_read_min_record_at;";
    private final String DROP_TABLE_USER_RECORD_EARLIEST_DATES = "DROP TABLE if exists " +
            "user_record_earliest_dates;";
    private final String DROP_TABLE_WEIGHT_RECORDS             = "DROP TABLE if exists " +
            "weight_records;";

    public static synchronized DBHelper getInstance(Context context) {
        DBHelper dBHelper;
        synchronized (DBHelper.class) {
            if (helper == null) {
                helper = new DBHelper(context);
            }
            dBHelper = helper;
        }
        return dBHelper;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, 21);
    }

    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("CREATE TABLE if not exists users (_id integer PRIMARY KEY autoincrement, " +
                    "token varchar(255), user_key varchar(255), cellphone varchar(255), user_name" +
                    " varchar(255), sex_type varchar(255), birthday varchar(255), height decimal" +
                    "(5,2), begin_weight decimal(5,2), target_weight decimal(5,2), target_date " +
                    "datetime, target_calory int(11), avatar varchar(255), updated_at datetime " +
                    "default CURRENT_TIMESTAMP, description vachar(255), latest_weight decimal(5," +
                    "2), latest_weight_at varchar(255), latest_waist decimal(5,2), " +
                    "latest_waist_at varchar(255), post_count int(11), envious_count int(11), " +
                    "following_count int(11), follower_count int(11), sport_condition varchar " +
                    "(255), diseases varchar(255), need_test int(11), badges_count int(11), " +
                    "begin_date varchar(255));");
            db.execSQL("CREATE TABLE if not exists search_histories (_id integer PRIMARY KEY " +
                    "autoincrement, name varchar(255), created_at datetime default " +
                    "CURRENT_TIMESTAMP);");
            db.execSQL("CREATE TABLE if not exists weight_records (_id integer PRIMARY KEY " +
                    "autoincrement, weight varchar(255), record_on varchar(255), photos varchar" +
                    "(255), created_at datetime default CURRENT_TIMESTAMP);");
            db.execSQL("CREATE UNIQUE INDEX if not exists record_on_index on weight_records " +
                    "(record_on);");
            db.execSQL("CREATE TABLE if not exists food_records (_id integer PRIMARY KEY " +
                    "autoincrement, food_name varchar(255), record_on varchar(255), unit_name " +
                    "varchar(255), code varchar(255), amount int(11), food_unit_id int(11), " +
                    "time_type int(11), calory decimal(5,2), user_key varchar(255));");
            db.execSQL("CREATE UNIQUE INDEX if not exists " +
                    "index_on_record_on_and_food_name_and_time_type_and_unit_name_and_user_key on" +
                    " food_records (record_on, food_name, unit_name, time_type, user_key);");
            db.execSQL("CREATE TABLE if not exists sport_records (_id integer PRIMARY KEY " +
                    "autoincrement, duration int(11), record_on varchar(255), activity_id int(11)" +
                    ", activity_name varchar(255), mets int(11), unit_name varchar(255), calory " +
                    "decimal(5,2), user_key varchar(255));");
            db.execSQL("CREATE UNIQUE INDEX if not exists " +
                    "index_on_record_on_and_activity_id_and_user_key on sport_records (record_on," +
                    " activity_id, user_key);");
            db.execSQL("CREATE TABLE if not exists steps (_id integer PRIMARY KEY autoincrement, " +
                    "record_on varchar(255), step int(11), distance int (11));");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case 11:
                    UserPreference.getInstance(MyApplication.getContext()).remove(PushManager
                            .KEY_IS_BINDED);
                    break;
                case 13:
                    db.beginTransaction();
                    try {
                        db.execSQL("DROP TABLE if exists food_records;");
                        db.execSQL("DROP TABLE if exists weight_records;");
                        db.execSQL("DROP TABLE if exists sport_records;");
                        db.execSQL("DROP TABLE if exists record_logs;");
                        db.execSQL("DROP TABLE if exists user_read_min_record_at;");
                        db.execSQL("DROP TABLE if exists user_record_earliest_dates;");
                        db.execSQL("DROP TABLE if exists sync_status;");
                        db.execSQL("DROP TABLE if exists common_foods;");
                        db.execSQL("DROP TABLE if exists favour_foods;");
                        db.execSQL("DROP TABLE if exists cart_goods;");
                        db.execSQL("DROP TABLE if exists common_sports;");
                        db.execSQL("CREATE TABLE if not exists weight_records (_id integer " +
                                "PRIMARY KEY autoincrement, weight varchar(255), record_on " +
                                "varchar(255), photos varchar(255), created_at datetime default " +
                                "CURRENT_TIMESTAMP);");
                        db.execSQL("CREATE UNIQUE INDEX if not exists record_on_index on " +
                                "weight_records (record_on);");
                        db.setTransactionSuccessful();
                        break;
                    } finally {
                        db.endTransaction();
                    }
                case 14:
                    db.beginTransaction();
                    try {
                        db.execSQL("CREATE TABLE if not exists food_records (_id integer PRIMARY " +
                                "KEY autoincrement, food_name varchar(255), record_on varchar" +
                                "(255), unit_name varchar(255), code varchar(255), amount int(11)" +
                                ", food_unit_id int(11), time_type int(11), calory decimal(5,2), " +
                                "user_key varchar(255));");
                        db.execSQL("CREATE UNIQUE INDEX if not exists " +
                                "index_on_record_on_and_food_name_and_time_type_and_unit_name_and_user_key on food_records (record_on, food_name, unit_name, time_type, user_key);");
                        db.execSQL("CREATE TABLE if not exists sport_records (_id integer PRIMARY" +
                                " KEY autoincrement, duration int(11), record_on varchar(255), " +
                                "activity_id int(11), activity_name varchar(255), mets int(11), " +
                                "unit_name varchar(255), calory decimal(5,2), user_key varchar" +
                                "(255));");
                        db.execSQL("CREATE UNIQUE INDEX if not exists " +
                                "index_on_record_on_and_activity_id_and_user_key on sport_records" +
                                " (record_on, activity_id, user_key);");
                        db.setTransactionSuccessful();
                        break;
                    } finally {
                        db.endTransaction();
                    }
                case 15:
                    db.beginTransaction();
                    try {
                        db.execSQL("DROP TABLE if exists sport_records;");
                        db.execSQL("CREATE TABLE if not exists sport_records (_id integer PRIMARY" +
                                " KEY autoincrement, duration int(11), record_on varchar(255), " +
                                "activity_id int(11), activity_name varchar(255), mets int(11), " +
                                "unit_name varchar(255), calory decimal(5,2), user_key varchar" +
                                "(255));");
                        db.execSQL("CREATE UNIQUE INDEX if not exists " +
                                "index_on_record_on_and_activity_id_and_user_key on sport_records" +
                                " (record_on, activity_id, user_key);");
                        db.setTransactionSuccessful();
                        break;
                    } finally {
                        db.endTransaction();
                    }
                case 16:
                    db.beginTransaction();
                    try {
                        db.execSQL("ALTER TABLE users ADD COLUMN avatar varchar(255);");
                        db.setTransactionSuccessful();
                        break;
                    } finally {
                        db.endTransaction();
                    }
                case 17:
                    db.beginTransaction();
                    try {
                        db.execSQL("ALTER TABLE users ADD COLUMN description varchar(255);");
                        db.execSQL("ALTER TABLE users ADD COLUMN latest_weight decimal(5,2);");
                        db.execSQL("ALTER TABLE users ADD COLUMN latest_weight_at varchar(255);");
                        db.execSQL("ALTER TABLE users ADD COLUMN latest_waist decimal(5,2);");
                        db.execSQL("ALTER TABLE users ADD COLUMN latest_waist_at varchar(255);");
                        db.execSQL("ALTER TABLE users ADD COLUMN post_count int(11);");
                        db.execSQL("ALTER TABLE users ADD COLUMN envious_count int(11);");
                        db.execSQL("ALTER TABLE users ADD COLUMN following_count int(11);");
                        db.execSQL("ALTER TABLE users ADD COLUMN follower_count int(11);");
                        db.execSQL("ALTER TABLE users ADD COLUMN sport_condition varchar(255);");
                        db.execSQL("ALTER TABLE users ADD COLUMN diseases varchar(255);");
                        db.execSQL("ALTER TABLE users ADD COLUMN need_test int(11);");
                        db.setTransactionSuccessful();
                        break;
                    } finally {
                        db.endTransaction();
                    }
                case 18:
                    db.beginTransaction();
                    try {
                        db.execSQL("ALTER TABLE users ADD COLUMN badges_count int(11);");
                        db.setTransactionSuccessful();
                        break;
                    } finally {
                        db.endTransaction();
                    }
                case 19:
                    db.beginTransaction();
                    try {
                        db.execSQL("ALTER TABLE users ADD COLUMN begin_date varchar(255);");
                        db.setTransactionSuccessful();
                        break;
                    } finally {
                        db.endTransaction();
                    }
                case 20:
                    db.beginTransaction();
                    try {
                        db.execSQL("CREATE TABLE if not exists steps (_id integer PRIMARY KEY " +
                                "autoincrement, record_on varchar(255), step int(11), distance " +
                                "int (11));");
                        db.setTransactionSuccessful();
                        break;
                    } finally {
                        db.endTransaction();
                    }
                default:
                    break;
            }
        }
        Helper.showLog(TAG, "onUpgrade end");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Helper.showLog(TAG, "onDowngrade");
    }
}
