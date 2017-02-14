package com.pingplusplus.android;

public enum h {
    SDK(Integer.valueOf(0)),
    ONE(Integer.valueOf(1));

    public Integer c;

    private h(Integer num) {
        this.c = num;
    }

    public String toString() {
        return String.valueOf(this.c);
    }
}
