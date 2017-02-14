package com.umeng.analytics;

import u.aly.ay;

public enum Gender {
    ;

    public int value;

    private Gender(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static Gender getGender(int i) {
        switch (i) {
            case 1:
                return Male;
            case 2:
                return Female;
            default:
                return Unknown;
        }
    }

    public static ay transGender(Gender gender) {
        switch (4. a[gender.ordinal()]){
            case 1:
                return ay.a;
            case 2:
                return ay.b;
            default:
                return ay.c;
        }
    }
}
