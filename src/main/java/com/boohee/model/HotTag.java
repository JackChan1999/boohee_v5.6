package com.boohee.model;

import com.google.gson.Gson;

import java.util.ArrayList;

import org.json.JSONObject;

public class HotTag extends ModelBase {
    public ArrayList<String> hot;
    public ArrayList<String> recent;

    public static HotTag parseHotTagFromJson(JSONObject object) {
        HotTag hotTag = null;
        try {
            return (HotTag) new Gson().fromJson(object.toString(), HotTag.class);
        } catch (Exception e) {
            e.printStackTrace();
            return hotTag;
        }
    }

    public ArrayList<String> getHot() {
        return this.hot;
    }

    public void setHot(ArrayList<String> hot) {
        this.hot = hot;
    }

    public ArrayList<String> getRecent() {
        return this.recent;
    }

    public void setRecent(ArrayList<String> recent) {
        this.recent = recent;
    }
}
