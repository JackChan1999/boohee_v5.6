package com.boohee.one.event;

import com.boohee.one.sport.model.DownloadRecord;

public class DownloadEvent {
    public DownloadRecord record;

    public DownloadEvent(DownloadRecord record) {
        this.record = record;
    }
}
