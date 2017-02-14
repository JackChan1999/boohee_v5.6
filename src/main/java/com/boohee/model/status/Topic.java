package com.boohee.model.status;

import com.google.gson.Gson;

import org.json.JSONObject;

public class Topic {
    public boolean choice;
    public boolean favorite;
    public String  head_image_url;
    public int     id;
    public String  page_url;
    public int     posts_count;
    public String  slug;
    public String  title;

    public static Topic parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (Topic) new Gson().fromJson(object.toString(), Topic.class);
    }
}
