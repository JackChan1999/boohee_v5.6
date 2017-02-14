package com.xiaomi.xmpush.thrift;

import com.joooonho.BuildConfig;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum q$a {
    DEBUG((short) 1, BuildConfig.BUILD_TYPE),
    TARGET((short) 2, "target"),
    ID((short) 3, "id"),
    APP_ID((short) 4, "appId"),
    REG_ID((short) 5, "regId"),
    APP_VERSION((short) 6, "appVersion"),
    PACKAGE_NAME((short) 7, "packageName"),
    h((short) 8, "token"),
    DEVICE_ID((short) 9, "deviceId"),
    ALIAS_NAME((short) 10, "aliasName");

    private static final Map<String, q$a> k = null;
    private final short  l;
    private final String m;

    static {
        k = new HashMap();
        Iterator it = EnumSet.allOf(q$a.class).iterator();
        while (it.hasNext()) {
            q$a com_xiaomi_xmpush_thrift_q_a = (q$a) it.next();
            k.put(com_xiaomi_xmpush_thrift_q_a.a(), com_xiaomi_xmpush_thrift_q_a);
        }
    }

    private q$a(short s, String str) {
        this.l = s;
        this.m = str;
    }

    public String a() {
        return this.m;
    }
}
