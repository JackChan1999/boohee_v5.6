package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Event {
    public String desc;
    public String pic_url;
    public String title;
    public String url;

    public static List<Event> parseEvents(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<List<Event>>() {
        }.getType());
    }
}
