package com.squareup.okhttp.internal.http;

import java.util.Comparator;

class OkHeaders$1 implements Comparator<String> {
    OkHeaders$1() {
    }

    public int compare(String a, String b) {
        if (a == b) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        return String.CASE_INSENSITIVE_ORDER.compare(a, b);
    }
}
