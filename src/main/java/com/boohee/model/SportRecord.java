package com.boohee.model;

public class SportRecord extends ModelBase {
    public int    activity_id;
    public String activity_name;
    public float  calory;
    public int    duration;
    public String record_on;
    public String remote_created_at;
    public int    remote_id;
    public String remote_updated_at;
    public String updated_at;
    public String user_key;

    public SportRecord(int id, int activity_id, String activity_name, int duration, int calory,
                       String record_on) {
        this.id = id;
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.duration = duration;
        this.calory = (float) calory;
        this.record_on = record_on;
    }

    public SportRecord(int id, int activity_id, String activity_name, int duration, int calory,
                       String record_on, String updated_at) {
        this(id, activity_id, activity_name, duration, calory, record_on);
        this.updated_at = updated_at;
    }

    public SportRecord(int id, int activity_id, String activity_name, int duration, int calory,
                       String record_on, String user_key, int remote_id, String
                               remote_created_at, String remote_updated_at, String updated_at) {
        this(id, activity_id, activity_name, duration, calory, record_on);
        this.user_key = user_key;
        this.remote_id = remote_id;
        this.remote_created_at = remote_created_at;
        this.remote_updated_at = remote_updated_at;
        this.updated_at = updated_at;
    }
}
