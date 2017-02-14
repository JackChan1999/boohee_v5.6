package com.boohee.one.event;

import com.boohee.model.RecordFood;

public class DietEvent {
    public static final int TYPE_ADD    = 1;
    public static final int TYPE_DELETE = 3;
    public static final int TYPE_EDIT   = 2;
    private int        editType;
    private int        index;
    private RecordFood recordFood;
    private int        timeType;

    public int getTimeType() {
        return this.timeType;
    }

    public DietEvent setTimeType(int timeType) {
        this.timeType = timeType;
        return this;
    }

    public int getIndex() {
        return this.index;
    }

    public DietEvent setIndex(int index) {
        this.index = index;
        return this;
    }

    public RecordFood getRecordFood() {
        return this.recordFood;
    }

    public DietEvent setRecordFood(RecordFood recordFood) {
        this.recordFood = recordFood;
        return this;
    }

    public int getEditType() {
        return this.editType;
    }

    public DietEvent setEditType(int editType) {
        this.editType = editType;
        return this;
    }
}
