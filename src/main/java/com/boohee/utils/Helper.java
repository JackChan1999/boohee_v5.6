package com.boohee.utils;

import android.content.Context;
import android.support.annotation.StringRes;

import com.boohee.myview.BooheeToast;
import com.boohee.one.MyApplication;

public class Helper {
    public static final boolean DEBUG = false;

    @Deprecated
    public static void showToast(Context ctx, CharSequence text) {
        BooheeToast.makeText(MyApplication.getContext(), text, 0).show();
    }

    public static void showToast(CharSequence text) {
        BooheeToast.makeText(MyApplication.getContext(), text, 0).show();
    }

    @Deprecated
    public static void showLong(Context ctx, CharSequence text) {
        BooheeToast.makeText(MyApplication.getContext(), text, 1).show();
    }

    @Deprecated
    public static void showLong(CharSequence text) {
        BooheeToast.makeText(MyApplication.getContext(), text, 1).show();
    }

    @Deprecated
    public static void showLong(Context ctx, @StringRes int resId) {
        BooheeToast.makeText(MyApplication.getContext(), resId, 1).show();
    }

    @Deprecated
    public static void showLong(@StringRes int resId) {
        BooheeToast.makeText(MyApplication.getContext(), resId, 1).show();
    }

    @Deprecated
    public static void showToast(Context ctx, @StringRes int resId) {
        BooheeToast.makeText(MyApplication.getContext(), resId, 0).show();
    }

    public static void showToast(@StringRes int resId) {
        BooheeToast.makeText(MyApplication.getContext(), resId, 0).show();
    }

    public static void showLog(String tag, String text) {
    }

    public static void showLog(String text) {
    }

    public static void logJson(String json) {
    }

    public static void logJson(String tag, String json) {
    }

    public static void showError(String tag, String msg, Throwable ex) {
    }

    public static void simpleLog(String tag, String msg) {
    }
}
