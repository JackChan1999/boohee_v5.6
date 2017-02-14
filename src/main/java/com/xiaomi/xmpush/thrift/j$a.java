package com.xiaomi.xmpush.thrift;

import com.joooonho.BuildConfig;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum j$a {
    DEBUG((short) 1, BuildConfig.BUILD_TYPE),
    TARGET((short) 2, "target"),
    ID((short) 3, "id"),
    APP_ID((short) 4, "appId"),
    APP_VERSION((short) 5, "appVersion"),
    PACKAGE_NAME((short) 6, "packageName"),
    g((short) 7, "token"),
    DEVICE_ID((short) 8, "deviceId"),
    ALIAS_NAME((short) 9, "aliasName"),
    SDK_VERSION((short) 10, "sdkVersion");

    private static final Map<String, j$a> k = null;
    private final short  l;
    private final String m;

    static {
        k = new HashMap();
        Iterator it = EnumSet.allOf(j$a.class).iterator();
        while (it.hasNext()) {
            j$a com_xiaomi_xmpush_thrift_j_a = (j$a) it.next();
            k.put(com_xiaomi_xmpush_thrift_j_a.a(), com_xiaomi_xmpush_thrift_j_a);
        }
    }

    private j$a(short s, String str) {
        this.l = s;
        this.m = str;
    }

    public String a() {
        return this.m;
    }
}
