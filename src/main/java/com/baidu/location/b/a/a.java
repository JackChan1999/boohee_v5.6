package com.baidu.location.b.a;

import android.content.Context;
import android.text.TextUtils;

public class a {
    private static final boolean a = false;
    private static final String if = a.class.getSimpleName();

    private static String a(Context context) {
        return b.a(context);
    }

    public static String if(Context context) {
        String a = a(context);
        String str = b.do(context);
        if (TextUtils.isEmpty(str)) {
            str = "0";
        }
        return a + "|" + new StringBuffer(str).reverse().toString();
    }
}
