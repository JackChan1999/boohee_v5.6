package com.xiaomi.smack.util;

import android.content.Context;
import android.util.Base64;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.f;

import java.security.MessageDigest;

public class h {
    private static Context a;
    private static String  b;

    public static Context a() {
        return a;
    }

    public static void a(Context context) {
        a = context;
    }

    public static String b() {
        if (b == null) {
            String b = f.b(a);
            if (b != null) {
                try {
                    b = Base64.encodeToString(MessageDigest.getInstance("SHA1").digest(b.getBytes
                            ()), 8).substring(0, 16);
                } catch (Throwable e) {
                    b.a(e);
                }
            }
        }
        return b;
    }
}
