package com.umeng.analytics;

import java.util.Locale;

enum Gender$3 extends Gender {
    Gender$3(String str,
    int i, int i2
    )

    {
        super(str, i, i2, null);
    }

    public String toString() {
        return String.format(Locale.US, "Unknown:%d", new Object[]{Integer.valueOf(this.value)});
    }
    }
