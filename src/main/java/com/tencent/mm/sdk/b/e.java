package com.tencent.mm.sdk.b;

import java.util.TimeZone;

public final class e {
    private static final long[]   G   = new long[]{300, 200, 300, 200};
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final long[]   H   = new long[]{300, 50, 300, 50};
    private static final char[]   I   = new char[]{'<', '>', '\"', '\'', '&', '\r', '\n', ' ',
            '\t'};
    private static final String[] J   = new String[]{"&lt;", "&gt;", "&quot;", "&apos;", "&amp;",
            "&#x0D;", "&#x0A;", "&#x20;", "&#x09;"};

    public static boolean j(String str) {
        return str == null || str.length() <= 0;
    }
}
