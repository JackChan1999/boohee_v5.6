package com.alipay.apmobilesecuritysdk.d;

import android.content.Context;

public final class b {
    public static String a(Context context) {
        try {
            return (String) Class.forName("com.ut.device.UTDevice").getMethod("getUtdid", new Class[]{Context.class}).invoke(null, new Object[]{context});
        } catch (Exception e) {
            return "";
        }
    }
}
