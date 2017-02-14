package com.boohee.one.event;

public class FoodCollectEvent {
    private boolean isCollect;

    public boolean isCollect() {
        return this.isCollect;
    }

    public FoodCollectEvent setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
        return this;
    }
}
