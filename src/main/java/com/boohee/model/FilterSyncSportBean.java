package com.boohee.model;

import com.google.gson.Gson;

public class FilterSyncSportBean {
    private int    calory;
    private int    duration;
    private int    sportId;
    private String tag;

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCalory() {
        return this.calory;
    }

    public void setCalory(int calory) {
        this.calory = calory;
    }

    public String toString() {
        return new Gson().toJson((Object) this);
    }

    public int getSportId() {
        return this.sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }
}
