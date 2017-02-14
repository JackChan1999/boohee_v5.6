package com.boohee.one.radar.entity;

public class Element {
    public int          carbohydrate;
    public int          fattiness;
    public RadarMessage message;
    public int          protein;

    public boolean isEmpty() {
        return this.carbohydrate == 0 && this.protein == 0 && this.fattiness == 0;
    }
}
