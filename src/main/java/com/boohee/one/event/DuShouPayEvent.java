package com.boohee.one.event;

public class DuShouPayEvent {
    private int orderId;

    public int getOrderId() {
        return this.orderId;
    }

    public DuShouPayEvent setOrderId(int orderId) {
        this.orderId = orderId;
        return this;
    }
}
