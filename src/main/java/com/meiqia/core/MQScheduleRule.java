package com.meiqia.core;

public enum MQScheduleRule {
    REDIRECT_NONE(1),
    REDIRECT_GROUP(2),
    REDIRECT_ENTERPRISE(3);

    private final int value;

    private MQScheduleRule(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }
}
