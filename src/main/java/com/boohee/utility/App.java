package com.boohee.utility;

import android.annotation.SuppressLint;
import android.content.Context;

import com.boohee.database.OnePreference;
import com.boohee.one.MyApplication;
import com.boohee.utils.FileUtil;

import java.io.File;

public class App {
    public static final String BHDB_NAME     = "bhdb.sqlite";
    public static final String BHDB_PATH     = (DB_PATH + BHDB_NAME);
    public static final int    BH_DB_VERSION = 14;
    @SuppressLint({"SdCardPath"})
    public static final String DB_PATH       = ("/data/data/" + MyApplication.getContext()
            .getPackageName() + "/databases/");
    @SuppressLint({"SdCardPath"})
    public static final String FILE_PATH     = ("/data/data/" + MyApplication.getContext()
            .getPackageName() + "/files/");
    public static final String REGION_NAME   = "regions.json";
    static final        String TAG           = App.class.getName();

    private static void saveBhDbVersion(Context context) {
        new OnePreference(context).putInt(Const.BHDB_VERSION, 14);
    }

    public static void checkBhDB(Context context) {
        if (!new File(BHDB_PATH).exists()) {
            FileUtil.copyDB(context, BHDB_NAME);
            saveBhDbVersion(context);
        }
        if (new OnePreference(context).getInt(Const.BHDB_VERSION) < 14) {
            FileUtil.copyDB(context, BHDB_NAME);
            saveBhDbVersion(context);
        }
    }

    public static void checkDB(Context context) {
        checkBhDB(context);
    }
}
