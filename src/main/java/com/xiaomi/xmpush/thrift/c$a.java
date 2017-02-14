package com.xiaomi.xmpush.thrift;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum c$a {
    ID((short) 1, "id"),
    MESSAGE_TS((short) 2, "messageTs"),
    TOPIC((short) 3, "topic"),
    TITLE((short) 4, "title"),
    DESCRIPTION((short) 5, "description"),
    NOTIFY_TYPE((short) 6, "notifyType"),
    URL((short) 7, "url"),
    PASS_THROUGH((short) 8, "passThrough"),
    NOTIFY_ID((short) 9, "notifyId"),
    EXTRA((short) 10, "extra"),
    INTERNAL((short) 11, "internal"),
    IGNORE_REG_INFO((short) 12, "ignoreRegInfo");

    private static final Map<String, c$a> m = null;
    private final short  n;
    private final String o;

    static {
        m = new HashMap();
        Iterator it = EnumSet.allOf(c$a.class).iterator();
        while (it.hasNext()) {
            c$a com_xiaomi_xmpush_thrift_c_a = (c$a) it.next();
            m.put(com_xiaomi_xmpush_thrift_c_a.a(), com_xiaomi_xmpush_thrift_c_a);
        }
    }

    private c$a(short s, String str) {
        this.n = s;
        this.o = str;
    }

    public String a() {
        return this.o;
    }
}
