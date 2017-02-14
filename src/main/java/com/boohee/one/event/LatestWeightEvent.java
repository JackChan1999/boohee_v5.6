package com.boohee.one.event;

public class LatestWeightEvent {
    private float latestWeight;

    public float getLatestWeight() {
        return this.latestWeight;
    }

    public LatestWeightEvent setLatestWeight(float latestWeight) {
        this.latestWeight = latestWeight;
        return this;
    }
}
