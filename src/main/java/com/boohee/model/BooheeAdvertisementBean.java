package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class BooheeAdvertisementBean {
    public String body;
    public int    id;
    public String link_url;
    public String photo_url;
    public int    position;

    public static LinkedList<BooheeAdvertisementBean> parseList(JSONObject object) {
        JSONArray array = object.optJSONArray("advertisements");
        if (array == null) {
            return null;
        }
        return (LinkedList) new Gson().fromJson(array.toString(), new
                TypeToken<LinkedList<BooheeAdvertisementBean>>() {
        }.getType());
    }
}
