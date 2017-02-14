package com.xiaomi.xmpush.thrift;

import com.joooonho.BuildConfig;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum i$a {
    DEBUG((short) 1, BuildConfig.BUILD_TYPE),
    TARGET((short) 2, "target"),
    ID((short) 3, "id"),
    APP_ID((short) 4, "appId"),
    TYPE((short) 5, "type"),
    REQUIRE_ACK((short) 6, "requireAck"),
    PAYLOAD((short) 7, "payload"),
    EXTRA((short) 8, "extra"),
    PACKAGE_NAME((short) 9, "packageName"),
    CATEGORY((short) 10, "category");

    private static final Map<String, i$a> k = null;
    private final short  l;
    private final String m;

    static {
        k = new HashMap();
        Iterator it = EnumSet.allOf(i$a.class).iterator();
        while (it.hasNext()) {
            i$a com_xiaomi_xmpush_thrift_i_a = (i$a) it.next();
            k.put(com_xiaomi_xmpush_thrift_i_a.a(), com_xiaomi_xmpush_thrift_i_a);
        }
    }

    private i$a(short s, String str) {
        this.l = s;
        this.m = str;
    }

    public String a() {
        return this.m;
    }
}
