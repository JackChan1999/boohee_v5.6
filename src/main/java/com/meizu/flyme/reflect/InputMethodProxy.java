package com.meizu.flyme.reflect;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;

public class InputMethodProxy extends Proxy {
    private static final String   TAG    = "InputMethod";
    private static       Class<?> sClass = InputMethodManager.class;
    private static Method sSetMzInputThemeLight;

    public static boolean setInputThemeLight(Context context, boolean light) {
        sSetMzInputThemeLight = Proxy.getMethod(sSetMzInputThemeLight, sClass,
                "setMzInputThemeLight", Boolean.TYPE);
        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
        if (imm == null) {
            return false;
        }
        return Proxy.invoke(sSetMzInputThemeLight, imm, Boolean.valueOf(light));
    }
}
