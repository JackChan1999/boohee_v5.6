package com.boohee.one.event;

import com.boohee.model.RecordPhoto;

public class PhotoDietEvent {
    public static final int TYPE_ADD    = 1;
    public static final int TYPE_DELETE = 3;
    public static final int TYPE_EDIT   = 2;
    private int         editType;
    private int         index;
    private RecordPhoto recordPhoto;
    private int         timeType;

    public int getTimeType() {
        return this.timeType;
    }

    public PhotoDietEvent setTimeType(int timeType) {
        this.timeType = timeType;
        return this;
    }

    public int getIndex() {
        return this.index;
    }

    public PhotoDietEvent setIndex(int index) {
        this.index = index;
        return this;
    }

    public RecordPhoto getRecordPhoto() {
        return this.recordPhoto;
    }

    public PhotoDietEvent setRecordPhoto(RecordPhoto recordPhoto) {
        this.recordPhoto = recordPhoto;
        return this;
    }

    public int getEditType() {
        return this.editType;
    }

    public PhotoDietEvent setEditType(int editType) {
        this.editType = editType;
        return this;
    }
}
