package com.umeng.socialize.bean;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender convertToEmun(String str) {
        for (Gender gender : values()) {
            if (gender.toString().equals(str)) {
                return gender;
            }
        }
        return null;
    }
}
