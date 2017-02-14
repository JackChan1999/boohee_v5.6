package com.umeng.analytics.social;

import java.util.Locale;

public enum UMPlatformData$GENDER {
    MALE(0) {
        public String toString() {
            return String.format(Locale.US, "Male:%d", new Object[]{Integer.valueOf(this.value)});
        }
    },
    FEMALE(1) {
        public String toString() {
            return String.format(Locale.US, "Female:%d", new Object[]{Integer.valueOf(this.value)});
        }
    };

    public int value;

    private UMPlatformData$GENDER(int i) {
        this.value = i;
    }
}
