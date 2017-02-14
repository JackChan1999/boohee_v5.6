package com.boohee.one.radar.entity;

public class Sports {
    public int[]        calorie;
    public RadarMessage message;

    public boolean isEmpty() {
        return this.calorie == null || this.calorie.length != 7 || allZero();
    }

    private boolean allZero() {
        if (this.calorie == null) {
            return false;
        }
        for (int i : this.calorie) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }
}
