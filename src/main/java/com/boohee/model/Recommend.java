package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Recommend {
    public int    id;
    public String page_url;
    public String pic_url;
    public String title;

    public String toString() {
        return new Gson().toJson((Object) this);
    }

    public static List<Recommend> parseRecommends(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<List<Recommend>>() {
        }.getType());
    }
}
