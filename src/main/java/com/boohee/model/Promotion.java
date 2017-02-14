package com.boohee.model;

public class Promotion extends ModelBase {
    public int    current_quota;
    public String current_timestamp;
    public String expires_at;
    public String flash_type;
    public String starts_at;
    public String state;
    public String state_text;
    public String title;
    public int    total_quota;

    public enum FLASH_TYPE {
        time,
        quota,
        both
    }

    public enum STATE_TYPE {
        preview,
        active,
        completed,
        closed
    }
}
