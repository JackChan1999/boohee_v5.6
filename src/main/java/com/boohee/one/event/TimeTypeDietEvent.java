package com.boohee.one.event;

import com.boohee.model.RecordFood;

public class TimeTypeDietEvent {
    private int        beforeTimeType;
    private int        index;
    private RecordFood recordFood;

    public TimeTypeDietEvent setBeforeTimeType(int beforeTimeType) {
        this.beforeTimeType = beforeTimeType;
        return this;
    }

    public TimeTypeDietEvent setIndex(int index) {
        this.index = index;
        return this;
    }

    public TimeTypeDietEvent setRecordFood(RecordFood recordFood) {
        this.recordFood = recordFood;
        return this;
    }

    public int getBeforeTimeType() {
        return this.beforeTimeType;
    }

    public int getIndex() {
        return this.index;
    }

    public RecordFood getRecordFood() {
        return this.recordFood;
    }
}
