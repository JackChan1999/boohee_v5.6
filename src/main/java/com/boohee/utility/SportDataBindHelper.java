package com.boohee.utility;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.boohee.one.MyApplication;

@Deprecated
public class SportDataBindHelper {
    private static Editor            editor      = preferences.edit();
    private static SharedPreferences preferences = MyApplication.getContext()
            .getSharedPreferences("SportDataBindHelper", 0);

    public static void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public static void clearData() {
        editor.clear().commit();
    }

    public static String getString(String key) {
        return preferences.getString(key, "");
    }
}
