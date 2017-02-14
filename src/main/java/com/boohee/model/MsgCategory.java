package com.boohee.model;

public class MsgCategory {
    public String code;
    public int    count;
    public int    iconId;
    public String title;

    public MsgCategory(int iconId, String title, String code) {
        this.iconId = iconId;
        this.title = title;
        this.code = code;
    }

    public MsgCategory(int iconId, String title) {
        this.iconId = iconId;
        this.title = title;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
