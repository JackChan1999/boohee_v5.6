package com.boohee.one.event;

public class ConstEvent {
    public static final int FLAG_COPY_DIET_SPORT_SUCCESS = 1;
    public static final int FLAG_SWITCH_TAB_SHOP         = 2;
    private int flag;

    public int getFlag() {
        return this.flag;
    }

    public ConstEvent setFlag(int flag) {
        this.flag = flag;
        return this;
    }
}
