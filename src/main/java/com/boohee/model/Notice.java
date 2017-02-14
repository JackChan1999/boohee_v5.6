package com.boohee.model;

public class Notice extends ModelBase {
    public int    alarm_tip_id;
    public String alarm_tip_message;
    public int    is_opened;

    public Notice(int id, int alarm_tip_id, String alarm_tip_message, int is_opened) {
        this.id = id;
        this.alarm_tip_id = alarm_tip_id;
        this.alarm_tip_message = alarm_tip_message;
        this.is_opened = is_opened;
    }

    public boolean isOpened() {
        return this.is_opened != 0;
    }
}
