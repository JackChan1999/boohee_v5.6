package com.boohee.model;

import com.google.gson.Gson;

public class FilterSyncFoodBean {
    private float  calory;
    private String tag;

    public float getCalory() {
        return this.calory;
    }

    public void setCalory(float calory) {
        this.calory = calory;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String toString() {
        return new Gson().toJson((Object) this);
    }
}
