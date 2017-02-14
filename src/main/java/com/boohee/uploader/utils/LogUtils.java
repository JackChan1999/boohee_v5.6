package com.boohee.uploader.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LogUtils {
    public static final boolean DEBUG = true;
    public static final String  TAG   = ">>>>>";

    public static void debug(String msg) {
        Log.d(TAG, msg);
    }

    public static void toast(View view, String msg) {
        toast(view.getContext(), msg);
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, 1).show();
    }

    public static void print(String msg) {
        System.out.println(TAG + msg);
    }
}
