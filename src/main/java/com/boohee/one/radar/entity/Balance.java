package com.boohee.one.radar.entity;

public class Balance {
    public RadarMessage message;
    public int          qualified;
    public int          unqualified;

    public boolean isEmpty() {
        return this.qualified == 0 && this.unqualified == 0;
    }
}
