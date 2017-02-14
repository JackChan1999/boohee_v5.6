package com.boohee.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtils {
    public static void openKeybord(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
        imm.showSoftInput(editText, 2);
        imm.toggleSoftInput(2, 1);
    }

    public static void closeKeybord(Context context, EditText editText) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow
                (editText.getWindowToken(), 0);
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
