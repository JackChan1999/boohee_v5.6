package com.boohee.one.radar.entity;

public class Dietary {
    public int          fattiness;
    public int          grain;
    public int          meat;
    public RadarMessage message;
    public int          veggie;

    public boolean isEmpty() {
        return this.grain == 0 && this.meat == 0 && this.veggie == 0 && this.fattiness == 0;
    }
}
