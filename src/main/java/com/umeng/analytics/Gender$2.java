package com.umeng.analytics;

import java.util.Locale;

enum Gender$2 extends Gender {
    Gender$2(String str,
    int i, int i2
    )

    {
        super(str, i, i2, null);
    }

    public String toString() {
        return String.format(Locale.US, "Female:%d", new Object[]{Integer.valueOf(this.value)});
    }
    }
