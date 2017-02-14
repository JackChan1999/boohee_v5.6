package com.boohee.one.event;

import com.boohee.model.RecordSport;

public class SportEvent {
    public static final int TYPE_ADD    = 1;
    public static final int TYPE_DELETE = 3;
    public static final int TYPE_EDIT   = 2;
    private int         editType;
    private int         index;
    private RecordSport recordSport;

    public int getIndex() {
        return this.index;
    }

    public SportEvent setIndex(int index) {
        this.index = index;
        return this;
    }

    public RecordSport getRecordSport() {
        return this.recordSport;
    }

    public SportEvent setRecordSport(RecordSport recordSport) {
        this.recordSport = recordSport;
        return this;
    }

    public int getEditType() {
        return this.editType;
    }

    public SportEvent setEditType(int editType) {
        this.editType = editType;
        return this;
    }
}
