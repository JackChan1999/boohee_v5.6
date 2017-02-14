package com.boohee.model;

import com.google.gson.Gson;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

public class FilterSyncFigureBean {
    private String name;
    private String unit = SocializeProtocolConstants.PROTOCOL_KEY_COMMENT_COUNT;
    private float value;

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String toString() {
        return new Gson().toJson((Object) this);
    }
}
