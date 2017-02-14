package com.boohee.model;

public class AlarmTip extends ModelBase {
    public String code;
    public String message;
    public String name;

    public AlarmTip(int id, String message, String code, String name) {
        this.id = id;
        this.message = message;
        this.code = code;
        this.name = name;
    }
}
