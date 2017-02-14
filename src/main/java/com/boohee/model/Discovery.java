package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import org.json.JSONObject;

public class Discovery {
    public List<Item> data;
    public String     more_url;
    public String     title;

    public class Item {
        public String description;
        public String pic_url;
        public String title;
        public String url;
    }

    public static List<Discovery> parseList(JSONObject object) {
        if (!object.has("discoveries")) {
            return null;
        }
        List<Discovery> discoveries = null;
        try {
            return (List) new Gson().fromJson(object.optJSONArray("discoveries").toString(), new
                    TypeToken<List<Discovery>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return discoveries;
        }
    }
}
