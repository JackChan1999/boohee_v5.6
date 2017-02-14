package com.umeng.analytics;

import java.util.Locale;

enum Gender$1 extends Gender {
    Gender$1(String str,
    int i, int i2
    )

    {
        super(str, i, i2, null);
    }

    public String toString() {
        return String.format(Locale.US, "Male:%d", new Object[]{Integer.valueOf(this.value)});
    }
    }
