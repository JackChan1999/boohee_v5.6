package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import org.json.JSONObject;

public class HomeTopic {
    public int      id;
    public String   image_url;
    public String[] images;
    public int      joiners_count;
    public String   link;
    public String   name;
    public int      posts_count;
    public int      tags_count;
    public User     user;

    public static List<HomeTopic> parseLists(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<List<HomeTopic>>() {
        }.getType());
    }

    public static HomeTopic parseTopic(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (HomeTopic) new Gson().fromJson(object.toString(), HomeTopic.class);
    }
}
