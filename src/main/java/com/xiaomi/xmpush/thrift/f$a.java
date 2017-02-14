package com.xiaomi.xmpush.thrift;

import com.joooonho.BuildConfig;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum f$a {
    DEBUG((short) 1, BuildConfig.BUILD_TYPE),
    TARGET((short) 2, "target"),
    ID((short) 3, "id"),
    APP_ID((short) 4, "appId"),
    CMD_NAME((short) 5, "cmdName"),
    CMD_ARGS((short) 6, "cmdArgs"),
    PACKAGE_NAME((short) 7, "packageName"),
    CATEGORY((short) 9, "category");

    private static final Map<String, f$a> i = null;
    private final short  j;
    private final String k;

    static {
        i = new HashMap();
        Iterator it = EnumSet.allOf(f$a.class).iterator();
        while (it.hasNext()) {
            f$a com_xiaomi_xmpush_thrift_f_a = (f$a) it.next();
            i.put(com_xiaomi_xmpush_thrift_f_a.a(), com_xiaomi_xmpush_thrift_f_a);
        }
    }

    private f$a(short s, String str) {
        this.j = s;
        this.k = str;
    }

    public String a() {
        return this.k;
    }
}
