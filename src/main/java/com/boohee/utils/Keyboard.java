package com.boohee.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Keyboard {
    public static void close(Context context, EditText editText) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow
                (editText.getWindowToken(), 0);
    }

    public static void open(Context context, EditText editText) {
        ((InputMethodManager) context.getSystemService("input_method")).showSoftInput(editText, 2);
    }

    public static void openImplicit(Context context, EditText editText) {
        ((InputMethodManager) context.getSystemService("input_method")).showSoftInput(editText, 1);
    }

    public static void toggle(Context context, EditText editText) {
        ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInputFromWindow
                (editText.getWindowToken(), 2, 0);
    }

    public static void closeAll(Context context) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
            try {
                if (context instanceof Activity) {
                    imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                            .getWindowToken(), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
